package sample.csv;
import java.io.*;
import java.util.List;
import com.opencsv.*;

public class CSVUtils {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\7400DB3D-3C7D-4DA1-8484-F9459A28A303_7400DB3D-3C7D-4DA1-8484-F9459A28A303_iPhone 5c (GSM+CDMA)_20180209145831.csv");


    }

    public static void getData(String path) throws IOException {
        System.out.println("path=="+path);
        File file = new File(path);
        //获取文件名中的设备
        String fileName = file.getName();
        String[] s = fileName.split("_");
        String devName = "";
        if(fileName.indexOf("iPhone")>-1){
            devName = s[2];
        }else {
            devName = s[0];
        }


        //获取表头
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        String[] strs = csvReader.readNext();
        csvReader.close();

        //表头time改为手机类型
        strs[0] = devName;



        //写入csv
        File file2 = new File("e:\\write.csv");
        Writer writer = new FileWriter(file2,true);
        CSVWriter csvWriter = new CSVWriter(writer);
        if(!file2.exists()){
            String[] title = {"设备类型" , "传感器类型"};
            csvWriter.writeNext(title);
        }
        if(!isSame(devName)){
            csvWriter.writeNext(strs);
        }
        csvWriter.close();


    }


    public static boolean isSame(String devName) throws IOException {
        File file = new File("e:\\write.csv");
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        List<String[]> list = csvReader.readAll();

        for (int i = 1; i < list.size(); i++) {
              String devNameTmp = list.get(i)[0];
              if(devName.equals(devNameTmp)){
                  return true;
              }
        }
        return false;
    }
}
