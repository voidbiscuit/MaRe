package DataMaRe.jobs;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess;

public class Job {
    private String name;
    private DataMaReProcess job;
    private Thread process;

    public Job(String name, DataMaReProcess job, DataMaRe dataMaRe) {
        this.name = name;
        this.job = job;
        this.job.setDataMaRe(dataMaRe);
        process = new Thread(job);
    }

    String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return getJob().getResult() != null;
    }

    public DataMaReProcess getJob() {
        return job;
    }

}
