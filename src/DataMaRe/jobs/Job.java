package DataMaRe.jobs;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Job {
    private String name;
    private Future<Object> returnValue;
    private ExecutorService service = Executors.newCachedThreadPool();

    public Job(String name, DataMaReProcess job, DataMaRe dataMaRe) {
        this.name = name;
        job.setDataMaRe(dataMaRe);
        returnValue = service.submit(job);
    }

    String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        try {
            returnValue.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public Object getReturnValue() {
        try {
            return returnValue.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public void kill() {
        this.service.shutdown();
    }

}
