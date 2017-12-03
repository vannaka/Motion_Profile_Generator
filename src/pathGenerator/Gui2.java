package pathGenerator;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.modifiers.TankModifier;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class Gui2 {

	private JFrame frmMotionProfileGenerator;
	
	private JTextField txtTime;
	private JTextField txtVelocity;
	private JTextField txtAcceleration;
	private JTextField txtJerk;
	private JTextField txtWheelBase;
	private JTextField txtAngle;
	private JTextField txtXValue;
	private JTextField txtYValue;
	private JTextField txtFileName;
	
	private JTabbedPane tabbedPane;
	
	FalconLinePlot blueAllianceGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
	FalconLinePlot velocityGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
	FalconLinePlot redAllianceGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
			
	private JTextArea txtAreaWaypoints;
	
	private JFileChooser fileChooser;
	private File directory;
	
	// Path Waypoints 
	//private Waypoint[] points;
	private List<Waypoint> points = new ArrayList<Waypoint>(); // can be variable length after creation
	
	Trajectory left;
	Trajectory right;
	
	File lFile;
	File rFile;
	
	String fileName;
		
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
		frmMotionProfileGenerator = new JFrame();
		frmMotionProfileGenerator.setResizable(false);
		frmMotionProfileGenerator.setTitle("Motion Profile Generator");
		frmMotionProfileGenerator.setLocation(150, 100);
		frmMotionProfileGenerator.setSize(1075, 677);
		frmMotionProfileGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMotionProfileGenerator.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(460, 22, 600, 617);
		frmMotionProfileGenerator.getContentPane().add(tabbedPane);
		
		velocityGraph.setSize(600, 600);
		velocityGraph.setLocation(1070, 0);
		
		JPanel trajecPanel = new JPanel();
		trajecPanel.setBounds(0, 22, 450, 617);
		frmMotionProfileGenerator.getContentPane().add(trajecPanel);
		trajecPanel.setLayout(null);
		
		JLabel lblTimeStep = new JLabel("Time Step");
		lblTimeStep.setBounds(142, 60, 80, 20);
		trajecPanel.add(lblTimeStep);
		
		JLabel lblVelocity = new JLabel("Velocity");
		lblVelocity.setBounds(142, 90, 80, 20);
		trajecPanel.add(lblVelocity);
		
		JLabel lblAcceleration = new JLabel("Acceleration");
		lblAcceleration.setBounds(142, 120, 80, 20);
		trajecPanel.add(lblAcceleration);
		
		JLabel lblJerk = new JLabel("Jerk");
		lblJerk.setBounds(142, 150, 80, 20);
		trajecPanel.add(lblJerk);
		
		txtTime = new JTextField();
		txtTime.setText("0.05");
		txtTime.setBounds(222, 60, 86, 20);
		trajecPanel.add(txtTime);
		txtTime.setColumns(10);
		
		txtVelocity = new JTextField();
		txtVelocity.setText("4");
		txtVelocity.setBounds(222, 90, 86, 20);
		trajecPanel.add(txtVelocity);
		txtVelocity.setColumns(10);
		
		txtAcceleration = new JTextField();
		txtAcceleration.setText("3");
		txtAcceleration.setBounds(222, 120, 86, 20);
		trajecPanel.add(txtAcceleration);
		txtAcceleration.setColumns(10);
		
		txtJerk = new JTextField();
		txtJerk.setText("60");
		txtJerk.setBounds(222, 150, 86, 20);
		trajecPanel.add(txtJerk);
		txtJerk.setColumns(10);
		
		JButton btnGeneratePath = new JButton("Generate Path");
		btnGeneratePath.setBounds(90, 566, 130, 24);
		trajecPanel.add(btnGeneratePath);
		
		btnGeneratePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnGeneratePathActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		JButton btnAddPoint = new JButton("Add Point");
		btnAddPoint.setBounds(130, 329, 90, 20);
		trajecPanel.add(btnAddPoint);
		
		btnAddPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnAddPointActionPerformed(evt);
            }
        });
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(230, 328, 90, 20);
		trajecPanel.add(btnClear);
		
		btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnClearActionPerformed(evt);
            }
        });
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(334, 522, 89, 24);
		trajecPanel.add(btnBrowse);
		
		btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnBrowseActionPerformed(evt);
            }
        });
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(230, 566, 130, 24);
		trajecPanel.add(btnSave);
		
		btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					btnSaveActionPerformed(evt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		JLabel lblMotionVariables = new JLabel("Motion Variables");
		lblMotionVariables.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblMotionVariables.setBounds(137, 11, 176, 40);
		trajecPanel.add(lblMotionVariables);
		
		JLabel lblWaypoints = new JLabel("Waypoints");
		lblWaypoints.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWaypoints.setBounds(170, 230, 110, 40);
		trajecPanel.add(lblWaypoints);
		
		txtWheelBase = new JTextField();
		txtWheelBase.setText("1.464");
		txtWheelBase.setBounds(222, 180, 86, 20);
		trajecPanel.add(txtWheelBase);
		txtWheelBase.setColumns(10);
		
		JLabel lblWheelBase = new JLabel(" Wheel Base       ");
		lblWheelBase.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblWheelBase.setBounds(142, 180, 80, 20);
		trajecPanel.add(lblWheelBase);
		
		txtAngle = new JTextField();
		txtAngle.setBounds(252, 298, 34, 20);
		trajecPanel.add(txtAngle);
		txtAngle.setColumns(10);
		
		txtAreaWaypoints = new JTextArea();
		txtAreaWaypoints.setEditable(false);
		txtAreaWaypoints.setFont(new Font("Monospaced", Font.PLAIN, 14));
		String format = "%1$4s %2$6s %3$9s";
    	String line = String.format(format, "X", "Y", "Angle");
		txtAreaWaypoints.append(line + "\n");
		txtAreaWaypoints.append("_______________________" + "\n");
		txtAreaWaypoints.setBounds(131, 363, 188, 176);
		//trajecPanel.add(txtAreaWaypoints);
					
		txtXValue = new JTextField();
		txtXValue.setBounds(164, 298, 34, 20);
		trajecPanel.add(txtXValue);
		txtXValue.setColumns(10);
		
		txtYValue = new JTextField();
		txtYValue.setBounds(208, 298, 34, 20);
		trajecPanel.add(txtYValue);
		txtYValue.setColumns(10);
		
		JLabel lblX = new JLabel("X");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblX.setBounds(176, 275, 10, 20);
		trajecPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblY.setBounds(220, 275, 10, 20);
		trajecPanel.add(lblY);
		
		JLabel lblAngle = new JLabel("Angle");
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAngle.setBounds(252, 275, 34, 20);
		trajecPanel.add(lblAngle);
		
		JScrollPane scrollPane = new JScrollPane(txtAreaWaypoints, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(130, 360, 190, 134);
		trajecPanel.add(scrollPane);
		
		txtFileName = new JTextField();
		txtFileName.setBounds(117, 524, 216, 20);
		trajecPanel.add(txtFileName);
		txtFileName.setColumns(10);
		
		JLabel lblLeftFileName = new JLabel("File Name");
		lblLeftFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftFileName.setBounds(27, 524, 90, 20);
		trajecPanel.add(lblLeftFileName);
								
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1075, 21);
		frmMotionProfileGenerator.getContentPane().add(menuBar);
		menuBar.setBackground(UIManager.getColor("Menu.background"));
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewProfile = new JMenuItem("New Profile");
		mnFile.add(mntmNewProfile);
		
		mntmNewProfile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnClearActionPerformed(evt);
            }
		});
		
		JMenuItem mntmSaveFile = new JMenuItem("Save Profile");
		mnFile.add(mntmSaveFile);
		
		mntmSaveFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					btnMenuSaveActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		});
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mntmExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
            }
		});
		
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		mntmHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				  if (Desktop.isDesktopSupported()) {
					  Desktop desktop = Desktop.getDesktop();
		              try {
		            	  URI uri = new URI("https://github.com/vannaka/Motion_Profile_Generator");
		                  desktop.browse(uri);
		              } catch (IOException ex) {
		                  return;
		              } catch (URISyntaxException ex) {
		            	  return;
		              }
				  } 
				  else {
					  return;
				  }
		    }
		});
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		mntmAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutPage();
            }
		});
								
		motionGraphBlue();
		motionGraphRed();
		velocityGraph();
	};
	
	private void aboutPage()
	{
		JFrame about = new JFrame();
        about.setLocationByPlatform(true);
        about.setVisible(true);
		about.setTitle("About");
		about.setBounds(100, 100, 600, 400);
		about.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		about.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 584, 361);
		about.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMotionProfileGenerator = new JLabel("Motion Profile Generator");
		lblMotionProfileGenerator.setFont(new Font("Arial", Font.PLAIN, 34));
		lblMotionProfileGenerator.setBounds(109, 29, 365, 64);
		panel.add(lblMotionProfileGenerator);
		
		JLabel lblVersion = new JLabel("Version 1.0.1");
		lblVersion.setFont(new Font("Arial", Font.PLAIN, 14));
		lblVersion.setBounds(82, 104, 85, 14);
		panel.add(lblVersion);
		
		JLabel lblThisProductIs = new JLabel("This product is licensed under the MIT license");
		lblThisProductIs.setFont(new Font("Arial", Font.PLAIN, 14));
		lblThisProductIs.setBounds(82, 128, 296, 14);
		panel.add(lblThisProductIs);
		
		JLabel lblDevelopers = new JLabel("Developers");
		lblDevelopers.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDevelopers.setBounds(82, 152, 85, 14);
		panel.add(lblDevelopers);
		
		JLabel lblLukeMammen = new JLabel("Luke Mammen");
		lblLukeMammen.setFont(new Font("Arial", Font.PLAIN, 14));
		lblLukeMammen.setBounds(109, 176, 110, 14);
		panel.add(lblLukeMammen);
		
		JLabel lblBlakeMammen = new JLabel("Blake Mammen");
		lblBlakeMammen.setFont(new Font("Arial", Font.PLAIN, 14));
		lblBlakeMammen.setBounds(109, 200, 110, 14);
		panel.add(lblBlakeMammen);
	}
	
	private void motionGraphBlue()
	{
		tabbedPane.insertTab("Blue Alliance", null, blueAllianceGraph, null, 0);
		// Create a blank grid for the field graph
		blueAllianceGraph.yGridOn();
		blueAllianceGraph.xGridOn();
		blueAllianceGraph.setYLabel("Y (feet)");
		blueAllianceGraph.setXLabel("X (feet)");
		blueAllianceGraph.setTitle("Top Down View of FRC Field - Blue Alliance (30ft x 27ft) \n shows global position of robot path with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 27.0;
		blueAllianceGraph.setXTic(0, 30, 1);
		blueAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
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
		blueAllianceGraph.addData(airShip, Color.black);
					
		// Auto Line
		double[][] baseLine = new double[][] {{7.77,0}, {7.77, fieldWidth}};
		blueAllianceGraph.addData(baseLine, Color.black);
						
		// Mid Field
		double[][] midLine = new double[][] {{27.3,0}, {27.3, fieldWidth}};
		blueAllianceGraph.addData(midLine, Color.black);
						
		// Boiler
		double[][] blueSideBoiler = new double[][] {{0,24.79}, {2.479,27}};
		blueAllianceGraph.addData(blueSideBoiler, Color.black);
						
		// Boiler Line
		double[][] blueSideBoilerLine = new double[][] {{0,18.61}, {8.39,27}};
		blueAllianceGraph.addData(blueSideBoilerLine, Color.blue);
						
		// Retrieval Zone
		double[][] blueSideRetrieval = new double[][] {{0,3.166}, {5.45,0}};
		blueAllianceGraph.addData(blueSideRetrieval, Color.black);
						
		// Retrieval Zone Line
		double[][] blueSideRetrievalLine = new double[][] {{0,7}, {13.75,0}};
		blueAllianceGraph.addData(blueSideRetrievalLine, Color.red);
	}
	
	private void motionGraphRed()
	{
		tabbedPane.insertTab("Red Alliance", null, redAllianceGraph, null, 1);
		// Create a blank grid for the field graph
		redAllianceGraph.yGridOn();
		redAllianceGraph.xGridOn();
		redAllianceGraph.setYLabel("Y (feet)");
		redAllianceGraph.setXLabel("X (feet)");
		redAllianceGraph.setTitle("Top Down View of FRC Field - Red Alliance (30ft x 27ft) \n shows global position of robot path with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 27.0;
		redAllianceGraph.setXTic(0, 30, 1);
		redAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
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
		redAllianceGraph.addData(airShip, Color.black);
					
		// Auto Line
		double[][] baseLine = new double[][] {{7.77,0}, {7.77, fieldWidth}};
		redAllianceGraph.addData(baseLine, Color.black);
						
		// Mid Field
		double[][] midLine = new double[][] {{27.3,0}, {27.3, fieldWidth}};
		redAllianceGraph.addData(midLine, Color.black);
						
		// Boiler
		double[][] redSideBoiler = new double[][] {{0,2.21}, {2.479,0}};
		redAllianceGraph.addData(redSideBoiler, Color.black);
						
		// Boiler Line
		double[][] redSideBoilerLine = new double[][] {{0,8.39}, {8.39,0}};
		redAllianceGraph.addData(redSideBoilerLine, Color.red);
						
		// Retrieval Zone
		double[][] blueSideRetrieval = new double[][] {{0,23.834}, {5.45,27}};
		redAllianceGraph.addData(blueSideRetrieval, Color.black);
						
		// Retrieval Zone Line
		double[][] redSideRetrievalLine = new double[][] {{0,20}, {13.75,27}};
		redAllianceGraph.addData(redSideRetrievalLine, Color.blue);
	}
	
	private void velocityGraph()
	{
		tabbedPane.insertTab("Velocity", null, velocityGraph, null, 2);
		velocityGraph.yGridOn();
      	velocityGraph.xGridOn();
      	velocityGraph.setYLabel("Velocity (ft/sec)");
      	velocityGraph.setXLabel("time (seconds)");
      	velocityGraph.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
	}
		
	private void btnGeneratePathActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
		double timeStep = 0; 
		double velocity = 0;
		double acceleration = 0;
		double jerk = 0;
		double wheelBase = 0;
		
		//get time step value
		try
		{
			timeStep = Double.parseDouble(txtTime.getText()); //default 0.05
		}
		catch ( Exception e)
		{
			JOptionPane.showMessageDialog(null, "The Time Step value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
    		return;
		}
		
		//get velocity value
		try
		{
			velocity = Double.parseDouble(txtVelocity.getText()); //default 4
		}
		catch ( Exception e)
		{
			JOptionPane.showMessageDialog(null, "The Velocity value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
    		return;
		}
		
		//get acceleration value
		try
		{
			acceleration = Double.parseDouble(txtAcceleration.getText()); // default 3
		}
		catch ( Exception e)
		{
			JOptionPane.showMessageDialog(null, "The Acceleration value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
	   		return;
		}
		
		//get jerk value
		try
		{
			jerk = Double.parseDouble(txtJerk.getText()); // default 60
		}
		catch ( Exception e)
		{
			JOptionPane.showMessageDialog(null, "The Jerk value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
	   		return;
		}
		
		//get wheel base value
		try
		{
			wheelBase = Double.parseDouble(txtWheelBase.getText()); //default 1.464
		}
		catch ( Exception e)
		{
			JOptionPane.showMessageDialog(null, "The Wheel Base value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
	  		return;
		}
		
		// If waypoints exist
		if( points.size() > 1 )
		{
			Waypoint tmp[] = new Waypoint[ points.size() ];
			points.toArray( tmp );
			try
			{
			trajectory( timeStep, velocity, acceleration, jerk, wheelBase, tmp );
			}
			catch ( Exception e )
			{
				JOptionPane.showMessageDialog(null, "The trajectory provided was invalid! Invalid trajectory could not be generated", "Invalid Points.", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "We need at least two points to generate a profile.", "Insufficient Points.", JOptionPane.INFORMATION_MESSAGE);
		}
		
    }
    
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
		
    	String format = "%1$6.2f %2$6.2f %3$7.2f";
    	String line = String.format(format, xValue, yValue, angle);
    	txtAreaWaypoints.append(line + "\n");
		
		// add new point to points list
		points.add( new Waypoint(xValue, yValue, Pathfinder.d2r(angle)));
		
		txtXValue.setText("");
		txtYValue.setText("");
		txtAngle.setText("");
		        
    }
    
    private void btnMenuSaveActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		if(directory != null)
    		{
    			if(left != null)
    			{
			    	lFile = new File(directory, fileName + "_left.csv");
			        rFile = new File(directory, fileName + "_right.csv");    	
			    	FileWriter lfw = new FileWriter( lFile );
					FileWriter rfw = new FileWriter( rFile );
					PrintWriter lpw = new PrintWriter( lfw );
					PrintWriter rpw = new PrintWriter( rfw );
					
			    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
			        File leftFile = new File(directory, fileName + "_left_detailed.csv");
			        Pathfinder.writeToCSV(leftFile, left);
			        
			        File rightFile = new File(directory, fileName + "_right_detailed.csv");
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
    			else
    			{
    				JOptionPane.showMessageDialog(null, "No Trajectory has been generated!", "Trajectory Not Generated", JOptionPane.INFORMATION_MESSAGE);
        			return;
    			}
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "No file destination chosen! \nClick the Browse button to choose a directory!", "File Destination Empty", JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}
    	
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name/directory field is empty! \nPlease enter a file name and click Browse for a destination!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}	
    }
    
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt)
    {
    	fileName = txtFileName.getText();
    	
    	fileChooser = new JFileChooser(); 
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose a Directory to Save Files In");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	//directory = fileChooser.getCurrentDirectory();
        	directory = fileChooser.getSelectedFile();
        }
        
        else
        {
        	return;
        }
        
        txtFileName.setText(directory + "\\" + fileName);
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		if(directory != null)
    		{
    			if(left != null)
    			{
			    	lFile = new File(directory, fileName + "_left.csv");
			        rFile = new File(directory, fileName + "_right.csv");    	
			    	FileWriter lfw = new FileWriter( lFile );
					FileWriter rfw = new FileWriter( rFile );
					PrintWriter lpw = new PrintWriter( lfw );
					PrintWriter rpw = new PrintWriter( rfw );
					
			    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
			        File leftFile = new File(directory, fileName + "_left_detailed.csv");
			        Pathfinder.writeToCSV(leftFile, left);
			        
			        File rightFile = new File(directory, fileName + "_right_detailed.csv");
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
    			else
    			{
    				JOptionPane.showMessageDialog(null, "No Trajectory has been generated!", "Trajectory Not Generated", JOptionPane.INFORMATION_MESSAGE);
        			return;
    			}
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "No file destination chosen! \nClick the Browse button to choose a directory!", "File Destination Empty", JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}
    	
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name/directory field is empty! \nPlease enter a file name and click Browse for a destination!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}	
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt)
    {
    	// clear graphs
    	blueAllianceGraph.clearGraph();
    	blueAllianceGraph.repaint();
    	velocityGraph.clearGraph();
    	velocityGraph.repaint();
    	redAllianceGraph.clearGraph();
    	redAllianceGraph.repaint();
    	
    	velocityGraph();
    	motionGraphBlue();
    	motionGraphRed();
		    	
    	points.clear();
    	
    	txtAreaWaypoints.setText(null);
    	String format = "%1$4s %2$6s %3$9s";
    	String line = String.format(format, "X", "Y", "Angle");
		txtAreaWaypoints.append(line + "\n");
		txtAreaWaypoints.append("_______________________" + "\n");
    }
    
    private void trajectory(double timeStep, double velocity, double acceleration, double jerk, double wheelBase, Waypoint[] points) throws IOException
    {
    	
		// Configure the trajectory with the time step, velocity, acceleration, jerk
		Trajectory.Config config = new Trajectory.Config( Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, timeStep, velocity, acceleration, jerk );
             
		// Generate the path
		Trajectory trajectory = Pathfinder.generate(points, config);
		
        // Tank drive modifier with the wheel base
        TankModifier modifier = new TankModifier(trajectory).modify(wheelBase);

        // Separate the trajectory into left and right
        left = modifier.getLeftTrajectory();
        right = modifier.getRightTrajectory();
        
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
       	
        if(tabbedPane.getSelectedIndex() == 0)
        {
        	blueAllianceGraph.addData(leftPath, Color.magenta);
        	blueAllianceGraph.addData(rightPath, Color.magenta);
        	blueAllianceGraph.repaint();
        }
        
        if(tabbedPane.getSelectedIndex() == 1)
        {
        	redAllianceGraph.addData(leftPath, Color.magenta);
         	redAllianceGraph.addData(rightPath, Color.magenta);
         	redAllianceGraph.repaint();
        }
        
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
       	velocityGraph.addData(leftVelocity, Color.magenta);
      	velocityGraph.addData(rightVelocity, Color.cyan);
      	velocityGraph.addData(middleVelocity, Color.blue);
      	velocityGraph.repaint();    
		
    }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui2 window = new Gui2();
					window.frmMotionProfileGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
