import java.util.Queue;

public abstract class BotState {

    protected Pathfinder pathfinder;
    protected Point destination;
    protected Queue<Move> path;
    protected BotState nextState;
    public abstract Move execute(int id);

    public BotState getNextState() {
        return nextState;
    }

    public BotState()
    {
        pathfinder = new BreadthFirstSearch();
        nextState = this;
    }

    protected boolean CheckForBombsAndEnemies(int id) {
        // check if we have to avoid bombs or enemies
        if (GameState.getInstance().isInsideBombExplosionRadius(id)) {
            nextState = new AvoidExplosionState();
            return true;
        } else if (GameState.getInstance().getClosestEnemyDistance(id) < CombatState.COMBAT_DISTANCE) {
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

        Point temp = new Point(Math.max(0, targetLocation.x - searchAreaSize), Math.max(0, targetLocation.y - searchAreaSize));

        for (; temp.x <= targetLocation.x + searchAreaSize; ++temp.x ) {

            for (; temp.y <= targetLocation.y + searchAreaSize; ++temp.y) {
                if (!GameState.getInstance().isValid(temp) || !GameState.getInstance().isPassable(temp) || GameState.getInstance().isInsideBombExplosionRadius(temp)) {
                    continue;
                }

                path = pathfinder.calculatePath(GameState.getInstance().getGameField(), targetLocation, temp);
                if (!path.isEmpty()) { // just pick first valid path
                    destination = temp;
                    break;
                }
            }
        }
    }
}
