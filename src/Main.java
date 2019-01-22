import data.DataMaRe;
import mapreduce.JobHandler;

public class Main {


    public static void main(String[] args) {
        TestJobs();
    }

    static void TestJobs() {
        JobHandler jobHandler = new JobHandler();
        FileLoader files = new FileLoader();
        DataMaRe dataMaRe = new DataMaRe(files.getFile(0));
        jobHandler.testJobs(dataMaRe);
    }

    static void TestDataFrame() {
        FileLoader files = new FileLoader();
        DataMaRe dm = new DataMaRe(files.getFile(0));
        dm.displayData();
    }


}
