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


    import java.util.*;

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
        public final double TREASURE_CHANGE;
        public final int INITIAL_COUNT_OF_BOMBS;

        public char [][] gameField;

        private Set<Point> bombLocations;
        private HashMap<Integer, Point> botLocations = new HashMap<Integer, Point>();

        public GameData(int botID, List<String> lines) {
            bombLocations = new HashSet<Point>();
            BOT_ID = botID;

            int pos = 1;
            WIDTH = Integer.parseInt(lines.get(pos++).split(" ")[2]);
            HEIGHT = Integer.parseInt(lines.get(pos++).split(" ")[2]);
            BOMBER_COUNT = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            POINTS_PER_TREASURE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            POINTS_LOST_FOR_DYING = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            DYING_COOL_DOWN = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_TIMER_DICE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_TIMER_SIDES = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            BOMB_FORCE = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            TURNS = Integer.parseInt(lines.get(pos++).split(" ")[1]);
            TREASURE_CHANGE = Double.parseDouble(lines.get(pos++).split(" ")[1]);
            INITIAL_COUNT_OF_BOMBS = Integer.parseInt(lines.get(pos++).split(" ")[1]);



            gameField = new char[HEIGHT][];

            for (int y = 0; y < HEIGHT; ++y) {
                ++pos;
                gameField[y] = new char[WIDTH];
                for (int x = 0; x < WIDTH; ++x) {
                    gameField[y][x] = lines.get(pos).charAt(x);
                }
            }

        }

        public void updateLevel(List<String> lines) {
            // update
            int linePos = 1;
            for (int y = 0; y < HEIGHT; ++y, ++linePos) {
                System.out.println(lines.get(linePos));
                for (int x = 0; x < WIDTH; ++x) {
                    gameField[y][x] = lines.get(linePos).charAt(x);
                }
            }
            botLocations.clear();
            bombLocations.clear();
            // update bomb and player locations
            for (; linePos < lines.size(); ++linePos) {
                String [] tokens = lines.get(linePos).split(" ");
                if (tokens.length == 0) {
                    continue;
                }

                // bot location
                if (tokens[0].charAt(0) == 'p' && tokens[2].equals("dead") == false) {
                    int id = Integer.parseInt("" + tokens[0].charAt(1));
                    String [] posTokens = tokens[2].split(",");
                    Point location = new Point(Integer.parseInt(posTokens[0]), Integer.parseInt(posTokens[1]));
                    botLocations.put(id, location);
                    DebugWriter.write("Bot + " + id + " located at: " + location + "\n");
                } else if (tokens[0].equals("bomb")) { // bomb location
                    String [] posTokens = tokens[2].split(",");
                    if (posTokens.length != 2) {
                        throw new RuntimeException("Check bomb parsing code, something odd going on");
                    }
                    Point bomb = new Point(Integer.parseInt(posTokens[0]), Integer.parseInt(posTokens[1]));
                    bombLocations.add(bomb);
                    DebugWriter.write("Bomb located at: " + bomb + "\n");

                }
            }

            GameState.getInstance().debugPrint();

        }

        public boolean tileHasBomb(int x, int y) {
            return bombLocations.contains(new Point(x, y));
        }

        public List<Point> getBombPositions() {
            ArrayList<Point> pos = new ArrayList<Point>();

            for (Point p : bombLocations)  {
                pos.add(p);
            }

            return pos;
        }

        public Point getBotPosition(int id) {
            if (botLocations.containsKey(id)) {
                return botLocations.get(id);
            }
            return null;
        }

        public Point getClosestOfType(Point point, char type) {
            int curX = 0;
            int curY = 0;
            double distance = 99999.0;

            for (int y = 0; y < HEIGHT; ++y) {
                for (int x = 0; x < WIDTH; ++x) {
                    if (gameField[y][x] == type) {
                        double temp = Math.abs(point.x - x) + Math.abs(point.y - y); // use manhattan distance
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
