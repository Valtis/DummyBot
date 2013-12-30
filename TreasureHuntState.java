
import java.util.Queue;

public class TreasureHuntState extends BotState {

    @Override
    public Move execute(int id) {
        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }

        // check if we have a valid path to a treasure - if so, continue moving towards it
        if (path != null && !path.isEmpty() && GameState.getInstance().getTileType(destination) == TileType.TREASURE) {
            Point nextPoint = Move.getNextPoint(GameState.getInstance().getBotPosition(id), path.peek());
            if (!GameState.getInstance().isInsideBombExplosionRadius(nextPoint)) {
                return path.poll();
            }
            return Move.WAIT; // bomb explosion area ahead - wait for it to explode
        }


        destination = GameState.getInstance().getClosestTreasure(id);

        if (destination == null) {
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        path = pathfinder.calculatePath(GameState.getInstance().getGameField(), GameState.getInstance().getBotPosition(id), destination);

        // no path
        if (path.isEmpty()) {
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        return path.poll();
    }
}
