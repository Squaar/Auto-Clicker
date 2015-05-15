import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class AutoClicker extends JPanel implements ActionListener, ChangeListener{

	private boolean run;
	private boolean running;
	private Robot robot;
	private JSpinner spinner;

	private class Clicker extends Thread{
		@Override
		public void run(){
			running = true;
			try{
				while(run){
					sleep((int) spinner.getValue());
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
			} catch (InterruptedException e){}
			running = false;
		}
	}

	public AutoClicker(){
		run = true;
		try{
			robot = new Robot();
		} catch(AWTException e){
			e.printStackTrace();
			System.exit(1);
		}

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(500, 0, 5000, 100);
		spinner = new JSpinner(spinnerModel);
		spinner.addChangeListener(this);

		JButton start = new JButton("START");
		start.setActionCommand("START");
		start.addActionListener(this);
		JButton stop = new JButton("STOP");
		stop.setActionCommand("STOP");
		stop.addActionListener(this);

		add(new JLabel("Delay(ms):"));
		add(spinner);
		add(start);
		add(stop);
	}

	public void actionPerformed(ActionEvent e){
		switch(e.getActionCommand()){
			case "START":
				System.out.println("start");
				run = true;
				if(!running)
					new Clicker().start();
				break;
			case "STOP":
				System.out.println("stop");
				run = false;
				break;
			default:
				System.out.println(e.getActionCommand());
				break;
		}
	}

	public void stateChanged(ChangeEvent e){
		System.out.println(spinner.getValue());
	}

	private static void setUp(){
		JFrame frame = new JFrame("Auto Clicker!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		AutoClicker autoClicker = new AutoClicker();
		autoClicker.setOpaque(true);
		frame.setContentPane(autoClicker);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String args[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				setUp();
			}
		});
	}
}