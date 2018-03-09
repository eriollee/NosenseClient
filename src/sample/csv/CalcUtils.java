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

import static java.util.stream.Collectors.toList;
import static sample.csv.FileUtils.getPath;

public class CalcUtils {


    public static ArrayList<String> avg000Tmp = new ArrayList<String>();
    public static ArrayList<String> avg010Tmp = new ArrayList<String>();
    public static ArrayList<String> avg020Tmp = new ArrayList<String>();
    public static ArrayList<String> avg100Tmp = new ArrayList<String>();
    public static ArrayList<String> avg110Tmp = new ArrayList<String>();
    public static ArrayList<String> avg120Tmp = new ArrayList<String>();
    public static ArrayList<String> avg201Tmp = new ArrayList<String>();
    public static ArrayList<String> avg301Tmp = new ArrayList<String>();
    public static ArrayList<String> avg401Tmp = new ArrayList<String>();


    public static int Tmp200 = 0;
    public static int Tmp300 = 0;
    public static int Tmp400 = 0;

    public static int totalIndexPostion = 0;//总偏移量



    public static void main(String[] args) throws IOException {
        dataProcess("01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_iPhone 7 Plus_20180212222815_A.csv");
    }

    public static void dataProcess(String CanonicalPath) throws IOException {
        List<String> indexAll = new  ArrayList<String>();//标题
        System.out.println("file=="+CanonicalPath);
        //新建3文件，讲数据保存到3中
        String filePath2 = getPath()+ "_ALL.csv";
        File retFile = new File(filePath2);
        boolean isFirstWrite = true;
        if (!retFile.exists()){
            isFirstWrite = true;
        }else {
            isFirstWrite = false;
        }
        List<String[]>  dataLines  = new ArrayList<>();
        //读取原数据
        if(!isFirstWrite){
              dataLines =  getContent(filePath2);
        }




       // String recordPath = FileUtils.getPath()+ "\\data\\01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_01F21F5D-9622-4F0F-B0D3-8ECC73715CB6_iPhone 7 Plus_20180212182520_A.csv";
        String recordPath = getPath()+ "\\total\\"+CanonicalPath;

        //所有的原始数据
        List<String[]> lines =  getContent(recordPath);

        String IME = getIMEName(recordPath);
        boolean isIPhone = isIPhone(recordPath);

        LinkedProperties propIdentity = PropUtils.loadLinkedProperties("identity.properties");
        ArrayList identityList = new ArrayList();
        identityList = propIdentity.getKeyList();
        int addIndexPosition = 0;//新增标题的位移
        int existIndexPosition = 0;//存量标题的位移

        List indexAllTmp = new ArrayList();

        //获取到存量数据的标题
        if(!isFirstWrite){
            String[] head =  CalcUtils.getHead(filePath2);
            indexAllTmp = Arrays.asList(head);
            indexAll = new ArrayList(indexAllTmp);
            indexAllTmp = new ArrayList(indexAllTmp);
            indexAll.subList(indexAll.size()-21,indexAll.size()).clear();
            indexAll.remove(0);
        }


        //获取indexHashMap
        HashMap<Integer,List> indexHashMap = new  HashMap<Integer,List>();
        //获取到原始数据的标题
        String[] titleA = lines.get(0);
        for(int i=0; i<titleA.length;i++){
            titleA[i] =titleA[i].trim();
            if(isFirstWrite){
                if(!"time".equals(titleA[i])&&!"type".equals(titleA[i])){
                    indexAll.add(titleA[i] );
                }
            }else {
                //如果没有重复的标题,则找出索引
                if(!"time".equals(titleA[i])
                        &&!"type".equals(titleA[i])
                        &&(indexAll.contains(titleA[i]+"_max")
                        ||indexAll.contains(titleA[i]+"_min")
                        ||indexAll.contains(titleA[i]+"_avg")
                        ||indexAll.contains(titleA[i]+"_loss"))){
                   // System.out.println("titleA["+i+"]=="+titleA[i]);
                    int max = indexAllTmp.indexOf(titleA[i]+"_max");
                    int min = indexAllTmp.indexOf(titleA[i]+"_min");
                    int avg = indexAllTmp.indexOf(titleA[i]+"_avg");
                    int loss = indexAllTmp.indexOf(titleA[i]+"_loss");


                    indexHashMap.put(i,new ArrayList<Integer>()
                    {
                        {
                        add(max);
                        add(min);
                        add(avg);
                        add(loss);
                        }
                    });

                    existIndexPosition += 4;



                }

                //如果没有重复的标题,则增加标题
                if(!"time".equals(titleA[i])
                        &&!"type".equals(titleA[i])
                        &&(!indexAll.contains(titleA[i]+"_max")
                        ||!indexAll.contains(titleA[i]+"_min")
                        ||!indexAll.contains(titleA[i]+"_avg")
                        ||!indexAll.contains(titleA[i]+"_loss"))){
                    indexAll.add(titleA[i] );
                    addIndexPosition += 4;
                }

            }

        }


        //增加标题即索引
        List identityListTmp = new ArrayList ();
        //System.out.println(indexAll);
        for (int i = 0; i < indexAll.size();i++) {
            if(isFirstWrite){
                identityListTmp.add(indexAll.get(i)+"_max");
                identityListTmp.add(indexAll.get(i)+"_min");
                identityListTmp.add(indexAll.get(i)+"_avg");
                identityListTmp.add(indexAll.get(i)+"_loss");
            }else{
                if(indexAll.get(i).indexOf("_max")==-1&&indexAll.get(i).indexOf("_min")==-1&&indexAll.get(i).indexOf("_avg")==-1&&indexAll.get(i).indexOf("_loss")==-1){
                    identityListTmp.add(indexAll.get(i)+"_max");
                    identityListTmp.add(indexAll.get(i)+"_min");
                    identityListTmp.add(indexAll.get(i)+"_avg");
                    identityListTmp.add(indexAll.get(i)+"_loss");
                }else {
                    identityListTmp.add(indexAll.get(i));
                }
            }

        }


        //java8新特性　去除不必要的元素
        identityList.removeIf(s -> s.toString().indexOf("_x")>-1
                ||s.toString().indexOf("_y")>-1
                ||s.toString().indexOf("_z")>-1);

        //获取操作目录
        List identityOper = new ArrayList();

        identityOper.addAll(identityList);

        identityList.addAll(1,identityListTmp);

        int[] indexs = new int[identityList.size()];
        for (int i = 0; i < indexs.length; i++) {
            indexs[i] = -1;
        }//数组初始化为-1

        int len = indexs.length;
        //找出列
        List<String[]> list = new ArrayList<String[]>();
        for(int j=1;j<titleA.length-2+1;j++){
            String[] tmp = new String[lines.size()];
            for(int i=0;i<lines.size()-1;i++){
                String line[] = lines.get(i+1);
                if(j<len){
                    try {
                        tmp[i] = line[j];//取索引
                    } catch (Exception e) {
                        tmp[i] = "0";
                    }
                }else {
                    tmp[i] = line[line.length-1];//取最后一行type值
                }
            }
            list.add(tmp);
        }

        Object[] title = identityList.toArray(new String[0]);
        // 输出String数组及标题
            String [] strs=new String[title.length];
            for (int i = 0; i < title.length; i++) {
                strs[i] = title[i].toString();
            }

        //计算偏移量
     //   System.out.println(indexHashMap);

        int indexPosition = 0;
        if(!isFirstWrite){
            if(addIndexPosition !=0){
                indexPosition = title.length-1-21-addIndexPosition;//偏移量=总长-固定type-增加的偏移量-存量偏移
          //      totalIndexPostion = indexPosition;
            }
        }
    //      System.out.println("indexPosition=="+indexPosition);
        //输出数据
        String[] para  = new String [title.length];
        Arrays.fill(para,"0");
        para[0] =IME;
        //输出方差均值最小最大
        int indexTmp = 0;//临时移位变量
        for(int ii = 0;ii<titleA.length-2; ii++){
            String[] tmp = list.get(ii);
            if(indexHashMap.containsKey(ii)){
                para[Integer.parseInt(String.valueOf(indexHashMap.get(ii).get(0)))] = getMax(list.get(ii-1))+"";
                para[Integer.parseInt(String.valueOf(indexHashMap.get(ii).get(1)))] = getMin(list.get(ii-1))+"";
                para[Integer.parseInt(String.valueOf(indexHashMap.get(ii).get(2)))] = getAver(list.get(ii-1))+"";
                para[Integer.parseInt(String.valueOf(indexHashMap.get(ii).get(3)))] = getLoss(list.get(ii-1))+"";
                indexTmp += 4;
            }else {
                para[4*(ii+1)-3+indexPosition-indexTmp] = getMax(tmp)+"";
                para[4*(ii+1)-2+indexPosition-indexTmp] = getMin(tmp)+"";
                para[4*(ii+1)-1+indexPosition-indexTmp] = getAver(tmp)+"";
                para[4*(ii+1)+indexPosition-indexTmp] = getLoss(tmp)+"";
            }

        }

        //其余的type值进行赋值
        int len2 =21;
        String[] operation =  operationHandler(lines,identityOper,propIdentity);
        for(int jj=0;jj<len2;jj++){
            para[len-len2+jj] =operation[jj];
        }

//        if(isFirstWrite){
        //先写入title
             Writer writer = new FileWriter(filePath2);
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(strs);
//        }
        if(!isFirstWrite){
            for (int i = 0; i < dataLines.size(); i++) {
                if(i != 0){
                    String[] data =dataLines.get(i);
                    String[] dataTemp = new String[para.length];
                    Arrays.fill(dataTemp,"0");
                    //dataTemp[0] = data[0];
                    //复制传感器的值到新数组
                    System.arraycopy(Arrays.copyOfRange(data, 0, data.length-21),0,dataTemp,0,data.length-21);
                    //复制type值到新数组
                    System.arraycopy(Arrays.copyOfRange(data, data.length-21, data.length),0,dataTemp,dataTemp.length-21,21);
                    csvWriter.writeNext(dataTemp);
                }
            }
        }

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

        BigDecimal aver = null;
        try {
            aver = sum.divide(m2,9, RoundingMode.HALF_UP);
        } catch (Exception e) {
            aver = new BigDecimal("0");
        }

        return aver.toString();
    }

    public static  String getLoss(String[] cs){
        BigDecimal loss = new BigDecimal("0");
        BigDecimal aver = new BigDecimal(getAver(cs));
        BigDecimal dVar = new BigDecimal("0");
        for(int i=0;i<cs.length;i++){//求方差
            try {
                dVar = dVar.add((new BigDecimal(cs[i]).subtract(aver)).multiply(new BigDecimal(cs[i]).subtract(aver)));
            } catch (Exception e) {

            }
        }

        try {
            loss = dVar.divide(new BigDecimal(cs.length),9, RoundingMode.HALF_UP);
        } catch (Exception e) {
            loss = new BigDecimal("0");
        }

        return loss.toString();
    }


    public static List<String[]>  getContent(String uploadPath) throws IOException{
        Reader reader = new FileReader(uploadPath);
        CSVReader csvReader= new CSVReader(reader);
        //所有的原始数据
        List<String[]> lines =  csvReader.readAll();
        return lines;
    }

    public static String[]  getHead(String uploadPath) throws IOException{

        Reader reader = new FileReader(uploadPath);
        CSVReader csvReader= new CSVReader(reader);
        String [] header = csvReader.readNext();   //用readnext读取之后就不存在于stream中了
        return header;
    }

    public static String getIMEName(String uploadPath) throws IOException{
        String name = "";
        File file = new File(uploadPath);
        String fileName = file.getName();
        String[] s = fileName.split("_");
        name = s[0];
        return name;
    }

    public static boolean isIPhone(String uploadPath) {
        String name = "";
        File file = new File(uploadPath);
        String fileName = file.getName();
        if(file.getName().indexOf("iPhone")>-1){
            return  true;
        }else {
            return  false;
        }
    }


    /**
     *
     * @param lines 表格中的值
     * @param identityOper 对应目录
     * @param propIdentity  目录对应的值
     */
    public static String[] operationHandler(List<String[]> lines,List identityOper,LinkedProperties propIdentity){
        String[] operationTotal = new String[identityOper.size()-1];//转换临时存储
        ArrayList<String> dataRemitTime = new ArrayList<String> ();//取时间
        ArrayList<String>  dataRemitType = new ArrayList<String> ();//取类型
        HashMap<String,String> identityMap = new HashMap<String,String>();//目录与操作的映射
        ArrayList amountSpeed = new ArrayList();
        ArrayList timeTmp = new ArrayList();//时间差计算
        String operationTmp = "";//操作临时存储
        String operationTmpPreType = "";//上一次操作临时存储
        String operationTmpPreTime = "";//上一次操作临时存储

        String[] operationTimeTmp = new String[1];//整体时长临时存储

        ArrayList<String>  operationTimeListTotalTmp = new ArrayList<String> ();//整体时长临时存储链表合计
        ArrayList  operationTimeListTmp = new ArrayList();//整体时长临时存储链表

        boolean isSameOperation = true; //是否相同操作

        for(int i=1;i<lines.size();i++) {

            String[] line = lines.get(i);
            if(!"-1".equals(line[line.length-1])) {
                dataRemitTime.add(line[0]);
                dataRemitType.add(line[line.length - 1]);
                //获取操作行为
                if(!identityMap.containsKey(line[line.length - 1])){
                    identityMap.put(line[line.length - 1],"1");
                }
              //  System.out.println(line[line.length - 1]);
                //收集操作时间
                if("".equals(operationTmp)){
                    if((("0|1|2|10|11|20|30|40").indexOf(line[line.length - 1]))>-1&&!"".equals(line[line.length - 1])) {
                                operationTmp = line[line.length - 1];
                                if(operationTmpPreType.equals(operationTmp)){
                                      timeTmp.add(operationTmpPreTime);//获取上一次操作时间
                                }
                                timeTmp.add(line[0]);//获取第一次操作时间
                    }
                }else{
                    if(operationTmp.equals(line[line.length - 1])){
                        if((("0|1|2|10|11|20|30|40").indexOf(line[line.length - 1]))>-1&&!"".equals(line[line.length - 1])){
                            timeTmp.add(line[0]);//获取下一次操作时间
                        }
                    }else {
                        if(timeTmp != null){
                    //        System.out.println(timeTmp+","+operationTmp);
                            calQuality(timeTmp,operationTmp,identityMap);//计算数量
                            calAVGTime(timeTmp,operationTmp,identityMap);//计算操作时间
                        }
                       timeTmp = new ArrayList();//清空时间差计算
                        operationTmpPreType = line[line.length - 1];
                        operationTmpPreTime = line[0];
                        operationTmp = ""; //清空初始化
                    }


                }

                //计算总时间
                if("10".equals(line[line.length - 1])&&isSameOperation==true){
                    operationTimeListTmp.add(line[0]);//获取起始操作时间
                    isSameOperation = false;
                   // System.out.println("line=="+line[0]);
                }

                if("12".equals(line[line.length - 1])){
                    operationTimeListTmp.add(line[0]);//获取终止操作时间
                    isSameOperation = true;
                    if(operationTimeListTmp.size()>1){
                        operationTimeListTotalTmp.add(String.valueOf(Long.parseLong((String)operationTimeListTmp.get(1))-Long.parseLong((String)operationTimeListTmp.get(0))));
                    }

                    operationTimeListTmp.clear();
                }

               // System.out.println(operationTimeListTotalTmp);


            }

        }
        setTimeTotal(operationTimeListTotalTmp,identityMap);


        for (int i = 1; i < identityOper.size(); i++) {
            operationTotal[i-1] =  propIdentity.getProperty((String)identityOper.get(i));
            if(identityMap.containsKey(propIdentity.getProperty((String)identityOper.get(i)))){//如果含有key则置为key值
                operationTotal[i-1] = identityMap.get(propIdentity.getProperty((String)identityOper.get(i)));
            }else {
                operationTotal[i-1] = "0";
            }
        }
        clearCatch();//清空缓存
        return  operationTotal;
    }

    private static void clearCatch() {
        avg010Tmp = new ArrayList<String>();
        avg020Tmp = new ArrayList<String>();
        avg100Tmp = new ArrayList<String>();
        avg110Tmp = new ArrayList<String>();
        avg120Tmp = new ArrayList<String>();
        avg201Tmp = new ArrayList<String>();
        avg301Tmp = new ArrayList<String>();
        avg401Tmp = new ArrayList<String>();
        Tmp200 = 0;
        Tmp300 = 0;
        Tmp400 = 0;
    }

    /**
     *
     * @param operationTimeListTotalTmp 总体时长
     * @param identityMap 赋值
     * @return
     */
    private static HashMap setTimeTotal(ArrayList<String> operationTimeListTotalTmp, HashMap<String, String> identityMap) {
        identityMap.put("120",getAver(operationTimeListTotalTmp.toArray(new String[0])));
        return identityMap;
    }

    private static HashMap calQuality(ArrayList timeTmp, String index, HashMap<String, String> identityMap) {
        if("20".equals(index)){
            Tmp200 += timeTmp.size();
            identityMap.put(index+"B",String.valueOf(Tmp200));
        }
        if("30".equals(index)){
            Tmp300 += timeTmp.size();
            identityMap.put(index+"B",String.valueOf(Tmp300));
        }
        if("40".equals(index)){
            Tmp400 += timeTmp.size();
            identityMap.put(index+"B",String.valueOf(Tmp400));
        }
        return identityMap;
    }

    private static HashMap calAVGTime(ArrayList timeTmp,String index,HashMap identityMap) {
        if(timeTmp.size()>1){
            String remitAmountAVG = round(Double.parseDouble(String.valueOf(Long.parseLong((String)timeTmp.get(timeTmp.size()-1))-Long.parseLong((String)timeTmp.get(0)))),
                    Double.parseDouble(String.valueOf(timeTmp.size()))
                    ,3);
            if("0".equals(index)){
                setTime(index,avg000Tmp,identityMap,remitAmountAVG);
            }
            if("1".equals(index)){
                setTime(index,avg010Tmp,identityMap,remitAmountAVG);
            }
            if("2".equals(index)){
                setTime(index,avg020Tmp,identityMap,remitAmountAVG);
            }
            if("10".equals(index)){
                setTime(index,avg100Tmp,identityMap,remitAmountAVG);
            }
            if("11".equals(index)){
                setTime(index,avg110Tmp,identityMap,remitAmountAVG);
            }
            if("12".equals(index)){
                setTime(index,avg120Tmp,identityMap,remitAmountAVG);
            }
            if("20".equals(index)){
                setTime(index,avg201Tmp,identityMap,remitAmountAVG);
            }
            if("30".equals(index)){
                setTime(index,avg301Tmp,identityMap,remitAmountAVG);
            }
            if("40".equals(index)){
                setTime(index,avg401Tmp,identityMap,remitAmountAVG);
            }
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

    public static HashMap setTime(String index,ArrayList timeTmp,HashMap identityMap,String remitAmountAVG){
        timeTmp.add(remitAmountAVG);
        //System.out.println(timeTmp);
        if(timeTmp.size()>1){
            BigDecimal end = new BigDecimal(String.valueOf(timeTmp.get(timeTmp.size()-1)));
            BigDecimal start = new BigDecimal(String.valueOf(timeTmp.get(0)));
            identityMap.put(index+"A",round((end.add(start)).doubleValue(),
                    Double.parseDouble(String.valueOf(timeTmp.size()))
                    ,3));

        }else {
            identityMap.put(index+"A",timeTmp.get(0));
        }
        return  identityMap;
    }

}
