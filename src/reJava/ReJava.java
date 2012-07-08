package reJava;

import java.awt.event.InputEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** Class offering lazy-man wrappers to swing. */
public final class ReJava
{
	/**
	 * @param items A string containing item names for the menu, separated by a pipe symbol (|).
	 * @param label The label to give to the menu.
	 * @param shortcuts An array of shortcut keystrokes, as integers.
	 * @return Returns a usable JMenu.
	 */
	public static JMenu makeMenu(String items, String label, int shortcuts[])
	{
		JMenu ret = new JMenu();
		if (label != null) ret.setText(label);
		String[] a = items.split("\\|");
		int i = 0;
		for (String b : a)
		{
			if (b.equals("-"))
				ret.addSeparator();
			else
			{
				JMenuItem tmenui = new JMenuItem();
				if (shortcuts != null && shortcuts[i] != 0)
				{
					int mods = 0;
					mods |= InputEvent.SHIFT_MASK;
					mods |= InputEvent.CTRL_MASK;
					mods |= InputEvent.ALT_MASK;
					KeyStroke ks = KeyStroke.getKeyStroke(shortcuts[i],mods);
					tmenui.setAccelerator(ks);
				}
				int io = b.replace("\\\\","..").replace("\\&","..").indexOf("&");
				if (io != -1)
				{
					tmenui.setMnemonic(b.charAt(io + 1));
					b = b.substring(0,io) + b.substring(io + 1);
				}
				tmenui.setText(b);
				ret.add(tmenui);
			}
			i++;
		}
		return ret;
	}

	/**
	 * Plays sound data in an array.
	 * @param data The sound bytes to play.
	 * @param sps The sample rate of the sound data, in samples per second.
	 * @throws LineUnavailableException Thrown in case of general failure.
	 */
	public static void playSample(byte[] data, int sps) throws LineUnavailableException
	{
		AudioFormat afmt = new AudioFormat(sps,8,1,true,false);
		//		AudioFormat afmt = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,sps,8,1,((8 + 7) / 8) * 1,
		//				sps,false);
		DataLine.Info info = new DataLine.Info(Clip.class,afmt);
		final Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(afmt,data,0,data.length);

		new Thread()
		{
			public void run()
			{
				clip.start();
				try
				{
					do
						Thread.sleep(99);
					while (clip.isActive());
				}
				catch (InterruptedException e)
				{
				}
				System.out.println("Clip done");
				clip.stop();
				clip.close();
			}
		}.start();
	}
}
