package seqGUI;

import java.awt.Graphics;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import waveFunc.WaveRenderPane;
import waveFunc.WaveRenderPane.WaveHolder;

public class PatternsTab extends JTabbedPane
{
	private static final long serialVersionUID = 1L;

	public PatternsTab()
	{
		super(JTabbedPane.LEFT);
		TestPanel p = new TestPanel();
		add("Test",new JScrollPane(new WaveRenderPane(p)
		{
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				super.paint(g);
				final int height = 128, origin = height + 10;
				double scale = (double) getWidth() / cache.length;

				int vol1 = 10, xoff1 = 57;
				double freq1 = 170.0;
				int vol2 = 10, xoff2 = -1;
				double freq2 = 21.25;
				int vol3 = 10, xoff3 = 25;
				double freq3 = 42.5;
				for (int i = 0; i < cache.length; i++)
				{
					//					byte b = 0;
					byte b = (byte) (vol1 * Math.sin((i + xoff1) / freq1 * 2 * Math.PI));
					b += (byte) (vol2 * Math.sin((i + xoff2) / freq2 * 2 * Math.PI));
					b += (byte) (vol3 * Math.sin((i + xoff3) / freq3 * 2 * Math.PI));
					g.drawLine((int) (i * scale),b + origin,(int) ((i + 1) * scale),b + origin);
				}
			}
		}));
		for (int i = 1; i < 7; i++)
			add("Example " + i,new PatternPanel());
	}

	class TestPanel extends JPanel implements WaveHolder
	{
		private static final long serialVersionUID = 1L;

		byte[] data;

		TestPanel()
		{
			File f = new File(System.getProperty("user.home"),"trumpet_c4.wav");
			try
			{
				//				AudioInputStream ais = AudioSystem.getAudioInputStream(f);

				InputStream in = new FileInputStream(f);
				byte data[] = new byte[(int) f.length() - 64];
				in.skip(64);

				// Read in the bytes
				int offset = 0;
				int numRead = 0;
				while (offset < data.length && (numRead = in.read(data,offset,data.length - offset)) >= 0)
					offset += numRead;

				// Ensure all the bytes have been read in
				if (offset < data.length) throw new EOFException(f.getName());

				// Close the input stream and return bytes
				in.close();

				this.data = new byte[data.length / 2];
				for (int i = 0; i < this.data.length - 1; i += 1)
					this.data[i] = data[i * 2 + 1];

				//				ReJava.play(data,44100);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public byte[] getWaveData()
		{
			return data;
		}

	}

	class PatternPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PatternPanel()
		{
			super();
		}
	}
}
