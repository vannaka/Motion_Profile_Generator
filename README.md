# Motion Profile Generator
Generate Motion Profiles to follow with a Talon SRX
 
![alt text][logo]

[logo]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/MotionWindow.PNG

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
- **Editing existing points**
	- Once you have added a point to the list you can double click on it to change it. 
	- The old point will be deleted and the edited point will take that place
	- After the point is replaced it will go back to normal behavoir and add points to the end

## Load in previous Profile
---
- When you save your profile, a preference file is also saved with your settings and waypoints as a text file with a .bot extention
- Press the "Load Profile" option in the File menu to choose the preference file 
- Your profile settings will be imported for you to use
	
## Save Profile to CSV
---
 
![alt text][logo1]

[logo1]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/ChooseDirectory.PNG

- Enter the file name in the File Name text box
- Click the Browse button to choose the directory you want to save the profiles in
- Click the Save button or the "File/Save Profile" Menu option to save the profiles
- The motion profiles are saved as CSV files. One for each left and right
	- Detailed CSV
		- This file contains more information than the normal CSV
		- It contains Time Step, X, Y, Position, Velocity, Acceleration, Jerk, and Heading
	- Normal CSV
		- Only position, velocity, and time step are output to this file
		- This is the file you will put on your RoboRio

## Menu Bar
---

![alt text][logo2]

[logo2]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/MenuBar.PNG

- **File Menu**
	- New Profile
		- Clears all your waypoints and graphs allowing you to start over
	- Save Profile
		- Allows you to save your profiles
	- Load Profile
		- Allows you to load a previous profile
	- Exit
		- Exit the application
- **Help Menu**
	- Help
		- Opens a browser window to this github
	- About
		- Displays a window with information about the app. ie: The app version and the developers
		
![alt text][logo3]

[logo3]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/About.PNG

## Acknowledgments
---

- [Jaci](https://github.com/JacisNonsense/Pathfinder) for the path generation code
- [KHEngineering](https://github.com/KHEngineering/SmoothPathPlanner) for the graph code
