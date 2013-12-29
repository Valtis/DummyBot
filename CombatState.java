public class CombatState extends BotState {
    public final static int COMBAT_DISTANCE = 4;

    @Override
    public Move execute(int id) {


        // if this is null, no walls are remaining. If no treasures are on the ground either, remain in combat state forever


        // check for bombs
        if (GameState.getInstance().isInsideBombExplosionRadius(id)) {
            nextState = new AvoidExplosionState();
            return Move.REDO;
        }

        Point closestWall = GameState.getInstance().getClosestWall(id);
        Point closestTreasure = GameState.getInstance().getClosestTreasure(id);

        // if closest enemy is further away than combat distance AND there are walls or treasures, switch to treasure hunting state
        if (GameState.getInstance().getClosestEnemyDistance(id) > COMBAT_DISTANCE && (closestTreasure != null || closestWall != null)) {
            nextState = new TreasureHuntState();
            return Move.REDO;
        }

        Point closestEnemy = GameState.getInstance().getClosestEnemy(id);
        Point myPosition = GameState.getInstance().getBotPosition(id);

        // check if closest enemy is inside bomb explosion radius - if so, drop a bomb and switch to AvoidExplosionState
        int explosionRadius = GameState.getInstance().getBombExplosionRadius();
        if (isOnSameLineAndCloseEnough(closestEnemy, myPosition, explosionRadius)) {
            nextState = new AvoidExplosionState();
            return Move.BOMB;
        }


        // check if we have a valid path, and follow it if we do. TODO: extract this code to a method, multiple states use similar code
        if (path != null && !path.isEmpty()) {
            Point nextPoint = Move.getNextPoint(GameState.getInstance().getBotPosition(id), path.peek());
            if (!GameState.getInstance().isInsideBombExplosionRadius(nextPoint)) {
                return path.poll();
            }
            return Move.WAIT; // bomb explosion area ahead - wait for it to explode
        }


        // no valid path - calculate a path to position near enemy position
        calculatePathToAClosePoint(closestEnemy, explosionRadius);

        if (!path.isEmpty()) {
            return path.poll();
        }

        return Move.WAIT;
    }
}
