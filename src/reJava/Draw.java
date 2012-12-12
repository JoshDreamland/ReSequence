package reJava;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;

/**
 * @author Josh Ventura
 * Draw things.
 */
public class Draw
{
	/**
	 * Draw a horizontally graded fucking rectangle.
	 * @param g The graphics object to draw on.
	 * @param x1 The top-left x-coordinate.
	 * @param y1 The top-left y-coordinate.
	 * @param w The width of this rectangle.
	 * @param h The height of this rectangle.
	 * @param c1 The top-left color.
	 * @param c2 The top-right color.
	 */
	public static void rectangleColor(Graphics2D g, int x1, int y1, int w, int h, Color c1, Color c2)
	{
		LinearGradientPaint gp = new LinearGradientPaint(new Point(x1 + (w - h) / 4,y1),new Point(x1
				+ (w + h) * 3 / 4,y1 + h),new float[] { 0,1 },new Color[] { c1,c2 });
		Paint op = g.getPaint();
		g.setPaint(gp);
		g.fillRect(x1,y1,w,h);
		g.setPaint(op);
	}

	/**
	 * @param dark True if this key is dark, false if it is light.
	 * @param highlighted Whether this key should be drawn with a highlight.
	 * @param depressed Whether this key should be drawn as if it is pushed.
	 * @return
	 */
	private static Color[] getKeyColors(boolean dark, boolean highlighted, boolean depressed)
	{
		if (depressed)
			return new Color[] { new Color(0f,1f,0f),new Color(0f,.75f,0f) };
		else if (highlighted)
			return new Color[] { new Color(.75f,.75f,.9f),new Color(.5f,.5f,.9f) };
		else if (dark)
			return new Color[] { new Color(.8f,.9f,.9f),new Color(0f,0f,0f) };
		else
			return new Color[] { new Color(1f,1f,.9f),new Color(1f,1f,.6f) };

	}

	/**
	 * Draw a light key.
	 * @param g The graphics object to draw on.
	 * @param x The top-left x-coordinate of the key.
	 * @param y The top-right y-coordinate of the key.
	 * @param w The width of the key.
	 * @param h The height of the key.
	 * @param highlighted True if the key should be given a highlight tint.
	 * @param depressed True if the key is pressed.
	 * @param margin The amount to bevel the key.
	 */
	public static void drawLightKey(Graphics g, int x, int y, int w, int h, boolean highlighted,
			boolean depressed, int margin)
	{
		Color c[] = getKeyColors(false,highlighted,depressed);
		rectangleColor((Graphics2D) g,x,y,w,h,c[1],c[0]);
		rectangleColor((Graphics2D) g,x + margin,y + margin,w - margin * 2,h - margin * 2,c[0],c[1]);
		g.setColor(new Color(c[1].getRed()/2, c[1].getGreen()/2, c[1].getBlue()/2));
		g.drawRect(x,y,w,h);
	}
	/**
	 * Draw a dark key.
	 * @param g The graphics object to draw on.
	 * @param x The top-left x-coordinate of the key.
	 * @param y The top-right y-coordinate of the key.
	 * @param w The width of the key.
	 * @param h The height of the key.
	 * @param highlighted True if the key should be given a highlight tint.
	 * @param depressed True if the key is pressed.
	 * @param m1 The amount to bevel the key on the left.
	 * @param m2 The amount to bevel the key on the top.
	 * @param m3 The amount to bevel the key on the right.
	 * @param m4 The amount to bevel the key on the bottom.
	 */
	public static void drawDarkKey(Graphics g, int x, int y, int w, int h, boolean highlighted,
			boolean depressed, int m1, int m2, int m3, int m4)
	{
		Color c[] = getKeyColors(true,highlighted,depressed);
		rectangleColor((Graphics2D) g,x,y,w,h,c[1],c[0]);
		rectangleColor((Graphics2D) g,x + m1,y + m2,w - m1 - m3,h - m2 - m4,c[0],c[1]);
	}

}
