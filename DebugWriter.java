import java.io.PrintWriter;

public class DebugWriter {
    private static PrintWriter writer;

    public static void write(String text) {

        if (writer == null) {
            try {
                writer = new PrintWriter("debug.txt");
            } catch (Exception ex) {
                return;
            }
        }

        writer.write(text);
        writer.flush();
    }

    public static void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

}
