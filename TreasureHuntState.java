
import java.util.Queue;

public class TreasureHuntState extends BotState {
    private Queue<Move> path = null;
    private Point destinationTile = null;
    @Override
    public Move execute(int id) {
        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }

        nextState = this;

        // check if we have a valid path to a treasure - if so, continue moving towards it
        if (path != null && !path.isEmpty() && GameState.getInstance().getTileType(destinationTile) == TileType.TREASURE) {
            Point nextPoint = Move.getNextPoint(GameState.getInstance().getBotPosition(id), path.peek());
            if (!GameState.getInstance().isInsideBombExplosionRadius(nextPoint)) {
                return path.poll();
            }
            return Move.WAIT; // bomb explosion area ahead - wait for it to explode
        }


        Point treasure = GameState.getInstance().getClosestTreasure(id);

        if (treasure == null) {
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        path = AStar.calculatePath(GameState.getInstance().getGameField(), GameState.getInstance().getBotPosition(id), treasure);

        // no path
        if (path.isEmpty()) {
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        return path.poll();
    }
}
