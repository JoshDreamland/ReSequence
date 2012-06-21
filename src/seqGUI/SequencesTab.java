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

public class SequencesTab extends JPanel implements ActionListener,WaveHolder
{
	private static final long serialVersionUID = 1L;

	JComboBox patternBox;
	JButton play;

	public byte[] buffer;
	public final int sps = 30000;

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

	public byte[] getWaveData()
	{
		return buffer;
	}

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

	static class Tone
	{
		int freq, dur, vol;

		/** frequency (e.g. 400), duration in ms, volume (0-128) */
		public Tone(int freq, int dur, int vol)
		{
			this.freq = freq;
			this.dur = dur;
			this.vol = vol;
		}
	}
}
