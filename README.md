# Gizmoball
Development of a program that plays Gizmoball

## Description
Gizmoball is a version of pinball, an arcade game in which the objective is to keep a ball moving around in the game, without falling off the bottom of the playing area. The player controls a set of flippers that can bat at the ball as it falls.

The advantage of Gizmoball over a traditional pinball machine is that Gizmoball allows users to construct their own machine layout by placing gizmos (such as bumpers, flippers, and absorbers) on the playing field.

Full specification of the project can be found in this link:
https://personal.cis.strath.ac.uk/murray.wood/Gizmoball/Gizmoball_spec.htm

## How to use it
The game has two modes: <b>Run mode</b> and <b>Build mode</b>.

In <b>Run mode</b> you can load a game file and use the control buttons at the bottom to play the game.

<img src="https://user-images.githubusercontent.com/15669909/83965235-ccb4b600-a8ba-11ea-97a4-077d8f39935e.png">

<img src="https://user-images.githubusercontent.com/15669909/83965241-eb1ab180-a8ba-11ea-9706-7d2e37ff6623.png">

In <b>Build mode</b> you can edit an already loaded game or build one from scratch.

<img src="https://user-images.githubusercontent.com/15669909/83965263-0d143400-a8bb-11ea-9f6a-f67c498e0764.png">

In build mode you can:
- Add new gizmos (squares, circles, triangles, left flippers, right flippers and absorbers)
- Add multiple balls (which is an additional feature)
- Add trigger connections to gizmos (activate gizmos action when another gizmos trigger os activated)
- Add key connections to gizmos (activate gizmos action when a button is pressed or released)
- Move, delete and rotate all elements on the board
- Adjust board parameters, such as gravity and friction
- Adjust ball velocity

## Requirements:
- JDK 8
- Maven

## Build:
````
mvn clean install
````
