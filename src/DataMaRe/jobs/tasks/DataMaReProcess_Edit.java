package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

public abstract class DataMaReProcess_Edit extends DataMaReProcess {

    public DataMaReProcess_Edit() {
    }

    @Override
    public DataMaRe getResult() {
        return (DataMaRe) super.getResult();
    }
}
