package waveFunc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import reJava.JSpinSlide;
import reJava.ReJava;
import seqGUI.ChannelsTab.Channel;
import waveFunc.WaveRenderPane.WaveHolder;

public class OscillatorPane extends JPanel implements WaveHolder,ChangeListener,ActionListener
{
	private static final long serialVersionUID = 1L;
	Channel chan;
	JSpinSlide vol;
	JSpinSlide freq, dur;
	JTextField eq;
	JButton play;
	WaveRenderPane renderPane = null;
	int sps = 40000;
	byte[] waveData = null;

	public OscillatorPane(Channel chan)
	{
		this.chan = chan;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(new JLabel("Volume:"));
		add(vol = new JSpinSlide(0,127,chan.vol));
		add(new JLabel("Frequency:"));
		add(freq = new JSpinSlide(0,sps / 4,chan.freq));
		add(new JLabel("Duration (ms):"));
		add(dur = new JSpinSlide(0,60000,chan.dur));
		add(new JLabel("Wave Equasion:"));
		add(eq = new JTextField(chan.eq));
		add(play = new JButton("Play"));
		setMaximumSize(new Dimension(200,Integer.MAX_VALUE));

		vol.setMajorTicks(true,8);
		vol.addChangeListener(this);
		freq.setMajorTicks(true,100);
		freq.addChangeListener(this);
		dur.setMajorTicks(true,2000);
		dur.addChangeListener(this);
		play.addActionListener(this);

		recalculateBytes();
	}

	public void setRenderPane(WaveRenderPane wrp)
	{
		renderPane = wrp;
	}

	public Object saw(double x) {
		return (x / (2 * Math.PI))*2 % 2 - 1;
	}
	public static class MathEnviron
	{
		static ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine;

		public MathEnviron()
		{
			engine = factory.getEngineByName("JavaScript");
			try
			{
				engine.eval("sin=Math.sin; cos=Math.cos; tan=Math.tan; min=Math.min; " +
						"max=Math.max; round=Math.round; rand=random=Math.random; " +
						"pow=Math.pow; exp=Math.exp; log=Math.log; ceil=Math.ceil; floor=Math.floor; " +
						"abs=Math.abs; acos=Math.acos; asin=Math.asin; atan=Math.atan; " +
						"E=e=Math.E; PI=pi=Math.PI; SQRT2=sqrt2=Math.SQRT2; SQRT1_2=sqrt1_2=Math.SQRT1_2; " +
						"LN2=ln2=Math.LN2; LN10=ln10=Math.LN10; LOG2E=log2e=Math.LOG2E; LOG10E=log10e=Math.LOG10E");
			}
			catch (ScriptException e) {
				System.err.println("This wasn't supposed to happen.");
			}
		}

		public void put(String key, Object value)
		{
			engine.put(key,value);
		}

		public double eval(String expression)
		{
			try
			{
				return (Double) engine.eval(expression);
			}
			catch (ScriptException e)
			{
				System.err.println("You fucked up the script: " + e.getMessage());
				return 0;
			}
		}
	}

	public static MathEnviron myEnviron = new MathEnviron();

	double eval(String expression)
	{
		//FIXME: Recasting from 'Double' to 'double' to 'Double' to 'double'?
		return (Double) myEnviron.eval(expression);
	}

	public void recalculateBytes()
	{
		chan.setValues(vol.getValue(),freq.getValue(),dur.getValue(),eq.getText());
		int duration = (int) (dur.getValue() / 1000.0 * sps);

		myEnviron.put("sps",sps);
		myEnviron.put("bps",sps);
		myEnviron.put("dur",duration);
		myEnviron.put("period",sps/(double)freq.getValue());
		myEnviron.put("freq",freq.getValue());
		myEnviron.put("vol",vol.getValue());

		byte[] ret = new byte[duration];
		myEnviron.put("z",ret);
		String script = "function calc(x) { return " + eq.getText() + "; }";
		script += " for (i = 0; i < " + duration + "; i++) z[i] = calc(i); z;";
		try
		{
			ret = (byte[]) myEnviron.engine.eval(script);
		}
		catch (ScriptException e)
		{
			System.err.println(e.getMessage());
		}

//		byte[] ret = new byte[duration];
//		for (int i = 0; i < ret.length; i++)
//		{
//			ret[i] = (byte) (vol.getValue() * Math.sin(i / (sps / (double) freq.getValue()) * 2 * Math.PI));
//			//			ret[i] = (byte) eval(eq.getText(),"x=" + i + ", freq=" + freq.getValue() + ", vol="
//			//					+ vol.getValue());
//		}
		for (int i = 0; i < 100 && i < ret.length; i++)
			System.out.print(ret[i] + ", ");
		System.out.println();
		waveData = ret;
		if (renderPane != null)
		{
			renderPane.renew();
			renderPane.updateUI();
		}
	}

	public boolean updating = false;

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (updating) return;
		if (e.getSource() instanceof JSpinSlide)
			if (!((JSpinSlide) e.getSource()).isAdjusting()) recalculateBytes();
	}

	public void setChannel(Channel chan)
	{
		if (chan == this.chan) return;
		this.chan = chan;
		updating = true;
		this.vol.setValue(chan.vol);
		this.freq.setValue(chan.freq);
		this.dur.setValue(chan.dur);
		this.eq.setText(chan.eq);
		updating = false;
		recalculateBytes();
	}

	public byte[] getWaveData()
	{
		return waveData;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == play)
		{
			try
			{
				ReJava.playSample(waveData,sps);
			}
			catch (LineUnavailableException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
