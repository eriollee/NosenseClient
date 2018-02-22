package sample.csv;

import org.apache.tools.ant.taskdefs.Zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
    /**
     * 读取某个文件夹下的所有文件
     */
    public static void readfile(String filepath,int type) throws FileNotFoundException, IOException {
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
//                System.out.println("path=" + file.getPath());
//                System.out.println("absolutepath=" + file.getAbsolutePath());
//                System.out.println("name=" + file.getName());
//                System.out.println("name=" + file.getParentFile());
               // ZipUtils.unzip(file.getAbsolutePath(),file.);
            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
//                        System.out.println("path=" + readfile.getPath());
//                        System.out.println("absolutepath="
//                                + readfile.getAbsolutePath());
//                        System.out.println("name=" + readfile.getName());
/*                        System.out.println("name indexOf=" + readfile.getName().indexOf("iPhone "));
                        try {
                            String[] s = file.getName().split("_");
                            System.out.println(s[0]+" "+s[1]+" "+s[2]);
                        }catch (Exception e){

                        }*/
                        System.out.println(readfile.getCanonicalPath());
                        System.out.println("filepath=="+filepath);
                       if(readfile.getName().indexOf("iPhone")>-1){
                            switch (type){
                                case 1:
                                    ZipUtils.ungzip(readfile.getCanonicalPath(), readfile);
                                    readfile.delete();
                                    break;
                                case 2:CSVUtils.getData(readfile.getCanonicalPath(),filepath);readfile.delete();break;
                            }

                       }
                       else{
                           if(type == 0){
                               ZipUtils.unzip(readfile.getCanonicalPath(),filepath);
                           }else {
                               switch (type){
                                   case 1:

                                       ZipUtils.unzip(readfile.getCanonicalPath(),filepath);
                                       readfile.delete();
                                       System.out.println("break");
                                       break;
                                   case 2:
                                       CSVUtils.getData(readfile.getCanonicalPath(),filepath);
                                       readfile.delete();break;
                               }

                           }

                        }
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i],0);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        try {
//            readfile( getPath()+ "data\\",0);
//            readfile( getPath()+ "data\\data\\",1);
//            readfile( getPath()+ "data\\data\\",2);
//            // deletefile("D:/file");
//        } catch (FileNotFoundException ex) {
//        } catch (IOException ex) {
//        }
        try {
            //readfile( getPath(),0);
//            System.out.println("121212");
            readfile( getPath()+ "\\data\\",1);
            System.out.println("3434");
            readfile( getPath()+ "\\data\\",2);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("ok");
    }

    public static String getDevInfo(){
        String exception ="";
        try {
            readfile( getPath(),0);
            readfile( getPath()+ "\\data\\",1);
            readfile( getPath()+ "\\data\\",2);
            exception = "TRY";
        } catch (FileNotFoundException ex) {
            exception = "FileNotFoundException";
        } catch (IOException ex) {
            exception = "IOException";
        }
        // deletefile("D:/file");
        return exception;
    }

    public static String getPath()   {
        //获取相对路径
//        String uploadPath = Thread.currentThread().getContextClassLoader().getResource("").toString();
//        uploadPath = uploadPath.replace('/','\\').replace("file:","")
//                .replace("classes\\","").replace("target\\","").substring(1);
//        System.out.println("uploadPath=="+uploadPath);
        String uploadPath= null;
        try {
           // uploadPath = new File("data/").getCanonicalPath();

            uploadPath = new File(new File(System.getProperty("user.dir")).getParent()+"/data").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadPath;
    }
}
