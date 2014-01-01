#!/bin/bash

# Do whatever needed for compiling
# Will be run on server efore gamse (onve for each version uploaded to server
# Compilation can be done in run.sh - but if compilation takes longer, this should be used
# our dummyBot is compiled in run.sh for ease of development
# javac DummyBot.java

javac DummyBot.java
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
