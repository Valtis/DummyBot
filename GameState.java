import java.util.ArrayList;
import java.util.List;

// todo: refactor this
public class GameState {

    private GameData gameData;
    private static GameState instance;
    private GameState() {
    }


    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void debugPrint() {
        DebugWriter.write("****DEBUG PRINT GAMEWORLD ****\n");
        for (int y = 0; y < gameData.HEIGHT; ++y) {
            for (int x = 0; x < gameData.WIDTH; ++x) {
                DebugWriter.write(""+gameData.gameField[y][x]);
            }
            DebugWriter.write("\n");
        }
        DebugWriter.write("****DEBUG END****\n");

    }

    public int manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }


    public void initializeGameData(int botID, List<String> lines) {
        gameData = new GameData(botID, lines);
    }

    public void updateGameData(List<String> lines) {
        gameData.updateLevel(lines);
    }

    public boolean isValid(Point point) {
        return !(point == null || point.x < 0 || point.x >= gameData.WIDTH
                || point.y < 0 || point.y >= gameData.HEIGHT);
    }

    public boolean isPassable(Point point) {
        return TileType.passable(gameData.gameField[point.y][point.x]);
    }

    public int getBombExplosionRadius() {
        return gameData.BOMB_FORCE;
    }



    public boolean isInsideBombExplosionRadius(Point point) {
        if (point == null || !isValid(point)) {
            return false;
        }
        // check x-axis to right
        for (int x = point.x ; x <= point.x + gameData.BOMB_FORCE+2; ++x) {
            if (x >= gameData.WIDTH) {
                break;
            }

            if (TileType.stopsExplosion(gameData.gameField[point.y][x])) {
                break;
            }
            if (gameData.tileHasBomb(x, point.y)) {
                return true;
            }
        }

        // check x-axis to left
        for (int x = point.x - 1; x >= point.x - gameData.BOMB_FORCE-1; --x) {
            if (x < 0) {
                break;
            }

            if (TileType.stopsExplosion(gameData.gameField[point.y][x])) {
                break;
            }
            if (gameData.tileHasBomb(x, point.y)) {
                return true;
            }
        }

        // check y-axis downwards
        for (int y = point.y + 1; y <= point.y + gameData.BOMB_FORCE+1; ++y) {
            if (y >= gameData.HEIGHT) {
                break;
            }

            if (TileType.stopsExplosion(gameData.gameField[y][point.x])) {
                break;
            }

            if (gameData.tileHasBomb(point.x, y)) {
                return true;
            }
        }

        // check y-axis upwards
        for (int y = point.y - 1; y >= point.y- gameData.BOMB_FORCE-1; --y) {
            if (y < 0) {
                break;
            }

            if (TileType.stopsExplosion(gameData.gameField[y][point.x])) {
                break;
            }

            if (gameData.tileHasBomb(point.x, y)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInsideBombExplosionRadius(int id) {
        return isInsideBombExplosionRadius(getBotPosition(id));
    }

    // returns 99999 if no enemy present
    public int getClosestEnemyDistance(int id) {
        Point myPosition = getBotPosition(id);
        Point p = getClosestEnemy(id);
        if (p == null) {
            return 99999;
        }

        return manhattanDistance(myPosition, p);
    }

    // returns 99999 if no bomb present
    public int getClosestBombDistance(int id) {
        List<Point> bombLocations= gameData.getBombPositions();
        int distance = 99999;
        Point myLocation = getBotPosition(id);
        for (Point p : bombLocations) {
            int temp = manhattanDistance(p, myLocation);
            if (temp < distance) {
                distance = temp;
            }
        }

        return distance;
    }


    public Point getClosestEnemy(int id) {
        Point myPosition = getBotPosition(id);
        Point enemyPoint = null;
        int distance = 99999;

        for (int i = 0; i < gameData.BOMBER_COUNT; ++i) {
            if (i == id) {
                continue;
            }

            Point p = getBotPosition(i);
            if (p != null) {
                int temp = manhattanDistance(p, myPosition);

                if (temp < distance) {
                    distance = temp;
                    enemyPoint = p;
                }
            }
        }
        return enemyPoint;
    }

    public Point getClosestWall(int id) {
       return gameData.getClosestOfType(getBotPosition(id), TileType.getCharRepresentation(TileType.SOFTBLOCK));
    }

    // returns null if no treasure present
    public Point getClosestTreasure(int id) {
        return gameData.getClosestOfType(getBotPosition(id), TileType.getCharRepresentation(TileType.TREASURE));
    }

    public Point getBotPosition(int id) {
        return gameData.getBotPosition(id);
    }

    public char [][] getGameField() {
        return gameData.gameField;
    }

    public TileType getTileType(Point point) {
        return TileType.getType(gameData.gameField[point.y][point.x]);
    }
}
