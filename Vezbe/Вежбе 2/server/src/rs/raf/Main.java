package rs.raf;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final int PORT = 9001;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Main.PORT);
            while (true) {
                System.out.println("Server ocekuje konekcije");
                Socket socket = serverSocket.accept();
                System.out.println("Server primio konekciju");
                Thread serverThread = new Thread(new ServerThread(socket));
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
