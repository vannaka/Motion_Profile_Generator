# Motion Profile Generator
Generate Motion Profiles to follow with a Talon SRX
 
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
	
## Save File
---
 
![alt text][logo2] 
![alt text][logo1] 

[logo1]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/ChooseDirectory.PNG "Directory Window"
[logo2]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/FileMenu.PNG "File Menu"

- Click "File/Save Profile" and the Directory window will appear
- Choose the directory you want to save your motion profiles in then click open and they will be saved
- The motion profiles are saved as CSV files. One for each left and right
	- Detailed CSV
		- This file contains more information than the normal CSV
		- It contains Time Step, X, Y, Position, Velocity, Acceleration, Jerk, and Heading
	- Normal CSV
		- Only position, velocity, and time step are output to this file
		- This is the file you will put on your RoboRio

## Menu Bar
---

![alt text][logo3] 
![alt text][logo4] 
![alt text][logo5] 

[logo3]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/FileMenu.PNG "File Menu"
[logo4]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/HelpMenu.PNG "Help Menu"
[logo5]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/About.PNG "About Page"

- **File Menu**
	- New Profile
		- Clears all your waypoints and graphs allowing you to start over
	- Save Profile
		- The directory chooser is brought up allowing you to save your profiles
	- Exit
		- Exit the application
- **Help Menu**
	- Help
		- Opens a browser window to this github
	- About
		- Displays a window with information about the app. ie: The app version and the developers