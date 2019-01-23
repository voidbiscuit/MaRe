import data.DataMaRe;
import mapreduce.JobHandler;

public class Main {


    public static void main(String[] args) {
        TestDataMaRe();
    }

    static void TestDataMaRe() {
        FileLoader files = new FileLoader();
        DataMaRe dataMaRe = new DataMaRe(files.getFile(0));
        dataMaRe.displayData(0,10);
    }

    static void TestJobs() {
        JobHandler jobHandler = new JobHandler();
        //jobHandler.testJobs(dataMaRe);
    }

    static void TestDataFrame() {
        FileLoader files = new FileLoader();
        DataMaRe dm = new DataMaRe(files.getFile(0));
        dm.displayData(0, 10);
    }


}
