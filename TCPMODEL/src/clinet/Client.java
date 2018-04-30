package clinet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args){
        try {
            Client object = new Client();
            Socket  clientObject = new Socket(InetAddress.getByName("127.0.0.1"),6578);
            DataInputStream dataInputStream = new DataInputStream(clientObject.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientObject.getOutputStream());
            byte[] buffer = object.CreateDataPacket("CodeVlog".getBytes("UTF8"));
            dataOutputStream.write(buffer);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] CreateDataPacket(byte[] data){
            byte[] packet = null;
        try {
            byte[] initilize = new byte[1];
            initilize[0] = 2;
            byte[] seperator = new byte[1];
            seperator[0] = 4;
            byte[] dataLength = String.valueOf(data.length).getBytes("UTF8");
            packet = new byte[initilize.length + seperator.length + dataLength.length + data.length];

            System.arraycopy(initilize,0,packet,0,initilize.length);
            System.arraycopy(dataLength,0,packet,initilize.length,dataLength.length);
            System.arraycopy(seperator,0,packet,initilize.length + dataLength.length,seperator.length);
            System.arraycopy(data,0,packet,initilize.length + dataLength.length + seperator.length,data.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return packet;
    }
}
