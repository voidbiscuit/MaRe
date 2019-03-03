package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class DataMaReProcess implements Callable<Object> {
    // Data
    protected DataMaRe dataMaRe;

    protected DataMaReProcess() {

    }

    public void setDataMaRe(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
    }

    protected void process() {

    }

    protected Object getResult() {
        return dataMaRe;
    }

    @Override
    public Object call() throws Exception {
        //System.err.println("Process Started");
        process();
        //System.err.println("Process Ended");
        return getResult();
    }
}
