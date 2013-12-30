/**
 * Created by Omistaja on 29.12.2013.
 */
public class MockupReader {
    private boolean initialized = false;
    int pos = 0;
    private String [] initialize = {
    "BEGIN MSG",
    "map width: 11",
    "map height: 11",
    "bombersCount 2",
    "POINTS_PER_TREASURE 1",
    "POINTS_LOST_FOR_DYING 3",
    "DYING_COOL_DOWN 10",
    "BOMB_TIMER_DICE 4",
    "BOMB_TIMER_SIDES 3",
    "BOMB_FORCE 4",
    "TURNS 200",
    "TREASURE_CHANGE 0.2",
    "INITIAL_COUNT_OF_BOMBS 2",
    "MAP:",
            "0..?????...",
            ".#.#?#?#.#.",
            "..???????..",
            "?#?#?#?#?#?",
            "????.$.????",
            "?#?#$$$#?#?",
            "????.$.????",
            "?#?#?#?#?#?",
            "..???????..",
            ".#.#?#?#?#.",
            "1..?????...",
    "END MSG"
    };


    private String [] normal = {
            "Turns left: 200",
            "0..?????...",
            ".#.#?#?#.#.",
            "..???????..",
            "?#?#?#?#?#?",
            "????.$.????",
            "?#?#$$$#?#?",
            "????.$.????",
            "?#?#?#?#?#?",
            "..???????..",
            ".#.#?#?#?#.",
            "1..?????...",
            "p0 at 0,0 with 0 points",
            "p1 at 0,10 with 0 points",

       };

    public String readLine() {
        if (!initialized) {
            if (pos >= initialize.length) {

                pos = 0;
                initialized = true;
                return "";
            } else {
                return initialize[pos++];
            }
        }


        if (pos >= normal.length) {
            pos = 0;
            return "";
        }
        return normal[pos++];

    }

}
