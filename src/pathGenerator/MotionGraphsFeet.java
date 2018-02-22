package pathGenerator;

import java.awt.Color;

public class MotionGraphsFeet {

	static void motionGraphBlue()
	{
		// Create a blank grid for the field graph
		Gui.blueAllianceGraph.yGridOn();
		Gui.blueAllianceGraph.xGridOn();
		Gui.blueAllianceGraph.setYLabel("Y (feet)");
		Gui.blueAllianceGraph.setXLabel("X (feet)");
		Gui.blueAllianceGraph.setTitle("Top Down View of FRC Field - Blue Alliance (32ft x 27ft) \n shows the left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 27.0;
		Gui.blueAllianceGraph.setXTic(0, 32, 1);
		Gui.blueAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
		//lets add field markers to help visual
		double[][] redSwitch = new double[][]{
				{11.7, 7.105},
				{16.3, 7.105},
				{16.3, 19.895},
				{11.7, 19.895},
				{11.7, 7.105},
			};
		Gui.blueAllianceGraph.addData(redSwitch, Color.black);
							
		// Auto Line
		double[][] autoLine = new double[][] {{10,0}, {10, fieldWidth}};
		Gui.blueAllianceGraph.addData(autoLine, Color.black);
								
		// Mid Field Up
		double[][] midLineUp = new double[][] {{27,27}, {27, 21}};
		Gui.blueAllianceGraph.addData(midLineUp, Color.black);
				
		// Mid Field Down
		double[][] midLineDown = new double[][] {{27,0}, {27, 6}};
		Gui.blueAllianceGraph.addData(midLineDown, Color.black);
								
		// Scale Up
		double[][] scaleUp = new double[][] {
				{25, 18}, 
				{29, 18},
				{29, 21},
				{25, 21},
				{25, 18},
			};
		Gui.blueAllianceGraph.addData(scaleUp, Color.blue);
								
		// Scale Down
		double[][] scaleDown = new double[][] {
				{25, 6}, 
				{29, 6},
				{29, 9},
				{25, 9},
				{25, 6},
			};
		Gui.blueAllianceGraph.addData(scaleDown, Color.red);
								
		// Scale sides
		double[][] scaleSides = new double[][] {
				{26.28, 9}, 
				{27.72, 9},
				{27.72, 18},
				{26.28, 18},
				{26.28, 9},
			};
		Gui.blueAllianceGraph.addData(scaleSides, Color.black);
								
		// Null zone Up
		double[][] nullZoneUp = new double[][] {
				{25, 19.06}, 
				{24, 19.06},
				{24, 27},
				{30, 27},
				{30, 19.06},
				{29, 19.06},
			};
		Gui.blueAllianceGraph.addData(nullZoneUp, Color.black);
				
		// Null zone Down
		double[][] nullZoneDown = new double[][] {
			    {25, 7.94}, 
				{24, 7.94},
				{24, 0},
				{30, 0},
				{30, 7.94},
				{29, 7.94},
			};
		Gui.blueAllianceGraph.addData(nullZoneDown, Color.black);
				
		// Platform Zone
		double[][] platformZoneOne = new double[][] {{16.3, 7.94}, {24, 7.94}};
		Gui.blueAllianceGraph.addData(platformZoneOne, Color.blue);
				
		// Platform Zone
		double[][] platformZoneTwo = new double[][] {{16.3, 19.06}, {24, 19.06}};
		Gui.blueAllianceGraph.addData(platformZoneTwo, Color.blue);
				
		// Platform
		double[][] platform = new double[][] {{21.78, 7.94}, {21.78, 19.06}};
		Gui.blueAllianceGraph.addData(platform, Color.blue);
				
		// Cube Zone
		double[][] cubeZone = new double[][] {
				{11.7, 11.625}, 
				{8.2, 11.625},
				{8.2, 15.375},
				{11.7, 15.375},
			};
		Gui.blueAllianceGraph.addData(cubeZone, Color.blue);
				
		// Switch Plate One
		double[][] switchPlateOne = new double[][] {
				{12, 7.405}, 
				{16, 7.405},
				{16, 10.405},
				{12, 10.405},
				{12, 7.405},
			};
		Gui.blueAllianceGraph.addData(switchPlateOne, Color.red);
				
		// Switch Plate Two
		double[][] switchPlateTwo = new double[][] {
				{12, 19.595}, 
				{16, 19.595},
				{16, 16.595},
				{12, 16.595},
				{12, 19.595},
			};
		Gui.blueAllianceGraph.addData(switchPlateTwo, Color.blue);
		
		// Portal Bottom
		double[][] portalBottom = new double[][] {{0, 2.5}, {2.916, 0}};
		Gui.blueAllianceGraph.addData(portalBottom, Color.blue);
		
		// Portal Top
		double[][] portalTop = new double[][] {{0, 24.5}, {2.916, 27}};
		Gui.blueAllianceGraph.addData(portalTop, Color.blue);
		
		// Exchange Zone
		double[][] exchangeZone = new double[][] {
				{0, 14.5}, 
				{3, 14.5},
				{3, 18.5},
				{0, 18.5},
			};
		Gui.blueAllianceGraph.addData(exchangeZone, Color.blue);
		
		// Cube Zone cubes
		double[][] cubeZoneCubes = new double[][] {
				{11.7, 15.209}, 
				{10.62, 15.209},
				{10.62, 14.669},
				{9.54, 14.669},
				{9.54, 14.129},
				{8.46, 14.129},
				{8.46, 12.871},
				{9.54, 12.871},
				{9.54, 12.331},
				{10.62, 12.331},
				{10.62, 11.791},
				{11.7, 11.791},
			};
		Gui.blueAllianceGraph.addData(cubeZoneCubes, Color.green);
		
		// Cube One
		double[][] cubeOne = new double[][] {
				{16.3, 19.895}, 
				{17.38, 19.895},
				{17.38, 18.815},
				{16.3, 18.815},
			};
		Gui.blueAllianceGraph.addData(cubeOne, Color.green);
		
		// Cube Two
		double[][] cubeTwo = new double[][] {
				{16.3, 17.557}, 
				{17.38, 17.557},
				{17.38, 16.477},
				{16.3, 16.477},
			};
		Gui.blueAllianceGraph.addData(cubeTwo, Color.green);
		
		// Cube Three
		double[][] cubeThree = new double[][] {
				{16.3, 15.219}, 
				{17.38, 15.219},
				{17.38, 14.139},
				{16.3, 14.139},
			};
		Gui.blueAllianceGraph.addData(cubeThree, Color.green);
		
		// Cube Three
		double[][] cubeFour = new double[][] {
				{16.3, 12.861}, 
				{17.38, 12.861},
				{17.38, 11.781},
				{16.3, 11.781},
			};
		Gui.blueAllianceGraph.addData(cubeFour, Color.green);
		
		// Cube Five
		double[][] cubeFive = new double[][] {
				{16.3, 10.523}, 
				{17.38, 10.523},
				{17.38, 9.443},
				{16.3, 9.443},
			};
		Gui.blueAllianceGraph.addData(cubeFive, Color.green);
		
		// Cube Six
		double[][] cubeSix = new double[][] {
				{16.3, 8.185}, 
				{17.38, 8.185},
				{17.38, 7.105},
				{16.3, 7.105},
			};
		Gui.blueAllianceGraph.addData(cubeSix, Color.green);
	}
	
	static void motionGraphRed()
	{
		// Create a blank grid for the field graph
		Gui.redAllianceGraph.yGridOn();
		Gui.redAllianceGraph.xGridOn();
		Gui.redAllianceGraph.setYLabel("Y (feet)");
		Gui.redAllianceGraph.setXLabel("X (feet)");
		Gui.redAllianceGraph.setTitle("Top Down View of FRC Field - Red Alliance (32ft x 27ft) \n shows the left and right wheel trajectories");
							
		//force graph to show field dimensions of 30ft x 27 feet
		double fieldWidth = 27.0;
		Gui.redAllianceGraph.setXTic(0, 32, 1);
		Gui.redAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
		//lets add field markers to help visual
		double[][] redSwitch = new double[][]{
				{11.7, 7.105},
				{16.3, 7.105},
				{16.3, 19.895},
				{11.7, 19.895},
				{11.7, 7.105},
			};
		Gui.redAllianceGraph.addData(redSwitch, Color.black);
					
		// Auto Line
		double[][] autoLine = new double[][] {{10,0}, {10, fieldWidth}};
		Gui.redAllianceGraph.addData(autoLine, Color.black);
						
		// Mid Field Up
		double[][] midLineUp = new double[][] {{27,27}, {27, 21}};
		Gui.redAllianceGraph.addData(midLineUp, Color.black);
		
		// Mid Field Down
		double[][] midLineDown = new double[][] {{27,0}, {27, 6}};
		Gui.redAllianceGraph.addData(midLineDown, Color.black);
						
		// Scale Up
		double[][] scaleUp = new double[][] {
				{25, 18}, 
				{29, 18},
				{29, 21},
				{25, 21},
				{25, 18},
			};
		Gui.redAllianceGraph.addData(scaleUp, Color.blue);
						
		// Scale Down
		double[][] scaleDown = new double[][] {
				{25, 6}, 
				{29, 6},
				{29, 9},
				{25, 9},
				{25, 6},
			};
		Gui.redAllianceGraph.addData(scaleDown, Color.red);
						
		// Scale sides
		double[][] scaleSides = new double[][] {
				{26.28, 9}, 
				{27.72, 9},
				{27.72, 18},
				{26.28, 18},
				{26.28, 9},
			};
		Gui.redAllianceGraph.addData(scaleSides, Color.black);
						
		// Null zone Up
		double[][] nullZoneUp = new double[][] {
				{25, 19.06}, 
				{24, 19.06},
				{24, 27},
				{30, 27},
				{30, 19.06},
				{29, 19.06},
			};
		Gui.redAllianceGraph.addData(nullZoneUp, Color.black);
		
		// Null zone Down
				double[][] nullZoneDown = new double[][] {
						{25, 7.94}, 
						{24, 7.94},
						{24, 0},
						{30, 0},
						{30, 7.94},
						{29, 7.94},
					};
		Gui.redAllianceGraph.addData(nullZoneDown, Color.black);
		
		// Platform Zone
		double[][] platformZoneOne = new double[][] {{16.3, 7.94}, {24, 7.94}};
		Gui.redAllianceGraph.addData(platformZoneOne, Color.red);
		
		// Platform Zone
		double[][] platformZoneTwo = new double[][] {{16.3, 19.06}, {24, 19.06}};
		Gui.redAllianceGraph.addData(platformZoneTwo, Color.red);
		
		// Platform
		double[][] platform = new double[][] {{21.78, 7.94}, {21.78, 19.06}};
		Gui.redAllianceGraph.addData(platform, Color.red);
		
		// Cube Zone
		double[][] cubeZone = new double[][] {
				{11.7, 11.625}, 
				{8.2, 11.625},
				{8.2, 15.375},
				{11.7, 15.375},
			};
		Gui.redAllianceGraph.addData(cubeZone, Color.red);
		
		// Switch Plate One
		double[][] switchPlateOne = new double[][] {
				{12, 7.405}, 
				{16, 7.405},
				{16, 10.405},
				{12, 10.405},
				{12, 7.405},
			};
		Gui.redAllianceGraph.addData(switchPlateOne, Color.red);
		
		// Switch Plate Two
		double[][] switchPlateTwo = new double[][] {
				{12, 19.595}, 
				{16, 19.595},
				{16, 16.595},
				{12, 16.595},
				{12, 19.595},
			};
		Gui.redAllianceGraph.addData(switchPlateTwo, Color.blue);
		
		// Portal Bottom
		double[][] portalBottom = new double[][] {{0, 2.5}, {2.916, 0}};
		Gui.redAllianceGraph.addData(portalBottom, Color.red);
				
		// Portal Top
		double[][] portalTop = new double[][] {{0, 24.5}, {2.916, 27}};
		Gui.redAllianceGraph.addData(portalTop, Color.red);
		
		// Exchange Zone
		double[][] exchangeZone = new double[][] {
				{0, 14.5}, 
				{3, 14.5},
				{3, 18.5},
				{0, 18.5},
			};
		Gui.redAllianceGraph.addData(exchangeZone, Color.red);
		
		// Exchange Zone
		double[][] cubeZoneCubes = new double[][] {
				{11.7, 15.209}, 
				{10.62, 15.209},
				{10.62, 14.669},
				{9.54, 14.669},
				{9.54, 14.129},
				{8.46, 14.129},
				{8.46, 12.871},
				{9.54, 12.871},
				{9.54, 12.331},
				{10.62, 12.331},
				{10.62, 11.791},
				{11.7, 11.791},
			};
		Gui.redAllianceGraph.addData(cubeZoneCubes, Color.green);
		
		// Cube One
		double[][] cubeOne = new double[][] {
				{16.3, 19.895}, 
				{17.38, 19.895},
				{17.38, 18.815},
				{16.3, 18.815},
			};
		Gui.redAllianceGraph.addData(cubeOne, Color.green);
				
		// Cube Two
		double[][] cubeTwo = new double[][] {
				{16.3, 17.557}, 
				{17.38, 17.557},
				{17.38, 16.477},
				{16.3, 16.477},
			};
		Gui.redAllianceGraph.addData(cubeTwo, Color.green);
				
		// Cube Three
		double[][] cubeThree = new double[][] {
				{16.3, 15.219}, 
				{17.38, 15.219},
				{17.38, 14.139},
				{16.3, 14.139},
			};
		Gui.redAllianceGraph.addData(cubeThree, Color.green);
			
		// Cube Three
		double[][] cubeFour = new double[][] {
				{16.3, 12.861}, 
				{17.38, 12.861},
				{17.38, 11.781},
				{16.3, 11.781},
			};
		Gui.redAllianceGraph.addData(cubeFour, Color.green);
			
		// Cube Five
		double[][] cubeFive = new double[][] {
				{16.3, 10.523}, 
				{17.38, 10.523},
				{17.38, 9.443},
				{16.3, 9.443},
			};
		Gui.redAllianceGraph.addData(cubeFive, Color.green);
				
		// Cube Six
		double[][] cubeSix = new double[][] {
				{16.3, 8.185}, 
				{17.38, 8.185},
				{17.38, 7.105},
				{16.3, 7.105},
			};
		Gui.redAllianceGraph.addData(cubeSix, Color.green);
	}
	
	static void velocityGraph()
	{
		Gui.velocityGraph.yGridOn();
      	Gui.velocityGraph.xGridOn();
      	Gui.velocityGraph.setYLabel("Velocity (ft/sec)");
      	Gui.velocityGraph.setXLabel("time (seconds)");
      	Gui.velocityGraph.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
	}
}