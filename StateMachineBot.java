import java.util.List;


/* basic idea: if other bot is far away, blow up tiles and search treasures
   when searching treasures:
     if no treasures are present -> move to a tile where a bomb can destroy walls
     drop bomb
     move away from explosion area
     wait until bomb explodes
     see if treasure is present and close enough; if so, pick it; otherwise begin from the start
   if enemy is close enough: 
     drop bomb if bot sees it's within splash radius
     otherwise, try to get some distance
     if the other bot seems to follow (distance is too low for multiple turns), go to a combat mode (might be a hunter/killer-bot)
     if other bot is far enough or dead, switch to treasure hunt mode

    in general, try to prefer the middle of the game level to maximise space where bot can move


*/ 
public class StateMachineBot extends Bot {

    private BotState currentState;
    private Move currentMove;

    public StateMachineBot(int id){
        super(id);
        currentState = new TreasureHuntState();
        currentMove = Move.WAIT;
    }

    @Override
    public String getCommand(List<String> lines) {
        updateState();
        return Move.getMoveString(currentMove);
    }

    private void updateState() {
        int counter = 0;

        do {
            ++counter;
            currentMove = currentState.execute(MY_ID);
            currentState = currentState.getNextState();
        } while (counter <= 5 && currentMove == Move.REDO); // somewhat terrible way handle state transitions - if state is changed, bot needs to re-evaluate its position

    }
}
