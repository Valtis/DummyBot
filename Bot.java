import java.util.EnumMap;
import java.util.List;

// Das Bot
public abstract class Bot {
    private final EnumMap<Moves, String> MOVES = new EnumMap<Moves, String>(Moves.class);

    public Bot() {
        MOVES.put(Moves.MOVE_UP, "MOVE U");
        MOVES.put(Moves.MOVE_DOWN, "MOVE D");
        MOVES.put(Moves.MOVE_LEFT, "MOVE L");
        MOVES.put(Moves.MOVE_RIGHT, "MOVE R");
        MOVES.put(Moves.WAIT, "WAIT");
        MOVES.put(Moves.BOMB, "BOMB");
    }

    public abstract String GetCommand(List<String> lines);

    protected String GetCommandString(Moves m) {
        return MOVES.get(m);
    }

}
