#!/bin/bash
#
# Starts the game
# for the ease of trying, well also compile java now. but when compiling takes time, use of compile.sh is recommended

javac DummyBot.java
javac AStar.java
javac AvoidExplosionState.java
javac Bot.java
javac BotState.java
javac CombatState.java
javac DestroyWallState.java
javac GameState.java
javac GameData.java
javac Point.java
javac Move.java
javac Pathfinder.java
javac StateMachineBot.java
javac TileType.java
javac TreasureHuntState.java
javac BreadthFirstSearch.java

java DummyBot %1 %2
