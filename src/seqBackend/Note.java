package seqBackend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Information about a note to be played. */
public class Note
{
	/** Map of notes by their common name. */
	protected static Map<String,Double> notes = new HashMap<String,Double>();

	/** Note indicating */
	public static Note PAUSE = new Note(' ',0);

	/** The letter name of this note. */
	public char name;
	/** True if this note is sharp. */
	public boolean sharp = false;
	/** The scale of this note, 0-7. */
	public int scale;
	/** Number of beats this note occupies. */
	public int beats = 1;
	/** The volume of this note. */
	public int volume;

	static
	{
		double freqs[] = { 16.35,17.32,18.35,19.45,20.60,21.83,23.12,24.50,25.96,27.50,29.14,30.87,
				32.70,34.65,36.71,38.89,41.20,43.65,46.25,49.00,51.91,55.00,58.27,61.74,65.41,69.30,73.42,
				77.78,82.41,87.31,92.50,98.00,103.83,110.00,116.54,123.47,130.81,138.59,146.83,155.56,
				164.81,174.61,185.00,196.00,207.65,220.00,233.08,246.94,261.63,277.18,293.66,311.13,329.63,
				349.23,369.99,392.00,415.30,440.00,466.16,493.88,523.25,554.37,587.33,622.25,659.26,698.46,
				739.99,783.99,830.61,880.00,932.33,987.77,1046.50,1108.73,1174.66,1244.51,1318.51,1396.91,
				1479.98,1567.98,1661.22,1760.00,1864.66,1975.53,2093.00,2217.46,2349.32,2489.02,2637.02,
				2793.83,2959.96,3135.96,3322.44,3520.00,3729.31,3951.07,4186.01,4434.92,4698.64,4978.03 };
		String[] noteNames = { "C","C#","D","D#","E","F","F#","G","G#","A","A#","B" };
		int n = 0;
		for (int scale = 0; scale < 8; scale++)
			for (String note : noteNames)
				notes.put(scale + note,freqs[n++]);
	}

	/** Construct with name and volume, using a mean scale. 
	 * @param name The character name of this note, A-G.
	 * @param volume The volume of this note, 0-0xFFFF.
	 **/
	public Note(char name, int volume)
	{
		this.name = name;
		this.volume = volume;
		this.scale = 4;
	}

	/** Construct with name and volume, using a mean scale 
	 * @param name The character name of this note, A-G.
	 * @param scale The scale of this note, 0-7.
	 * @param sharp True if this note is sharp, false otherwise.
	 * @param beats The number of beats for which this note plays.
	 * @param volume The volume of this note, 0-0xFFFF.
	 **/
	public Note(char name, int scale, boolean sharp, int beats, int volume)
	{
		this.name = name;
		this.scale = scale;
		this.sharp = sharp;
		this.beats = beats;
		this.volume = volume;
	}

	/** Write this note to an output stream.
	 * @param os The output stream to write to.
	 * @param tempo Tempo in beats per minute.
	 * @param sps Sample rate, in samples per second.
	 * @throws IOException Thrown if write fails for any reason.
	 **/
	public void write(OutputStream os, int tempo, int sps) throws IOException
	{
		int ndur = (int) ((60.0 / (double) tempo) * sps * beats);
		if (name == ' ')
		{
			for (int x = 0; x < ndur; x++)
				os.write(0);
			return;
		}
		String noteName = String.format("%c%c%s",scale < 10 ? scale + '0' : scale,name,sharp ? "#" : "");
		Double freq = notes.get(noteName);
		if (freq == null) System.err.println(noteName);
		for (int x = 0; x < ndur; x++)
		{
			byte b = (byte) ((volume * Math.sin((double) x / (sps / freq) * 2 * Math.PI) * (ndur - x < 100 ? (ndur - x) / 100.
					: 1)));
			os.write(b);
		}
		os.write(0);
	}

	/**
	 * Debug/toy function to parse a string of note letters into a list of notes to play.
	 * @param song The string representing the song to play.
	 * @return The array of notes to the song represented in the given string.
	 */
	public static List<Note> parseNotesPostfix(String song)
	{
		List<Note> notes = new ArrayList<Note>();
		int volume = 24;
		Note note = null;
		for (char c : song.toCharArray())
		{
			if (c >= 'A' && c <= 'G')
			{
				note = new Note(c,volume);
				continue;
			}
			if (c >= '0' && c <= '8')
			{
				if (note != null) note.scale = c;
				continue;
			}
			switch (c)
			{
				case ' ':
					note = null;
					notes.add(Note.PAUSE);
					break;
				case '-':
					if (note != null) note.beats++;
					break;
				case '#':
					if (note != null) note.sharp = true;
					break;
				case 'b': //flat (translate into next sharp down)
					if (note != null)
					{
						note.name--;
						if (note.name < 'A') note.name = 'G';
						//Cb = B, Fb = E
						if (note.name != 'B' && note.name != 'E') note.sharp = true;
					}
					break;
			}
		}

		return notes;
	}

	/** Similar to parseNotesPrefix, but parses notes into an array of bytes representing playable sound data. 
	 * @param song The string containing the song to rasterize.
	 * @param sps The sample rate of the song to generate, in samples per second.
	 * @return The playable sound bytes.
	 **/
	public static byte[] parseSong(String song, int sps)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean sharp = false;
		int scale = '4';
		int lastNote = -1;
		int tempo = 120 * 2; //*2 for since everything is an eighth note
		int volume = 24;
		try
		{
			for (int i = 0; i < song.length(); i++)
			{
				char note = song.charAt(i);
				if (note == '-') continue;
				if (note == ' ')
				{
					Note.PAUSE.write(baos,tempo,sps);
					lastNote = i;
					continue;
				}
				if (note == '#')
				{
					sharp = true;
					lastNote++;
					continue;
				}
				if (note >= '0' && note <= '8')
				{
					scale = note;
					lastNote++;
					continue;
				}
				Note n = new Note(note,scale,sharp,i - lastNote,volume);
				n.write(baos,tempo,sps);
				lastNote = i;
				sharp = false;
			}
		}
		catch (IOException e)
		{
			// This will never happen unless we change the BAOS to something else.
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
