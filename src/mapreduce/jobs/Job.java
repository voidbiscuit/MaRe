package mapreduce.jobs;

import mapreduce.jobs.tasks.DataMaReProcess;

public class Job {
    private String name;
    private Thread thread;
    private DataMaReProcess job;

    Job(String name, DataMaReProcess job) {
        this.name = name;
        this.job = job;
        this.thread = new Thread(this.job);
    }

    String getName() {
        return name;
    }

    public DataMaReProcess getJob() {
        return job;
    }

    public void printStatus(String status) {
        System.out.println(String.format("[%s] %s", name, status));
    }
}
