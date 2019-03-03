package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

import static DataMaRe.data.DataMaRe.format;

public abstract class DataMaReProcess_Edit extends DataMaReProcess {
    private String prefix;

    /**
     * Override constructor for a process that returns an edited datamae
     */
    public DataMaReProcess_Edit() {
        this.prefix = DataMaRe.prefix.replace("\n", "");
    }

    /**
     * Get edited datamare
     * @return edited datamare
     */
    @Override
    public DataMaRe getResult() {
        return (DataMaRe) super.getResult();
    }

    /**
     * Remove a row from datamare
     * @param row index of row to remove
     */
    protected void Remove(int row) {
        System.err.println(dataMaRe.getRecord(row).view(prefix, format, row));
        dataMaRe.removeRecord(row);
    }
}
