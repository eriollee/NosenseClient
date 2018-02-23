package sample.csv;
import java.io.*;
import java.util.List;
import com.opencsv.*;

public class CSVUtils {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\7400DB3D-3C7D-4DA1-8484-F9459A28A303_7400DB3D-3C7D-4DA1-8484-F9459A28A303_iPhone 5c (GSM+CDMA)_20180209145831.csv");


    }

    public static void getData(String path,String exportPath) throws IOException {
        System.out.println("exportPath=="+exportPath);
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
        try{

            String[] strs = csvReader.readNext();
            csvReader.close();



            //写入csv
            File file2 = new File(exportPath+"\\write.csv");

            if(!file2.exists()){

                Writer writer = new FileWriter(file2,true);
                CSVWriter csvWriter = new CSVWriter(writer);
                String[] title = {"设备类型" , "传感器类型"};
                csvWriter.writeNext(title);
                csvWriter.close();
            }
            if(!isSame(devName,exportPath)){
                //表头time改为手机类型
                strs[0] = devName;
                Writer writer = new FileWriter(file2,true);
                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeNext(strs);
                csvWriter.close();
            }

        }catch (Exception e){
            System.out.println("filename="+fileName);
            e.printStackTrace();
            csvReader.close();
            file.delete();
        }finally {

        }

    }


    public static boolean isSame(String devName,String exportPath) throws IOException {
        File file = new File(exportPath+"\\write.csv");
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

    public static void getDetailData(String path,String exportPath,String fileName) throws IOException {
            File file = new File(path);
            FileReader fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
            String[] strs = csvReader.readNext();
            if(strs != null && strs.length > 0){
                for(String str : strs)
                    if(null != str && !str.equals(""))
                        System.out.print(str + " , ");
                System.out.println("\n---------------");
            }


        //写入csv
        File file2 = new File(exportPath+"\\"+fileName+"_A.csv");

        //写入标题
        if(!file2.exists()){

            Writer writer = new FileWriter(file2,true);
            CSVWriter csvWriter = new CSVWriter(writer);
            String[] title = strs;
            csvWriter.writeNext(title);
            csvWriter.close();
        }

        //写入内容
        Writer writer = new FileWriter(file2,true);
        CSVWriter csvWriter = new CSVWriter(writer);

        List<String[]> list = csvReader.readAll();
        for(String[] ss : list){
            for(String s : ss)
                if(null != s && !s.equals(""))
                    System.out.print(s + " , ");
            System.out.println();
            csvWriter.writeNext(ss);
        }
        csvReader.close();



        csvWriter.close();
    }

}
