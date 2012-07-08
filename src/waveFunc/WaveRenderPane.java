package waveFunc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Josh Ventura
 * Class to render the entirety of a wave for the user to view.
 */
public class WaveRenderPane extends JPanel
{
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;
	/** The object whose wave data is being depicted. */
	protected WaveHolder bufferOwner;
	/** Cached wave data to render. */
	protected byte[] cache = null;

	/**
	 * @author Josh Ventura
	 * Interface for any object which can contain a wave to be displayed.
	 */
	public static interface WaveHolder
	{
		/** @return Return the sound bytes of the wave data contained. */
		byte[] getWaveData();
	}

	/**
	 * Construct with a WaveHolder to answer us.
	 * @param bufferOwner The object which can be polled for wave data.
	 */
	public WaveRenderPane(WaveHolder bufferOwner)
	{
		this.bufferOwner = bufferOwner;
		cache = bufferOwner.getWaveData();
	}

	/** Kill the cached wavedata, forcing renew. */
	public void invalidate()
	{
		cache = null;
	}

	/** Refresh the wave data by polling the source again. */
	public void renew()
	{
		cache = bufferOwner.getWaveData();
	}

	/** Return how much space we need to render our wave. */
	@Override
	public Dimension getPreferredSize()
	{
		if (cache == null)
		{
			cache = bufferOwner.getWaveData();
			if (cache == null) return super.getPreferredSize();
		}
		return new Dimension(cache.length,276);
	}

	/** Override to render our waveform. */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (cache == null) cache = bufferOwner.getWaveData();
		if (cache == null || cache.length == 0)
		{
			g.drawString("No wave data available at this time.",0,200);
			return;
		}
		g.drawString(Integer.toString(cache.length),0,30);
		final int height = 128, origin = height + 10, ticks = 100;

		double scale = (double) getWidth() / cache.length;
		g.drawLine(0,origin - height,getWidth(),origin - height);
		g.drawLine(0,origin,getWidth(),origin);
		g.drawLine(0,origin + height,getWidth(),origin + height);
		for (int i = 0; i < cache.length - 1; i++)
		{
			g.drawLine((int) (i * scale),cache[i] + origin,(int) ((i + 1) * scale),cache[i + 1] + origin);
			if (i % ticks == 0)
			{
				Color c = g.getColor();
				g.setColor(Color.RED);
				g.drawLine(i,origin - 10,i,origin + 10);
				g.drawString(Integer.toString(i),i + 2,origin - 2);
				g.setColor(c);
			}
		} //data points
	} //paint
}
