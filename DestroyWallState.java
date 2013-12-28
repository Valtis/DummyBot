import java.util.Queue;

public class DestroyWallState extends BotState {
    private Point wallLocation;
    Queue<Move> path;
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

        if ((myLocation.x == wallLocation.x && Math.abs(myLocation.y - wallLocation.y) < GameState.getInstance().getBombExplosionRadius()) ||
                myLocation.y == wallLocation.y && Math.abs(myLocation.x - wallLocation.x) < GameState.getInstance().getBombExplosionRadius()) {
            return Move.BOMB;
        }


        // check if a path already exists and if the end point is still valid





        // calculate new path

        Point wall = GameState.getInstance().getClosestWall(id);



        // check position x-wise if we can drop a bomb there


        return Move.WAIT;
    }
}
