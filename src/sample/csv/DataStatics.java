package sample.csv;

import java.io.File;
import java.io.IOException;

import static sample.csv.CalcUtils.dataProcess;
import static sample.csv.MergeUtils.mergeHandler;

public class DataStatics {
    public static void main(String[] args) throws Exception {
        mergeHandler();
        File file = new File(FileUtils.getPath()+"//total");
        String[] filelist = file.list();
        for(String s:filelist){
            dataProcess(s);
        }
    }
}
