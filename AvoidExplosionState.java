import java.util.Queue;


public class AvoidExplosionState extends BotState {
    private final int SEARCH_AREA_SIZE = 3;

    @Override
    public Move execute(int id) {

        // danger over, switch back to treasure hunting
        if (!GameState.getInstance().isInsideBombExplosionRadius(id)) {
            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        // check if we have calculated escape path already and if it's valid
        if (path != null && !path.isEmpty() && GameState.getInstance().isInsideBombExplosionRadius(destination)) {
            return path.poll();
        }

        Point myLocation = GameState.getInstance().getBotPosition(id);
        // find a safe point, calculate path to it. if we fail to calculate path (safe tiles not reachable), give up and wait
         // check tiles around bot [x - 3, y - 3] -> [x + 3, y + 3]

        calculatePathToAClosePoint(myLocation, SEARCH_AREA_SIZE);

        if (path.isEmpty()) {
            return Move.WAIT; // we're gonna dieeee
        }

        return path.poll();
    }
}
