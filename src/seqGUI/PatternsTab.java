package seqGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * @author Josh Ventura
 * Tab containing patterns of notes which can be played and repeated.
 */
public class PatternsTab extends JTabbedPane
{
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	/** Construct, building the UI. */
	public PatternsTab()
	{
		super(JTabbedPane.LEFT);
		for (int i = 1; i < 7; i++)
			add("Track " + i,new JScrollPane(new PatternPanel()));
	}

	/** Panel rendering our pattern and allowing its manipulation. */
	class PatternPanel extends JPanel
	{
		/** Shut up, ECJ. */
		private static final long serialVersionUID = 1L;

		/** Construct default, requesting enough size for our notes and ranges. */
		public PatternPanel()
		{
			super();
			setPreferredSize(new Dimension(640,640));
			setMinimumSize(new Dimension(640,640));
			setSize(640,640);
		}

		/** Render our notes and keyboard buttons.
		 * @param g The graphics object to paint to. */
		@Override
		public void paint(Graphics g)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(),getHeight());
			Rectangle r = g.getClipBounds();
			g.setColor(Color.WHITE);
			g.fillRect(r.x,r.y,64,r.height);
		}
	}
}
