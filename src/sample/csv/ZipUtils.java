package sample.csv;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import java.io.File;
import java.io.FileOutputStream;


public class ZipUtils {

    /**
     * 解压zip格式压缩包
     * 对应的是ant.jar
     */
    private static void unzip(String sourceZip,String destDir) throws Exception{
        try{
            Project p = new Project();
            Expand e = new Expand();
            e.setProject(p);
            e.setSrc(new File(sourceZip));
            e.setOverwrite(false);
            e.setDest(new File(destDir));
           /*
           ant下的zip工具默认压缩编码为UTF-8编码，
           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
           所以解压缩时要制定编码格式
           */
            e.setEncoding("gbk");
            e.execute();
        }catch(Exception e){
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        unzip("D://352689080271350-D9E89E137D8E77BD_Pixel_20180209155446_2.csv.zip","D:\\");
    }
}
