package mapreduce.jobs.tasks;

import data.DataMaRe;

public class Sort extends DataMaReProcess {
    private String columnName;
    private int column;
    private int i, j;


    public Sort(DataMaRe dataMaRe, String columnName) {
        super(dataMaRe);
        this.columnName = columnName;
        column = dataMaRe.getColumn(columnName);
    }


    @Override
    void process() {
        super.process();
        // Sort
        for (int i = 0; i < dataMaRe.getRows() - 1; i++) {
            for (int j = 1; j < dataMaRe.getRows(); j++) {
                if (dataMaRe.compare(i, j, column))
                    dataMaRe.swap(i, j);
            }
        }
        finished();
    }

    @Override
    Object finishedResult() {
        return dataMaRe;
    }
}

