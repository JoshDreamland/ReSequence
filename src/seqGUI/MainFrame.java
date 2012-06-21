package seqGUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import reJava.ReJava;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

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

	public static void main(String[] args)
	{
		//Java says this is the proper way to start using swing.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				MainFrame f = new MainFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
}
