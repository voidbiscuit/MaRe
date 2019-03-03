package DataMaRe.jobs.tasks.edit;

import DataMaRe.jobs.tasks.DataMaReProcess_Edit;


public class RemoveNULL extends DataMaReProcess_Edit {
    public RemoveNULL() {
        super();
    }

    @Override
    protected void process() {
        super.process();
        System.err.println("Remove NULL");
        for (int row = 0; row < dataMaRe.getRows(); row++)
            for (int column = 0; column < dataMaRe.getColumns(); column++) {
                if (dataMaRe.getRecord(row).get(column).equals("") || dataMaRe.getRecord(row).get(column).equals(0)) {
                    Remove(row--);
                    break;
                }
            }
    }
}
