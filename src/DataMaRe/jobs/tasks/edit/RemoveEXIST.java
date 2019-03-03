package DataMaRe.jobs.tasks.edit;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RemoveEXIST extends DataMaReProcess_Edit {
    private String column_name;
    private int check_column;
    private ArrayList<Object> compare_values;

    /**
     * Remove values if they do not exist
     * @param compare DataMaRe to compare to
     * @param column_name_compare the name of the column to check existence of
     * @param column_name local column to check
     */
    public RemoveEXIST(DataMaRe compare, String column_name_compare, String column_name) {
        super();
        int compare_column = compare.getColumn(column_name_compare);
        this.column_name = column_name;
        compare_values = new ArrayList<>();
        for (int i = 0; i < compare.getRows(); i++)
            compare_values.add(compare.getRecord(i).get(compare_column));
        compare_values = (ArrayList<Object>) compare_values.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Update the datamare, and get the column
     * @param dataMaRe Host Datamare
     */
    @Override
    public void setDataMaRe(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
        this.check_column = dataMaRe.getColumn(column_name);
    }

    /**
     * Override Process - Check if Data should exist
     */
    @Override
    protected void process() {
        super.process();
        System.err.println("Remove EXIST");
        for (int row = 0; row < dataMaRe.getRows(); row++) {
            boolean match = false;
            for (Object compare : compare_values) {
                if (dataMaRe.getRecord(row).get(check_column).equals((dataMaRe.getType(check_column)).cast(compare))) {
                    match = true;
                    break;
                }
            }
            if (!match)
                Remove(row--);

        }
    }
}
