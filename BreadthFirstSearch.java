import java.util.*;

public class BreadthFirstSearch implements Pathfinder {

    private class Node {
        final Point location;
        final Point comeFrom;

        public Node(Point location, Point comeFrom) {
            this.location = location;
            this.comeFrom = comeFrom;
        }

        public Move getMoveDirection() {
            if (comeFrom.x < location.x) {
                return Move.MOVE_RIGHT;
            } else if (comeFrom.x > location.x) {
                return Move.MOVE_LEFT;
            } else if (comeFrom.y > location.y) {
                return Move.MOVE_UP;
            } else {
                return Move.MOVE_DOWN;
            }
        }

        @Override
        public int hashCode() {
            return location.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass()) {
                return false;
            }

            return ((Node)o).location.equals(this.location);
        }

    }

    @Override
    public Queue<Move> calculatePath(Point start, Point destination) {
        Map<Point, Node> handledNodes = new HashMap<Point, Node>();
        DebugWriter.write("  Pathfinder: Calculating path from " + start + " to " + destination + "\n");

        if (!GameState.getInstance().isValid(destination) || !GameState.getInstance().isPassable(destination)) {
            DebugWriter.write("  Pathfinder: Destination tile either not valid or is not reachable\n");
            return new LinkedList<Move>();
        }


        Queue<Node> queue = new LinkedList<Node>();
        Node n = new Node(start, null);
        queue.add(n);

        while (!queue.isEmpty()) {
            n = queue.poll();
            if (n.location.equals(destination)) {
                DebugWriter.write("  Pathfinder: Constructing path\n");
                return ConstructPath(n, handledNodes);
            }

            HandleNeighbours(n, queue, handledNodes);

            handledNodes.put(n.location, n);


        }

        return new LinkedList<Move>();
    }

    private void HandleNeighbours(Node n, Queue<Node> queue, Map<Point, Node> handledNodes) {

        Point newLocation;

        newLocation = new Point(n.location.x-1, n.location.y);
        HandleNeighbourNode(newLocation, n.location, queue, handledNodes);

        newLocation = new Point(n.location.x+1, n.location.y);
        HandleNeighbourNode(newLocation, n.location, queue, handledNodes);

        newLocation = new Point(n.location.x, n.location.y-1);
        HandleNeighbourNode(newLocation, n.location, queue, handledNodes);

        newLocation = new Point(n.location.x, n.location.y+1);
        HandleNeighbourNode(newLocation, n.location, queue, handledNodes);

    }

    void HandleNeighbourNode(Point newLocation, Point oldLocation, Queue<Node> queue, Map<Point, Node> handledNodes) {

        if (!GameState.getInstance().isValid(newLocation) || !GameState.getInstance().isPassable(newLocation)) {
            return;
        }

        Node neighbour;
        neighbour = new Node(newLocation, oldLocation);

        if (queue.contains(neighbour) || handledNodes.containsKey(newLocation)) {
            return;
        }
        queue.add(neighbour);
    }


    Queue<Move> ConstructPath(Node n, Map<Point, Node> handledNodes) {
        LinkedList<Move> moves = new LinkedList<Move>();

        while (n.comeFrom != null) {
            moves.add(n.getMoveDirection());
            n = handledNodes.get(n.comeFrom);
        }

        Collections.reverse(moves);
        return moves;
    }
}
