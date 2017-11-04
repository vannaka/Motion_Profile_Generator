package pathGenerator;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.modifiers.TankModifier;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class Gui2 {

	private JFrame frame;
	private JTextField txtTime;
	private JTextField txtVelocity;
	private JTextField txtAcceleration;
	private JTextField txtJerk;
	private JTextField txtWheelBase;
	
	FalconLinePlot fig3 = new FalconLinePlot(new double[][]{{0.0,0.0}});
	FalconLinePlot fig4 = new FalconLinePlot(new double[][]{{0.0,0.0}});
	
	private JTextField txtAngle;
	private JTextField txtXValue;
	private JTextField txtYValue;
	
	private JTextArea txtAreaWaypoints;
	
	// Path Waypoints 
	//private Waypoint[] points;
	private List<Waypoint> points = new ArrayList<Waypoint>();			// can be variable length after creation
	
	/**
	 * Create the application.
	 */
	public Gui2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setBounds(100, 100, 463, 600);
		frame.setLocation(150, 100);
		frame.setSize(1693, 645);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		fig3.setSize(600, 600);
		fig3.setLocation(460, 0);
		frame.getContentPane().add(fig3);
		
		fig4.setSize(600, 600);
		fig4.setLocation(1070, 0);
		frame.getContentPane().add(fig4);
		
		JPanel trajecPanel = new JPanel();
		trajecPanel.setBounds(0, 0, 450, 600);
		frame.getContentPane().add(trajecPanel);
		trajecPanel.setLayout(null);
		
		JLabel lblTimeStep = new JLabel("Time Step");
		lblTimeStep.setBounds(113, 63, 61, 14);
		trajecPanel.add(lblTimeStep);
		
		JLabel lblVelocity = new JLabel("Velocity");
		lblVelocity.setBounds(113, 94, 46, 14);
		trajecPanel.add(lblVelocity);
		
		JLabel lblAcceleration = new JLabel("Acceleration");
		lblAcceleration.setBounds(113, 125, 77, 14);
		trajecPanel.add(lblAcceleration);
		
		JLabel lblJerk = new JLabel("Jerk");
		lblJerk.setBounds(113, 156, 46, 14);
		trajecPanel.add(lblJerk);
		
		txtTime = new JTextField();
		txtTime.setText("0.05");
		txtTime.setBounds(212, 60, 86, 20);
		trajecPanel.add(txtTime);
		txtTime.setColumns(10);
		
		txtVelocity = new JTextField();
		txtVelocity.setText("4");
		txtVelocity.setBounds(212, 91, 86, 20);
		trajecPanel.add(txtVelocity);
		txtVelocity.setColumns(10);
		
		txtAcceleration = new JTextField();
		txtAcceleration.setText("3");
		txtAcceleration.setBounds(212, 122, 86, 20);
		trajecPanel.add(txtAcceleration);
		txtAcceleration.setColumns(10);
		
		txtJerk = new JTextField();
		txtJerk.setText("60");
		txtJerk.setBounds(212, 153, 86, 20);
		trajecPanel.add(txtJerk);
		txtJerk.setColumns(10);
		
		JButton btnGeneratePath = new JButton("Generate Path");
		btnGeneratePath.setBounds(158, 566, 130, 23);
		trajecPanel.add(btnGeneratePath);
		
		btnGeneratePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		JLabel lblMotionVariables = new JLabel("Motion Variables");
		lblMotionVariables.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblMotionVariables.setBounds(131, 11, 188, 41);
		trajecPanel.add(lblMotionVariables);
		
		JLabel lblWaypoints = new JLabel("Waypoints");
		lblWaypoints.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWaypoints.setBounds(158, 235, 130, 29);
		trajecPanel.add(lblWaypoints);
		
		txtWheelBase = new JTextField();
		txtWheelBase.setText("1.464");
		txtWheelBase.setBounds(212, 191, 86, 20);
		trajecPanel.add(txtWheelBase);
		txtWheelBase.setColumns(10);
		
		JLabel lblWheelBase = new JLabel(" Wheel Base       ");
		lblWheelBase.setHorizontalAlignment(SwingConstants.LEFT);
		lblWheelBase.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblWheelBase.setBounds(113, 194, 90, 14);
		trajecPanel.add(lblWheelBase);
		
		txtAngle = new JTextField();
		txtAngle.setBounds(254, 325, 34, 20);
		trajecPanel.add(txtAngle);
		txtAngle.setColumns(10);
		
		txtAreaWaypoints = new JTextArea();
		txtAreaWaypoints.setText(" X         Y      Angle\r\n______________\n");
		txtAreaWaypoints.setBounds(131, 406, 188, 133);
		trajecPanel.add(txtAreaWaypoints);
		
		JButton btnAddPoint = new JButton("Add Point");
		btnAddPoint.setBounds(179, 372, 89, 23);
		trajecPanel.add(btnAddPoint);
		
		btnAddPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnAddPointActionPerformed(evt);
            }
        });
		
		txtXValue = new JTextField();
		txtXValue.setBounds(156, 325, 34, 20);
		trajecPanel.add(txtXValue);
		txtXValue.setColumns(10);
		
		txtYValue = new JTextField();
		txtYValue.setBounds(202, 325, 34, 20);
		trajecPanel.add(txtYValue);
		txtYValue.setColumns(10);
		
		JLabel lblX = new JLabel("X");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblX.setBounds(168, 306, 19, 14);
		trajecPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblY.setBounds(217, 306, 19, 14);
		trajecPanel.add(lblY);
		
		JLabel lblAngle = new JLabel("Angle");
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAngle.setBounds(254, 302, 34, 23);
		trajecPanel.add(lblAngle);
		
		motionGraph();
		velocityGraph();
	};
	
	private void motionGraph()
	{
		// Create a blank grid for the field graph
		fig3.yGridOn();
		fig3.xGridOn();
		fig3.setYLabel("Y (feet)");
		fig3.setXLabel("X (feet)");
		fig3.setTitle("Top Down View of FRC Field (30ft x 27ft) \n shows global position of robot path, along with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 27.0;
		fig3.setXTic(0, 30, 1);
		fig3.setYTic(0, fieldWidth, 1);
					
					
		//lets add field markers to help visual
		double[][] airShip = new double[][]{
				{9.443, 11.805},
				{12.381, 10.11},
				{15.318, 11.805},
				{15.318, 15.195},
				{12.381, 16.89},
				{9.443, 15.195},
				{9.443, 11.805},
			};
		fig3.addData(airShip, Color.black);
					
		// Auto Line
		double[][] baseLine = new double[][] {{7.77,0}, {7.77, fieldWidth}};
		fig3.addData(baseLine, Color.black);
						
		// Mid Field
		double[][] midLine = new double[][] {{27.3,0}, {27.3, fieldWidth}};
		fig3.addData(midLine, Color.black);
						
		// Boiler
		double[][] blueSideBoiler = new double[][] {{0,24.79}, {2.479,27}};
		fig3.addData(blueSideBoiler, Color.black);
						
		// Boiler Line
		double[][] blueSideBoilerLine = new double[][] {{0,18.61}, {8.39,27}};
		fig3.addData(blueSideBoilerLine, Color.blue);
						
		// Retrieval Zone
		double[][] blueSideRetrieval = new double[][] {{0,3.166}, {5.45,0}};
		fig3.addData(blueSideRetrieval, Color.black);
						
		// Retrieval Zone Line
		double[][] blueSideRetrievalLine = new double[][] {{0,7}, {13.75,0}};
		fig3.addData(blueSideRetrievalLine, Color.red);
	}
	
	private void velocityGraph()
	{
		fig4.yGridOn();
      	fig4.xGridOn();
      	fig4.setYLabel("Velocity (ft/sec)");
      	fig4.setXLabel("time (seconds)");
      	fig4.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
	}
		
	private void btnActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
		double timeStep = Double.parseDouble(txtTime.getText()); 				//default 0.05
		double velocity = Double.parseDouble(txtVelocity.getText()); 			//default 4
		double acceleration = Double.parseDouble(txtAcceleration.getText()); 	// default 3	
		double jerk = Double.parseDouble(txtJerk.getText()); 					// default 60
		double wheelBase = Double.parseDouble(txtWheelBase.getText());  		//default 1.464
		
		// If waypoints exist
		if( points.size() > 1 )
		{
			Waypoint tmp[] = new Waypoint[ points.size() ];
			points.toArray( tmp );
			trajectory( timeStep, velocity, acceleration, jerk, wheelBase, tmp );
		}
		else
		{
			JOptionPane.showMessageDialog(null, "We need at least two points to generate a profile.", "Insufficient Points.", JOptionPane.INFORMATION_MESSAGE);
		}
		
    };
    
    private void btnAddPointActionPerformed(java.awt.event.ActionEvent evt)
    {
    	double xValue = 0;
    	double yValue = 0;
    	double angle = 0;
    	
    	// get x value
    	try
    	{
    		xValue = Double.parseDouble(txtXValue.getText());   		
    	}
    	catch ( Exception e )
    	{
    		JOptionPane.showMessageDialog(null, "The X value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
    		return;
    	}
    	
    	// get y value
    	try
    	{
			yValue = Double.parseDouble(txtYValue.getText());
	    }
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog(null, "The Y value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// get angle value
    	try
    	{
			angle = Double.parseDouble(txtAngle.getText());
	    }
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog(null, "The Angle value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
				
		txtAreaWaypoints.append(Double.toString(xValue) + "     " + Double.toString(yValue) + "     " + Double.toString(angle) + "\n");
		
		// add new point to points list
		points.add( new Waypoint(xValue, yValue, Pathfinder.d2r(angle)));
		
		txtXValue.setText("");
		txtYValue.setText("");
		txtAngle.setText("");
		        
    }
    
    private void trajectory(double timeStep, double velocity, double acceleration, double jerk, double wheelBase, Waypoint[] points) throws IOException
    {
    	File lFile = new File("mp_left.csv");
		File rFile = new File("mp_right.csv");
		FileWriter lfw = new FileWriter( lFile );
		FileWriter rfw = new FileWriter( rFile );
		PrintWriter lpw = new PrintWriter( lfw );
		PrintWriter rpw = new PrintWriter( rfw );
		
		// Configure the trajectory with the time step, velocity, acceleration, jerk
		Trajectory.Config config = new Trajectory.Config( Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, timeStep, velocity, acceleration, jerk );
             
		// Generate the path
		Trajectory trajectory = Pathfinder.generate(points, config);
		
        // Tank drive modifier with the wheel base
        TankModifier modifier = new TankModifier(trajectory).modify(wheelBase);

        // Separate the trajectory into left and right
        Trajectory left = modifier.getLeftTrajectory();
        Trajectory right = modifier.getRightTrajectory();
        
        // Left and Right paths to display on the Field Graph
        double[][] leftPath = new double[left.length()][2];
        double[][] rightPath = new double[right.length()][2];
        
        for(int i = 0; i < left.length(); i++)
        {
        	leftPath[i][0] = left.get(i).x;
        	leftPath[i][1] = left.get(i).y;
        	rightPath[i][0] = right.get(i).x;
        	rightPath[i][1] = right.get(i).y;
        }
      	
       	fig3.addData(leftPath, Color.magenta);
     	fig3.addData(rightPath, Color.magenta);
     	fig3.repaint();
      	
     	// Velocity to be used in the Velocity graph
     	double[][] leftVelocity = new double[left.length()][2];
     	double[][] rightVelocity = new double[right.length()][2];
     	double[][] middleVelocity = new double[trajectory.length()][2];
     	
     	for(int i = 0; i < left.length(); i++)
     	{
     		leftVelocity[i][0] = left.segments[i].dt * i;
     		leftVelocity[i][1] = left.segments[i].velocity;
     		rightVelocity[i][0] = right.segments[i].dt * i;
     		rightVelocity[i][1] = right.segments[i].velocity;
     		middleVelocity[i][0] = trajectory.segments[i].dt * i;
     		middleVelocity[i][1] = trajectory.segments[i].velocity;
     	}
     	
      	// Velocity Graph
       	fig4.addData(leftVelocity, Color.magenta);
      	fig4.addData(rightVelocity, Color.cyan);
      	fig4.addData(middleVelocity, Color.blue);
      	fig4.repaint();
      	      	     	 	
     	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
        File leftFile = new File("mp_left_detailed.csv");
        Pathfinder.writeToCSV(leftFile, left);
        
        File rightFile = new File("mp_right_detailed.csv");
        Pathfinder.writeToCSV(rightFile, right);
        
		// CSV with position and velocity. To be used with your robot. 
		// save left path to CSV
		for (int i = 0; i < left.length(); i++) 
		{			
			Segment seg = left.get(i);
			lpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
		}
		
		// save right path to CSV
		for (int i = 0; i < right.length(); i++) 
		{			
			Segment seg = right.get(i);
			rpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
		}
		
		lpw.close();
		rpw.close();
    }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui2 window = new Gui2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
