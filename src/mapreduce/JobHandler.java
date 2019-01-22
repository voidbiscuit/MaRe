package mapreduce;

import data.DataMaRe;
import mapreduce.jobs.JobList;
import mapreduce.jobs.tasks.Reducer;

public class JobHandler {

    // Final
    private static final String
            Tim = "Tim",
            Steven = "Steven";
    // Vars
    private JobList jobList;

    public JobHandler() {
        this.jobList = new JobList();
    }

    public void testJobs(DataMaRe dataMaRe) {
        jobList.addJob(Tim, new Reducer(dataMaRe));
        jobList.addJob(Steven, new Reducer(dataMaRe));
        jobList.startJob(Tim);
        jobList.startJob(Steven);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jobList.stopJob(Tim);
        jobList.stopJob(Steven);
    }
}
