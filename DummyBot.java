
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DummyBot {
    //dummy bot for BombestMan - just runs around randomly

    public static void main(String[] args) throws InterruptedException, IOException {
      // int botId = 0;
      //   MockupReader read = new MockupReader();
        int botId = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        Socket sock = connect("127.0.0.1", port);

        //initialize reader and writer for the socket
        PrintWriter write = new PrintWriter(sock.getOutputStream());
        BufferedReader read = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        String line;
        List<String> lines = new ArrayList<String>();
        Bot bot = new StateMachineBot(botId);


       //while(true) should be sufficient - the server automatically ends processes
        boolean isInitialized = false;
        while (true) {


            //read lines from the server until an empty line is delivered
            while (!(line = read.readLine()).isEmpty()) {
               lines.add(line);
            }

            if (lines.isEmpty()) {
                continue;
            }

            if (!isInitialized) {
                isInitialized = true;
                GameState.getInstance().initializeGameData(botId, lines);
                lines.clear();
                continue;
            } else {
                GameState.getInstance().updateGameData(lines);
            }

            String command = bot.getCommand(lines);
            System.out.println(command);
            write.append(command);
            //line break signals end of the command
            write.append("\n");
            //finally, flush the writer
            write.flush();

            lines.clear();
        }
    }

    private static Socket connect(String host, int port) throws InterruptedException {
        //a crude but simple connection method. It simply retries connection until it is established
        while (true) {
            try {
                return new Socket(host, port);
            } catch (Exception e) {
                System.err.println("Starting socket failed: " + e.toString());
                Thread.sleep(500);
                System.err.println("Retrying connection...");
            }
        }
    }
}
