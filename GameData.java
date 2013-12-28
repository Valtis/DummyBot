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


    import java.util.List;

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


        public GameData(int botID, List<String> lines) {
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
            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++i) {
                    gameField[i][j] = lines.get(i).charAt(j);
                }
            }
        }


}
