package mapreduce.jobs;

import java.util.ArrayList;

public class JobList {
    ArrayList<Job> joblist;

    public JobList() {
        joblist = new ArrayList<>();
    }

    public void addJob(String name, Runnable job) {
        if (getJob(name) == null)
            joblist.add(new Job(name, job));
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

    public void startJob(String name) {
        getJob(name).startJob();
    }

    public void stopJob(String name) {
        getJob(name).stopJob();
    }
}
