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
            DebugWriter.write("Threatened by a bomb - switching to AvoidExplosionState()\n");
            return true;
        }

        // check if bot is both reachable and close
        Queue<Move> temp = pathfinder.calculatePath(GameState.getInstance().getBotPosition(id), GameState.getInstance().getClosestEnemy(id));
        if (!temp.isEmpty() && temp.size() < CombatState.COMBAT_DISTANCE) {
            nextState = new CombatState();
            DebugWriter.write("Threatened by a bot - switching to CombatState()\n");
            return true;
        }
        return false;
    }


    protected boolean isOnSameLineAndCloseEnough(Point pos1, Point pos2, int distance) {
        return (pos1.x == pos2.x && Math.abs(pos1.y - pos2.y) < distance) ||
                (pos1.y == pos2.y && Math.abs(pos1.x - pos2.x) < distance);
    }


    protected void calculatePathToAClosePoint(Point startLocation, Point targetLocation, int searchAreaSize) {
        if (path != null) {
            path.clear();
        }

        Point temp = new Point(0, 0);

        for (temp.x = targetLocation.x - searchAreaSize; temp.x <= targetLocation.x + searchAreaSize; ++temp.x ) {

            for (temp.y = targetLocation.y - searchAreaSize ; temp.y <= targetLocation.y + searchAreaSize; ++temp.y) {
                if (!GameState.getInstance().isValid(temp) || !GameState.getInstance().isPassable(temp) || GameState.getInstance().isInsideBombExplosionRadius(temp)) {
                    continue;
                }
                path = pathfinder.calculatePath(startLocation, temp);
                if (!path.isEmpty()) { // just pick first valid path
                    destination = temp;
                    break;
                }
            }

            if (path != null && !path.isEmpty()) {
                break;
            }
        }
    }

    protected Move  MoveOrWaitForBomb(int id) {
        Point nextPoint = Move.getNextPoint(GameState.getInstance().getBotPosition(id), path.peek());
        if (!GameState.getInstance().isInsideBombExplosionRadius(nextPoint)) {
            DebugWriter.write("Moving using old path to " + nextPoint + "\n");
            return path.poll();
        }
        DebugWriter.write("Bomb blocking next movement - waiting\n");
        return Move.WAIT; // bomb explosion area ahead - wait for it to explode
    }
}
