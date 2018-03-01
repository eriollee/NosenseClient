package sample.csv;

import org.apache.tools.ant.taskdefs.Zip;

import java.io.*;
import java.nio.channels.FileChannel;

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
                                   //    System.out.println("break");
                                       break;
                                   case 2:
                                       CSVUtils.getData(readfile.getCanonicalPath(),filepath);
                                   //    readfile.delete();break;
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
    /*
     * 使用文件通道的方式复制文件
     */
    public static void fileChannelCopy(String srcDirName,String destDirName) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(new File(srcDirName));
            fo = new FileOutputStream(new File(destDirName));
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            /*
             *       public abstract long transferTo(long position, long count,
                                         WritableByteChannel target)throws IOException;
             *          position - 文件中的位置，从此位置开始传输；必须为非负数
             *          count - 要传输的最大字节数；必须为非负数
             *          target - 目标通道
             *          返回：
                        实际已传输的字节数，可能为零
             */
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道中
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
       //     System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag =  deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag =  deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
         //   System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
         //   System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
           // System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
             //   System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
              //  System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
         //   System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
}
