package seqGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.MouseInputListener;

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
	class PatternPanel extends JPanel implements MouseInputListener
	{
		/** Shut up, ECJ. */
		private static final long serialVersionUID = 1L;
		/** The currently pressed key index, or -1. */
		private int depressedKey = -1;
		/** The currently hovered key index, or -1. */
		private int highlightedKey = -1;
		/** The height used when displaying key bars. */
		private static final int BARHEIGHT = 12;
		/** The width used to draw keys. */
		private static final int KEYLENGTH = 72;
		/** The width used to draw the little dark keys. */
		private static final int DARKKEYLENGTH = 40;
		/** The number of octaves to display */
		int numOctaves = 10;

		/** Construct default, requesting enough size for our notes and ranges. */
		public PatternPanel()
		{
			super();
			setPreferredSize(new Dimension(640,numOctaves * 12 * BARHEIGHT * 2));
			setMinimumSize(new Dimension(640,640));
			setSize(640,640);
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		/** Array of light key heights. */
		int octaveKeys[] = { BARHEIGHT * 3 / 2,BARHEIGHT * 2,BARHEIGHT * 2,BARHEIGHT * 3 / 2,
				BARHEIGHT * 3 / 2,BARHEIGHT * 2,BARHEIGHT * 3 / 2 };
		/** Array of light key indices. */
		int lightKeyNum[] = { 0,2,4,6,7,9,11 };
		/** Array of dark key indices */
		int darkKeyNum[] = { 1,3,5,8,10 };

		/** Render our notes and keyboard buttons.
		 * @param g The graphics object to paint to. */
		@Override
		public void paint(Graphics g)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(),getHeight());
			Rectangle r = g.getClipBounds();
			g.setColor(Color.WHITE);
			g.fillRect(r.x,0,KEYLENGTH,getHeight());
			g.setColor(Color.BLUE);
			for (int y = 0, keyIndBase = 0; y < getHeight(); y += BARHEIGHT * 12, keyIndBase += 12)
			{
				for (int yy = y, yo = 0; yo < octaveKeys.length; yy += octaveKeys[yo++])
					reJava.Draw.drawLightKey(g,r.x,yy,KEYLENGTH,octaveKeys[yo],
							(keyIndBase + lightKeyNum[yo] == highlightedKey),
							(keyIndBase + lightKeyNum[yo] == depressedKey),2);
				int yy = y + BARHEIGHT;
				for (int i = 0; i < 3; ++i, yy += BARHEIGHT * 2)
					reJava.Draw.drawDarkKey(g, r.x,yy,DARKKEYLENGTH,BARHEIGHT,
							(keyIndBase + darkKeyNum[i] == highlightedKey),
							(keyIndBase + darkKeyNum[i] == depressedKey),2,2,4,2);
				yy += BARHEIGHT;
				for (int i = 0; i < 2; ++i, yy += BARHEIGHT * 2)
					reJava.Draw.drawDarkKey(g, r.x,yy,DARKKEYLENGTH,BARHEIGHT,
							(keyIndBase + darkKeyNum[i+3] == highlightedKey),
							(keyIndBase + darkKeyNum[i+3] == depressedKey),2,2,4,2);
			}
		}

		/** Mouse clicked: do nothing */
		@Override
		public void mouseClicked(MouseEvent arg0)
		{
		}

		/** Mouse entered */
		@Override
		public void mouseEntered(MouseEvent arg0)
		{
		}

		/** Mouse left */
		@Override
		public void mouseExited(MouseEvent me)
		{
		}

		/** Whether the mouse is being dragged across our keyboard. */
		boolean mouseDown = false;

		/** Pressing a key */
		@Override
		public void mousePressed(MouseEvent me)
		{
			depressedKey = me.getY() / BARHEIGHT;
			repaint();
		}

		/** Releasing a key */
		@Override
		public void mouseReleased(MouseEvent me)
		{
			depressedKey = -1;
			repaint();
		}

		/** Dragging over keys */
		@Override
		public void mouseDragged(MouseEvent me)
		{
			depressedKey = me.getY() / BARHEIGHT;
			highlightedKey = -1;
			repaint();
		}

		/** Moving over keys */
		@Override
		public void mouseMoved(MouseEvent me)
		{
			highlightedKey = me.getY() / BARHEIGHT;
			repaint();
		}
	}
}
