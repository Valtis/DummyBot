
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Point p = (Point)o;
        return p.x == this.x && p.y == this.y;
    }

    @Override
    public int hashCode() {
            return 51 * x + 51 * y;
    }
}
