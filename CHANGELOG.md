# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2018-1-27
### Added 
- Add button to delete last point in the list (Thanks Team 1414)

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