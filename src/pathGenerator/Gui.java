package pathGenerator;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.Document;
import javax.swing.text.Utilities;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.SwerveModifier;
import jaci.pathfinder.modifiers.TankModifier;

public class Gui {

	private JFrame frmMotionProfileGenerator;
	
	private JTextField txtTime;
	private JTextField txtVelocity;
	private JTextField txtAcceleration;
	private JTextField txtJerk;
	private JTextField txtWheelBaseW;
	private JTextField txtAngle;
	private JTextField txtXValue;
	private JTextField txtYValue;
	private JTextField txtFileName;
	
	private JButton btnAddPoint;
	private JButton btnClear;
	private JButton btnDeleteLast;
	private JRadioButton rdbtnTankDrive;
	private JRadioButton rdbtnSwerveDrive;
	
	static JTabbedPane tabbedPane;
	
	private JComboBox<String> cbFitMethod;
	private JComboBox<String> cbUnits;
	
	static FalconLinePlot blueAllianceGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
	static FalconLinePlot velocityGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
	static FalconLinePlot redAllianceGraph = new FalconLinePlot(new double[][]{{0.0,0.0}});
			
	private JTextArea txtAreaWaypoints;
	int lineNum;
	int rowStart;
	
	private JFileChooser fileChooser;
	private File directory;
	private File pFile;
	
	// Path Waypoints 
	//private Waypoint[] points;
	private List<Waypoint> points = new ArrayList<Waypoint>(); // can be variable length after creation
	
	double timeStep;
	double velocity;
	double acceleration;
	double jerk;
	double wheelBaseW;
	double wheelBaseD;
	
	//Tank Drive
	private Trajectory left;
	private Trajectory right;
	
	//Swerve Drive
	private Trajectory fl;
	private Trajectory fr;
	private Trajectory bl;
	private Trajectory br;
	
	private File lFile;
	private File rFile;
	private File flFile;
	private File frFile;
	private File blFile;
	private File brFile;
	private File preferenceFile;
	
	private String fileName;
	private JTextField txtWheelBaseD;
		
	/**
	 * Create the application.
	 */
	public Gui() {
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
		tabbedPane.insertTab("Blue Alliance", null, blueAllianceGraph, null, 0);
		tabbedPane.insertTab("Red Alliance", null, redAllianceGraph, null, 1);
		tabbedPane.insertTab("Velocity", null, velocityGraph, null, 2);
		
		velocityGraph.setSize(600, 600);
		velocityGraph.setLocation(1070, 0);
		
		JPanel trajecPanel = new JPanel();
		trajecPanel.setBounds(0, 22, 450, 617);
		frmMotionProfileGenerator.getContentPane().add(trajecPanel);
		trajecPanel.setLayout(null);
		
		JLabel lblTimeStep = new JLabel("Time Step");
		lblTimeStep.setBounds(50, 55, 80, 20);
		trajecPanel.add(lblTimeStep);
		
		JLabel lblVelocity = new JLabel("Velocity");
		lblVelocity.setBounds(50, 85, 80, 20);
		trajecPanel.add(lblVelocity);
		
		JLabel lblAcceleration = new JLabel("Acceleration");
		lblAcceleration.setBounds(50, 115, 80, 20);
		trajecPanel.add(lblAcceleration);
		
		JLabel lblJerk = new JLabel("Jerk");
		lblJerk.setBounds(50, 145, 80, 20);
		trajecPanel.add(lblJerk);
		
		txtTime = new JTextField();
		txtTime.setText("0.05");
		txtTime.setBounds(140, 55, 86, 20);
		trajecPanel.add(txtTime);
		txtTime.setColumns(10);
		
		txtVelocity = new JTextField();
		txtVelocity.setText("4");
		txtVelocity.setBounds(140, 85, 86, 20);
		trajecPanel.add(txtVelocity);
		txtVelocity.setColumns(10);
		
		txtAcceleration = new JTextField();
		txtAcceleration.setText("3");
		txtAcceleration.setBounds(140, 115, 86, 20);
		trajecPanel.add(txtAcceleration);
		txtAcceleration.setColumns(10);
		
		txtJerk = new JTextField();
		txtJerk.setText("60");
		txtJerk.setBounds(140, 145, 86, 20);
		trajecPanel.add(txtJerk);
		txtJerk.setColumns(10);
		
		JButton btnGeneratePath = new JButton("Generate Path");
		btnGeneratePath.setToolTipText("Generate Path");
		btnGeneratePath.setBounds(90, 566, 130, 24);
		trajecPanel.add(btnGeneratePath);
		
		btnGeneratePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
					btnGeneratePathActionPerformed();
            }
        });
		
		btnAddPoint = new JButton("Add Point");
		btnAddPoint.setToolTipText("Add Point");
		btnAddPoint.setBounds(20, 329, 130, 20);
		trajecPanel.add(btnAddPoint);
		btnAddPoint.addActionListener(evt -> btnAddPointActionPerformed());
		
		btnClear = new JButton("Clear");
		btnClear.setToolTipText("Clear");
		btnClear.setBounds(160, 328, 130, 20);
		trajecPanel.add(btnClear);
		btnClear.addActionListener(evt -> btnClearActionPerformed(evt));
		
		btnDeleteLast = new JButton("Delete last point");
		btnDeleteLast.setToolTipText("Delete last point");
		btnDeleteLast.setBounds(300,328,130,20);
		trajecPanel.add(btnDeleteLast);
		btnDeleteLast.addActionListener(evt -> {
				if(points.size()== 1)
				{
					txtAreaWaypoints.setText(null);
					points.remove(0);
				}
				else{
					int pos = txtAreaWaypoints.getText().lastIndexOf("\n", txtAreaWaypoints.getText().length() -2 );
					txtAreaWaypoints.setText(txtAreaWaypoints.getText().substring(0, pos +1));
					points.remove(points.size()- 1);
				}
		});
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setToolTipText("Browse");
		btnBrowse.setBounds(334, 522, 89, 24);
		trajecPanel.add(btnBrowse);
		btnBrowse.addActionListener(evt -> btnBrowseActionPerformed(evt));
		
		JButton btnSave = new JButton("Save");
		btnSave.setToolTipText("Save");
		btnSave.setBounds(230, 566, 130, 24);
		trajecPanel.add(btnSave);
		
		btnSave.addActionListener(evt -> {
				try {
					btnSaveActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
		});
		
		JLabel lblMotionVariables = new JLabel("Motion Variables");
		lblMotionVariables.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblMotionVariables.setBounds(137, 6, 176, 40);
		trajecPanel.add(lblMotionVariables);
		
		JLabel lblWaypoints = new JLabel("Waypoints");
		lblWaypoints.setToolTipText("Dean, We want a water game");
		lblWaypoints.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWaypoints.setBounds(170, 230, 110, 40);
		trajecPanel.add(lblWaypoints);
		
		txtWheelBaseW = new JTextField();
		txtWheelBaseW.setText("1.464");
		txtWheelBaseW.setBounds(140, 175, 86, 20);
		trajecPanel.add(txtWheelBaseW);
		txtWheelBaseW.setColumns(10);
		
		txtAngle = new JTextField();
		txtAngle.setToolTipText("Angle");
		txtAngle.setBounds(257, 298, 63, 20);
		trajecPanel.add(txtAngle);
		txtAngle.setColumns(10);
		
		txtAreaWaypoints = new JTextArea();
		txtAreaWaypoints.setEditable(false);
		txtAreaWaypoints.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtAreaWaypoints.setBounds(131, 363, 188, 176);
		txtAreaWaypoints.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) {
	            mouseEvent(e);
	         }
	      });
					
		txtXValue = new JTextField();
		txtXValue.setToolTipText("X Point");
		txtXValue.setBounds(130, 298, 63, 20);
		trajecPanel.add(txtXValue);
		txtXValue.setColumns(10);
		
		txtYValue = new JTextField();
		txtYValue.setToolTipText("Y Point");
		txtYValue.setBounds(193, 298, 64, 20);
		trajecPanel.add(txtYValue);
		txtYValue.setColumns(10);
		
		JLabel lblX = new JLabel("X");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblX.setBounds(160, 275, 10, 20);
		trajecPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblY.setBounds(220, 275, 10, 20);
		trajecPanel.add(lblY);
		
		JLabel lblAngle = new JLabel("Angle");
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAngle.setBounds(270, 275, 34, 20);
		trajecPanel.add(lblAngle);
		
		JScrollPane scrollPane = new JScrollPane(txtAreaWaypoints, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(130, 385, 190, 118);
		trajecPanel.add(scrollPane);
		
		txtFileName = new JTextField();
		txtFileName.setBounds(117, 524, 216, 20);
		trajecPanel.add(txtFileName);
		txtFileName.setColumns(10);
		
		JLabel lblLeftFileName = new JLabel("File Name");
		lblLeftFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftFileName.setBounds(27, 524, 90, 20);
		trajecPanel.add(lblLeftFileName);
		
		JTextArea txtAreaWaypointsTitle = new JTextArea();
		txtAreaWaypointsTitle.setBounds(130, 361, 190, 24);
		trajecPanel.add(txtAreaWaypointsTitle);
		txtAreaWaypointsTitle.setEditable(false);
		txtAreaWaypointsTitle.setFont(new Font("Monospaced", Font.PLAIN, 14));
		String format = "%1$4s %2$6s %3$9s";
    	String line = String.format(format, "X", "Y", "Angle");
		txtAreaWaypointsTitle.append(line + "\n");
		
		cbFitMethod = new JComboBox<String>();
		cbFitMethod.setToolTipText("Fit Method");
		cbFitMethod.addItem("Cubic");
		cbFitMethod.addItem("Quintic");
		cbFitMethod.setBounds(140, 205, 86, 20);
		trajecPanel.add(cbFitMethod);	
		
		JLabel lblFitMethod = new JLabel("Fit Method");
		lblFitMethod.setBounds(50, 205, 80, 20);
		trajecPanel.add(lblFitMethod);
		
		cbUnits = new JComboBox<String>();
		cbUnits.setToolTipText("Units");
		cbUnits.addItem("Feet");
		cbUnits.addItem("Meters");
		cbUnits.setBounds(340, 205, 86, 20);
		trajecPanel.add(cbUnits);
		
		cbUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	cbUnitsActionPerformed(evt);
		    }
		});
		
		JLabel lblUnits = new JLabel("Units");
		lblUnits.setBounds(250, 205, 100, 20);
		trajecPanel.add(lblUnits);
		
		rdbtnTankDrive = new JRadioButton("Tank Drive");
		rdbtnTankDrive.setSelected(true);
		rdbtnTankDrive.setBounds(289, 84, 109, 23);
		trajecPanel.add(rdbtnTankDrive);
		
		rdbtnTankDrive.addActionListener(evt -> txtWheelBaseD.setEnabled(false));
		
		rdbtnSwerveDrive = new JRadioButton("Swerve Drive");
		rdbtnSwerveDrive.setToolTipText("Why not combine Tank and Swerve into Swank?");
		rdbtnSwerveDrive.setBounds(289, 110, 109, 23);
		trajecPanel.add(rdbtnSwerveDrive);
		
		rdbtnSwerveDrive.addActionListener(evt -> txtWheelBaseD.setEnabled(true));
		
		ButtonGroup tankSwerve = new ButtonGroup();
		tankSwerve.add(rdbtnTankDrive);
		tankSwerve.add(rdbtnSwerveDrive);
		
		JLabel lblWheelBaseW = new JLabel("Wheel Base W");
		lblWheelBaseW.setToolTipText("Wheel Base Width");
		lblWheelBaseW.setBounds(50, 176, 100, 20);
		trajecPanel.add(lblWheelBaseW);
		
		JLabel lblWheelBaseD = new JLabel("Wheel Base D");
		lblWheelBaseD.setToolTipText("Wheel Base Depth");
		lblWheelBaseD.setBounds(250, 175, 100, 20);
		trajecPanel.add(lblWheelBaseD);
		
		txtWheelBaseD = new JTextField();
		txtWheelBaseD.setText("0");
		txtWheelBaseD.setBounds(340, 175, 86, 20);
		txtWheelBaseD.setEnabled(false);
		trajecPanel.add(txtWheelBaseD);
		txtWheelBaseD.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1075, 21);
		frmMotionProfileGenerator.getContentPane().add(menuBar);
		menuBar.setBackground(UIManager.getColor("Menu.background"));
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewProfile = new JMenuItem("New Profile");
		mnFile.add(mntmNewProfile);
		mntmNewProfile.addActionListener(evt -> btnClearActionPerformed(evt));
		
		JMenuItem mntmSaveFile = new JMenuItem("Save Profile");
		mnFile.add(mntmSaveFile);
		
		mntmSaveFile.addActionListener(evt -> {
				try {
					btnMenuSaveActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
		});
		
		JMenuItem mntmLoadProfile = new JMenuItem("Load Profile");
		mnFile.add(mntmLoadProfile);
		
		mntmLoadProfile.addActionListener(evt -> {
				try {
					btnMenuLoadActionPerformed();
				} catch (IOException e) {
					e.printStackTrace();
				}
		});
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mntmExit.addActionListener(evt -> System.exit(0));
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		mntmHelp.addActionListener(evt -> {
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
				  }
		});
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		mntmAbout.addActionListener(evt -> aboutPage());
		
		MotionGraphsFeet.motionGraphBlue();
		MotionGraphsFeet.motionGraphRed();
		MotionGraphsFeet.velocityGraph();
	}
	
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
		
		JLabel lblVersion = new JLabel("Version 2.3.0");
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
		
		JLabel lblAcknowedgements = new JLabel("Acknowledgments");
		lblAcknowedgements.setFont(new Font("Arial", Font.PLAIN, 14));
		lblAcknowedgements.setBounds(82, 224, 150, 14);
		panel.add(lblAcknowedgements);
		
		JLabel lblJaci = new JLabel("Jaci for the path generation code");
		lblJaci.setFont(new Font("Arial", Font.PLAIN, 14));
		lblJaci.setBounds(109, 248, 250, 14);
		panel.add(lblJaci);
		
		JLabel lblJH = new JLabel("KHEngineering for the graph code");
		lblJH.setFont(new Font("Arial", Font.PLAIN, 14));
		lblJH.setBounds(109, 272, 250, 14);
		panel.add(lblJH);
	}
	
	void cbUnitsActionPerformed(java.awt.event.ActionEvent evt)
	{
		if(cbUnits.getSelectedIndex() == 0)
		{
			// clear graphs
	    	blueAllianceGraph.clearGraph();
	    	blueAllianceGraph.repaint();
	    	velocityGraph.clearGraph();
	    	velocityGraph.repaint();
	    	redAllianceGraph.clearGraph();
	    	redAllianceGraph.repaint();
	    	
			MotionGraphsFeet.motionGraphBlue();
			MotionGraphsFeet.motionGraphRed();
			MotionGraphsFeet.velocityGraph();
		}
		else
		{
			// clear graphs
	    	blueAllianceGraph.clearGraph();
	    	blueAllianceGraph.repaint();
	    	velocityGraph.clearGraph();
	    	velocityGraph.repaint();
	    	redAllianceGraph.clearGraph();
	    	redAllianceGraph.repaint();
	    	
			MotionGraphsMeters.motionGraphBlue();
			MotionGraphsMeters.motionGraphRed();
			MotionGraphsMeters.velocityGraph();
		}
	}
	
	private void btnMenuLoadActionPerformed() throws IOException
	{		
    	fileChooser = new JFileChooser(); 
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose a file to load.");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	pFile = fileChooser.getSelectedFile();
        	
        	String preference = pFile.getName();
        	String extension = "";

        	int i = preference.lastIndexOf('.');
        	if (i > 0) {
        	    extension = preference.substring(i+1);
        	}
        	
        	if(extension.equals("bot"))
        	{
	        	@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(pFile));
	        	
	        	String sTime = br.readLine();
	        	String sVelocity = br.readLine();
	        	String sAcceleration = br.readLine();
	        	String sJerk = br.readLine();
	        	String sWheelW = br.readLine();
	        	String sWheelD = br.readLine();
	        	String sGen = br.readLine();
	        	
	        	txtTime.setText(sTime);
	        	txtVelocity.setText(sVelocity);
	        	txtAcceleration.setText(sAcceleration);
	        	txtJerk.setText(sJerk);
	        	txtWheelBaseW.setText(sWheelW);
	        	txtWheelBaseD.setText(sWheelD);
	        	cbFitMethod.setSelectedItem(sGen);
	        	
	        	points.clear();
	        	txtAreaWaypoints.setText(null);
	        	
	        	String st;
	        	while ((st = br.readLine()) != null)
	        	{
	        		
	        				
	    	       	String[] splitStr = st.trim().split("\\s*,\\s*");
	    	       	String xValueS = splitStr[0];
	    	   		String yValueS = splitStr[1];
	    	      	String aValueS = splitStr[2];
	    	      	
	    	      	double dX = Double.parseDouble(xValueS);
	    	      	double dY = Double.parseDouble(yValueS);
	    	      	double dA = Double.parseDouble(aValueS);
	    	      	
	    	      	String format = "%1$6.2f %2$6.2f %3$7.2f";
	    	      	String line = String.format(format, dX, dY, dA);
	    	    	
	    	    	txtAreaWaypoints.append(line + "\n");
	    	    	points.add( new Waypoint(dX, dY, Pathfinder.d2r(dA)));
	        	}
        	}
        	else
        	{
        		JOptionPane.showMessageDialog(null, "The file type is invalid! Make sure it is .bot", "Invalid file type", JOptionPane.INFORMATION_MESSAGE);
        	}
        	
        }
        
        else
        {
        	return;
        }
	}
		
	private void btnGeneratePathActionPerformed()
    {
		timeStep = Double.parseDouble(txtTime.getText()); //default 0.05 
		velocity = Double.parseDouble(txtVelocity.getText()); //default 4
		acceleration = Double.parseDouble(txtAcceleration.getText()); // default 3
		jerk = Double.parseDouble(txtJerk.getText()); // default 60
		wheelBaseW = Double.parseDouble(txtWheelBaseW.getText()); //default 0
		
		if (rdbtnSwerveDrive.isSelected())
		{
			wheelBaseD = Double.parseDouble(txtWheelBaseD.getText());
		}
		else 
		{
			wheelBaseD = 0;
		}
		
		
		int selectedIndex = cbFitMethod.getSelectedIndex();
		
		FitMethod CUBIC = Trajectory.FitMethod.HERMITE_CUBIC;
		FitMethod QUINTIC = Trajectory.FitMethod.HERMITE_QUINTIC;
		FitMethod fitMethod;
		
		if(selectedIndex == 0)
		{
			fitMethod = CUBIC; 
		}
		else
		{
			fitMethod = QUINTIC;
		}
		
		// clear graphs
    	velocityGraph.clearGraph();
    	velocityGraph.repaint();
    	redAllianceGraph.clearGraph();
    	redAllianceGraph.repaint();
    	blueAllianceGraph.clearGraph();
    	blueAllianceGraph.repaint();    	
    	
    	if(cbUnits.getSelectedIndex() == 0)
		{
			// clear graphs
	    	blueAllianceGraph.clearGraph();
	    	blueAllianceGraph.repaint();
	    	velocityGraph.clearGraph();
	    	velocityGraph.repaint();
	    	redAllianceGraph.clearGraph();
	    	redAllianceGraph.repaint();
	    	
			MotionGraphsFeet.motionGraphBlue();
			MotionGraphsFeet.motionGraphRed();
			MotionGraphsFeet.velocityGraph();
		}
		else
		{
			// clear graphs
	    	blueAllianceGraph.clearGraph();
	    	blueAllianceGraph.repaint();
	    	velocityGraph.clearGraph();
	    	velocityGraph.repaint();
	    	redAllianceGraph.clearGraph();
	    	redAllianceGraph.repaint();
	    	
			MotionGraphsMeters.motionGraphBlue();
			MotionGraphsMeters.motionGraphRed();
			MotionGraphsMeters.velocityGraph();
		}
		
		if(timeStep > 0)
		{
			if(velocity > 0)
			{
				if(acceleration > 0)
				{
					if(jerk > 0)
					{
						if(wheelBaseW > 0)
						{
							if(rdbtnSwerveDrive.isSelected())
							{
								if(wheelBaseD > 0)
								{
									// If waypoints exist
									if( points.size() > 1 )
									{
										Waypoint tmp[] = new Waypoint[ points.size() ];
										points.toArray( tmp );
										try
										{
										trajectory( timeStep, velocity, acceleration, jerk, wheelBaseW, wheelBaseD, tmp, fitMethod);
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
								else
								{
									JOptionPane.showMessageDialog(null, "The Wheel Base D value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
								}
							}
							else
							{
								// If waypoints exist
								if( points.size() > 1 )
								{
									Waypoint tmp[] = new Waypoint[ points.size() ];
									points.toArray( tmp );
									try
									{
									trajectory( timeStep, velocity, acceleration, jerk, wheelBaseW, wheelBaseD, tmp, fitMethod);
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
						}
						else
						{
							JOptionPane.showMessageDialog(null, "The Wheel Base W value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "The Jerk value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The Acceleration value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "The Velocity value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The Time Step value is invalid!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);
		}
		
    }
	
	private void mouseEvent(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1) {
            return;
         }
         if (e.getClickCount() != 2) {
            return;
         }

         int offset = txtAreaWaypoints.viewToModel(e.getPoint());

         try {
            rowStart = Utilities.getRowStart(txtAreaWaypoints, offset);
            int rowEnd = Utilities.getRowEnd(txtAreaWaypoints, offset);
            String selectedLine = txtAreaWaypoints.getText().substring(rowStart, rowEnd);
           
            btnAddPoint.setText("Update");
            
	       	String[] splitStr = selectedLine.trim().split("\\s+");
	       	String xValueS = splitStr[0];
	   		String yValueS = splitStr[1];
	      	String aValueS = splitStr[2];
	       		
	       	txtXValue.setText(xValueS);
	       	txtYValue.setText(yValueS);
	       	txtAngle.setText(aValueS);
	       		
	       	int off = txtAreaWaypoints.getCaretPosition();
	        lineNum = txtAreaWaypoints.getLineOfOffset(off);
	
	        Document document = txtAreaWaypoints.getDocument();
	
	        int len = rowEnd - rowStart + 1;
	        if (rowStart + len > document.getLength()) 
	        {
	        	len--;
	        }
	            document.remove(rowStart, len);
			}
		catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "The Row is empty!", "Row Empty", JOptionPane.INFORMATION_MESSAGE);
				btnAddPoint.setText("Add Point");
				return;
			}
		points.remove(lineNum);
		btnClear.setEnabled(false);
		btnDeleteLast.setEnabled(false);
	}
    
    private void btnAddPointActionPerformed()
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
    		
    	if(btnAddPoint.getText() == "Add Point")
    	{
    		txtAreaWaypoints.append(line + "\n");
    		// add new point to points list
    		points.add( new Waypoint(xValue, yValue, Pathfinder.d2r(angle)));
    	}
    	else
    	{
    		txtAreaWaypoints.insert(line + "\n", rowStart);
    		points.add(lineNum, new Waypoint(xValue, yValue, Pathfinder.d2r(angle)));
    		btnClear.setEnabled(true);
    		btnDeleteLast.setEnabled(true);
    	}
    		
    	txtXValue.setText("");
    	txtYValue.setText("");
    	txtAngle.setText("");
    		
    	btnAddPoint.setText("Add Point");
    }
    
    private void btnMenuSaveActionPerformed(java.awt.event.ActionEvent evt) throws IOException
    {
    	if(txtFileName.getText().equals("") == false)
    	{
    		if(directory != null)
    		{
    			if(left != null || fl != null)
    			{
    				if(rdbtnTankDrive.isSelected())
    				{
	    				lFile = new File(directory, fileName + "_left.csv");
				        rFile = new File(directory, fileName + "_right.csv");
				        
				        if( lFile.exists() || rFile.exists() )
				        {
				        	int n = JOptionPane.showConfirmDialog(null, "File already exist. Would you like to replace it?", "File Exists", JOptionPane.YES_NO_OPTION);
				        	
				        	switch( n )
				        	{
				        	case JOptionPane.YES_OPTION:
				        		break;		// Continue with method
				        		
				        	case JOptionPane.NO_OPTION:
				        		return;		// Stop Saving
				        		
				        	default:
				        		return;
				        	}
				        }
				        	
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
    					flFile = new File(directory, fileName + "_FrontLeft.csv");
				        frFile = new File(directory, fileName + "_FrontRight.csv");
				        blFile = new File(directory, fileName + "_BackLeft.csv");
				        brFile = new File(directory, fileName + "_BackRight.csv");
				        
				        if( flFile.exists() || frFile.exists() || blFile.exists() || brFile.exists() )
				        {
				        	int n = JOptionPane.showConfirmDialog(null, "File already exist. Would you like to replace it?", "File Exists", JOptionPane.YES_NO_OPTION);
				        	
				        	switch( n )
				        	{
				        	case JOptionPane.YES_OPTION:
				        		break;		// Continue with method
				        		
				        	case JOptionPane.NO_OPTION:
				        		return;		// Stop Saving
				        		
				        	default:
				        		return;
				        	}
				        }
				        	
				    	FileWriter flfw = new FileWriter( flFile );
						FileWriter frfw = new FileWriter( frFile );
						FileWriter blfw = new FileWriter( blFile );
						FileWriter brfw = new FileWriter( brFile );
						PrintWriter flpw = new PrintWriter( flfw );
						PrintWriter frpw = new PrintWriter( frfw );
						PrintWriter blpw = new PrintWriter( blfw );
						PrintWriter brpw = new PrintWriter( brfw );
						
				    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
				        File frontLeftFile = new File(directory, fileName + "_FrontLeft_detailed.csv");
				        Pathfinder.writeToCSV(frontLeftFile, fl);
				        
				        File frontRightFile = new File(directory, fileName + "_FrontRight_detailed.csv");
				        Pathfinder.writeToCSV(frontRightFile, fr);
				        
				        File backLeftFile = new File(directory, fileName + "_BackLeft_detailed.csv");
				        Pathfinder.writeToCSV(backLeftFile, bl);
				        
				        File backRightFile = new File(directory, fileName + "_BackRight_detailed.csv");
				        Pathfinder.writeToCSV(backRightFile, br);
				        
				    	// CSV with position and velocity. To be used with your robot.
				    	// save front left path to CSV
				    	for (int i = 0; i < fl.length(); i++) 
				    	{			
				    		Segment seg = fl.get(i);
				    		flpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	// save front right path to CSV
				    	for (int i = 0; i < fr.length(); i++) 
				    	{			
				    		Segment seg = fr.get(i);
				    		frpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    	
				    	// save back left path to CSV
				    	for (int i = 0; i < bl.length(); i++) 
				    	{			
				    		Segment seg = bl.get(i);
				    		blpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	// save back right path to CSV
				    	for (int i = 0; i < br.length(); i++) 
				    	{			
				    		Segment seg = br.get(i);
				    		brpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	flpw.close();
				    	frpw.close();
				    	blpw.close();
				    	brpw.close();
    				}
			    	
			    	preferenceFile = new File(directory, fileName + "_Preferences.bot");
			    	FileWriter pfw = new FileWriter(preferenceFile);
			    	PrintWriter ppw = new PrintWriter(pfw);
			    	
			    	ppw.println(timeStep);
			    	ppw.println(velocity);
			    	ppw.println(acceleration);
			    	ppw.println(jerk);
			    	ppw.println(wheelBaseW);
			    	ppw.println(wheelBaseD);
			    	ppw.println(cbFitMethod.getSelectedItem());
			    	
			    	for(int i = 0; i < points.size(); i++)
			    	{
			    		ppw.printf("%4.2f, %4.2f, %4.2f", points.get(i).x, points.get(i).y, Pathfinder.r2d(points.get(i).angle));
			    		ppw.println();
			    	}
			    	
			    	ppw.close();
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
    	if(txtFileName.getText().equals("") == false)
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
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "The File Name field is empty! \nPlease enter a file name!", "File Name Empty", JOptionPane.INFORMATION_MESSAGE);
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
    			if(left != null || fl != null)
    			{
    				if(rdbtnTankDrive.isSelected())
    				{
	    				lFile = new File(directory, fileName + "_left.csv");
				        rFile = new File(directory, fileName + "_right.csv");
				        
				        if( lFile.exists() || rFile.exists() )
				        {
				        	int n = JOptionPane.showConfirmDialog(null, "File already exist. Would you like to replace it?", "File Exists", JOptionPane.YES_NO_OPTION);
				        	
				        	switch( n )
				        	{
				        	case JOptionPane.YES_OPTION:
				        		break;		// Continue with method
				        		
				        	case JOptionPane.NO_OPTION:
				        		return;		// Stop Saving
				        		
				        	default:
				        		return;
				        	}
				        }
				        	
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
    					flFile = new File(directory, fileName + "_FrontLeft.csv");
				        frFile = new File(directory, fileName + "_FrontRight.csv");
				        blFile = new File(directory, fileName + "_BackLeft.csv");
				        brFile = new File(directory, fileName + "_BackRight.csv");
				        
				        if( flFile.exists() || frFile.exists() || blFile.exists() || brFile.exists() )
				        {
				        	int n = JOptionPane.showConfirmDialog(null, "File already exist. Would you like to replace it?", "File Exists", JOptionPane.YES_NO_OPTION);
				        	
				        	switch( n )
				        	{
				        	case JOptionPane.YES_OPTION:
				        		break;		// Continue with method
				        		
				        	case JOptionPane.NO_OPTION:
				        		return;		// Stop Saving
				        		
				        	default:
				        		return;
				        	}
				        }
				        	
				    	FileWriter flfw = new FileWriter( flFile );
						FileWriter frfw = new FileWriter( frFile );
						FileWriter blfw = new FileWriter( blFile );
						FileWriter brfw = new FileWriter( brFile );
						PrintWriter flpw = new PrintWriter( flfw );
						PrintWriter frpw = new PrintWriter( frfw );
						PrintWriter blpw = new PrintWriter( blfw );
						PrintWriter brpw = new PrintWriter( brfw );
						
				    	// Detailed CSV with dt, x, y, position, velocity, acceleration, jerk, and heading
				        File frontLeftFile = new File(directory, fileName + "_FrontLeft_detailed.csv");
				        Pathfinder.writeToCSV(frontLeftFile, fl);
				        
				        File frontRightFile = new File(directory, fileName + "_FrontRight_detailed.csv");
				        Pathfinder.writeToCSV(frontRightFile, fr);
				        
				        File backLeftFile = new File(directory, fileName + "_BackLeft_detailed.csv");
				        Pathfinder.writeToCSV(backLeftFile, bl);
				        
				        File backRightFile = new File(directory, fileName + "_BackRight_detailed.csv");
				        Pathfinder.writeToCSV(backRightFile, br);
				        
				    	// CSV with position and velocity. To be used with your robot.
				    	// save front left path to CSV
				    	for (int i = 0; i < fl.length(); i++) 
				    	{			
				    		Segment seg = fl.get(i);
				    		flpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	// save front right path to CSV
				    	for (int i = 0; i < fr.length(); i++) 
				    	{			
				    		Segment seg = fr.get(i);
				    		frpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    	
				    	// save back left path to CSV
				    	for (int i = 0; i < bl.length(); i++) 
				    	{			
				    		Segment seg = bl.get(i);
				    		blpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	// save back right path to CSV
				    	for (int i = 0; i < br.length(); i++) 
				    	{			
				    		Segment seg = br.get(i);
				    		brpw.printf("%f, %f, %d\n", seg.position, seg.velocity, (int)(seg.dt * 1000));
				    	}
				    			
				    	flpw.close();
				    	frpw.close();
				    	blpw.close();
				    	brpw.close();
    				}
			    	
			    	preferenceFile = new File(directory, fileName + "_Preferences.bot");
			    	FileWriter pfw = new FileWriter(preferenceFile);
			    	PrintWriter ppw = new PrintWriter(pfw);
			    	
			    	ppw.println(timeStep);
			    	ppw.println(velocity);
			    	ppw.println(acceleration);
			    	ppw.println(jerk);
			    	ppw.println(wheelBaseW);
			    	ppw.println(wheelBaseD);
			    	ppw.println(cbFitMethod.getSelectedItem());
			    	
			    	for(int i = 0; i < points.size(); i++)
			    	{
			    		ppw.printf("%4.2f, %4.2f, %4.2f", points.get(i).x, points.get(i).y, Pathfinder.r2d(points.get(i).angle));
			    		ppw.println();
			    	}
			    	
			    	ppw.close();
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
    	    	
    	if(cbUnits.getSelectedIndex() == 0)
		{	
			MotionGraphsFeet.motionGraphBlue();
			MotionGraphsFeet.motionGraphRed();
			MotionGraphsFeet.velocityGraph();
		}
		else
		{
			MotionGraphsMeters.motionGraphBlue();
			MotionGraphsMeters.motionGraphRed();
			MotionGraphsMeters.velocityGraph();
		}  
		
    	points.clear();
    	
    	txtAreaWaypoints.setText(null);
    }
    
    private void trajectory(double timeStep, double velocity, double acceleration, double jerk, double wheelBaseW, double wheelBaseD, Waypoint[] points, FitMethod fitMethod) throws IOException
    {
    	
		// Configure the trajectory with the time step, velocity, acceleration, jerk
		Trajectory.Config config = new Trajectory.Config(fitMethod, Trajectory.Config.SAMPLES_HIGH, timeStep, velocity, acceleration, jerk );
             
		// Generate the path
		Trajectory trajectory = Pathfinder.generate(points, config);
		
		if(rdbtnTankDrive.isSelected())
		{
	        // Tank drive modifier with the wheel base
	        TankModifier modifier = new TankModifier(trajectory).modify(wheelBaseW);
	
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
	        blueAllianceGraph.addData(leftPath, Color.magenta);
	       	blueAllianceGraph.addData(rightPath, Color.magenta);
	       	blueAllianceGraph.repaint();
	        	
	       	redAllianceGraph.addData(leftPath, Color.magenta);
	        redAllianceGraph.addData(rightPath, Color.magenta);
	        redAllianceGraph.repaint();
	        
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
		else
		{
			// The swerve mode to generate will be the 'default' mode, where the 
			// robot will constantly be facing forward and 'sliding' sideways to 
			// follow a curved path.
			SwerveModifier.Mode mode = SwerveModifier.Mode.SWERVE_DEFAULT;

			// Create the Modifier Object
			SwerveModifier modifier = new SwerveModifier(trajectory);

			// Generate the individual wheel trajectories using the original trajectory
			// as the center
			modifier.modify(wheelBaseD, wheelBaseW, mode);
			
			bl = modifier.getFrontLeftTrajectory();       // Get the Back Left wheel
			fl = modifier.getFrontRightTrajectory();      // Get the Front Left wheel
			br = modifier.getBackLeftTrajectory();        // Get the Back Right wheel
			fr = modifier.getBackRightTrajectory();       // Get the Front Right wheel
			
			// Left and Right paths to display on the Field Graph
	        double[][] frontLeftPath = new double[fl.length()][2];
	        double[][] frontRightPath = new double[fr.length()][2];
	        double[][] backLeftPath = new double[bl.length()][2];
	        double[][] backRightPath = new double[br.length()][2];
	        
	        for(int i = 0; i < fl.length(); i++)
	        {
	        	frontLeftPath[i][0] = fl.get(i).x;
	        	frontLeftPath[i][1] = fl.get(i).y;
	        	frontRightPath[i][0] = fr.get(i).x;
	        	frontRightPath[i][1] = fr.get(i).y;
	        	backLeftPath[i][0] = bl.get(i).x;
	        	backLeftPath[i][1] = bl.get(i).y;
	        	backRightPath[i][0] = br.get(i).x;
	        	backRightPath[i][1] = br.get(i).y;
	        }
	        blueAllianceGraph.addData(frontLeftPath, Color.red);
	       	blueAllianceGraph.addData(frontRightPath, Color.blue);
	       	blueAllianceGraph.addData(backLeftPath, Color.red);
	       	blueAllianceGraph.addData(backRightPath, Color.blue);
	       	blueAllianceGraph.repaint();
	        	
	       	redAllianceGraph.addData(frontLeftPath, Color.red);
	       	redAllianceGraph.addData(frontRightPath, Color.blue);
	       	redAllianceGraph.addData(backLeftPath, Color.red);
	       	redAllianceGraph.addData(backRightPath, Color.blue);
	        redAllianceGraph.repaint();
	        
	     	// Velocity to be used in the Velocity graph
	     	double[][] middleVelocity = new double[trajectory.length()][2];
	     	double[][] frontLeftVelocity = new double[fl.length()][2];
	     	double[][] frontRightVelocity = new double[fr.length()][2];
	     	double[][] backLeftVelocity = new double[bl.length()][2];
	     	double[][] backRightVelocity = new double[br.length()][2];
	     	
	     	for(int i = 0; i < fl.length(); i++)
	     	{
	     		middleVelocity[i][0] = trajectory.segments[i].dt * i;
	     		middleVelocity[i][1] = trajectory.segments[i].velocity;
	     		frontLeftVelocity[i][0] = fl.segments[i].dt * i;
	     		frontLeftVelocity[i][1] = fl.segments[i].velocity;
	     		frontRightVelocity[i][0] = fr.segments[i].dt * i;
	     		frontRightVelocity[i][1] = fr.segments[i].velocity;
	     		backLeftVelocity[i][0] = bl.segments[i].dt * i;
	     		backLeftVelocity[i][1] = bl.segments[i].velocity;
	     		backRightVelocity[i][0] = br.segments[i].dt * i;
	     		backRightVelocity[i][1] = br.segments[i].velocity;
	     	}
	     	
	      	// Velocity Graph
	      	velocityGraph.addData(middleVelocity, Color.blue);
	      	velocityGraph.repaint(); 
		}
		
    }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmMotionProfileGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
