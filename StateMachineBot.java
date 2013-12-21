import java.util.List;

public class StateMachineBot extends Bot {
    private enum State {
        SEEK,
        ESCAPE
    }

    private State currentState;
    String currentCommand;


    public StateMachineBot(){
        currentState = State.SEEK;
    }

    @Override
    public String GetCommand(List<String> lines) {
        currentCommand = GetCommandString(Moves.WAIT);
        UpdateState();
        return currentCommand;
    }

    private void UpdateState() {
        switch (currentState) {
            case SEEK:
                Seek();
                break;
            case ESCAPE:
                Escape();
                break;
            default:
                throw new RuntimeException("Shouldn't happen...");
        }
    }

    private void Seek() {

    }

    private void Escape() {


    }
}
