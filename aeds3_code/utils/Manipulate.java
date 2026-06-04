package utils;

public class Manipulate {
    public static int toInt(String str) {
        int resp = 0;
        
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if(c < '0' || c > '9') {
                return -1;
            }

            resp = (resp*10) + (c - '0');
        }

        return resp;
    }
}
