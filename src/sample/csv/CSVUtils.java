package sample.csv;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import com.opencsv.*;

public class CSVUtils {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\7400DB3D-3C7D-4DA1-8484-F9459A28A303_7400DB3D-3C7D-4DA1-8484-F9459A28A303_iPhone 5c (GSM+CDMA)_20180209145831.csv");

        //获取文件名中的设备
        String fileName = file.getName();
        String[] s = fileName.split("_");
        String devName = s[2];
        //获取相对路径
        String uploadPath = Thread.currentThread().getContextClassLoader().getResource("").toString();
        uploadPath = uploadPath.replace('/','\\').replace("file:","")
                .replace("classes\\","").replace("target\\","").substring(1);
        System.out.println("uploadPath=="+uploadPath);

        //获取表头
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        String[] strs = csvReader.readNext();
        csvReader.close();

        //表头time改为手机类型
        strs[0] = devName;



        //写入csv
        File file2 = new File("e:\\write.csv");
        Writer writer = new FileWriter(file2);
        CSVWriter csvWriter = new CSVWriter(writer);
        String[] title = {"设备类型" , "传感器类型"};
        csvWriter.writeNext(title);
        csvWriter.writeNext(strs);
        csvWriter.close();
    }
}
