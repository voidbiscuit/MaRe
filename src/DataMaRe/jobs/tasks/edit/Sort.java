package DataMaRe.jobs.tasks.edit;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

public class Sort extends DataMaReProcess_Edit {
    private String columnName;
    private int column;


    public Sort(String columnName) {
        super();
        this.columnName = columnName;
    }

    @Override
    public void setDataMaRe(DataMaRe dataMaRe) {
        super.setDataMaRe(dataMaRe);
        column = this.dataMaRe.getColumn(columnName);
    }

    @Override
    protected void process() {
        super.process();
        System.err.println("Sort by " + columnName);
        for (int cycle = 0; cycle < dataMaRe.getRows(); cycle++) {
            for (int index = 0; index < dataMaRe.getRows() - cycle; index++) {
                if (dataMaRe.compare(index, index + 1, column))
                    dataMaRe.swap(index, index + 1);
            }
        }
    }

}

