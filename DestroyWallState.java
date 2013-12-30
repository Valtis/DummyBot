import java.util.LinkedList;
import java.util.Queue;

public class DestroyWallState extends BotState {

    @Override
    public Move execute(int id) {

        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }

        // if there are treasures that are reachable, switch to treasure hunt mode
        Point closestTreasure = GameState.getInstance().getClosestTreasure(id);
        if (closestTreasure != null &&
                pathfinder.calculatePath(GameState.getInstance().getGameField(), GameState.getInstance().getBotPosition(id), closestTreasure).size() != 0) {
            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        // check if we are close enough to the tile - todo: add logic to check that the tile is actually reachable!
        Point myLocation = GameState.getInstance().getBotPosition(id);

        int explosionRadius = GameState.getInstance().getBombExplosionRadius();
        if (myLocation != null && destination != null && isOnSameLineAndCloseEnough(myLocation, destination, explosionRadius)) {
            nextState = new AvoidExplosionState(); // have to escape from the bomb
            return Move.BOMB;

        }

        // check if a path already exists and if the end point is still valid - todo: similar code exists in multiple states - extract to base class? DRY princible being violated
        if (path != null && !path.isEmpty() && GameState.getInstance().getTileType(destination) == TileType.SOFTBLOCK) {
            Point nextPoint = Move.getNextPoint(GameState.getInstance().getBotPosition(id), path.peek());
            if (!GameState.getInstance().isInsideBombExplosionRadius(nextPoint)) {
                return path.poll();
            }
            return Move.WAIT; // bomb explosion area ahead - wait for it to explode
        }


        // calculate new path

        destination = GameState.getInstance().getClosestWall(id);

        if (destination == null) {
            // uh, ran out of walls. I guess should move to combat then
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
            path = pathfinder.calculatePath(GameState.getInstance().getGameField(), myPoint, new Point(x, destination.y));

            if (path.isEmpty()) {
                path = pathfinder.calculatePath(GameState.getInstance().getGameField(), myPoint, new Point(destination.x, y));
            }
        }

        if (!path.isEmpty()) {
            return path.poll();
        }

        return Move.WAIT;
    }

}
