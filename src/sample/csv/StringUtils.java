package sample.csv;

public class StringUtils {
    public static String getDeviceName(String fileName){
        String[] s = fileName.split("_");
        String devName = "";
        if(fileName.indexOf("iPhone")>-1){
            devName = s[2];
        }else {
            devName = s[0];
        }
        return devName;
    }
}
