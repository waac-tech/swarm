package clinet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        try {
            Socket  clientObject = new Socket(InetAddress.getByName("127.0.0.1"),6578);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
