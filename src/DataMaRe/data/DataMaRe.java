package DataMaRe.data;


import DataMaRe.data.Series.Record;
import DataMaRe.jobs.tasks.DataMaReProcess;

import java.util.ArrayList;
import java.util.Arrays;


public class DataMaRe {

    // Constants
    public static final String // Formatting Strings
            regex_split = ",",
            prefix = "\n\t%10s\t|",
            format = "\t%20s |";
    // Individual Data
    private String name;
    private String[] headers;
    private Class[] types;
    private ArrayList<Record> data;
    private ArrayList<DataMaRe_Fragment> fragments;

    /**
     * Constructor for DataMaRe
     * @param name DataMaRe name
     * @param headers Column names
     * @param types Column types
     * @param data Records
     * @param fragments Any current fragments
     */
    DataMaRe(String name, String[] headers, Class[] types, ArrayList<Record> data, ArrayList<DataMaRe_Fragment> fragments) {
        this.name = name;
        this.headers = headers;
        this.types = types;
        this.data = data;
        this.fragments = fragments;
    }

    /**
     * Constructor for DataMaRe
     * @param name DataMaRe name
     * @param headers Column names
     * @param types Column types
     * @param data Records
     */
    public DataMaRe(String name, String[] headers, Class[] types, ArrayList<Record> data) {
        this(name, headers, types, data, new ArrayList<>());
    }


    /**
     * Constructor for DDataMaRe loading from a file
     * @param file_data file data to load including file name, headers, types, and records
     */
    public DataMaRe(String file_data) {
        // Get file contents from each line of file DataMaRe.data
        ArrayList<String> file_contents = new ArrayList<>(Arrays.asList(file_data.split("\\r?\\n")));
        // Load metadata
        name = file_contents.remove(0);
        headers = file_contents.remove(0).split(regex_split);
        types = getTypes(file_contents.remove(0).split(regex_split));
        // Load DataMaRe.data
        data = new ArrayList<>();
        for (String record : file_contents) {
            if (!record.equals(""))
                addRecord(record);
        }
        fragments = new ArrayList<>();
    }

    /**
     * Translate the types of the data from String to Class (from top of file_data)
     * @param types String array of types
     * @return Class array of types
     */
    private Class[] getTypes(String[] types) {
        ArrayList<Class> classTypes = new ArrayList<>();
        for (String type : types)
            classTypes.add(getType(type));
        return classTypes.toArray(new Class[0]);
    }

    /**
     * Get the type of a column
     * @param index Column index
     * @return Class of the specified column
     */
    public Class getType(int index) {
        return types[index];
    }

    /**
     * Check that a record matches the specified types of the headers.
     * @param record Specified record to confirm
     * @return Whether the record matches the data types of each column
     */
    private boolean checkRecordTypes(String[] record) {
        if (!isColumn(record.length - 1)) return false;
        for (int column = 0; column < getColumns(); column++) {
            try {
                if (types[column] == Integer.class) {
                    Integer.parseInt(record[column]);
                }
                if (types[column] == Float.class) {
                    Float.parseFloat(record[column]);
                }
                if (types[column] == String.class) {
                    record[column].toString();
                }
            } catch (NumberFormatException ignore) {
                return false;
            } catch (Error e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Convert from string to class, hardcoded
     * @param type String type
     * @return Class
     */
    private Class getType(String type) {
        type = type.toLowerCase();
        if (type.equals("integer"))
            return Integer.class;
        if (type.equals("float"))
            return Float.class;
        if (type.equals("string"))
            return String.class;
        return Object.class;
    }

    /**
     * Add a Record to the DataMaRe
     * @param record Record to add
     */
    public void addRecord(String record) {
        String[] newRecord = record.split(regex_split);
        if (checkRecordTypes(newRecord))
            data.add(new Record(newRecord, types));

    }

    /**
     * Remove record from the datamare
     * @param record Index of record to delete
     */
    public void removeRecord(int record) {
        if (!isRow(record)) return;
        data.remove(record);
    }

    /**
     * Wait until all fragments have completed
     */
    public void waitForFragments() {
        while (isProcessing()) ;
    }

    /**
     * Wait for processing to finish then display data
     */
    public void displayData() {
        if (isProcessing()) {
            System.err.println("Still Processing");
            return;
        }
        displayData(0, getRows());
    }

    /**
     * Display each record of the DataMaRe formatted.
     * @param start start index of the display
     * @param end end index of the display
     */
    public void displayData(int start, int end) {
        // Wait for DataMaRe to finish processing
        if (isProcessing()) {
            System.err.println("Still Processing");
            return;
        }
        // Build the output
        StringBuilder output = new StringBuilder();
        // File Metadata
        output.append(String.format("\n-- %s", name));
        output.append(String.format("\n#%d records", data.size()));
        output.append(lineFormat(prefix, "Headers", format, headers));
        output.append(lineFormat(prefix, "Types", format, typeNames()));
        // All records
        output.append(view(start, end, prefix, format));
        // Output
        System.out.println(output.toString());
    }

    /**
     * Line Formatting
     * @param rowInfoFormat Format for info
     * @param rowInfoData Info
     * @param lineFormat Format for line data
     * @param lineData Line data
     * @return Formatted string
     */
    private String lineFormat(String rowInfoFormat, String rowInfoData, String lineFormat, String[] lineData) {
        // Build String
        StringBuilder line = new StringBuilder();
        // Add formatted line
        line.append(String.format(rowInfoFormat, rowInfoData));
        // Add each formatted piece of data
        for (String data : lineData)
            line.append(String.format(lineFormat, data));
        // Return built line
        return line.toString();
    }

    /**
     * Check indices are in bounds of the DataMaRe
     * @param start start index
     * @param end end index
     * @return whether in bounds
     */
    private boolean checkBounds(int start, int end) {
        return 0 <= start && start <= end && end < getRows();
    }

    /**
     * View the DataMaRe
     * @param start start index
     * @param end end index
     * @param prefix line prefix
     * @param format data format
     * @return Formatted String
     */
    private String view(int start, int end, String prefix, String format) {
        if (checkBounds(start, end)) return "";
        // Get rows
        StringBuilder records = new StringBuilder();
        for (int record = start; record < end; record++)
            records.append(data.get(record).view(prefix, format, record));
        return records.toString();
    }

    /**
     * Check if a record is legitimate
     * @param index index to check
     * @return whether it's legitimate
     */
    private boolean isRow(int index) {
        return index >= 0 && index < data.size();
    }

    /**
     * Check a value is within column bounds
     * @param index index of column
     * @return whether column index is in bounds
     */
    private boolean isColumn(int index) {
        return index >= 0 && index < headers.length;
    }

    /**
     * Get a Record from the DataMaRe
     * @param index row to get the record from
     * @return the record
     */
    public Record getRecord(int index) {
        if (!isRow(index)) return null;
        return data.get(index);
    }

    /**
     * Get number of rows
     * @return number of rows
     */
    public int getRows() {
        return data.size();
    }

    /**
     * Get column index
     * @param column_name name of column
     * @return index of collumn specified
     */
    public int getColumn(String column_name) {
        for (int header = 0; header < headers.length; header++)
            if (column_name.equals(headers[header])) return header;
        return -1;
    }

    /**
     * Get column name
     * @param column column index
     * @return Name of specified column
     */
    public String getColumn(int column) {
        return headers[column];
    }

    /**
     * Get number of columns
     * @return number of columns
     */
    public int getColumns() {
        return headers.length;
    }

    /**
     * Compare 2 values in the DataMaRe by column values
     * @param indexA index of first compare
     * @param indexB index of second compare
     * @param column column to compare
     * @return true if A is bigger, false if B is bigger
     */
    public boolean compare(int indexA, int indexB, int column) {
        if (!isRow(indexA) || !isRow(indexB)) return false;
        if (!isColumn(column)) return false;
        if (types[column] == Integer.class)
            return (int) data.get(indexA).get(column) > (int) data.get(indexB).get(column);
        if (types[column] == Float.class)
            return (float) data.get(indexA).get(column) > (float) data.get(indexB).get(column);
        if (types[column] == String.class)
            return data.get(indexA).get(column).toString().compareTo(data.get(indexB).get(column).toString()) > 0;
        return false;
    }

    /**
     * Swap 2 Records
     * @param indexA first index to swap
     * @param indexB second index to swap
     */
    public void swap(int indexA, int indexB) {
        if (!isRow(indexA) || !isRow(indexB))
            return;
        Record temp = data.get(indexA);
        data.set(indexA, data.get(indexB));
        data.set(indexB, temp);
    }

    /**
     * Get names of types
     * @return names of types as string array
     */
    private String[] typeNames() {
        String[] type_names = new String[types.length];
        for (int type = 0; type < types.length; type++) {
            String[] cname = types[type].toString().split("\\.");
            type_names[type] = cname[cname.length - 1];
        }
        return type_names;
    }

    // Fragmenting

    /**
     * Create a fragment
     * @param start start index of fragment
     * @param end end index of fragment
     * @return True if fragment is created else false
     */
    private boolean makeFragment(int start, int end) {
        for (DataMaRe_Fragment fragment : fragments)
            if ((fragment.getStart() <= start && start <= fragment.getEnd()) ||
                (fragment.getStart() <= end && end <= fragment.getEnd())) {
                System.err.println(String.format("WARNING FRAGMENT NOT CREATED [%d-%d OVERLAPS %d-%d]", start, end, fragment.getStart(), fragment.getEnd()));
                return false;
            }
        fragments.add(new DataMaRe_Fragment(this, start, end));
        //System.err.println("Fragment Created " + start + "-" + end);
        return true;
    }

    /**
     * Start a process on a fragment
     * @param process process to start
     */
    public void processFragment(DataMaReProcess process) {
        processFragment(process, 0, getRows() - 1);
    }

    /**
     * Start a process on a fragment
     * @param process process to start
     * @param start start index of new fragment
     * @param end end index of new fragment
     */
    public void processFragment(DataMaReProcess process, int start, int end) {
        // Fix Params
        start = start < 0 ? 0 : start > getRows() - 1 ? getRows() - 1 : start;
        end = end > getRows() - 1 ? getRows() - 1 : end <= start ? start : end;

        if (!makeFragment(start, end)) return;
        DataMaRe_Fragment fragment = getFragment(start, end);
        if (fragment == null) {
            System.err.println("Null Fragment");
            return;
        }
        fragment.runProcess(process);
    }

    /**
     * Check for finished fragments
     * @return True if there were any updates
     */
    public boolean updateFragments() {
        boolean updates = false;
        boolean fixed = false;
        // Whilst the DataMaRe is not fixed
        while (!fixed) {
            fixed = true;
            // Check for each fragment
            for (int fragment_id = 0; fragment_id < fragments.size(); fragment_id++) {
                DataMaRe_Fragment fragment = fragments.get(fragment_id);
                // If the result isn't null, the fragment is finished
                if (fragment.getResult() != null) {
                    // There are updates, and the DataMaRe is not fixed
                    updates = true;
                    fixed = false;
                    // If the fragment returns a DataMaRe fragment, replace the old data
                    if (fragment.getResult().getClass() == DataMaRe.class)
                        updateFragment_Edit(fragment);
                    // If it's an arraylist, build the output as a string, and print it
                    else if (fragment.getResult().getClass() == ArrayList[].class) {
                        ArrayList[] results = (ArrayList[]) fragment.getResult();
                        for (int i = 0; i < results[0].size(); i++) {
                            StringBuilder line = new StringBuilder();
                            line.append(String.format(prefix.replace("\n", ""), i));
                            for (int j = 0; j < results.length; j++)
                                line.append(String.format(format, results[j].get(i)));
                            System.out.println(line.toString());
                        }
                    }
                    // Remove the fragment as it has been completed.
                    fragments.remove(fragment);
                    break;
                }
            }
            // After processing all fragments, if it's all fixed, return updates
        }
        return updates;
    }

    /**
     * Update a DataMaRe fragment once it's finished
     * @param fragment the fragment to update
     * @return whether the fragment successfully updated
     */
    private boolean updateFragment_Edit(DataMaRe_Fragment fragment) {
        // If the fragment hasn't finished exit
        if (fragment.getResult() == null)
            return false;
        //System.err.println("Reloading Fragment " + fragment.getStart() + "-" + fragment.getEnd());
        // Check the records deleted or added
        int delta = (fragment.getEnd() - fragment.getStart()) - ((DataMaRe) fragment.getResult()).getRows();
        // Kill old data, and add the new data
        data.removeAll(getData(fragment.getStart(), fragment.getEnd()));
        data.addAll(fragment.getStart(), ((DataMaRe) fragment.getResult()).getData());
        // For every fragment in the DataMaRe, if it falls after, subtract the delta.
        for (DataMaRe_Fragment each_fragment : fragments)
            if (each_fragment.getStart() > fragment.getStart())
                each_fragment.shift(delta);
        return true;
    }

    /**
     * Get a fragment from the datamare
     * @param start start index
     * @param end end index
     * @return if the fragment is valid, the fragment
     */
    DataMaRe_Fragment getFragment(int start, int end) {
        for (DataMaRe_Fragment fragment : fragments)
            if (fragment.getStart() == start && fragment.getEnd() == end) {
                return fragment;
            }
        return null;
    }

    /**
     * Check if there are still fragments
     * @return if there are still fragments after updates
     */
    public boolean isProcessing() {
        updateFragments();
        return fragments.size() > 0;
    }

    /**
     * Getter for name
     * @return name
     */
    String getName() {
        return name;
    }

    /**
     * Getter for headers
     * @return headers
     */
    String[] getHeaders() {
        return headers;
    }

    /**
     * Getter for types
     * @return return types
     */
    Class[] getTypes() {
        return types;
    }

    /**
     * Getter for data in a range
     * @param start start index
     * @param end end index
     * @return Data in specified range
     */
    ArrayList<Record> getData(int start, int end) {
        if (!checkBounds(start, end)) return null;
        return new ArrayList<>(data.subList(start, end + 1));
    }

    /**
     * Get all data
     * @return data
     */
    ArrayList<Record> getData() {
        return data;
    }


}
