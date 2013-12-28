
public abstract class BotState {
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
}
