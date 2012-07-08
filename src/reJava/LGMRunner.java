package reJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import seqGUI.MainFrame;

/** LGM Plugin interface. Program entry point is at constructor. */
public class LGMRunner implements ActionListener//,SubframeListener
{
	/** The frame to use in LateralGM's interface. */
	MainFrame f;
	/** Whether we are requesting a subframe in LateralGM. */
	boolean needsubframe = false;

	/** Constructor; specific to our class */
	public LGMRunner()
	{
		f = new MainFrame();
		//SubframeInformer.addSubframeListener(this);
	}

	/** Called when LGM opens a subframe (like a Resource Frame) */
	public void subframeAppeared(/*MDIFrame source*/)
	{
		//if (source instanceof SoundFrame) ((SoundFrame) source).edit.addActionListener(this);
	}

	/** Unused at this time. Would trigger when e.g. the sound Edit button is clicked. */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//
		f.setVisible(true);
	}

	/**
	 * Unused. Must be implemented to be a SubframeListener.
	 * Called when LGM is going to try and open a resource editor.
	 * @return Whether to consume the event and override the default editor.
	 */
	public boolean subframeRequested(/*Resource<?,?> res, ResNode node*/)
	{
		return needsubframe;
	}
}
