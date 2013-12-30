import java.util.*;

public class AStar implements Pathfinder {
    
    private class Node implements Comparable<Node> {
        private final Point POINT;
        private Point comeFrom;
        private int travelledDistance;
        private int heuristicDistance;

        public Node(int x, int y, int travelledDistance, int heuristicDistance) {
            POINT = new Point(x, y);
            comeFrom = null;
            this.travelledDistance = travelledDistance;
            this.heuristicDistance = heuristicDistance;
        }

        public void setComeFrom(Point p) { comeFrom = p; }
        public Point getComeFrom() { return comeFrom; }
        // return the move you need to perform to get from comeFrom to this node
        public Move getMoveDirection() {
            if (comeFrom.x < POINT.x) {
                return Move.MOVE_RIGHT;
            } else if (comeFrom.x > POINT.x) {
                return Move.MOVE_LEFT;
            } else if (comeFrom.y > POINT.y) {
                return Move.MOVE_UP;
            } else {
             return Move.MOVE_DOWN;
            }

        }

        public Node(Point p, int travelledDistance, int heuristicDistance) {
            this(p.x, p.y, travelledDistance, heuristicDistance);
        }


        public void setTravelledDistance(int d) {
            travelledDistance = d;
        }

        public Point getPoint() {
            return POINT;
        }

        public int getTravelledDistance() {
            return travelledDistance;
        }

        public int getDistanceEstimate() {
            return travelledDistance + heuristicDistance;
        }
        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass()) {
                return false;
            }
            return ((Node)o).POINT.equals(this.POINT);
        }

        @Override
        public int compareTo(Node o) {
            return getDistanceEstimate() - o.getDistanceEstimate();
        }
    }

    public Queue<Move> calculatePath(char[][] field, Point start, Point destination) {

        PriorityQueue<Node> queue = new PriorityQueue<Node>();

        HashSet<Point> closedSet = new HashSet<Point>();
        HashMap<Point, Node> nodeLookup = new HashMap<Point, Node>();


        queue.add(new Node(start, 0, calculateHeuristicDistance(start, destination)));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.getPoint().equals(destination)) {
                return constructPath(node, nodeLookup);
            }
            closedSet.add(node.getPoint());
            HandleSurroundingTiles(field, node, closedSet, nodeLookup, queue, destination);

        }


        // no path found - return empty path to signal this
        return new LinkedList<Move>();
    }
    // could be prettier
    private void HandleSurroundingTiles(char[][] field, Node node, HashSet<Point> closedSet, HashMap<Point, Node> lookup, PriorityQueue<Node> queue, Point destination) {
        Point p;

        p = new Point(node.getPoint().x+1, node.getPoint().y);
        if (GameState.getInstance().isValid(p) && TileType.passable(field[p.x][p.y])) {
            HandleNeighbour(node, closedSet, lookup, queue, destination, p);
        }

        p = new Point(node.getPoint().x-1, node.getPoint().y);
        if (GameState.getInstance().isValid(p) && TileType.passable(field[p.x][p.y])) {
            HandleNeighbour(node, closedSet, lookup, queue, destination, p);
        }

        p = new Point(node.getPoint().x, node.getPoint().y-1);
        if (GameState.getInstance().isValid(p) && TileType.passable(field[p.x][p.y])) {
            HandleNeighbour(node, closedSet, lookup, queue, destination, p);
        }

        p = new Point(node.getPoint().x, node.getPoint().y+1);
        if (GameState.getInstance().isValid(p) && TileType.passable(field[p.x][p.y])) {
            HandleNeighbour(node, closedSet, lookup, queue, destination, p);
        }

    }

    private void HandleNeighbour(Node node, HashSet<Point> closedSet, HashMap<Point, Node> lookup, PriorityQueue<Node> queue, Point destination, Point p) {
        // this tile is in closed set - do nothing
        if (closedSet.contains(p)) {
            return;
        }

        // check if tile is already in queue; if not, add it
        if (!lookup.containsKey(p)) {
            Node n = new Node(p, node.getTravelledDistance() + 1, calculateHeuristicDistance(p, destination));
            n.setComeFrom(node.getPoint());
            queue.add(n);
            lookup.put(p, n);
        }
        else {
            Node n = lookup.get(p);
            // check if path through this tile would be faster - if so, update tile in the queue
            if (n.getTravelledDistance() > node.getTravelledDistance() + 1) {
                queue.remove(n);
                n.setTravelledDistance(node.getTravelledDistance() + 1);
                node.setComeFrom(node.getPoint());
                queue.add(n);
            }
        }

    }

    Queue<Move> constructPath(Node destination, HashMap<Point, Node> lookup) {
        Queue moves = new LinkedList<Move>();

        Node n = destination;

        while (n.getComeFrom() != null) {
            moves.add(n.getMoveDirection());
            assert(lookup.containsKey(n.getComeFrom()));
            n = lookup.get(n.getComeFrom());
        }
        return moves;
    }

    private  int calculateHeuristicDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }


}
