package sample.csv;
import java.io.*;
import java.util.ArrayList;
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

    public static void getDetailData(File f,String exportPath,String fileName) throws IOException {
            File file = new File(f.getCanonicalPath());
            FileReader fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
            String[] strs = csvReader.readNext();
//            if(strs != null && strs.length > 0){
//                for(String str : strs)
//                    if(null != str && !str.equals(""))
//                        System.out.print(str + " , ");
//                System.out.println("\n---------------");
//            }


        //写入csv
        File file3 = new File(f.getParentFile().getParentFile().getPath()+"\\total\\");
        File file2 = null;
        if(file3.exists()){
            file2 = new File(f.getParentFile().getParentFile().getPath()+"\\total\\"+fileName+"_A.csv");
        }else {
            file3.mkdirs();
            file2 = new File(f.getParentFile().getParentFile().getPath()+"\\total\\"+fileName+"_A.csv");
        }


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
//            for(String s : ss)
//                if(null != s && !s.equals(""))
//                    System.out.print(s + " , ");
//            System.out.println();
            csvWriter.writeNext(ss);
        }
        csvReader.close();



        csvWriter.close();
    }


    /**
     * 导出
     *
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;

        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }

    /**
     * 导入
     *
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();

        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }


}
