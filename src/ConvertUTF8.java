
public class ConvertUTF8 {
	public static String toBinary(String str, int bits) {
	    String result = "", t = "";
	    char[] messChar = str.toCharArray();
	    for( int i = 0; i < bits; i++) t.concat("0");
	    for( int i = 0; i < messChar.length; i++) {
	        result = Integer.toBinaryString(messChar[i]);
	        result = (t + result).substring(result.length());//Awsome
	        result.concat(result);
	    } 
	    //System.out.println(result.length());
	    return result;
	}
	public static String tostring(String str, int bits) {
		System.out.println("String UTF-8:" + str.length());
		String result = "";
	    for (int i = 0; i < str.length()/bits; i++) {
	        int a = Integer.parseInt(str.substring(bits*i,(i+1)*bits),2);
	        result += (char)(a);
	    }
	    //System.out.println(result);
	    return result;
	}
}
