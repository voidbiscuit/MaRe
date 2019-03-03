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

    /**
     * Create a job with a name, process, and data to act upon
     * @param name name of job
     * @param job process to run
     * @param dataMaRe data to run on
     */
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

    /**
     * Check if the job is finished
     * @return status of job
     */
    public boolean isFinished() {
        try {
            returnValue.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /**
     * Get return value from job
     * @return result
     */
    public Object getReturnValue() {
        try {
            return returnValue.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * kill a job
     */
    public void kill() {
        this.service.shutdown();
    }

}
