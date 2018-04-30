package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6578);
        while (true){
            try {
                serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class ClientWorker implements Runnable{
        private Socket tagetSocket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        public ClientWorker(Socket recieveSocket){
            try {
                tagetSocket = recieveSocket;
                dataInputStream = new DataInputStream(tagetSocket.getInputStream());
                dataOutputStream = new DataOutputStream(tagetSocket.getOutputStream());
            }catch (IOException ex){
              ex.printStackTrace();
            } }

        @Override
        public void run() {

        }
    }
}
