import java.util.Queue;


public interface Pathfinder {
    public Queue<Move> calculatePath(Point start, Point destination);
}
