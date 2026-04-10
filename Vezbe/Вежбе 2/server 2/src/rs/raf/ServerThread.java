package rs.raf;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            /**
            Otvara fajl, rezerviše deskriptor fajla u OS-u, alocira buffer u memoriji
            To NIJE samo Java objekat, To je veza ka operativnom sistemu.
            */
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            while (true) {
                int x = Integer.parseInt(in.readLine());
                int y = Integer.parseInt(in.readLine());
                int z = x + y;

                out.println(z);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /** GC oslobađa memoriju objekta, ali ne garantuje zatvaranje sistemskih resursa */
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                out.close();
            }

            if (this.socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
