package ftpserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


//import ftpclient.Block;

public class server extends Thread {
	private static final int BLOCK_LEN=1024;
	private static final int MAX_FILE_SZ=0xffffffff;
	private ServerSocket ss;
	String file;
	
	public server(int port, String files) {	
		try {
			ss = new ServerSocket(port);
			file = files;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			try {
				Socket clientSock = ss.accept();
				sendFile(clientSock,file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendFile(Socket s,String file) throws IOException {
		byte[] genesisdata="!MGENEGISBLOCKFORTCPFILETRANSFERIAMROOTHERE".getBytes();
		Block genesisBlock = new Block(genesisdata, "0".getBytes());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		
		byte[] buffer = new byte[BLOCK_LEN+128];
		int lenread = 0;
		int readcount = 0;
		while ((lenread = fis.read(buffer,0,BLOCK_LEN)) > 0) {
			dos.write(genesisBlock.hash,0,genesisBlock.hash.length);
			dos.write(buffer, 0, lenread);
			//if(readcount> 10) buffer[10]='a';
			byte[] bufferdata = new byte[lenread];
			System.arraycopy(buffer, 0, bufferdata, 0, lenread);
			Block next = new Block(bufferdata,genesisBlock.hash);
			dos.write(next.hash,0,next.hash.length);
			//System.out.println("Cli-Hash for genesis block["+readcount+"]: " + genesisBlock.hash.length + "bufferlen: " + buffer.length + "lenread:"+lenread);
			//System.out.println("Cli-Hash for next    block["+readcount+"]: " + next.hash.length);
			//StringUtil.printbytes(genesisBlock.hash,"Cli genesisBlock");
			//StringUtil.printbytes(next.hash,"Cli Next calculated:");
			genesisBlock.hash = next.hash;
			if(next.hash.length != genesisBlock.hash.length)
			{
				System.out.println("Cli: Error copying hash nextlen"+next.hash.length+" genesislen:"+genesisBlock.hash.length);
				return;
			}
			System.arraycopy(next.hash, 0, genesisBlock.hash, 0, genesisBlock.hash.length);
			readcount++;
		}
		
		fis.close();
		dos.close();	
	}


	public static void main(String[] args) {
		server fs = new server(8888, "server.txt");
		fs.start();
	}
}

