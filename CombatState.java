public class CombatState extends BotState {
    public final static int COMBAT_DISTANCE = 4;

    @Override
    public Move execute(int id) {
        DebugWriter.write("\nEntering CombatState\n");

        // if this is null, no walls are remaining. If no treasures are on the ground either, remain in combat state forever


        // check for bombs
        if (GameState.getInstance().isInsideBombExplosionRadius(id)) {
            nextState = new AvoidExplosionState();
            DebugWriter.write("Threatened by a bomb - switching to AvoidExplosionState()\n");
            return Move.REDO;
        }

        Point closestWall = GameState.getInstance().getClosestWall(id);
        Point closestTreasure = GameState.getInstance().getClosestTreasure(id);

        // if closest enemy is further away than combat distance AND there are walls or treasures, switch to treasure hunting state
        if (GameState.getInstance().getClosestEnemyDistance(id) > COMBAT_DISTANCE && (closestTreasure != null || closestWall != null)) {
            nextState = new TreasureHuntState();

            DebugWriter.write("No bots near and treasures or walls exist - switching to TreasureHuntState()\n");
            return Move.REDO;
        }

        Point closestEnemy = GameState.getInstance().getClosestEnemy(id);
        Point myPosition = GameState.getInstance().getBotPosition(id);

        // check if closest enemy is inside bomb explosion radius - if so, drop a bomb and switch to AvoidExplosionState
        int explosionRadius = GameState.getInstance().getBombExplosionRadius();
        if (isOnSameLineAndCloseEnough(closestEnemy, myPosition, explosionRadius)) {
            DebugWriter.write("Closest enemy is within bomb explosion radius - dropping bomb and switching to AvoidExplosionState()\n");
            nextState = new AvoidExplosionState();
            return Move.BOMB;
        }


        // check if we have a valid path, and follow it if we do. TODO: extract this code to a method, multiple states use similar code
        if (path != null && !path.isEmpty()) {
            return MoveOrWaitForBomb(id);
        }


        // no valid path - calculate a path to position near enemy position
        calculatePathToAClosePoint(myPosition, closestEnemy, explosionRadius);

        DebugWriter.write("No valid path - calculating new one\n");

        if (path != null &&  !path.isEmpty()) {
            return MoveOrWaitForBomb(id);
        }

        DebugWriter.write("No valid path exists - waiting\n");

        return Move.WAIT;
    }
}
