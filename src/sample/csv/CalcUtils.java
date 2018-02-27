package sample.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import sample.properties.LinkedProperties;
import sample.properties.PropUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.apache.commons.lang3.*;

public class CalcUtils {

    public static ArrayList<String> avg000Tmp = new ArrayList<String>();
    public static ArrayList<String> avg010Tmp = new ArrayList<String>();
    public static ArrayList<String> avg020Tmp = new ArrayList<String>();
    public static ArrayList<String> avg100Tmp = new ArrayList<String>();
    public static ArrayList<String> avg110Tmp = new ArrayList<String>();
    public static ArrayList<String> avg201Tmp = new ArrayList<String>();
    public static ArrayList<String> avg301Tmp = new ArrayList<String>();
    public static ArrayList<String> avg401Tmp = new ArrayList<String>();


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

        //获取操作目录
        List identityOper = new ArrayList();
        identityOper.addAll(identityList);

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
        System.out.println(identityOper);
        String[] operation =  operationHandler(lines,identityOper,propIdentity);
        for(int jj=0;jj<len2;jj++){
            para[4*len+jj+1]=operation[jj];
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

    /**
     *
     * @param lines 表格中的值
     * @param identityOper 对应目录
     * @param propIdentity  目录对应的值
     */
    public static String[] operationHandler(List<String[]> lines,List identityOper,LinkedProperties propIdentity){
        String[] operationTotal = new String[identityOper.size()-1];
        ArrayList<String> dataRemitTime = new ArrayList<String> ();//取时间
        ArrayList<String>  dataRemitType = new ArrayList<String> ();//取类型
        HashMap<String,String> identityMap = new HashMap<String,String>();//目录与操作的映射
        ArrayList amountSpeed = new ArrayList();
        ArrayList timeTmp = new ArrayList();//时间差计算
        String operationTmp = "";
        for(int i=1;i<lines.size();i++) {
            String[] line = lines.get(i);
            if(!"-1".equals(line[line.length-1])) {
                dataRemitTime.add(line[0]);
                dataRemitType.add(line[line.length - 1]);
                //获取操作行为
                if(!identityMap.containsKey(line[line.length - 1])){
                    identityMap.put(line[line.length - 1],"1");
                }
//                if("10".equals(line[line.length - 1])){
//                    System.out.println(line[0]);
//                }
                //计算操作时间
                if("".equals(operationTmp)){

                 //   System.out.println("line = "+line[0]);
                    if("30".equals(line[line.length - 1])) {
                        operationTmp = line[line.length - 1];//初始化操作
                        System.out.println("line=="+line[0]);
                        timeTmp.add(line[0]);
                    }
                }else{
                    if(operationTmp.equals(line[line.length - 1])){

                        if("30".equals(line[line.length - 1])){
                       //     System.out.println("line="+line[0]);
                            timeTmp.add(line[0]);
                        }
                    }else {
                        if(timeTmp != null){
//                            for(int j =0;j<timeTmp.size();j++){
//                                System.out.println(line[0]);
//                            }
                            System.out.println(timeTmp);
                            calAVGTime(timeTmp,operationTmp,identityMap);
                        }
                        timeTmp.clear();//清空时间差计算
                        operationTmp = ""; //清空初始化
                    }

                }

//                //计算操作时间
//                if((("00|01|02|10|11|20|30|40").indexOf(line[line.length - 1]))>-1&&!"".equals(line[line.length - 1])){
//                    String[] line2 = lines.get(i+1);
//                    System.out.println(line[line.length - 1]);
//                    System.out.println(line2[line2.length - 1]);
//                    if(line2[line2.length - 1].equals(line[line.length - 1])){
//                        System.out.println("line=="+line2[line2.length - 1]);
//                    }
//                }


            }
        }
//        System.out.println(identityMap);
//        System.out.println(dataRemitTime);
//        System.out.println(dataRemitType);
      //  System.out.println(identityOper.size());
        for(Object i :identityOper){

            //propIdentity.getProperty(i.toString());
           // System.out.println(propIdentity.getProperty(i.toString()));
          //  System.out.println( dataRemitType.contains(propIdentity.getProperty(i.toString())));

        }

        for (int i = 1; i < identityOper.size(); i++) {
           // System.out.println(propIdentity.getProperty((String)identityOper.get(i)));
            operationTotal[i-1] =  propIdentity.getProperty((String)identityOper.get(i));
            if(identityMap.containsKey(propIdentity.getProperty((String)identityOper.get(i)))){//如果含有key则置为key值
                operationTotal[i-1] = identityMap.get(propIdentity.getProperty((String)identityOper.get(i)));
            }else {
                operationTotal[i-1] = "0";
            }
        }

        return  operationTotal;
    }

    private static HashMap calAVGTime(ArrayList timeTmp,String index,HashMap identityMap) {
        if(timeTmp.size()>1){
            System.out.println("index=="+index);
            String remitAmountAVG = round(Double.parseDouble(String.valueOf(Long.parseLong((String)timeTmp.get(timeTmp.size()-1))-Long.parseLong((String)timeTmp.get(0)))),
                    Double.parseDouble(String.valueOf(timeTmp.size()))
                    ,3);
            if("30".equals(index)){
                avg301Tmp.add(remitAmountAVG);
                if(avg301Tmp.size()>1){
                    System.out.println(avg301Tmp);
                    BigDecimal end = new BigDecimal(String.valueOf(avg301Tmp.get(avg301Tmp.size()-1)));
                    BigDecimal start = new BigDecimal(String.valueOf(avg301Tmp.get(0)));
                    identityMap.put("301",round((end.add(start)).doubleValue(),
                            Double.parseDouble(String.valueOf(avg301Tmp.size()))
                            ,3));
                }else {
                    identityMap.put("301",avg301Tmp.get(0));
                }

            }
                System.out.println("avg=="+identityMap);
        }

        return identityMap;
    }
    /**
     * 提供精确的小数位四舍五入处理
     * @param scale 小数点保留几位
     * @return 四舍五入后的结果
     */
    public  static  String round(double numerator,double denominator,int scale){
        BigDecimal a = new BigDecimal(Double.toString(numerator));
        BigDecimal b = new BigDecimal(Double.toString(denominator));
        return String.valueOf(a.divide(b,scale,RoundingMode.HALF_UP));
    }

    public static HashMap setTime(String index,ArrayList timeTmp,HashMap identityMap){
        return  identityMap;
    }

}
