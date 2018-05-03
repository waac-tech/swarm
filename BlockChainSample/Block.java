import java.util.Date;
import java.security.MessageDigest;

class Block {
	
	
	public String hash;
	public String previousHash;
	private String data;
	private long timeStamp;
	
	public Block(String data, String previousHash){
		
		// Block constructor
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
	}
}