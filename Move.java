
public enum Move {
    WAIT,
    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,
    BOMB,
    REDO
    ;

    public static Point getNextPoint(Point oldPoint, Move move) {
        int x = 0;
        int y = 0;

        switch (move) {
            case MOVE_UP:
                y = -1;
                break;
            case MOVE_DOWN:
                y = 1;
                break;
            case MOVE_LEFT:
                x = -1;
                break;
            case MOVE_RIGHT:
                x = 1;
                break;
            default:
                break;
        }

        return new Point(oldPoint.x + x, oldPoint.y + y);
    }


    public static String getMoveString(Move move) {
        switch (move) {
            case MOVE_UP:
                return "MOVE U";
            case MOVE_DOWN:
                return "MOVE D";
            case MOVE_RIGHT:
                return "MOVE R";
            case MOVE_LEFT:
                return "MOVE L";
            case WAIT:
                return "WAIT";
            case BOMB:
                return "BOMB";
            case REDO:
                return "WAIT";
            default:
                return "WAIT";
        }
    }
}
