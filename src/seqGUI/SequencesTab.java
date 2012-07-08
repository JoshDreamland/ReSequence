package seqGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import reJava.ReJava;
import seqBackend.Note;
import waveFunc.WaveRenderPane;
import waveFunc.WaveRenderPane.WaveHolder;

/**
 * @author Josh Ventura
 * Tab containing the finished sequence, or complete song.
 */
public class SequencesTab extends JPanel implements ActionListener,WaveHolder
{
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	/** Box to select the pattern to place. */
	JComboBox patternBox;
	/** Button to play the sequence. */
	JButton play;

	/** Pre-rendered version of our song. */
	public byte[] buffer;
	/** The sample rate of our song. */
	public final int sps = 30000;

	/** Construct default, building the UI. */
	public SequencesTab()
	{
		super(new BorderLayout());
		play = new JButton("Play");
		play.addActionListener(this);
		add(play,BorderLayout.NORTH);
		add(new JScrollPane(new WaveRenderPane(this)),BorderLayout.CENTER);

		//we generate a test song
		//		String intro = "6D#C5BB6#C      5E6D#C5B-B6#C";
		//		String part1 = intro + "  5-AB-E      E-B6#C-D5B#-G--A-BE-E6#C      " + intro
		//				+ "5-AB-E      -B6#C-D5B#-GA-B ";
		//		String part2 = "5EAB6C5BAG  E#E-G6-CBAAG";
		//triumph, huge success
		String p1 = "5G#FEE#F          4A5G#FE-E#F  -DE4-A      ";
		//overstate satisfaction
		String p2 = "A5-E#F-GE#-C--D-E4A-A5#-F         ";
		String p3 = "G#FEE#F          4A5G#FEE  #FD  E4-A       ";
		String p4 = "5-E#F-GE#-CDE 4A5DEFEDC  ";
		String p5 = "4A#A5-C-FEDDCDC-CC 4A#A5-C-FGFEDDE-FF ";
		String p6 = "GA#A#A-A-GFGAA-G-FDCDFF-EE#F#--F";
		String p7 = "                         ";
		String p8 = "";
		String verse1 = p1 + p2 + p3 + p4 + p5 + p6;
		String verse2 = p8;
		//		String song = p2;
		String song = verse1 + p7 + verse2;

		buffer = Note.parseSong(song,sps);
	}

	/** Return our wave data buffer.
	 * @return The sound bytes of our complete song. */
	public byte[] getWaveData()
	{
		return buffer;
	}

	/** One of our buttons was pressed, or whatever.
	 * @param e Info about the event that transpired. */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == play)
		{
			try
			{
				ReJava.playSample(buffer,sps);
			}
			catch (LineUnavailableException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Class representing a scope.
	 * @author Josh Ventura
	 */
	static class Tone
	{
		/** The frequency of this tone, in Hz. */
		int freq;
		/** The duration of this tone, in milliseconds. */
		int dur;
		/** The volume of this tone, in range of a byte. */
		int vol;

		/** 
		 * @param freq Frequency in Hz. (e.g. 400)
		 * @param dur Duration in milliseconds.
		 * @param vol Volume of this note, ranging from 0-128. */
		public Tone(int freq, int dur, int vol)
		{
			this.freq = freq;
			this.dur = dur;
			this.vol = vol;
		}
	}
}
