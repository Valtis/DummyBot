import java.util.Queue;


public class AvoidExplosionState extends BotState {
    private final int SMALL_SEARCH_AREA_SIZE = 3;
    private final int SEARCH_AREA_SIZE = 6;

    @Override
    public Move execute(int id) {
        DebugWriter.write("\nEntering AvoidExplosionState\n");

        // danger over, switch back to treasure hunting
        if (!GameState.getInstance().isInsideBombExplosionRadius(id)) {
            DebugWriter.write("No longer inside bomb explosion radius - switching back to TreasureHuntState()\n");

            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        // check if we have calculated escape path already and if it's valid
        if (path != null && !path.isEmpty() && !GameState.getInstance().isInsideBombExplosionRadius(destination)) {
            DebugWriter.write("Valid path exists - moving to next tile\n");

            return path.poll();
        }


        // find a safe point, calculate path to it. if we fail to calculate path (safe tiles not reachable), give up and wait
        DebugWriter.write("No valid path - calculating escape path\n");

        calculateEscapePath(id);

        // failed to calculate escape path - just move anywhere as it's better than sitting still waiting for death
        if (path == null || path.isEmpty()) {
            return moveToRandomDirection(id);
        }
        DebugWriter.write("Path calculated successfully - escaping\n");
        DebugWriter.write("Calculated path (backwards):\n");

        for (Move m : path) {
            DebugWriter.write("  " + Move.getMoveString(m) + "\n");
        }
        DebugWriter.write("\n");


        DebugWriter.write("Returning " + Move.getMoveString(path.peek()));
        return path.poll(); // next step may still be withing bomb explosion raidus. Thus move and do not use MoveOrWaitForBomb(id);
    }

    private void calculateEscapePath(int id) {
        Point myLocation = GameState.getInstance().getBotPosition(id);



        Point p = new Point(0, 0);


        p.x = myLocation.x + 1;
        p.y = myLocation.y + 1;
        if (calculatePathToPoint(myLocation, p)) {
            DebugWriter.write("Pathing to lower right to escape\n");
            return;
        }

        p.x = myLocation.x + 1;
        p.y = myLocation.y - 1;
        if (calculatePathToPoint(myLocation, p)) {

            DebugWriter.write("Pathing to upper right to escape\n");
            return;
        }

        p.x = myLocation.x - 1;
        p.y = myLocation.y + 1;
        if (calculatePathToPoint(myLocation, p)) {

            DebugWriter.write("Pathing to lower left to escape\n");
            return;
        }

        p.x = myLocation.x - 1;
        p.y = myLocation.y - 1;
        if (calculatePathToPoint(myLocation, p)) {
            DebugWriter.write("Pathing to upper left to escape\n");
            return;
        }

        // diagonal points are not acceptable - do a sweep using small search area and see if a path is found
        calculatePathToAClosePoint(myLocation, myLocation, SMALL_SEARCH_AREA_SIZE);

        if (path != null && !path.isEmpty()) {

            DebugWriter.write("Using path by small area search\n");
            return;
        }

        // still failing to calculate escape path - this time increase search area
        calculatePathToAClosePoint(myLocation, myLocation, SEARCH_AREA_SIZE);
    }

    private boolean calculatePathToPoint(Point start, Point end) {
        if (!GameState.getInstance().isValid(end) || !GameState.getInstance().isPassable(end) || GameState.getInstance().isInsideBombExplosionRadius(end)) {
            DebugWriter.write("  DiagonalEscape from: " + start + " to " + end + "\n");
            DebugWriter.write("  DiagonalEscape: Validity: " + GameState.getInstance().isValid(end) + "\n");
            if (GameState.getInstance().isValid(end)) {
                DebugWriter.write("  DiagonalEscape: Passable: " + GameState.getInstance().isPassable(end) + "\n");
                DebugWriter.write("  DiagonalEscape: Safe: " + GameState.getInstance().isInsideBombExplosionRadius(end) + "\n");
            }
            return false;
        }
        path = pathfinder.calculatePath(start, end);
        DebugWriter.write("  DiagonalEscape: NULL?" + (path == null) + "\n");
        if (path != null) {
            DebugWriter.write("  DiagonalEscape: Not empty: " + path.isEmpty() + "\n");
        }
        DebugWriter.write("  DiagonalEscape return value: " + (path != null && !path.isEmpty()) + "\n");

        return path != null && !path.isEmpty();
    }


    private Move moveToRandomDirection(int id) {
        Point myLocation = GameState.getInstance().getBotPosition(id);
        Point p;

        DebugWriter.write("Regular search failed - moving to random direction\n");
        p = Move.getNextPoint(myLocation, Move.MOVE_DOWN);
        if (passable(p, id)) {
            DebugWriter.write("Moving down\n");
            return Move.MOVE_DOWN;
        }

        p = Move.getNextPoint(myLocation, Move.MOVE_LEFT);
        if (passable(p, id)) {

            DebugWriter.write("Moving left\n");
            return Move.MOVE_LEFT;
        }

        p = Move.getNextPoint(myLocation, Move.MOVE_RIGHT);
        if (passable(p, id)) {

            DebugWriter.write("Moving right\n");
            return Move.MOVE_RIGHT;
        }

        p = Move.getNextPoint(myLocation, Move.MOVE_UP);
        if (passable(p, id)) {

            DebugWriter.write("Moving up\n");
            return Move.MOVE_UP;
        }


        DebugWriter.write("Failed - waiting\n");
        return Move.WAIT; // looks like we have exhausted any possibilities.
    }
    private boolean passable(Point p, int id) {
        // is valid, is passable, enemy or bomb isn't blocking it
        return GameState.getInstance().isValid(p) && GameState.getInstance().isPassable(p)
                && p.equals(GameState.getInstance().getClosestEnemy(id)) == false;
    }

}
