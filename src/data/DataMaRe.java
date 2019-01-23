package data;

import data.Series.Record;

import java.util.ArrayList;
import java.util.Arrays;

public class DataMaRe {

    // Constants
    private static final String
            regex_split = ",",
            prefix = "\n\t%10s\t|",
            format = "\t%20s |";
    // Individual Data
    private String file_name;
    private String[] headers;
    private Class[] types;
    private ArrayList<Record> data;


    public DataMaRe(String file_data) {
        // Get file contents from each line of file data
        ArrayList<String> file_contents = new ArrayList<>(Arrays.asList(file_data.split("\\r?\\n")));
        // Load metadata
        file_name = file_contents.remove(0);
        headers = file_contents.remove(0).split(regex_split);
        types = getTypes(file_contents.remove(0).split(regex_split));
        // Load data
        data = new ArrayList<>();
        for (String record : file_contents)
            addRecord(record);
        System.out.println("Created File");
    }

    private Class[] getTypes(String[] types) {
        ArrayList<Class> classTypes = new ArrayList<>();
        for (String type : types)
            classTypes.add(getType(type));
        return classTypes.toArray(new Class[0]);
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

    private void addRecord(String record) {
        data.add(new Record(record.split(regex_split), types));
    }

    public void displayData(int start, int end) {
        StringBuilder output = new StringBuilder();
        output.append(String.format("\n-- %s", file_name));
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

    private String view(int start, int end, String prefix, String format) {
        // If the user is stupid, return
        if (start >= end) return "";
        if (start < 0) start = 0;
        if (end < 0) return "";
        if (start > getRows() - 1) return "";
        if (end < getRows() - 1) end = getRows() - 1;
        // Get rows
        StringBuilder records = new StringBuilder();
        for (int record = start; record < end; record++)
            records.append(data.get(record).view(prefix, format, record));
        return records.toString();
    }

    private int getRows() {
        return data.size();
    }

    private int getColumns() {
        return headers.length;
    }

    private String[] typeNames() {
        String[] type_names = new String[types.length];
        for (int type = 0; type < types.length; type++) {
            String[] cname = types[type].toString().split("\\.");
            type_names[type] = cname[cname.length - 1];
        }
        return type_names;
    }
}
