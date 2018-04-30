package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    public static void main(String[] args) throws IOException {
        try {
        ServerSocket serverSocket = new ServerSocket(6578);
        while (true) {
            new Thread(new ClientWorker(serverSocket.accept())).start();
        }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ClientWorker implements Runnable{
        private Socket tagetSocket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        public ClientWorker(Socket recieveSocket){
            try {
                tagetSocket = recieveSocket;
                dataInputStream = new DataInputStream(tagetSocket.getInputStream());
                dataOutputStream = new DataOutputStream(tagetSocket.getOutputStream());
            }catch (IOException e){
              e.printStackTrace();
            } }

        @Override
        public void run() {
            while (true) {
                byte[] initialize = new byte[1];
                try {
                    dataInputStream.read(initialize, 0, initialize.length);
                    if (initialize[0]==2){
                        System.out.println(new String(ReadStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private byte[] ReadStream(){
            byte[] dataBuffer = null;
            try{
                int b = 0;
                String lengthBuffer = "";
                while ((b = dataInputStream.read())!=4){
                    lengthBuffer += (char)b;
                }
                int dataLength = Integer.parseInt(lengthBuffer);
                dataBuffer = new byte[Integer.parseInt(lengthBuffer)];
                  int byteRead = 0;
                  int byteOffset = 0;
                  while (byteOffset < dataLength){
                      byteRead = dataInputStream.read(dataBuffer, byteOffset,dataLength - byteOffset);
                      byteOffset += byteRead;
                  }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataBuffer;
        }
    }


