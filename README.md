# Motion Profile Generator
Generate motion profiles to follow with a Talon SRX
 
![alt text][logo]

[logo]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/MotionWindow.PNG "Motion Window"

## Motion Variables
---
- **Time Step**
	- The rate at which the control loop on the RoboRio runs
	- Units are in seconds
- **Velocity**
	- The max velocity rate your robot is capable of achieving
	- Units are in ft/s
- **Acceleration**
	- The max acceleration rate your robot is capable of achieving
	- Units are in ft/s/s
- **Jerk**
	- The rate of change of acceleration; that is, the derivative of acceleration with respect to time
	- Units are in ft/s/s/s
- **Wheel Base**
	- The distance between your left and right wheels
	- Units are in feet
	
## Waypoints
---
- **All points are relative meaning you do not have to start at 0,0**
- **X**
	- Forward and Backwards movement of the robot
	- Inrease X to move forwards
	- Decrease X to move backwards
- **Y**
	- Left and Right movement of the robot
	- Inrease Y to move left
	- Decrease Y to move right
- **Angle**
	- This is the **ending** angle after the robot reaches the point
	- The first point should **always** have an angle of Zero
 
