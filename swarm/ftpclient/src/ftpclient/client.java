package ftpclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;



public class client {
		
private Socket s;
private static final int BLOCK_LEN=1024;
private static final int MAX_FILE_SZ=0xffffffff;

	public client(String host, int port) {
		try {
			s = new Socket(host, port);
			saveFile(s);


		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void saveFile(Socket clientSock) throws IOException {
		byte[] genesisdata="!MGENEGISBLOCKFORTCPFILETRANSFERIAMROOTHERE".getBytes();
		Block genesisBlock = new Block(genesisdata, "0".getBytes());
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream("clirecvfile.txt");
		byte[] buffer = new byte[4096];
		
		int filesize = MAX_FILE_SZ; // send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		// Now we need to read the block with initil hash + body + final hash
		// This is BLOCK_LEN + 2 hash tags
		int hashlen = genesisBlock.hash.length;
		int blocklen=BLOCK_LEN+ 2*hashlen;
		int blockread = 0;
		int off=0;
		boolean filetoread=true;
		while(filetoread)
		{
			read = dis.read(buffer, off, blocklen-off);
			if(read>0)
			{
				if(off+read < blocklen)
				{
					off+= read;
					continue;
				}
				read = blocklen;
			}
			else
			{
				filetoread = false;
				if(off<=2*hashlen)
					continue;
				read = off;
			}
			    totalRead += (read-2*hashlen);
				remaining -= (read-2*hashlen);
				int len = buffer.toString().length();
				
				byte[] headhash = new byte[hashlen];
				System.arraycopy(buffer, 0, headhash, 0, hashlen);
				if(!Arrays.equals(genesisBlock.hash,headhash))
					{
					System.out.println("Recv Headhash["+blockread+" fail,read " + totalRead + " bytes.");
					System.out.println("Recv Headhash["+blockread+" fail,read " + totalRead + " bytes.");
					System.out.println("Recv Headhash["+blockread+" fail,calc ");
					StringUtil.printbytes(genesisBlock.hash,"Serv Head Calculated");
					StringUtil.printbytes(headhash,"Serv Head Received:");
					return;
				}
				//System.out.println("Recv bytes len:" + read +" hashlen: "+hashlen + " blocklen: "+ blocklen);
				byte[] datarecv = new byte[read-2*hashlen];
				System.arraycopy(buffer, hashlen, datarecv, 0, read-2*hashlen);
				Block next = new Block(datarecv,genesisBlock.hash);
				byte[] tailhash = new byte[hashlen];
				System.arraycopy(buffer, read-hashlen, tailhash, 0, hashlen);

				if(!Arrays.equals(next.hash,tailhash))
				{
					System.out.println("Recv Tailhash["+blockread+" fail,read " + totalRead + " bytes.");
					StringUtil.printbytes(next.hash,"Serv Tail Calculated:");
					StringUtil.printbytes(tailhash, "Serv Tail Received  :");
					return;
				}	
				
				//System.out.println("read " + totalRead + " bytes.");
				fos.write(buffer, hashlen, read-2*hashlen);
				//System.out.println("Recv Tailhash["+blockread+" success,read " + totalRead + " bytes.");
				//StringUtil.printbytes(tailhash,"Serv Tail:");
				//StringUtil.printbytes(headhash,"Serv Head:");
				if((next.hash.length ==hashlen) && (genesisBlock.hash.length==hashlen))
					System.arraycopy(next.hash, 0, genesisBlock.hash, 0, hashlen);
				else
				{
					System.out.println("Serv: Error copying hash nextlen"+next.hash.length+" genesislen:"+genesisBlock.hash.length+" hashlen:"+hashlen);
					return;
				}
				off=0;
		}
		
		fos.close();
		dis.close();
	}
		
	public static void main(String[] args) {
//		Block genesisBlock = new Block("!MGENEGISBLOCKFORTCPFILETRANSFERIAMROOTHERE", "0");
//		System.out.println("Hash for block 1 : " + genesisBlock.hash);
		try {
		client fc = new client("localhost", 8888);
		}
		 catch (Exception e) {
			 System.out.println("File download got error");
		}		
	}

}

