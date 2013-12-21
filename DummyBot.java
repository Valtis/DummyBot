
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author kviiri
 */
public class DummyBot {
    //dummy bot for BombestMan - just runs around randomly

    public static void main(String[] args) throws InterruptedException, IOException {
        int botId = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        Socket sock = connect("127.0.0.1", port);

        //initialize reader and writer for the socket
        PrintWriter write = new PrintWriter(sock.getOutputStream());
        BufferedReader read = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        String line;
        //while(true) should be sufficient - the server automatically ends processes
        while (true) {
            //read lines from the server until an empty line is delivered
            while (!(line = read.readLine()).isEmpty()) {
                //do what you will with the line
            }

            //make a random move
            double rand = Math.random();
            write.append("move ");
            write.append(rand < 0.25 ? "u" :
                         rand < 0.5  ? "r" :
                         rand < 0.75 ? "d" :
                                       "l");
            //line break signals end of the command
            write.append("\n");
            //finally, flush the writer
            write.flush();
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
