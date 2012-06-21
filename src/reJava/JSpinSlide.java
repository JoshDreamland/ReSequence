package reJava;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Widget class displayed as a spin box and slider. */
public class JSpinSlide extends JPanel implements ChangeListener,MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	protected JSlider slider;
	protected SpinnerNumberModel spinModel;
	protected boolean snap = false;

	/** Default constructor */
	public JSpinSlide()
	{
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(slider = new JSlider());
		add(new JSpinner(spinModel = new SpinnerNumberModel()));
	}

	/** Construct with bound information and a default value.
	 * @param lower The lower bound for the value.
	 * @param upper The upper bound for the value.
	 * @param value The default value.
	 **/
	public JSpinSlide(int lower, int upper, int value)
	{
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(slider = new JSlider(lower,upper,value));
		add(new JSpinner(spinModel = new SpinnerNumberModel(value,lower,upper,1)));
		slider.addChangeListener(this);
		slider.addMouseMotionListener(this);
		spinModel.addChangeListener(this);
	}

	/** @return Returns the current value of the spinner/slider combo. */
	public int getValue()
	{
		return slider.getValue(); //or you can use the spinner, doesn't matter
	}

	/** Sets the value displayed in the spinner. 
	 * @param n The new value. */
	public void setValue(int n)
	{
		slider.setValue(n);
	}

	/** Tests whether the slider is adjusting.
	 * @return True if the slider is adjusting, false otherwise.
	 */
	public boolean isAdjusting()
	{
		return slider.getValueIsAdjusting();
	}

	/** Add a listener to be informed when the value of this spinner changes. 
	 * @param l The listener to add. */
	public void addChangeListener(ChangeListener l)
	{
		listenerList.add(ChangeListener.class,l);
	}

	protected transient ChangeEvent changeEvent = null;

	protected void fireStateChanged()
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ChangeListener.class)
			{
				if (changeEvent == null) changeEvent = new ChangeEvent(this);
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	/**
	 * This method sets the major tick spacing and visibility.
	 * If you have a slider with a range from 0 to 50 and the tick spacing
	 * is set to 10, you will get major ticks next to the following values:
	 * 0, 10, 20, 30, 40, 50.
	 * @param visible Whether the tickmarks are visible.
	 * @param spacing The value margin between ticks.
	 */
	public void setMajorTicks(boolean visible, int spacing)
	{
		slider.setPaintTicks(visible);
		slider.setMajorTickSpacing(spacing);
	}

	private boolean changing = false;

	/**
	 * Fires a state change event.
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (changing) return;
		changing = true;
		if (e.getSource() == slider)
			spinModel.setValue(slider.getValue());
		else if (e.getSource() == spinModel) slider.setValue(spinModel.getNumber().intValue());
		fireStateChanged();
		changing = false;
	}

	/** Fires a mouse drag event. */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		boolean ctrl = (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
		if (ctrl != snap) slider.setSnapToTicks(snap = ctrl);
	}

	/** Doesn't actually do anything. */
	@Override
	public void mouseMoved(MouseEvent e)
	{
	}
}
