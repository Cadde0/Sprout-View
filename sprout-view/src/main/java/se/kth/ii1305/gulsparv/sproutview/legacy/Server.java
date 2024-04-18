package se.kth.ii1305.gulsparv.sproutview.legacy;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

//String OK = "HTTP/1.1 200 OK\r\nContent-Length: " + res.length() + "\r\n\r\n";

public class Server {

    public static Server INSTANCE = new Server(8888);

    static String BadRequest = "HTTP/1.1 400 Bad Request\r\n";

    int portNumber;

    public static Server getInstance() {return INSTANCE;}

    private Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);

            List<Thread> clientThreads = new ArrayList<Thread>();

            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                System.out.println("SERVER: Connection established");
                Thread clientThread = new Thread(clientHandler);
                clientThreads.add(clientThread);
                clientThread.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }
}