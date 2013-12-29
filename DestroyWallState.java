import java.util.Queue;

public class DestroyWallState extends BotState {

    @Override
    public Move execute(int id) {
        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }

        // if treasures are near, switch to treasure hunt mode
        if (GameState.getInstance().getClosestTreasure(id) != null) {
            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        // check if we are close enough to the tile - todo: add logic to check that the tile is actually reachable!
        Point myLocation = GameState.getInstance().getBotPosition(id);

        int explosionRadius = GameState.getInstance().getBombExplosionRadius();
        if (isOnSameLineAndCloseEnough(myLocation, destination, explosionRadius)) {
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


        calculatePathToAClosePoint(destination, explosionRadius);


        if (!path.isEmpty()) {
            return path.poll();
        }

        return Move.WAIT;
    }

}
