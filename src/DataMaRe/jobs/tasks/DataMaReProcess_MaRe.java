package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

import java.util.ArrayList;

public abstract class DataMaReProcess_MaRe extends DataMaReProcess {
    // Data
    protected DataMaRe dataMaRe;
    protected ArrayList[] result;

    protected DataMaReProcess_MaRe() {

    }

    public void setDataMaRe(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
    }

    protected void process() {

    }

    protected ArrayList[] getResult() {
        return result;
    }

    /**
     * Override of call, returns an object, not datamare
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        process();
        return getResult();
    }
}
