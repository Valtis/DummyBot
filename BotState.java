import java.util.Queue;

public abstract class BotState {

    protected Point destination;
    protected Queue<Move> path;
    protected BotState nextState;
    public abstract Move execute(int id);
    public BotState getNextState() {
        return nextState;
    }

    protected boolean CheckForBombsAndEnemies(int id) {
        // check if we have to avoid bombs or enemies
        if (GameState.getInstance().isInsideBombExplosionRadius(id)) {
            nextState = new AvoidExplosionState();
            return true;
        } else if (GameState.getInstance().closestEnemyDistance(id) < CombatState.COMBAT_DISTANCE) {
            nextState = new CombatState();
            return true;
        }
        return false;
    }


    protected boolean isOnSameLineAndCloseEnough(Point pos1, Point pos2, int distance) {
        return (pos1.x == pos2.x && Math.abs(pos1.y - pos2.y) < distance) ||
                (pos1.y == pos2.y && Math.abs(pos1.x - pos2.x) < distance);
    }


    protected void calculatePathToAClosePoint(Point targetLocation, int searchAreaSize) {
        if (path != null) {
            path.clear();
        }

        for (destination.x = targetLocation.x - searchAreaSize; destination.x <= targetLocation.x + searchAreaSize; ++destination.x ) {

            for (destination.y = targetLocation.y - searchAreaSize; destination.y <= targetLocation.y + searchAreaSize; ++destination.y) {
                if (!GameState.getInstance().isValid(destination) || !GameState.getInstance().isPassable(destination) || GameState.getInstance().isInsideBombExplosionRadius(destination)) {
                    continue;
                }

                path = AStar.calculatePath(GameState.getInstance().getGameField(), targetLocation, destination);
            }

            if (!path.isEmpty()) {
                break;
            }
        }
    }
}
