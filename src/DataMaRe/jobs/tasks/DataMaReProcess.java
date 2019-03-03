package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class DataMaReProcess implements Callable<Object> {
    // Data
    protected DataMaRe dataMaRe;

    protected DataMaReProcess() {

    }

    /**
     * Constructor
     * @param dataMaRe datamare to be used
     */
    public void setDataMaRe(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
    }

    /**
     * Super for process
     */
    protected void process() {

    }

    /**
     * Result to return
     * @return result
     */
    protected Object getResult() {
        return dataMaRe;
    }

    /**
     * When the thread is run, call this function, process the thread then return result
     * @return result
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        //System.err.println("Process Started");
        process();
        //System.err.println("Process Ended");
        return getResult();
    }
}
