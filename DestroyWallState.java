import java.util.LinkedList;
import java.util.Queue;

public class DestroyWallState extends BotState {

    @Override
    public Move execute(int id) {
        DebugWriter.write("\nEntering DestroyWallState\n");

        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }

        // if there are treasures that are reachable, switch to treasure hunt mode
        Point closestTreasure = GameState.getInstance().getClosestTreasure(id);
        if (closestTreasure != null &&
                pathfinder.calculatePath(GameState.getInstance().getBotPosition(id), closestTreasure).size() != 0) {
            DebugWriter.write("Found reachable treasures - switching to TreasureHuntState()\n");
            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        // check if we are close enough to the tile - if so, drop a bomb todo: add logic to check that the tile is actually reachable!
        // also check if there are already bombs nearby - if so, do not drop a bomb to reduce changes of suiciding
        Point myLocation = GameState.getInstance().getBotPosition(id);

        int explosionRadius = GameState.getInstance().getBombExplosionRadius();
        int closestBomb = GameState.getInstance().getClosestBombDistance(id);

        if (destination != null && isOnSameLineAndCloseEnough(myLocation, destination, explosionRadius) && closestBomb > explosionRadius + 2) {
            DebugWriter.write("Dropping a bomb to destroy a wall - switching to AvoidExplosionState()\n");
            nextState = new AvoidExplosionState(); // have to escape from the bomb
            return Move.BOMB;

        }

        // check if a path already exists and if the end point is still valid - todo: similar code exists in multiple states - extract to base class? DRY princible being violated
        if (path != null && !path.isEmpty() && GameState.getInstance().getTileType(destination) == TileType.SOFTBLOCK) {
            return MoveOrWaitForBomb(id);
        }


        // calculate new path
        destination = GameState.getInstance().getClosestWall(id);
        DebugWriter.write("Calculating new path to " + destination + "\n");

        if (destination == null) {
            // uh, ran out of walls. I guess should move to combat then
            DebugWriter.write("No walls to destroy - swithing to combat state!\n");
            nextState = new CombatState();
            return Move.REDO;
        }

        if (path == null) {
            path = new LinkedList<Move>();
        } else {
            path.clear();
        }
        Point myPoint = GameState.getInstance().getBotPosition(id);

        for (int x = destination.x - explosionRadius, y = destination.y - explosionRadius; path.isEmpty() && x <= destination.x + explosionRadius && y <= destination.y + explosionRadius; ++x, ++y) {
            path = pathfinder.calculatePath(myPoint, new Point(x, destination.y));

            if (path.isEmpty()) {
                path = pathfinder.calculatePath(myPoint, new Point(destination.x, y));
            }
        }

        if (path != null && !path.isEmpty()) {
           return MoveOrWaitForBomb(id);
        }

        DebugWriter.write("No valid path exists - waiting\n");
        return Move.WAIT;
    }

}
