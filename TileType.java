// blatantly stolen from the server code

public enum TileType {
    TREASURE,
    HARDBLOCK,
    SOFTBLOCK,
    FLOOR;

    public boolean passable() {
        return this == TREASURE ||
                this == FLOOR;
    }

    public static boolean passable(char floorTile) {
        return floorTile == '$' || floorTile == '.';
    }

    @Override
    public String toString() {
        switch(this) {
            case TREASURE:
                return "$";
            case HARDBLOCK:
                return "#";
            case SOFTBLOCK:
                return "?";
            case FLOOR:
            default:
                return ".";
        }
    }

    public static TileType getType(char floorTile) {
        switch (floorTile) {
            case '$':
                return TREASURE;
            case '#':
                return HARDBLOCK;
            case '?':
                return SOFTBLOCK;
            case '.':
                return FLOOR;
            default:
                throw new RuntimeException("Invalid floor tile : " + floorTile);
        }

    }
}