package sample.csv;

//java按文件大小、名称、日期排序方法
import java.io.*;
import java.util.*;

public class MergeUtils {
    public static void main(String[] args) throws Exception {
        List<File> files = orderByName(FileUtils.getPath()+ "\\data\\");
        ArrayList<File> filesTemp = new   ArrayList<File>(); //存放临时文件合并
        for (int i=0;i<files.size();i++) {
            File f = files.get(i);
            File f2 = null;
            if(i==files.size()-1){
                f2 = files.get(i);//下一个文件
            }else {
                f2 = files.get(i+1);//下一个文件
            }


            if(f.getName().indexOf("iPhone")>-1){
                String[] s = f.getName().split("_");
                System.out.println(s[4].substring(0,1));
//                ZipUtils.ungzip(f.getCanonicalPath(), f);
//                f.delete();
                if(!"0".equals(f2.getName().split("_")[4].substring(0,1))){
                    CSVUtils.getDetailData(f.getCanonicalPath(),FileUtils.getPath()+ "\\data\\");
                }

            }else {

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
}
