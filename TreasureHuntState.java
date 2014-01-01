
import java.util.Queue;

public class TreasureHuntState extends BotState {

    @Override
    public Move execute(int id) {
        DebugWriter.write("\nEntering TreasureHuntState\n");
        if (CheckForBombsAndEnemies(id)) {
            return Move.REDO;
        }


        // check if we have a valid path to a treasure - if so, continue moving towards it
        if (path != null && !path.isEmpty() && GameState.getInstance().getTileType(destination) == TileType.TREASURE) {
            return MoveOrWaitForBomb(id);
        }

        DebugWriter.write("Calculating path to closest treasure\n");
        destination = GameState.getInstance().getClosestTreasure(id);

        if (destination == null) {
            DebugWriter.write("No treasures - switching to DestroyWallState\n");
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        path = pathfinder.calculatePath(GameState.getInstance().getBotPosition(id), destination);

        // no path
        if (path == null || path.isEmpty()) {
            DebugWriter.write("No path to treasures - switching to DestroyWallState\n");
            nextState = new DestroyWallState();
            return Move.REDO;
        }

        return MoveOrWaitForBomb(id);
    }
}
