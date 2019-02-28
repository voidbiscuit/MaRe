package DataMaRe.data;


import DataMaRe.data.Series.Record;
import DataMaRe.jobs.tasks.DataMaReProcess;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

import java.util.ArrayList;
import java.util.Arrays;

public class DataMaRe {

    // Constants
    public static final String
            regex_split = ",",
            prefix = "\n\t%10s\t|",
            format = "\t%20s |";
    // Individual Data
    private String name;
    private String[] headers;
    private Class[] types;
    private ArrayList<Record> data;
    private ArrayList<DataMaRe_Fragment> fragments;

    DataMaRe(String name, String[] headers, Class[] types, ArrayList<Record> data, ArrayList<DataMaRe_Fragment> fragments) {
        this.name = name;
        this.headers = headers;
        this.types = types;
        this.data = data;
        this.fragments = fragments;
    }

    public DataMaRe(String name, String[] headers, Class[] types, ArrayList<Record> data) {
        this(name, headers, types, data, new ArrayList<>());
    }


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

    private Class[] getTypes(String[] types) {
        ArrayList<Class> classTypes = new ArrayList<>();
        for (String type : types)
            classTypes.add(getType(type));
        return classTypes.toArray(new Class[0]);
    }

    public Class getType(int index) {
        return types[index];
    }


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

    public void addRecord(String record) {
        String[] newRecord = record.split(regex_split);
        if (checkRecordTypes(newRecord))
            data.add(new Record(newRecord, types));

    }

    public void removeRecord(int record) {
        if (!isRow(record)) return;
        data.remove(record);
    }

    public void waitForFragments() {
        while (isProcessing()) ;
    }

    public void displayData() {
        if (isProcessing()) {
            System.err.println("Still Processing");
            return;
        }
        displayData(0, getRows());
    }

    public void displayData(int start, int end) {
        if (isProcessing()) {
            System.err.println("Still Processing");
            return;
        }
        StringBuilder output = new StringBuilder();
        output.append(String.format("\n-- %s", name));
        output.append(String.format("\n#%d records", data.size()));
        output.append(lineFormat(prefix, "Headers", format, headers));
        output.append(lineFormat(prefix, "Types", format, typeNames()));
        output.append(view(start, end, prefix, format));
        System.out.println(output.toString());
    }

    private String lineFormat(String rowInfoFormat, String rowInfoData, String lineFormat, String[] lineData) {
        StringBuilder line = new StringBuilder();
        line.append(String.format(rowInfoFormat, rowInfoData));
        for (String data : lineData)
            line.append(String.format(lineFormat, data));
        return line.toString();
    }

    // Bounds
    private boolean checkBounds(int start, int end) {
        return 0 <= start && start <= end && end < getRows();
    }

    private String view(int start, int end, String prefix, String format) {
        if (checkBounds(start, end)) return "";
        // Get rows
        StringBuilder records = new StringBuilder();
        for (int record = start; record < end; record++)
            records.append(data.get(record).view(prefix, format, record));
        return records.toString();
    }

    private boolean isRow(int index) {
        return index >= 0 && index < data.size();
    }

    private boolean isColumn(int index) {
        return index >= 0 && index < headers.length;
    }

    public Record getRecord(int index) {
        if (!isRow(index)) return null;
        return data.get(index);
    }

    public int getRows() {
        return data.size();
    }

    public int getColumn(String column_name) {
        for (int header = 0; header < headers.length; header++)
            if (column_name.equals(headers[header])) return header;
        return -1;
    }

    public String getColumn(int column) {
        return headers[column];
    }

    public int getColumns() {
        return headers.length;
    }

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

    public void swap(int indexA, int indexB) {
        if (!isRow(indexA) || !isRow(indexB))
            return;
        Record temp = data.get(indexA);
        data.set(indexA, data.get(indexB));
        data.set(indexB, temp);
    }

    private String[] typeNames() {
        String[] type_names = new String[types.length];
        for (int type = 0; type < types.length; type++) {
            String[] cname = types[type].toString().split("\\.");
            type_names[type] = cname[cname.length - 1];
        }
        return type_names;
    }

    // Fragmenting

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

    public void processFragment_Edit(DataMaReProcess_Edit process) {
        processFragment_Edit(process, 0, getRows() - 1);
    }

    public void processFragment_Edit(DataMaReProcess_Edit process, int start, int end) {
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

    public boolean updateFragments() {
        boolean updates = false;
        boolean fixed = false;
        while (!fixed) {
            fixed = true;
            for (int fragment = 0; fragment < fragments.size(); fragment++) {
                if (updateFragment_Edit(fragments.get(fragment))) {
                    updates = true;
                    fixed = false;
                    break;
                }
            }
        }
        return updates;
    }

    private boolean updateFragment_Edit(DataMaRe_Fragment fragment) {
        if (fragment.getResult() == null)
            return false;
        //System.err.println("Reloading Fragment " + fragment.getStart() + "-" + fragment.getEnd());
        int delta = (fragment.getEnd() - fragment.getStart()) - fragment.getResult().getRows();
        data.removeAll(getData(fragment.getStart(), fragment.getEnd()));
        data.addAll(fragment.getStart(), fragment.getResult().getData());
        fragments.remove(fragment);
        for (DataMaRe_Fragment each_fragment : fragments)
            if (each_fragment.getStart() > fragment.getStart())
                each_fragment.shift(delta);
        return true;
    }

    public void fragmentise_processFragment_Edit(int fragment_size, DataMaReProcess_Edit process) {
        System.err.println(String.format("\n\n###Fragmenting"));
        for (int fragment = 0; fragment < getRows() - 1; fragment += fragment_size) {
            DataMaReProcess new_process = process;
            DataMaReProcess_Edit x = (DataMaReProcess_Edit) new_process;
            processFragment_Edit(x, fragment, fragment + fragment_size - 1);
        }
        waitForFragments();
        System.err.println("###Finished\n");
    }

    DataMaRe_Fragment getFragment(int start, int end) {
        for (DataMaRe_Fragment fragment : fragments)
            if (fragment.getStart() == start && fragment.getEnd() == end) {
                return fragment;
            }
        return null;
    }

    public boolean isProcessing() {
        updateFragments();
        return fragments.size() > 0;
    }

    String getName() {
        return name;
    }

    String[] getHeaders() {
        return headers;
    }

    Class[] getTypes() {
        return types;
    }

    ArrayList<Record> getData(int start, int end) {
        if (!checkBounds(start, end)) return null;
        return new ArrayList<>(data.subList(start, end + 1));
    }

    ArrayList<Record> getData() {
        return data;
    }


}
