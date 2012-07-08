package seqGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import waveFunc.OscillatorPane;
import waveFunc.WaveRenderPane;

/**
 * @author Josh Ventura
 * Tab containing all channel names and instrument definitions.
 */
public class ChannelsTab extends JSplitPane implements ActionListener
{
	/** Shut up, ECJ. */
	private static final long serialVersionUID = 1L;

	// Actually used variables
	/** Oscillator pane to create custom waveforms for use as samples. */
	OscillatorPane oscPane;
	/** Map of all channels by the button used to switch to them. */
	Map<AbstractButton,Channel> channels;

	// Variables Josh decided to make global scope
	/** Tree displaying available samples. */
	JTree instrumentTree;
	/** Root of the instrument tree. */
	DefaultMutableTreeNode instrumentRoot;
	/** Panel displaying all channel switching buttons. */
	JPanel channelList;
	/** Button group to contain individual channel switch buttons. */
	ButtonGroup channelGroup;

	/**
	 * @author Josh Ventura
	 * A channel, as represented in this tab.
	 */
	public class Channel
	{
		/** The overall volume of this channel. */
		public int vol;
		/** The base frequency of notes in this channel. */
		public int freq;
		/** The maximum or default duration of notes in this channel. */
		public int dur;
		/** The equation describing the wave in this channel. */
		public String eq;

		/** Construct with reasonable defaults. */
		public Channel()
		{
			this(127,400,1000,"vol * sin(x / period * 2 * pi)");
		}

		/** Construct with more information.
		 * @param vol The volume of this channel. 
		 * @param freq The base frequency of this channel.
		 * @param dur The maximum duration of notes in this channel. 
		 * @param eq The equation of the wave depicted. */
		public Channel(int vol, int freq, int dur, String eq)
		{
			setValues(vol,freq,dur,eq);
		}

		/**
		 * Alternative to the constructor.
		 * @param vol The volume of this channel. 
		 * @param freq The base frequency of this channel.
		 * @param dur The maximum duration of notes in this channel. 
		 * @param eq The equation of the wave depicted. 
		 */
		public void setValues(int vol, int freq, int dur, String eq)
		{
			this.vol = vol;
			this.freq = freq;
			this.dur = dur;
			this.eq = eq;
		}
	}

	/** Construct this tab, laying out the UI.  */
	public ChannelsTab()
	{
		super(); // Divide the view into a tree and an editor

		// Populate the tree with a root node and add it
		instrumentRoot = new DefaultMutableTreeNode("Instruments");
		instrumentTree = new JTree(instrumentRoot);
		setLeftComponent(new JScrollPane(instrumentTree));
		instrumentTree.setShowsRootHandles(true);
		instrumentTree.setRootVisible(false);
		setResizeWeight(.2);
		setDividerLocation(128);

		populateTree();

		// Populate the editor part
		JPanel right = new JPanel();
		setRightComponent(right);

		// Divide the right side into two, vertically
		BoxLayout bl = new BoxLayout(right,BoxLayout.Y_AXIS);
		right.setLayout(bl);

		// Now construct a list to hold our channel "tab" buttons
		channelList = new JPanel();
		bl = new BoxLayout(channelList,BoxLayout.X_AXIS);
		channelList.setLayout(bl);
		JScrollPane jsp = new JScrollPane(channelList); // Make it scrollable
		jsp.setMaximumSize(new Dimension(Integer.MAX_VALUE,80)); // Make it narrow
		jsp.setMinimumSize(new Dimension(240,80)); // Make it right
		right.add(jsp);

		channelGroup = new ButtonGroup();
		channels = new HashMap<AbstractButton,Channel>();

		// Set up our main view
		//		mainView = new JPanel();
		//		CardLayout cl = new CardLayout();
		//		mainView.setLayout(cl);
		//		right.add(mainView);

		// Allocate a single channel for now
		//		RSChannel chan1 = new RSChannel("Channel 1");
		//		channels.add(chan1);
		//		mainView.add(chan1.panel);
		//		channelList.add(chan1.button);
		//		channelGroup.add(chan1.button);

		// Set up our main, updatable view
		JPanel panel = new JPanel();
		bl = new BoxLayout(panel,BoxLayout.X_AXIS);
		panel.setLayout(bl);
		right.add(panel);

		Channel chan;
		AbstractButton b = makeButton(chan = new Channel(),"Channel 1",
				"Or how I learned to stop worrying and love the bomb");
		makeButton(new Channel(),"Channel 2","Faux News");
		b.setSelected(true);
		oscPane = new OscillatorPane(chan);
		WaveRenderPane wrp = new WaveRenderPane(oscPane);
		oscPane.setRenderPane(wrp);
		panel.add(new JScrollPane(wrp));
		panel.add(oscPane);
	}

	/**
	 * @author Josh Ventura
	 * A tree node containing a sample name to display and File to open.
	 */
	class SampleNode
	{
		/** The user-friendly name of this sample. */
		public String sampleName;
		/** The File which can be opened to read the sound data for this sample. */
		public File sampleFile = null;

		/** Get the user-friendly string representation. */
		@Override
		public String toString()
		{
			return sampleName;
		}

		/** Construct with only a name.
		 * @param n The name of the sample. */
		public SampleNode(String n)
		{
			sampleName = n;
		}

		/** Construct with only a name and File.
		 * @param n The name of the sample.
		 * @param f The file to open to read the sample data. */
		public SampleNode(String n, File f)
		{
			sampleName = n;
			sampleFile = f;
		}
	}

	/** Populates the instrument tree with all available samples. */
	private void populateTree()
	{
		File a = new File(System.getProperty("user.home"));
		DefaultMutableTreeNode nnode = new DefaultMutableTreeNode(new SampleNode("Samples"));
		instrumentRoot.add(nnode);
		instrumentTree.expandPath(new TreePath(instrumentRoot));
		if (a.exists())
		{
			a = new File(a,"ReSequence");
			if (a.exists())
			{
				a = new File(a,"Samples");
				if (a.exists()) subPopulateTree(a,nnode);
			}
		}
	}

	/**
	 * Read samples from a directory, recursively.
	 * @param dir The directory to read samples from recursively.
	 * @param samplenode The node in the tree which corresponds to the given directory.
	 */
	private void subPopulateTree(File dir, DefaultMutableTreeNode samplenode)
	{
		for (String fn : dir.list())
		{
			File sample = new File(dir,fn);
			DefaultMutableTreeNode nnode;
			SampleNode nsnode = new SampleNode(fn);
			if (sample.isDirectory())
				subPopulateTree(sample,nnode = new DefaultMutableTreeNode(nsnode));
			else
			{
				nsnode.sampleFile = sample;
				nnode = new DefaultMutableTreeNode(nsnode);
			}
			samplenode.add(nnode);
		}
	}

	/**
	 * Create a button for a channel.
	 * @param chan The Channel the button represents.
	 * @param name The name of the channel.
	 * @param desc A short description of the channel.
	 * @return
	 */
	AbstractButton makeButton(Channel chan, String name, String desc)
	{
		AbstractButton b = new JToggleButton("<html><b>" + name + "</b><br />" + desc + "</html>");
		b.setMaximumSize(new Dimension(96,Integer.MAX_VALUE));
		b.setMinimumSize(new Dimension(64,64)); // Make it narrow
		b.setPreferredSize(new Dimension(96,64));
		b.setMargin(new Insets(1,1,1,1));
		b.setFont(new Font(Font.SANS_SERIF,0,10));
		b.setAlignmentX(0);
		b.setAlignmentY(0);
		b.addActionListener(this);
		channelGroup.add(b);
		channelList.add(b);
		channels.put(b,chan);
		return b;
	}

	/*** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof AbstractButton)
		{
			Channel c = channels.get(o);
			if (c != null) oscPane.setChannel(c);
		}
	}
}
