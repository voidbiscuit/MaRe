package DataMaRe.jobs.tasks.mapreduce;

import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.DataMaReProcess_MaRe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Reduce extends DataMaReProcess_MaRe {
    private String column_name;
    private String search_column_name;
    private int search_column;
    private ArrayList<Object> search_values = new ArrayList<>();
    private ArrayList<Integer> count_values = new ArrayList<>();

    public Reduce(String column_name) {
        this.column_name = column_name;
    }

    public Reduce(DataMaRe compare, String search_column_name, String column_name) {
        super();
        this.column_name = column_name;
        setSearchValues(compare, search_column_name);

    }

    private void setSearchValues(DataMaRe compare, String search_column_name) {
        this.search_column_name = search_column_name;
        search_values = new ArrayList<>();
        int count_column = compare.getColumn(search_column_name);
        for (int i = 0; i < compare.getRows(); i++)
            search_values.add(compare.getRecord(i).get(count_column));
        search_values = (ArrayList<Object>) search_values.stream().distinct().collect(Collectors.toList());
        count_values = new ArrayList<>(Collections.nCopies(search_values.size(), 0));
    }

    @Override
    public void setDataMaRe(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
        this.search_column = dataMaRe.getColumn(column_name);
    }


    @Override
    protected void process() {
        super.process();
        if (this.search_values.isEmpty())
            setSearchValues(dataMaRe, column_name);
        System.err.println(String.format("Reduce %s from %s", column_name, search_column_name));
        for (int row = 0; row < dataMaRe.getRows(); row++) {
            for (int search_id = 0; search_id < search_values.size(); search_id++) {
                Object search = search_values.get(search_id);
                if (dataMaRe.getRecord(row).get(search_column).equals((dataMaRe.getType(search_column)).cast(search))) {
                    count_values.set(search_id, count_values.get(search_id) + 1);
                    break;
                }
            }
        }
        // Sort
        for (int i = 0; i < count_values.size(); i++) {
            for (int j = 0; j < count_values.size() - i - 1; j++) {
                if (count_values.get(j) > count_values.get(j + 1)) {
                    Object tempa = search_values.get(j);
                    search_values.set(j, search_values.get(j + 1));
                    search_values.set(j + 1, tempa);
                    int tempb = count_values.get(j);
                    count_values.set(j, count_values.get(j + 1));
                    count_values.set(j + 1, tempb);
                }
            }
        }

        result = new ArrayList[]{
                search_values,
                count_values
        };
    }
}
