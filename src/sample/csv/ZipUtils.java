package sample.csv;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.util.zip.GZIPInputStream;


public class ZipUtils {

    /**
     * 解压zip格式压缩包
     * 对应的是ant.jar
     */
    public static void unzip(String sourceZip,String destDir) throws Exception{
        File file = new File(sourceZip);
        String[] s = sourceZip.split("_");
        String s1[] = s[0].split("\\\\");
        System.out.println("s[0]="+s1[s1.length-1]);
        try{
            Project p = new Project();
            Expand e = new Expand();
            e.setProject(p);
            e.setSrc(file);
            e.setOverwrite(false);
            File file2 = new File(destDir+"\\temp\\");
            e.setDest(file2);
           /*
           ant下的zip工具默认压缩编码为UTF-8编码，
           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
           所以解压缩时要制定编码格式
           */
            e.setEncoding("gbk");

            try {
                e.execute();
                for(String  f:file2.list()){
                    // System.out.println("f=="+f);
                    File fileRe =new File(destDir+"\\temp\\"+f);
                    fileRe.renameTo(new File(destDir+"\\temp\\"+s1[s1.length-1]+"_"+f));
                    FileUtils.fileChannelCopy(destDir+"\\temp\\"+s1[s1.length-1]+"_"+f,destDir+s1[s1.length-1]+"_"+f);
                }
                FileUtils.deleteDirectory(destDir+"\\temp\\");
            } catch (BuildException e1) {
                file.delete();
            }



        }catch(Exception e){
            file.delete();
            throw e;
        }
    }

    public static  void ungzip(String sourceZip,File file) throws Exception{
        // 转为二进制
        byte[] ret1 = fileToBytes(sourceZip);
        // 还原文件
        byte[] ret2 =  ungzipByte(ret1);

        String fileName = file.getName().replace(".zip","");
        // 写出文件
        String writePath =file.getParent()+"\\"+fileName;
        System.out.println("writePath=="+writePath);
        FileOutputStream fos = new FileOutputStream(writePath);
        fos.write(ret2);
        fos.close();
    }

    public static byte[] ungzipByte(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream gzip = new GZIPInputStream(bis);
        byte[] buf = new byte[1024];
        int num = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((num = gzip.read(buf, 0, buf.length)) != -1) {
            bos.write(buf, 0, num);
        }
        gzip.close();
        bis.close();
        byte[] ret = bos.toByteArray();
        bos.flush();
        bos.close();
        return ret;
    }

    public static void main(String[] args) throws Exception {
       // unzip("D:\\00000000-0000-0000-0000-000000000000_00000000-0000-0000-0000-000000000000_iPhone 6 Plus_20180209145907.csv.zip","D:\\");
       // ZipUtil.unpack(new File("D:\\352689080271350-D9E89E137D8E77BD_Pixel_20180209155446_2.csv.zip"), new File("D:\\"));


        // 转为二进制
        byte[] ret1 = fileToBytes("D:\\00000000-0000-0000-0000-000000000000_00000000-0000-0000-0000-000000000000_iPhone 6 Plus_20180209145907.csv.zip");
        // 还原文件
        byte[] ret2 =  ungzipByte(ret1);

        // 写出文件
        String writePath = "D:\\00000000-0000-0000-0000-000000000000_00000000-0000-0000-0000-000000000000_iPhone 6 Plus_20180209145907.csv";
        FileOutputStream fos = new FileOutputStream(writePath);
        fos.write(ret2);
        fos.close();
    }

    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally{
                try {
                    if(null!=fis){
                        fis.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return buffer;
    }

    //获取不带后缀名的文件名
    public static String getFileNameWithoutSuffix(File file){
        String file_name = file.getName();
        return file_name.substring(0, file_name.lastIndexOf("."));
    }
}
