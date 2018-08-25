# Motion Profile Generator
Generate Motion Profiles to follow with a Talon SRX.
 
![alt text][logo]

[logo]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/MotionWindow.JPG/

## Motion Variables
- **Time Step**
	- The rate at which the control loop on the RoboRio runs
	- Units are in seconds
- **Velocity**
	- The max velocity rate your robot is capable of achieving
- **Acceleration**
	- The max acceleration rate your robot is capable of achieving
- **Jerk**
	- The rate of change of acceleration; that is, the derivative of acceleration with respect to time
- **Wheel Base Width**
	- The distance between your left and right wheels
- **Wheel Base Depth**
	- The distance between your front and back wheels
	- Used with the Swerve modifier
- **Drive Base**
    - The type of configuration your drive base is using
    - Can be Tank or Swerve
- **Units**
    - The unit of measurement used for measuring distance
    - Can be Imperial (ft) or Metric (m)
- **Fit Method**
    - The hermitic method for interpolating points from waypoints
    - Can be cubic or quintic	

## Waypoints
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
	- Once you have added a point to the list you can double click on it to change it
	- After you have edited the point, the trajectory will be updated

## Load Project
- Motion profile projects are saved in XML format. 
You can load them to continue editing them by using the "Open" option in.
the "File" menu.
- Projects from the previous version, 2.3.0, can be loaded using "Import" option in the "File" menu.
	
## Export Profile
 
![alt text][logo1]

[logo1]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/exportprofile.JPG

1. Enter the root name for the trajectory as the file name
2. Choose the type of export you want to save the trajectories as
   - *.csv: Comma Separated Values, human readable
   - *.traj: Binary Trajectory file, not human readable

3-5 trajectories should be exported, depending on the type of drive base you use:
- Source trajectory: the center trajectory, what is created regardless of the drive base.
- **Tank Drive:**
  - Left Trajectory: trajectory for the left side of the drive base.
  - Right Trajectory: trajectory for the right side of the drive base.
  
- **Swerve Drive:**
  - Front-Left Trajectory: trajectory for the front-left motor of the drive base.
  - Front-Right Trajectory: trajectory for the front-right motor of the drive base.
  - Back-Left Trajectory: trajectory for the back-left motor of the drive base.
  - Back-Right Trajectory: trajectory for the back-right motor of the drive base.

## Menu Bar

![alt text][logo2]

[logo2]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/menubar.JPG

- **File Menu**
	- New: Clears all your waypoints and graphs allowing you to start over
	- Open: Opens a motion profile project (project files are *.xml)
	- Save: Saves to the working project file
	- Save As...: Saves the project to a new file
	- Import...: Imports a project from v2.3.0 .bot file
	- Export...: Exports the trajectory files to a directory
	- Settings...: Change application settings
	- Exit: Exits the application
- **Help Menu**
	- Help: Opens a browser window to this github
	- About: Displays a window with information about the app. ie: The app version and the developers
	
## Settings
    
![alt text][logo3]

[logo3]: https://github.com/vannaka/Motion_Profile_Generator/blob/master/images/settings.JPG
**NOTE: Settings file saved to `%user.dir%\.motion-profile-generator\mpg.properties`**
- Position graph overlay: 32:27 image to use as an overlay to the position chart

## Build Information
- You will need JDK 8
- In the project folder run `gradle build`
- The JAR artifact will be created in the build folder
		
## Libraries Used

- **[Pathfinder](https://github.com/JacisNonsense/Pathfinder)** 
- **[native-lib-loader](https://github.com/scijava/native-lib-loader)** 
