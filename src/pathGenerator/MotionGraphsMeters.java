package pathGenerator;

import java.awt.Color;

public class MotionGraphsMeters {
	
	static void motionGraphBlue()
	{
		// Create a blank grid for the field graph
		Gui.blueAllianceGraph.yGridOn();
		Gui.blueAllianceGraph.xGridOn();
		Gui.blueAllianceGraph.setYLabel("Y (Meters)");
		Gui.blueAllianceGraph.setXLabel("X (Meters)");
		Gui.blueAllianceGraph.setTitle("Top Down View of FRC Field - Blue Alliance (10m x 8.23m) \n shows global position of robot path with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 10m x 8.23m
		double fieldWidth = 8.23;
		Gui.blueAllianceGraph.setXTic(0, 10, 1);
		Gui.blueAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
		//lets add field markers to help visual
		double[][] redSwitch = new double[][]{
				{3.566, 2.165},
				{4.968, 2.165},
				{4.968, 6.064},
				{3.566, 6.064},
				{3.566, 2.165},
			};
		Gui.blueAllianceGraph.addData(redSwitch, Color.black);
							
		// Auto Line
		double[][] autoLine = new double[][] {{3.048,0}, {3.048, fieldWidth}};
		Gui.blueAllianceGraph.addData(autoLine, Color.black);
								
		// Mid Field Up
		double[][] midLineUp = new double[][] {{8.229, fieldWidth}, {8.229, 6.401}};
		Gui.blueAllianceGraph.addData(midLineUp, Color.black);
				
		// Mid Field Down
		double[][] midLineDown = new double[][] {{8.229,0}, {8.229, 1.829}};
		Gui.blueAllianceGraph.addData(midLineDown, Color.black);
								
		// Scale Up
		double[][] scaleUp = new double[][] {
				{7.62, 5.486}, 
				{8.839, 5.486},
				{8.839, 6.401},
				{7.62, 6.401},
				{7.62, 5.486},
			};
		Gui.blueAllianceGraph.addData(scaleUp, Color.blue);
								
		// Scale Down
		double[][] scaleDown = new double[][] {
				{7.62, 1.829}, 
				{8.839, 1.829},
				{8.839, 2.743},
				{7.62, 2.743},
				{7.62, 1.829},
			};
		Gui.blueAllianceGraph.addData(scaleDown, Color.red);
								
		// Scale sides
		double[][] scaleSides = new double[][] {
				{8.01, 2.743}, 
				{8.449, 2.743},
				{8.449, 5.486},
				{8.01, 5.486},
				{8.01, 2.743},
			};
		Gui.blueAllianceGraph.addData(scaleSides, Color.black);
								
		// Null zone Up
		double[][] nullZoneUp = new double[][] {
				{7.62, 5.809}, 
				{7.315, 5.809},
				{7.315, 8.229},
				{9.144, 8.229},
				{9.144, 5.809},
				{8.839, 5.809},
			};
		Gui.blueAllianceGraph.addData(nullZoneUp, Color.black);
				
		// Null zone Down
		double[][] nullZoneDown = new double[][] {
			    {7.62, 2.42}, 
				{7.315, 2.42},
				{7.315, 0},
				{9.144, 0},
				{9.144, 2.42},
				{8.839, 2.42},
			};
		Gui.blueAllianceGraph.addData(nullZoneDown, Color.black);
				
		// Platform Zone
		double[][] platformZoneOne = new double[][] {{4.968, 2.42}, {7.315, 2.42}};
		Gui.blueAllianceGraph.addData(platformZoneOne, Color.blue);
				
		// Platform Zone
		double[][] platformZoneTwo = new double[][] {{4.968, 5.809}, {7.315, 5.809}};
		Gui.blueAllianceGraph.addData(platformZoneTwo, Color.blue);
				
		// Platform
		double[][] platform = new double[][] {{6.639, 2.42}, {6.639, 5.809}};
		Gui.blueAllianceGraph.addData(platform, Color.blue);
				
		// Cube Zone
		double[][] cubeZone = new double[][] {
				{3.566, 3.543}, 
				{2.499, 3.543},
				{2.499, 4.686},
				{3.566, 4.686},
			};
		Gui.blueAllianceGraph.addData(cubeZone, Color.blue);
				
		// Switch Plate One
		double[][] switchPlateOne = new double[][] {
				{3.658, 2.257},
				{4.877, 2.257},
				{4.877, 3.171},
				{3.658, 3.171},
				{3.658, 2.257},
			};
		Gui.blueAllianceGraph.addData(switchPlateOne, Color.red);
				
		// Switch Plate Two
		double[][] switchPlateTwo = new double[][] {
				{3.658, 5.973}, 
				{4.877, 5.973},
				{4.877, 5.058},
				{3.658, 5.058},
				{3.658, 5.973},
			};
		Gui.blueAllianceGraph.addData(switchPlateTwo, Color.blue);
		
		// Portal Bottom
		double[][] portalBottom = new double[][] {{0, .762}, {.889, 0}};
		Gui.blueAllianceGraph.addData(portalBottom, Color.blue);
		
		// Portal Top
		double[][] portalTop = new double[][] {{0, 7.468}, {.889, 8.296}};
		Gui.blueAllianceGraph.addData(portalTop, Color.blue);
		
		// Exchange Zone
		double[][] exchangeZone = new double[][] {
				{0, 4.419}, 
				{0.914, 4.419},
				{0.914, 5.639},
				{0, 5.639},
			};
		Gui.blueAllianceGraph.addData(exchangeZone, Color.blue);
		
		// Cube Zone cubes
		double[][] cubeZoneCubes = new double[][] {
				{3.566, 4.636}, 
				{3.237, 4.636},
				{3.237, 4.471},
				{2.908, 4.471},
				{2.908, 4.307},
				{2.579, 4.307},
				{2.579, 3.923},
				{2.908, 3.923},
				{2.908, 3.758},
				{3.237, 3.758},
				{3.237, 3.594},
				{3.566, 3.594},
			};
		Gui.blueAllianceGraph.addData(cubeZoneCubes, Color.green);
		
		// Cube One
		double[][] cubeOne = new double[][] {
				{4.968, 6.064}, 
				{5.297, 6.064},
				{5.297, 5.735},
				{4.968, 5.735},
			};
		Gui.blueAllianceGraph.addData(cubeOne, Color.green);
		
		// Cube Two
		double[][] cubeTwo = new double[][] {
				{4.968, 5.351}, 
				{5.297, 5.351},
				{5.297, 5.022},
				{4.968, 5.022},
			};
		Gui.blueAllianceGraph.addData(cubeTwo, Color.green);
		
		// Cube Three
		double[][] cubeThree = new double[][] {
				{4.968, 4.639}, 
				{5.297, 4.639},
				{5.297, 4.310},
				{4.968, 4.310},
			};
		Gui.blueAllianceGraph.addData(cubeThree, Color.green);
		
		// Cube Three
		double[][] cubeFour = new double[][] {
				{4.968, 3.920}, 
				{5.297, 3.920},
				{5.297, 3.591},
				{4.968, 3.591},
			};
		Gui.blueAllianceGraph.addData(cubeFour, Color.green);
		
		// Cube Five
		double[][] cubeFive = new double[][] {
				{4.968, 3.207}, 
				{5.297, 3.207},
				{5.297, 2.878},
				{4.968, 2.878},
			};
		Gui.blueAllianceGraph.addData(cubeFive, Color.green);
		
		// Cube Six
		double[][] cubeSix = new double[][] {
				{4.968, 2.495}, 
				{5.297, 2.495},
				{5.297, 2.167},
				{4.968, 2.167},
			};
		Gui.blueAllianceGraph.addData(cubeSix, Color.green);
	}
	
	static void motionGraphRed()
	{
		// Create a blank grid for the field graph
		Gui.redAllianceGraph.yGridOn();
		Gui.redAllianceGraph.xGridOn();
		Gui.redAllianceGraph.setYLabel("Y (Meters)");
		Gui.redAllianceGraph.setXLabel("X (Meters)");
		Gui.redAllianceGraph.setTitle("Top Down View of FRC Field - Blue Alliance (10m x 8.23m) \n shows global position of robot path with left and right wheel trajectories");
			
					
		//force graph to show field dimensions of 10m x 8.23m
		double fieldWidth = 8.23;
		Gui.redAllianceGraph.setXTic(0, 10, 1);
		Gui.redAllianceGraph.setYTic(0, fieldWidth, 1);
					
					
		//lets add field markers to help visual
		double[][] redSwitch = new double[][]{
				{3.566, 2.165},
				{4.968, 2.165},
				{4.968, 6.064},
				{3.566, 6.064},
				{3.566, 2.165},
			};
		Gui.redAllianceGraph.addData(redSwitch, Color.black);
							
		// Auto Line
		double[][] autoLine = new double[][] {{3.048,0}, {3.048, fieldWidth}};
		Gui.redAllianceGraph.addData(autoLine, Color.black);
								
		// Mid Field Up
		double[][] midLineUp = new double[][] {{8.229, fieldWidth}, {8.229, 6.401}};
		Gui.redAllianceGraph.addData(midLineUp, Color.black);
				
		// Mid Field Down
		double[][] midLineDown = new double[][] {{8.229,0}, {8.229, 1.829}};
		Gui.redAllianceGraph.addData(midLineDown, Color.black);
								
		// Scale Up
		double[][] scaleUp = new double[][] {
				{7.62, 5.486}, 
				{8.839, 5.486},
				{8.839, 6.401},
				{7.62, 6.401},
				{7.62, 5.486},
			};
		Gui.redAllianceGraph.addData(scaleUp, Color.blue);
								
		// Scale Down
		double[][] scaleDown = new double[][] {
				{7.62, 1.829}, 
				{8.839, 1.829},
				{8.839, 2.743},
				{7.62, 2.743},
				{7.62, 1.829},
			};
		Gui.redAllianceGraph.addData(scaleDown, Color.red);
								
		// Scale sides
		double[][] scaleSides = new double[][] {
				{8.01, 2.743}, 
				{8.449, 2.743},
				{8.449, 5.486},
				{8.01, 5.486},
				{8.01, 2.743},
			};
		Gui.redAllianceGraph.addData(scaleSides, Color.black);
								
		// Null zone Up
		double[][] nullZoneUp = new double[][] {
				{7.62, 5.809}, 
				{7.315, 5.809},
				{7.315, 8.229},
				{9.144, 8.229},
				{9.144, 5.809},
				{8.839, 5.809},
			};
		Gui.redAllianceGraph.addData(nullZoneUp, Color.black);
				
		// Null zone Down
		double[][] nullZoneDown = new double[][] {
			    {7.62, 2.42}, 
				{7.315, 2.42},
				{7.315, 0},
				{9.144, 0},
				{9.144, 2.42},
				{8.839, 2.42},
			};
		Gui.redAllianceGraph.addData(nullZoneDown, Color.black);
				
		// Platform Zone
		double[][] platformZoneOne = new double[][] {{4.968, 2.42}, {7.315, 2.42}};
		Gui.redAllianceGraph.addData(platformZoneOne, Color.red);
				
		// Platform Zone
		double[][] platformZoneTwo = new double[][] {{4.968, 5.809}, {7.315, 5.809}};
		Gui.redAllianceGraph.addData(platformZoneTwo, Color.red);
				
		// Platform
		double[][] platform = new double[][] {{6.639, 2.42}, {6.639, 5.809}};
		Gui.redAllianceGraph.addData(platform, Color.red);
				
		// Cube Zone
		double[][] cubeZone = new double[][] {
				{3.566, 3.543}, 
				{2.499, 3.543},
				{2.499, 4.686},
				{3.566, 4.686},
			};
		Gui.redAllianceGraph.addData(cubeZone, Color.red);
				
		// Switch Plate One
		double[][] switchPlateOne = new double[][] {
				{3.658, 2.257},
				{4.877, 2.257},
				{4.877, 3.171},
				{3.658, 3.171},
				{3.658, 2.257},
			};
		Gui.redAllianceGraph.addData(switchPlateOne, Color.red);
				
		// Switch Plate Two
		double[][] switchPlateTwo = new double[][] {
				{3.658, 5.973}, 
				{4.877, 5.973},
				{4.877, 5.058},
				{3.658, 5.058},
				{3.658, 5.973},
			};
		Gui.redAllianceGraph.addData(switchPlateTwo, Color.blue);
		
		// Portal Bottom
		double[][] portalBottom = new double[][] {{0, .762}, {.889, 0}};
		Gui.redAllianceGraph.addData(portalBottom, Color.red);
		
		// Portal Top
		double[][] portalTop = new double[][] {{0, 7.468}, {.889, 8.296}};
		Gui.redAllianceGraph.addData(portalTop, Color.red);
		
		// Exchange Zone
		double[][] exchangeZone = new double[][] {
				{0, 4.419}, 
				{0.914, 4.419},
				{0.914, 5.639},
				{0, 5.639},
			};
		Gui.redAllianceGraph.addData(exchangeZone, Color.red);
		
		// Cube Zone cubes
		double[][] cubeZoneCubes = new double[][] {
				{3.566, 4.636}, 
				{3.237, 4.636},
				{3.237, 4.471},
				{2.908, 4.471},
				{2.908, 4.307},
				{2.579, 4.307},
				{2.579, 3.923},
				{2.908, 3.923},
				{2.908, 3.758},
				{3.237, 3.758},
				{3.237, 3.594},
				{3.566, 3.594},
			};
		Gui.redAllianceGraph.addData(cubeZoneCubes, Color.green);
		
		// Cube One
		double[][] cubeOne = new double[][] {
				{4.968, 6.064}, 
				{5.297, 6.064},
				{5.297, 5.735},
				{4.968, 5.735},
			};
		Gui.redAllianceGraph.addData(cubeOne, Color.green);
		
		// Cube Two
		double[][] cubeTwo = new double[][] {
				{4.968, 5.351}, 
				{5.297, 5.351},
				{5.297, 5.022},
				{4.968, 5.022},
			};
		Gui.redAllianceGraph.addData(cubeTwo, Color.green);
		
		// Cube Three
		double[][] cubeThree = new double[][] {
				{4.968, 4.639}, 
				{5.297, 4.639},
				{5.297, 4.310},
				{4.968, 4.310},
			};
		Gui.redAllianceGraph.addData(cubeThree, Color.green);
		
		// Cube Three
		double[][] cubeFour = new double[][] {
				{4.968, 3.920}, 
				{5.297, 3.920},
				{5.297, 3.591},
				{4.968, 3.591},
			};
		Gui.redAllianceGraph.addData(cubeFour, Color.green);
		
		// Cube Five
		double[][] cubeFive = new double[][] {
				{4.968, 3.207}, 
				{5.297, 3.207},
				{5.297, 2.878},
				{4.968, 2.878},
			};
		Gui.redAllianceGraph.addData(cubeFive, Color.green);
		
		// Cube Six
		double[][] cubeSix = new double[][] {
				{4.968, 2.495}, 
				{5.297, 2.495},
				{5.297, 2.167},
				{4.968, 2.167},
			};
		Gui.redAllianceGraph.addData(cubeSix, Color.green);
	}
	
	static void velocityGraph()
	{
		Gui.velocityGraph.yGridOn();
      	Gui.velocityGraph.xGridOn();
      	Gui.velocityGraph.setYLabel("Velocity (m/sec)");
      	Gui.velocityGraph.setXLabel("time (seconds)");
      	Gui.velocityGraph.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
	}
}