package sample.csv;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import static sample.csv.FileUtils.getPath;

public class StaticUtils {
    public static void main(String[] args) throws IOException {
 //       csvStatic();
  //      csvOutNotNull();
        csvOutGreater();

    }

    private static void csvOutGreater() throws IOException {
        List<String[]> dataList =  CalcUtils.getContent(getPath()+ "_Final_NotNull.csv");
        List<String> dataGT10 = new ArrayList<>();
        List<String> dataGT20 = new ArrayList<>();
        List<String> dataGT30  = new ArrayList<>();

        Map<String, Integer> map = new HashMap<>();
        for (String[] data:dataList){
            if("ID".equals(data[0])){
                setHead(dataGT10,data);
                setHead(dataGT20,data);
                setHead(dataGT30,data);
            }
            Integer num = map.get(data[2]);
            map.put(data[2], num == null ? 1 : num + 1);
        }
        map.forEach((k,v)->{
                    if(v>=10){
                        setList(k,dataList,dataGT10);
                        CSVUtils.exportCsv(new File(getPath()+ "_Final_10.csv"),dataGT10);
                    }
                    if(v>=20){
                        setList(k,dataList,dataGT20);
                        CSVUtils.exportCsv(new File(getPath()+ "_Final_20.csv"),dataGT20);
                    }
                    if(v>=30){
                        setList(k,dataList,dataGT30);
                        CSVUtils.exportCsv(new File(getPath()+ "_Final_30.csv"),dataGT30);
                    }
                }
        );
        System.out.println(map);
    }

    private static List setHead(List<String> dataGT,String[] data) {
        dataGT.add(Arrays.toString(data).replace("[","").replace("]",""));
        return dataGT;
    }

    private static List setList(String key,List<String[]> dataList, List<String> dataGT) {
        for (String[] data:dataList){
            if(key.equals(data[2])){
                dataGT.add(Arrays.toString(data).replace("[","").replace("]",""));
            }
        }
        return dataGT;
    }



    private static void csvOutNotNull() throws IOException {
        List<String> dataNotNullList = new ArrayList<>();
        //所有的原始数据
        List<String[]> dataList =  CalcUtils.getContent(getPath()+ "_Final.csv");
        for (String[] data:dataList){
            if(!"".equals(data[0])){
                dataNotNullList.add(Arrays.toString(data).replace("[","").replace("]",""));
            }
        }
        CSVUtils.exportCsv(new File(getPath()+ "_Final_NotNull.csv"),dataNotNullList);
    }

    private static void csvStatic() throws IOException {
        String namePath = getPath()+ "//name.csv";
        String dataPath = getPath()+ "_ALL.csv";
        HashMap<String,String> nameMap = new HashMap<String,String>();
        List<String> nameList = new ArrayList<String>();

         //所有人名的原始数据
        List<String[]> linesName =  CalcUtils.getContent(namePath);
        for(String [] line:linesName){
            if(!"".equals(line[2])){
                nameMap.put(line[0],getPingYin(line[2]));
            }
        }

        //所有的原始数据
        List<String[]> lines =  CalcUtils.getContent(dataPath);
        for(String [] line:lines){
           String name= "" ;
           String ID= "" ;
           String passPort= "" ;
           if(nameMap.get(line[0])!=null){
               name = nameMap.get(line[0]);
               String[] nameArr = name.split("\\,");
               ID = nameArr[1];
               passPort = "'"+nameArr[0];
               name = ID +"," + "\t"+passPort+"," ;
           }else{
               name= ",," ;
            }
            nameList.add(name);
          //  System.out.println(nameList);
        }
        nameList.set(0,"ID,PassPort,");
        List<String> dataList = CSVUtils.importCsv(new File(dataPath));
        List<String> dataListFinal = new ArrayList<>();
        for (int i = 0; i <dataList.size(); i++) {
            dataListFinal.add(nameList.get(i)+dataList.get(i)) ;
        }
        CSVUtils.exportCsv(new File(getPath()+ "_Final.csv"),dataListFinal);
        //System.out.println(nameMap);
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputString 汉字
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String output = "";
        if (inputString != null && inputString.length() > 0
                && !"null".equals(inputString)) {
            char[] input = inputString.trim().toCharArray();
            try {
                for (int i = 0; i < input.length; i++) {
                    if (java.lang.Character.toString(input[i]).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                                input[i], format);
                        output += temp[0];
                    } else
                        output += java.lang.Character.toString(input[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            return "*";
        }
        return output;
    }


}
