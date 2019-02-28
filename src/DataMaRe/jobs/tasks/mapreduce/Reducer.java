package DataMaRe.jobs.tasks.mapreduce;

import DataMaRe.jobs.tasks.DataMaReProcess;

public class Reducer extends DataMaReProcess {
    private String
            column,
            key;
    private int
            count;

    public Reducer() {
        super();
    }

    public void setKey(String column, String key) {
        this.column = column;
        this.key = key;
    }

    @Override
    protected void process() {
        super.process();
        count = 0;
        for (int record = 0; record < dataMaRe.getRows(); record++) {
            if (dataMaRe.getRecord(record).get(dataMaRe.getColumn(column)) == key)
                count++;
        }
    }


    Object finishedResult() {
        return count;
    }
}
