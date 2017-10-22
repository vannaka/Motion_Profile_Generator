package pathGenerator;

import java.awt.Color;
import java.io.*;
import jaci.pathfinder.*;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.modifiers.TankModifier;
import pathGenerator.FalconLinePlot;

public class main {

	public static void main(String[] args) throws IOException {
		
		File lFile = new File("mp_left.csv");
		File rFile = new File("mp_right.csv");
		FileWriter lfw = new FileWriter( lFile );
		FileWriter rfw = new FileWriter( rFile );
		PrintWriter lpw = new PrintWriter( lfw );
		PrintWriter rpw = new PrintWriter( rfw );
		
		// Create a blank grid for the field graph
		FalconLinePlot fig3 = new FalconLinePlot(new double[][]{{0.0,0.0}});
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
		
		// Path Waypoints 
		Waypoint[] points = new Waypoint[] {
                new Waypoint(2, 23, 0),
                new Waypoint(11, 16, Pathfinder.d2r(-60))
        };
		
		// Configure the trajectory with the time step, velocity, acceleration, jerk
		Trajectory.Config config = new Trajectory.Config( Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 4, 3, 60.0 );
             
		// Generate the path
		Trajectory trajectory = Pathfinder.generate(points, config);
		
        // Tank drive modifier with the wheel base
        TankModifier modifier = new TankModifier(trajectory).modify(1.464);

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
      	
      	FalconLinePlot fig4 = new FalconLinePlot(middleVelocity,null,Color.blue);
      	fig4.yGridOn();
      	fig4.xGridOn();
      	fig4.setYLabel("Velocity (ft/sec)");
      	fig4.setXLabel("time (seconds)");
      	fig4.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
      	fig4.addData(leftVelocity, Color.magenta);
      	fig4.addData(rightVelocity, Color.cyan);
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

}
