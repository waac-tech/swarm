package ftpclient;

import java.security.MessageDigest;

public class StringUtil {
	//Applies Sha256 to a string and returns the result. 
	public static byte[] applySha256(byte input[]){
		try {
			printbytes(input,"applySha256: ");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input);	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			//System.out.println("HashString:"+ hexString.toString());
			return hash;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void printbytes(byte input[],String src){
		try {
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < input.length; i++) {
				String hex = Integer.toHexString(0xff & input[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			//System.out.println(src+" HashString:"+ hexString.toString());
			return;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}	

}