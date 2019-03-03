package DataMaRe.data.Series;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Record {

    private Object[] data;

    // Constructors

    /**
     * Constructor for Record
     *
     * @param data  input data
     * @param types input data type
     */
    public Record(Object[] data, Class[] types) {
        this.data = new Object[data.length];
        for (int fragment = 0; fragment < data.length; fragment++)
            this.data[fragment] = toType(data[fragment], types[fragment]);
    }

    /**
     * Function to convert the object to a type
     *
     * @param value input data
     * @param type  type to convert input data to
     * @return input data converted to that type
     */
    private Object toType(Object value, Class type) {
        try {
            if (type == Integer.class) // Convert to Integer
                return Integer.parseInt(value.toString());
            if (type == Float.class) // Convert to Float
                return Float.parseFloat(value.toString());
        } catch (NumberFormatException ignore) { // Catch errors converting to numbers
        }
        if (type == String.class)
            return value.toString(); // Return the value as a string
        return value; // Else return the Object
    }

    /**
     * View the record in a formatted line
     *
     * @param prefix line prefix
     * @param format format of each element of data
     * @param index  the record number
     * @return a formatted line using the specified parameters
     */
    public String view(String prefix, String format, int index, String[] headers) {
        StringBuilder output = new StringBuilder();
        output.append(String.format(prefix, index + 1));
        for (int field = 0; field < data.length; field++)
            output.append(String.format(
                    format, headers[field].toLowerCase().contains("time") ? Date.from(Instant.ofEpochSecond(1000*Long.parseLong(getString(field))))
                            : headers[field].toLowerCase().contains("duration") ? new SimpleDateFormat("HH:mm:ss").format(new Date(1000*Long.parseLong(getString(field))))
                            : getString(field)
            ));
        return output.toString();
    }

    /**
     * Get an item from a column of the record
     *
     * @param index column to retrieve data from
     * @return data from column of record
     */
    public Object get(int index) {
        if (!inBounds(index)) return null;
        if (data[index] == null) System.err.println("NULL");
        return data[index];
    }

    /**
     * Get an item from a column of the record as a string
     *
     * @param index column to retrieve data from
     * @return data from column of record as a string
     */
    public String getString(int index) {
        Object value = get(index);
        if (value == null || value.toString().isEmpty())
            return "(null)";
        return value.toString();
    }

    /**
     * Check if the user inputted column index is valid
     *
     * @param index column to retrieve data from
     * @return whether the column index is valid
     */
    private boolean inBounds(int index) {
        return index >= 0 && index < data.length;
    }

}
