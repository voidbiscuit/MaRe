package DataMaRe.jobs.tasks.edit;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

public class RemoveNULL extends DataMaReProcess_Edit {
    public RemoveNULL() {
        super();
    }

    @Override
    protected void process() {
        super.process();
        for (int row = 0; row < dataMaRe.getRows(); row++)
            for (int column = 0; column < dataMaRe.getColumns(); column++)
                if (dataMaRe.getRecord(row).get(column) == null)
                    dataMaRe.removeRecord(row);
    }
}
