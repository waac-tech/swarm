package ftpserver;
import java.util.Date;


import java.util.Date;

public class Block {

	public byte[] hash;
	public byte[] previousHash;
	private byte[] data; //data will be a simple message.
	private long timeStamp;

	//Block Constructor.
	public Block(byte[] data, byte[] previousHash ) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash(); 
	}
	
	public byte[] calculateHash() {
		byte[] dataprevhash = new byte[previousHash.length + data.length];
		System.arraycopy(previousHash, 0, dataprevhash, 0, previousHash.length);
		System.arraycopy(data, 0, dataprevhash, previousHash.length, data.length);
		byte[] calculatedhash = StringUtil.applySha256(dataprevhash);
		return calculatedhash;
	}
}