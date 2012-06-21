package seqGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import waveFunc.OscillatorPane;
import waveFunc.WaveRenderPane;

public class ChannelsTab extends JSplitPane implements ActionListener
{
	private static final long serialVersionUID = 1L;

	//actually used variables
	OscillatorPane oscPane;
	Map<AbstractButton,Channel> channels;

	//variables Josh decided to make global scope
	JTree instrumentTree;
	DefaultMutableTreeNode instrumentRoot;
	JPanel channelList;
	ButtonGroup channelGroup;

	public class Channel
	{
		public int vol, freq, dur;
		public String eq;

		public Channel()
		{
			this(127,400,1000,"vol * sin(x / period * 2 * pi)");
		}

		public Channel(int vol, int freq, int dur, String eq)
		{
			setValues(vol,freq,dur,eq);
		}

		public void setValues(int vol, int freq, int dur, String eq)
		{
			this.vol = vol;
			this.freq = freq;
			this.dur = dur;
			this.eq = eq;
		}
	}

	public ChannelsTab()
	{
		super(); // Divide the view into a tree and an editor

		// Populate the tree with a root node and add it
		instrumentRoot = new DefaultMutableTreeNode("Instruments");
		instrumentTree = new JTree(instrumentRoot);
		setLeftComponent(new JScrollPane(instrumentTree));

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
