    /*
        BEGIN MSG
        map width: 9
        map height: 9
        bombersCount 2
        POINTS_PER_TREASURE 3
        POINTS_LOST_FOR_DYING 3
        DYING_COOL_DOWN 5
        BOMB_TIMER_DICE 1
        BOMB_TIMER_SIDES 3
        BOMB_FORCE 3
        TURNS 30
        TREASURE_CHANGE 1.0
        INITIAL_COUNT_OF_BOMBS 3
        MAP:
        0........
        .#....#..
        .........
        .#.......
        .....#...
        .#.......
        .........
        .#....#..
        1........
        END MSG
      */


    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class GameData {

        public final int BOT_ID;
        public final int WIDTH;
        public final int HEIGHT;
        public final int BOMBER_COUNT;
        public final int POINTS_PER_TREASURE;
        public final int POINTS_LOST_FOR_DYING;
        public final int DYING_COOL_DOWN;
        public final int BOMB_TIMER_DICE;
        public final int BOMB_TIMER_SIDES;
        public final int BOMB_FORCE;
        public final int TURNS;
        public final int TREASURE_CHANGE;
        public final int INITIAL_COUNT_OF_BOMBS;

        public char [][] gameField;

        private Set<Point> bombLocations;


        public GameData(int botID, List<String> lines) {
            bombLocations = new HashSet<Point>();
            BOT_ID = botID;

            int pos = 1;
            WIDTH = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            HEIGHT = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMBER_COUNT = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            POINTS_PER_TREASURE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            POINTS_LOST_FOR_DYING = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            DYING_COOL_DOWN = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_TIMER_DICE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_TIMER_SIDES = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_FORCE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            TURNS = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            TREASURE_CHANGE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            INITIAL_COUNT_OF_BOMBS = Integer.parseInt(lines.get(pos++).split(" ")[1]);



            gameField = new char[WIDTH][];

            for (int i = 0; i < WIDTH; ++i) {
                ++pos;
                gameField[i] = new char[HEIGHT];
                for (int j = 0; j < HEIGHT; ++i) {
                    gameField[i][j] = lines.get(i).charAt(j);
                }
            }

        }

        public void updateLevel(List<String> lines) {
            // update
            int linePos = 1;
            for (int i = 0; i < WIDTH; ++i, ++linePos) {
                for (int j = 0; j < HEIGHT; ++i) {
                    gameField[i][j] = lines.get(linePos).charAt(j);
                }
            }

            bombLocations.clear();
            // update bomb locations
            for (; linePos < lines.size(); ++linePos) {
                String [] tokens = lines.get(linePos).split(" ");
                if (tokens.length == 0) {
                    continue;
                }
                // example: bomb at 0,10 (owned by 1)
                if (tokens[0].equals("bomb")) {
                    String [] posTokens = tokens[2].split(",");
                    if (posTokens.length != 2) {
                        throw new RuntimeException("Check bomb parsing code, something odd going on");
                    }

                    bombLocations.add(new Point(Integer.parseInt(posTokens[0]), Integer.parseInt(posTokens[1])));

                }
            }

        }

        public boolean tileHasBomb(int x, int y) {
            return bombLocations.contains(new Point(x, y));
        }

        public Point getBotPosition(int id) {
            if (id < 0 || id > 9) {
                throw new RuntimeException("Invalid bot id. Id must be a number between 0 - 9");
            }
            String temp = "" + id;
            for (int x = 0; x < WIDTH; ++x) {
                for (int y = 0; y < HEIGHT; ++y) {
                    if (gameField[x][y] == temp.charAt(0)) {
                        return new Point(x, y);
                    }
                }
            }
            return null;
        }

        public Point getClosestOfType(Point point, char type) {
            int curX = 0;
            int curY = 0;
            double distance = 99999.0;

            for (int x = 0; x < WIDTH; ++x) {
                for (int y = 0; y < HEIGHT; ++y) {
                    if (gameField[x][y] == type) {
                        double temp = Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2);
                        if (temp < distance) {
                            distance = temp;
                            curX = x;
                            curY = y;
                        }
                    }
                }

            }

            if (Math.abs(distance - 99999.0) < 0.0001) {
                return null;
            }

            return new Point(curX, curY);
        }

}
