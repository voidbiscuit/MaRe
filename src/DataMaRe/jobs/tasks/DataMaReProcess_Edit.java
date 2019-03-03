package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

import static DataMaRe.data.DataMaRe.format;

public abstract class DataMaReProcess_Edit extends DataMaReProcess {
    private String prefix;

    public DataMaReProcess_Edit() {
        this.prefix = DataMaRe.prefix.replace("\n", "");
    }


    @Override
    public DataMaRe getResult() {
        return (DataMaRe) super.getResult();
    }

    protected void Remove(int row) {
        System.err.println(dataMaRe.getRecord(row).view(prefix, format, row));
        dataMaRe.removeRecord(row);
    }
}
