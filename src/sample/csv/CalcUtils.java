package sample.csv;

import com.opencsv.CSVWriter;
import sample.properties.LinkedProperties;
import sample.properties.PropUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
        System.out.println();

        for (int i = 0; i < propIdentity.getKeyList().size();i++) {
             if(identityList.get(i).toString().indexOf("_x")>-1
                     ||identityList.get(i).toString().indexOf("_y")>-1
                     ||identityList.get(i).toString().indexOf("_z")>-1){

                 identityListTmp.add(identityList.get(i)+"_max");
                 identityListTmp.add(identityList.get(i)+"_min");
                 identityListTmp.add(identityList.get(i)+"_avg");
                 identityListTmp.add(identityList.get(i)+"_loss");
             }

            System.out.println(propIdentity.getKeyList().get(i).toString());
        }
        //java8新特性
        identityList.removeIf(s -> s.toString().indexOf("_x")>-1
                ||s.toString().indexOf("_y")>-1
                ||s.toString().indexOf("_z")>-1);
        identityList.addAll(1,identityListTmp);

        Object[] title = identityList.toArray(new String[0]);
        // 输出数组
        for (int i = 0; i < title.length; i++) {
           System.out.println("array--> " + title[i]);
        }

        String [] strs=new String[title.length];
        for(int i=0;i<title.length;i++){
            strs[i]=title[i].toString();
        }

        csvWriter.writeNext(strs);
        csvWriter.close();
        //保存每一笔汇款数据的list

//        for(int k = 0 ; k < timeIndexotal.size();k++){
//            List<String[]> list = new ArrayList<String[]>();
//            int indexLenth = timeIndexTotal.get(k)[1]-timeIndexTotal.get(k)[0]+1;//设置索引长度
//            for(int j = 0;j<len;j++){
//                //某一列的数据
//                String[] tmp = new String[indexLenth];
//                for(int i=0;i<indexLenth;i++){
//                    //每一行的数据
//
//                    String[] line = lines.get(timeIndexTotal.get(k)[0]+i-1);
//                    tmp[i] = line[indexs[j]]; //保存具体数据
//
//                }
//                list.add(tmp);
//            }
//
//            //获取到原始数据的标题
//            String[] title = lines.get(0);
//
//            //titles用来保存标题
//            String[] titles = new String[len*4+1];
//            System.out.println("titles=="+titles.length);
//            titles[0] = "id";
//
//
//
//
//            //para保存所有计算之后的数据
//            String[] para = new String[len*4+1];
//            para[0] = name;
//
//
//            System.out.println(111);
//            for(int ii = 0;ii<len; ii++){
//                String[] tmp = list.get(ii);
//                para[ii+1] = getMax(tmp)+"";
//                para[ii+len+1] = getMin(tmp)+"";
//                para[ii+len*2+1] = getLoss(tmp)+"";
//                para[ii+len*3+1] = getAver(tmp)+"";
//
//                //设置title
//                titles[ii+1] = title[indexs[ii]].replace(" ","")+"_max";
//                titles[ii+len+1] = title[indexs[ii]].replace(" ","")+"_min";
//                titles[ii+len*2+1] = title[indexs[ii]].replace(" ","")+"_loss";
//                titles[ii+len*3+1] = title[indexs[ii]].replace(" ","")+"_avg";
//
//
//            }
//            if(k==0){
//                csvWriter.writeNext(titles);//写入标题
//            }
//
//            csvWriter.writeNext(para);

//        }
    }

    public String getMax(String[] cs){
        BigDecimal max = new BigDecimal(cs[0]);

        for(int i=0;i<cs.length-1;i++){
            BigDecimal max2 = new BigDecimal(cs[i+1]);
            if(max2.compareTo(max)>0){
                max = max2;
            }
        }
        return max.toString();
    }

    public String getMin(String[] cs){
        BigDecimal min = new BigDecimal(cs[0]);

        for(int i=0;i<cs.length-1;i++){
            BigDecimal min2 = new BigDecimal(cs[i+1]);
            if(min2.compareTo(min)<0){
                min = min2;
            }
        }
        return min.toString();
    }

    public String getAver(String[] cs){
        int m=cs.length;
        BigDecimal m2 = new BigDecimal(String.valueOf(m));
        BigDecimal m3 = new BigDecimal("100");
        BigDecimal sum = new BigDecimal("0");
        for(int i=0;i<m;i++){//求和
            sum = sum.add( new BigDecimal(cs[i]));
        }
//        System.out.println("sum=="+sum.toString());
//        System.out.println("m2=="+m2.toString());
        BigDecimal aver = sum.divide(m2,9,RoundingMode.HALF_UP);
//        System.out.println("aver=="+aver);

        return aver.toString();
    }

    public String getLoss(String[] cs){
//        double 1loss = 0;
        BigDecimal loss = new BigDecimal("0");
        //double aver=getAver(cs);
        BigDecimal aver = new BigDecimal(getAver(cs));
        //double dVar=0;
        BigDecimal dVar = new BigDecimal("0");
        System.out.println("aver=="+aver);
        for(int i=0;i<cs.length;i++){//求方差
            dVar = dVar.add((new BigDecimal(cs[i]).subtract(aver)).multiply(new BigDecimal(cs[i]).subtract(aver)));
//            dVar+=(Double.parseDouble(cs[i])-aver)*(Double.parseDouble(cs[i])-aver);
            System.out.println(cs[i]+"=="+cs[i]);
            System.out.println("dVar=="+dVar);
        }
//        loss = dVar/cs.length;
        loss = dVar.divide(new BigDecimal(cs.length),9, RoundingMode.HALF_UP);
        System.out.println("loss=="+loss);
        return loss.toString();
    }

}
