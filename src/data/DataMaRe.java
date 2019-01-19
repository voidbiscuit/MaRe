package data;

import data.Series.Series;

import java.util.ArrayList;

public class DataMaRe {

    // Constants
    private static final String regex_split = ",";

    // Individual Data
    private String file_name;
    private String[] headers;
    private ArrayList<Series> data;
    // Metadata
    private int rows, columns;

    /**
     * Object to represent a CSV file, loaded in.
     *
     * @param data File Data of CSV
     */
    public DataMaRe(ArrayList<String> data) {
        file_name = data.remove(0);
        loadData(data);
    }

    private void loadData(ArrayList<String> data) {
        // Data is an array of records
        rows = data.size(); // Rows is the number of records
        headers = data.remove(0).split(regex_split); // The first row is Column Names
        columns = headers.length; // Number of Columns is number of Column Names
        this.data = new ArrayList<>();
        for (String column : headers)
            this.data.add(new Series(column, Object.class));
        String[] fragments;
        for (String record : data) {
            fragments = record.split(regex_split);
            if (fragments.length == columns)
                for (int i = 0; i < columns; i++)
                    this.data.get(i);
        }
    }


    public void displayData() {
        int max = 5;
        StringBuilder output = new StringBuilder();
        // Header
        output.append(String.format("\n\n-- %s\n%d records\n", file_name, data.size()));
        // For every stored record
        for (int record = 0; record < max; record++) {
            // Print a new line
            output.append("\n\t|");
            // Print out each fragment, 20 wide
            for (int column = 0; column < columns; column++)
                output.append(String.format("%20s", data.get(record).get(column)));
        }
        System.out.print(String.format("%150s", output.toString()));
    }


}
