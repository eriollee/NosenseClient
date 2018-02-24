package sample.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import sample.properties.LinkedProperties;
import sample.properties.PropUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.lang3.*;

public class CalcUtils {
    public static void main(String[] args) throws IOException {
        dataProcess();
    }

    public static void dataProcess() throws IOException {

        //新建3文件，讲数据保存到3中
        String filePath2 = FileUtils.getPath()+ "_temp.csv";
        File retFile = new File(filePath2);
        if(!retFile.exists()){
            retFile.createNewFile();
        }

        //先写入title
        Writer writer = new FileWriter(filePath2);
        CSVWriter csvWriter = new CSVWriter(writer);
        List identityList = new ArrayList();
        List identityListTmp = new ArrayList ();

        LinkedProperties propIdentity = PropUtils.loadLinkedProperties("identity.properties");
        identityList = propIdentity.getKeyList();


        String recordPath = FileUtils.getPath()+ "\\data\\01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_iPhone 7 Plus_20180212182520_A.csv";
       // System.out.println(identityList);

        //所有的原始数据
        List<String[]> lines =  getContent(recordPath);
        //获取到原始数据的标题
        String[] titleA = lines.get(0);
        for(int i=0; i<titleA.length;i++){
            titleA[i] =titleA[i].trim();
        }


        List<Integer> indexList = new ArrayList<Integer>();
        //增加标题即索引
        for (int i = 0; i < propIdentity.getKeyList().size();i++) {
             if(identityList.get(i).toString().indexOf("_x")>-1
                     ||identityList.get(i).toString().indexOf("_y")>-1
                     ||identityList.get(i).toString().indexOf("_z")>-1){

                 identityListTmp.add(identityList.get(i)+"_max");
                 identityListTmp.add(identityList.get(i)+"_min");
                 identityListTmp.add(identityList.get(i)+"_avg");
                 identityListTmp.add(identityList.get(i)+"_loss");

                 //获取索引
                 String titleSingle = propIdentity.getProperty(identityList.get(i).toString());
                 for(String title:titleA){
                     if(titleSingle.indexOf(title) >-1){
                      //   System.out.println(title);
                         Arrays.binarySearch(titleA,title);

                         int indexOf= ArrayUtils.indexOf( titleA, title);
                         indexList.add(indexOf);
                     }
                 }

             }

       //     System.out.println(propIdentity.getKeyList().get(i).toString());
        }


        Integer[] indexs = indexList.toArray(new Integer[0]);
        int len = indexs.length;

        //找出列
        List<String[]> list = new ArrayList<String[]>();
        for(int j=0;j<len+1;j++){
            String[] tmp = new String[lines.size()];
            for(int i=0;i<lines.size()-1;i++){
                String line[] = lines.get(i+1);
                if(j<len){
                    tmp[i] = line[indexs[j]];//取索引
                }else {
                    tmp[i] = line[line.length-1];//取最后一行type值
                }
            }
            list.add(tmp);
        }






        //java8新特性　去除不必要的元素
        identityList.removeIf(s -> s.toString().indexOf("_x")>-1
                ||s.toString().indexOf("_y")>-1
                ||s.toString().indexOf("_z")>-1);

        List identityOper = identityList;

        identityList.addAll(1,identityListTmp);
        //System.out.println(identityList);

        Object[] title = identityList.toArray(new String[0]);
        // 输出String数组及标题
        String [] strs=new String[title.length];
        for (int i = 0; i < title.length; i++) {
            strs[i] = title[i].toString();
        }

        //输出数据
        String[] para  = new String [title.length];
        //输出方差均值最小最大
        for(int ii = 0;ii<len; ii++){
            String[] tmp = list.get(ii);
            para[4*(ii+1)-3] = getMax(tmp)+"";
            para[4*(ii+1)-2] = getMin(tmp)+"";
            para[4*(ii+1)-1] = getLoss(tmp)+"";
            para[4*(ii+1)] = getAver(tmp)+"";
        }

        //其余的值进行赋值

        int len2 =title.length-4*len-1;
        System.out.println("len2=="+len2);
        operationHandler(lines,len2,identityOper,propIdentity);
        for(int jj=0;jj<len2;jj++){
            para[4*len+jj+1]="0";
        }

        csvWriter.writeNext(strs);
        csvWriter.writeNext(para);
        csvWriter.close();
        //保存每一笔汇款数据的list


    }

    public static String getMax(String[] cs){
        BigDecimal max = new BigDecimal(cs[0]);

        for(int i=0;i<cs.length-1;i++){
            try{
                BigDecimal max2 = new BigDecimal(cs[i+1]);
                if(max2.compareTo(max)>0){
                    max = max2;
                }
            }catch (Exception e){

            }

        }
        return max.toString();
    }

    public static String getMin(String[] cs){
        BigDecimal min = new BigDecimal(cs[0]);

        for(int i=0;i<cs.length-1;i++){
            try {
                BigDecimal min2 = new BigDecimal(cs[i+1]);
                if(min2.compareTo(min)<0&&min2!=null&&min!=null){
                    min = min2;
                }
            } catch (Exception e) {

            }
        }
        return min.toString();
    }

    public static String getAver(String[] cs){
        int m=cs.length;
        BigDecimal m2 = new BigDecimal(String.valueOf(m));
        BigDecimal m3 = new BigDecimal("100");
        BigDecimal sum = new BigDecimal("0");
        try {
            for(int i=0;i<m;i++){//求和
                sum = sum.add( new BigDecimal(cs[i]));
            }
        } catch (Exception e) {

        }
//        System.out.println("sum=="+sum.toString());
//        System.out.println("m2=="+m2.toString());
        BigDecimal aver = sum.divide(m2,9,RoundingMode.HALF_UP);
//        System.out.println("aver=="+aver);

        return aver.toString();
    }

    public static  String getLoss(String[] cs){
//        double 1loss = 0;
        BigDecimal loss = new BigDecimal("0");
        //double aver=getAver(cs);
        BigDecimal aver = new BigDecimal(getAver(cs));
        //double dVar=0;
        BigDecimal dVar = new BigDecimal("0");
      //  System.out.println("aver=="+aver);
        for(int i=0;i<cs.length;i++){//求方差
            try {
                dVar = dVar.add((new BigDecimal(cs[i]).subtract(aver)).multiply(new BigDecimal(cs[i]).subtract(aver)));
//            dVar+=(Double.parseDouble(cs[i])-aver)*(Double.parseDouble(cs[i])-aver);
            //    System.out.println(cs[i]+"=="+cs[i]);
            //    System.out.println("String key"+dVar);
            } catch (Exception e) {

            }
        }
//        loss = dVar/cs.length;
        loss = dVar.divide(new BigDecimal(cs.length),9, RoundingMode.HALF_UP);
    //    System.out.println("loss=="+loss);
        return loss.toString();
    }


    public static List<String[]>  getContent(String uploadPath) throws IOException{
        Reader reader = new FileReader(uploadPath);
        CSVReader csvReader= new CSVReader(reader);
        //所有的原始数据
        List<String[]> lines =  csvReader.readAll();
        return lines;
    }


    public static void operationHandler(List<String[]> lines,int length,List identityOper,LinkedProperties propIdentity){
        String[] operationTotal = new String[length];
        ArrayList<String> dataRemitTime = new ArrayList<String> ();//取时间
        ArrayList<String>  dataRemitType = new ArrayList<String> ();//取类型
        for(int i=1;i<lines.size();i++) {
            String[] line = lines.get(i);
            if(!"-1".equals(line[line.length-1])) {
                dataRemitTime.add(line[0]);
                dataRemitType.add(line[line.length - 1]);
            }
        }
        System.out.println(dataRemitTime);
        System.out.println(dataRemitType);
        for(Object i :identityOper){

            propIdentity.getProperty(i.toString());
            System.out.println(propIdentity.getProperty(i.toString()));
        }

        for (int i = 0; i <length; i++) {
            operationTotal[i] = "0";

        }
    }


}
