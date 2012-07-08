package seqGUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import reJava.ReJava;

/**
 * @author Josh Ventura
 * The main class of the ReSequencer interface.
 */
public class MainFrame extends JFrame
{
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	/** Construct, creating GUI. */
	public MainFrame()
	{
		super("ReSequence - A Josh & Ism Production");
		JMenuBar mb = new JMenuBar();
		{
			mb.add(ReJava.makeMenu("&Save|&Discard","File",new int[] { 'S','D' }));
			mb.add(ReJava.makeMenu("&New","Channel",null));
			mb.add(ReJava.makeMenu("&New","Pattern",null));
			mb.add(ReJava.makeMenu("&Edit","Sequence",null));
		}
		setJMenuBar(mb);

		JTabbedPane jtp = new JTabbedPane();
		jtp.add("Channels",new ChannelsTab());
		jtp.add("Patterns",new PatternsTab());
		jtp.add("Sequence",new SequencesTab());

		add(jtp,BorderLayout.CENTER);

		setSize(640,480);
		setLocationRelativeTo(null);
	}

	/**
	 * @param args System-passed parameters to this program.
	 */
	public static void main(String[] args)
	{
		//Java says this is the proper way to start using swing.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				UIManager.put("swing.boldMetal", false);
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				MainFrame f = new MainFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
}
