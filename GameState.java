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


    public void initializeGameData(int botID, List<String> lines) {
        gameData = new GameData(botID, lines);
    }

    public boolean isValid(Point point) {
        return !(point == null || point.x < 0 || point.x >= gameData.WIDTH
                || point.y < 0 || point.y >= gameData.HEIGHT);
    }

    public boolean isPassable(Point point) {
        return TileType.passable(gameData.gameField[point.x][point.y]);
    }

    public int getBombExplosionRadius() {
        return gameData.BOMB_FORCE;
    }

    public boolean isInsideBombExplosionRadius(Point point) {
        return false;
    }

    public boolean isInsideBombExplosionRadius(int id) {
        return false;
    }

    // returns 99999 if no enemy present
    public int closestEnemyDistance(int id) {
        return 99999;
    }

    public Point getClosestWall(int id) {
        return null;
    }

    public Point getClosetEnemyPosition(int id) {
        return null;
    }


    // returns null if no treasure present
    public Point getClosestTreasure(int id) {
        return null;
    }

    public Point getBotPosition(int id) {
        return null;
    }

    public char [][] getGameField() {
        return gameData.gameField;
    }

    public TileType getTileType(Point point) {
        return TileType.getType(gameData.gameField[point.x][point.y]);
    }

    public static void ParseGameState() {

        
    }



}
