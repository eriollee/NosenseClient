package sample.csv;

import java.io.File;
import java.io.IOException;

import static sample.csv.CalcUtils.dataProcess;
import static sample.csv.FileUtils.getPath;
import static sample.csv.MergeUtils.mergeHandler;

public class DataStatics {
    public static void main(String[] args) throws Exception {
       // FileUtils.readfile( getPath()+ "\\data\\",1);
       // mergeHandler();
        File file = new File(getPath()+"//total");


        String[] filelist = file.list();
        for(String s:filelist){
            dataProcess(s);
        }
    }
}
