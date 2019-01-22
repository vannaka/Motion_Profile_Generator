# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [4.0.1] - 2019-1-22
### Changed
- Fix bug where the x-axis was labeled incorrectly on the position graph when the units were changed.

## [4.0.0] - 2019-1-2
### Added
- Waypoints can now be repositioned by clicking and dragging.
- Now select what data you want exported in the csv and in what order.
- Select which generator you want to use ( Only Pathfinder V1 for now ).
- Graph maintains aspect ratio when resized.
- Field image loaded from jar.
- Now supports Windows, Mac and Linux.

### Changed
- MOVED TO JAVA 11
- Moved generator (Pathfinder) variables into the settings window.
- Added a slight transparency to the grid lines on the position graph.
- Misc. UI changes.

### Removed
- "CSV Type" setting.
- Support for .bot files (project settings).
- Support for .traj export file type (binary path file).

## [3.0.0] - 2018-4-29
### Added
- Completely rewritten using JavaFX for the GUI
- Added a settings menu with settings that will persist across runs
- Settings now saved in xml in home directory
- Added new unit (Inches)
- Changeable background image for position graph.
- Export CSV type; Talon SRX or Jaci
- Show waypoints on position graph.
- Auto regenerate trajectory when any value is changed.
- Other misc changes. Easter eggs?


## [2.3.0] - 2018-2-22
### Added
- Added ability to choose between different units (Feet and Meters)

### Fixed
- Bug where a user could get stuck in point update mode

## [2.2.0] - 2018-2-1
### Added
- Added ability to choose the Fit method of the points. Hermite Cubic or Hermite Quintic
- Added ability to choose between the Tank and Swerve modifiers
- Added Tool tip text

### Fixed
- Bug where app would crash when you double clicked in white space to update a point 

## [2.1.0] - 2018-1-27
### Added 
- Added button to delete last point in the list (Thanks Team 1414)

## [2.0.0] - 2018-1-10
### Added 
- Added graphs for this years game!!!

## [1.2.0] - 2017-12-29
### Added
- Added a preference system to reload old profile settings

### Fixed
- The menu saving option now checks if file exists before saving

## [1.1.0] - 2017-12-20
### Added
- Ability to edit existing waypoints without clearing entire list

## [1.0.2] - 2017-12-16
### Added
- Ability to replace existing file when saving

### Changed
- Moved Velocity Graph to the tabbed pane
- Reworked Saving UX to make it more user friendly
- Motion Profile displays on both red and blue tab

### Fixed
- Resized menu bar to extend across entire window
- Negative variables are now checked for
- Fixed bug that allowed multiple graphs to be displayed on one graph

## [1.0.1] - 2017-11-11
### Added
- Originally only had the blue alliance graph. Added red alliance graph
- Tabbed view to switch between red and blue graphs
- Motion Profile displays based on chosen tab
- Menu bar with various items

### Removed
- Save Button (moved action to menu bar)

## [1.0.0] - 2017-11-05
### Added
- Initial Release
- Graphs to display the motion profile and the velocity
- Text boxes to enter the variables: Time Step, Velocity, Acceleration, Jerk, Wheel Base
- Ability to enter waypoints through a separate text box for each: X, Y, and Angle
- Validation for waypoints
- Display points in a list box each time the "Add Point" button is pressed
- Clear button clears the graphs and the list box 
- Text box to name the output file
- Save button to choose directory and save motion profile
- Generate button to calculate profile and velocity and display them