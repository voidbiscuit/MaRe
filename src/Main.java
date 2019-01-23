import data.DataMaRe;
import mapreduce.jobs.JobList;
import mapreduce.jobs.tasks.Sort;

public class Main {
    static FileLoader files;
    static DataMaRe dataMaRe;

    public static void main(String[] args) {
        getFile();
        TestDataMaRe();
    }

    static void getFile() {
        files = new FileLoader();
        dataMaRe = new DataMaRe(files.getFile(0));
    }

    static void TestDataMaRe() {
        dataMaRe.displayData();
        JobList jobList = new JobList();
        jobList.addJob("Tim", new Sort(dataMaRe, "Something Else"));
        jobList.getJob("Tim").getJob().run();
        while (jobList.getJob("Tim").getJob().getResult() == null) ;
        dataMaRe = (DataMaRe) jobList.getJob("Tim").getJob().getResult();
        dataMaRe.displayData();
    }


}
