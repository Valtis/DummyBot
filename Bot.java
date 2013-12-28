import java.util.EnumMap;
import java.util.List;

// Das Bot
public abstract class Bot {
    protected final int MY_ID;

    public Bot(int id) {
        MY_ID = id;

    }

    public abstract String getCommand(List<String> lines);
}
