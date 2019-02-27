package DataMaRe.jobs;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess;

import java.util.ArrayList;

public class JobList {

    private ArrayList<Job> joblist;

    public JobList() {
        joblist = new ArrayList<>();
    }

    public void addJob(String name, DataMaReProcess job, DataMaRe dataMaRe) {
        if (getJob(name) == null)
            joblist.add(new Job(name, job, dataMaRe));
    }

    public void removeJob(String name) {
        joblist.remove(getJob(name));
    }

    public Job getJob(String name) {
        for (Job job : joblist)
            if (job.getName().equals(name))
                return job;
        return null;
    }

    public Object awaitJobResult(String name) {
        while (getJob(name).getJob().getResult() == null) ;
        return getJob(name).getJob().getResult();
    }
}
