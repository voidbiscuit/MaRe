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
        // Data is an array of recordss
        headers = data.remove(0).split(regex_split); // The first row is Column Names
        rows = data.size(); // Rows is the number of record
        columns = headers.length; // Number of Columns is number of Column Names
        this.data = new ArrayList<>();
        for (String column : headers)
            this.data.add(new Series(column));
        String[] fragments;
        for (String record : data) {
            fragments = record.split(regex_split);
            for (int i = 0; i < fragments.length; i++)
                this.data.get(i).add(fragments[i]);
        }
    }

    public void displayData() {
        displayData(rows);
    }

    public void displayData(int count) {
        displayData(0, count);
    }

    public void displayData(int start, int end) {
        // If the user is stupid, return
        if (start >= end)
            return;
        // Set the start and end values to sensible
        if (start < 0) start = 0;
        if (end < 0) return;
        if (start > rows - 1) return;
        if (end < rows - 1) end = rows - 1;

        // Prepare output
        StringBuilder output = new StringBuilder();
        // Metadata
        output.append(String.format("" +
                        "\n-- %s" + // -- Filename
                        "\n%d records" // x records
                , file_name, data.size()
        ));
        // Column Headers
        output.append("\n--------.");
        for (int column = 0; column < columns; column++)
            output.append(String.format("%20s |", data.get(column).header()));
        // Column Datatypes
        output.append("\n\t\t|");
        for (int column = 0; column < columns; column++)
            output.append(String.format("%20s |", data.get(column).getType()));
        // Spacer
        output.append("\n\t\t|");
        // Print Records
        for (int record = start; record < end; record++) {
            // New Record
            output.append(String.format("\n%4d\t|", record + 1));
            // Print each column 20 wide
            for (int column = 0; column < columns; column++)
                output.append(String.format("%20s |", data.get(column).getString(record)));
        }
        System.out.print(String.format("%s", output.toString()));
    }


    public void removeNA() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; columns++)
                if (data.get(column).get(row) == null)
                    for (int i = 0; i < columns; i++)
                        data.get(i).set(i, "nopp");
        }
    }

}
