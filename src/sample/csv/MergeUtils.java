package sample.csv;

//java按文件大小、名称、日期排序方法
import java.io.*;
import java.util.*;

public class MergeUtils {
    public static void mergeHandler() throws Exception {
        List<File> files = orderByName(FileUtils.getPath()+ "\\data\\");
        ArrayList<File> filesTemp = new   ArrayList<File>(); //存放临时文件合并
        //System.out.println(files);
        for (int i=0;i<files.size();i++) {
            File f = files.get(i);
            File f2 = null;
            if(i==files.size()-1){
                f2 = files.get(i);//下一个文件
            }else {
                f2 = files.get(i+1);//下一个文件
            }
            String[] s = f.getName().split("_");
            String next = null;
            String current = null;
            if(s[2].indexOf("iPhone")>-1){
//                System.out.println(s[4].substring(0,1));
//                System.out.println(s[2]);

                try {
                    next = f2.getName().split("_")[4].substring(0,1);
                } catch (Exception e) {
                    next = "0";//若下一条为安卓则置为0
                }

                if(!"0".equals(next)){
                    filesTemp.add(f);
                }

                if("0".equals(next)){
                    filesTemp.add(f);
                    mergeFile(filesTemp);
                    filesTemp = new ArrayList<File>();
                }else if (i==files.size()-1){
                   //if (i==files.size()-1)
                    mergeFile(filesTemp);
                    filesTemp = new ArrayList<File>();
                }

            }else {//安卓
                //todo
                try {
                    next = f2.getName().split("_")[3].substring(0,1);
                } catch (Exception e) {
                    next = "1";//若下一条为iphone则置为1
                }
                System.out.println(f.getName());

                try {
                    current = f.getName().split("_")[3].substring(0,1);
                } catch (Exception e) {
                    current = "";
                }

                //System.out.println("next=="+next);
                if(!"1".equals(next)){
                    filesTemp.add(f);
                }

                if("1".equals(next)){
                    if(!"7".equals(current)&&!"".equals(current)){
                        filesTemp.add(f);
                    }
                    mergeFile(filesTemp);
                    filesTemp = new ArrayList<File>();
                }else if (i==files.size()-1){
                    //if (i==files.size()-1)
                    if("7".equals(current)&&!"".equals(current)) {
                        filesTemp.remove(filesTemp.size()-1);
                    }
                    mergeFile(filesTemp);
                    filesTemp = new ArrayList<File>();
                }

            }

        }

    }

    //按照文件名称排序
    public static List orderByName(String fliePath) {
        List<File> files = Arrays.asList(new File(fliePath).listFiles());
        Collections.sort(files, new Comparator< File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
//        for (File f : files) {
//            System.out.println(f.getName());
//        }
        return files;
    }

    public static void mergeFile(List<File> files)  throws Exception{
         for(int i=0;i<files.size();i++){
             File f = files.get(i);
             CSVUtils.getDetailData(f,FileUtils.getPath()+ "\\data\\",files.get(0).getName().substring(0,files.get(0).getName().length()-6));
        }

    }


}
