import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
	public static String encode(String pass, String data){
		
		if (pass.equals("")){
			return data;				//End point of function
		}
		//Create key integer from input pass
		byte[] tmp = CreateKey(pass);
		String key  = "";
		for(int i = 0; i < pass.length(); i++){
			key += Integer.toString(Math.abs(tmp[i]));
		}
		System.out.println(key);
		//Get Array index from key
		int[] index = new int[key.length()];
		for(int i = 0; i < key.length(); i++){
			index[i] = key.charAt(i)-48;
		}
		
		//Change state of bit, ever block array index
		//System.out.println(index);
		char[] dataArr = data.toCharArray();
		/*
		for(int i = 0; i < dataArr.length ; ){
			for(int in:index){
				try
				{
					dataArr[in+i] = (dataArr[in+i] == '0') ? '1' : '0';
					i = i + in + 1;
				}catch(Exception e)
				{
					return String.valueOf(dataArr);
				}
			}
		}*/
		//System.out.println(dataArr);
		return String.valueOf(dataArr); //not use .toString for convert char[] to String, because value is HEXA of address memory	
	}
	public static String decode(String pass, String data){
		return encode(pass, data);
	}
	private static byte[] CreateKey(String key){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md.digest(key.getBytes());
	}
	
}
