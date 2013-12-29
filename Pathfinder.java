import java.util.Queue;


public interface Pathfinder {
    public Queue<Move> calculatePath(char[][] field, Point start, Point destination);
}
