
public class ConvertUTF8 {
	public static String toBinary(String str, int bits) {
	    String result = "";
	    String tmpStr;
	    int tmpInt;
	    char[] messChar = str.toCharArray();

	    for (int i = 0; i < messChar.length; i++) {
	        tmpStr = Integer.toBinaryString(messChar[i]);
	        tmpInt = tmpStr.length();
	        if(tmpInt != bits) {
	            tmpInt = bits - tmpInt;
	            if (tmpInt == bits) {
	                result += tmpStr;
	            } else if (tmpInt > 0) {
	                for (int j = 0; j < tmpInt; j++) {
	                    result += "0";
	                }
	                result += tmpStr;
	            } 
	        } else {
	            result += tmpStr;
	        }        
	    } 
	    return result;
	}
	public static String tostring(String str, int bits) {
		//System.out.println(str);
		String result = "";
	    for (int i = 0; i < str.length()/bits; i++) {
	        int a = Integer.parseInt(str.substring(bits*i,(i+1)*bits),2);
	        result += (char)(a);
	    }
	    return result;
	}
}
