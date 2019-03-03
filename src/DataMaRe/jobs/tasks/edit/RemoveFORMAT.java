package DataMaRe.jobs.tasks.edit;

import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

public class RemoveFORMAT extends DataMaReProcess_Edit {
    String[] formats;

    public RemoveFORMAT(String... formats) {
        super();
        this.formats = formats;
    }

    /**
     * Override process, check the format of the data with Regex
     */
    @Override
    protected void process() {
        super.process();
        System.err.println("Remove FORMAT");
        for (int row = 0; row < dataMaRe.getRows(); row++)
            for (int column = 0; column < dataMaRe.getColumns() && column < formats.length; column++) {
                if (!dataMaRe.getRecord(row).getString(column).matches(formats[column])) {
                    Remove(row--);
                    break;

                }
            }
    }
}
