package data.Series;

public class Record {

    private Object[] data;

    // Constructors

    public Record(Object[] data, Class[] types) {
        this.data = new Object[data.length];
        for (int fragment = 0; fragment < data.length; fragment++)
            this.data[fragment] = toType(data[fragment], types[fragment]);
    }

    private Object toType(Object value, Class type) {
        try {
            if (type == Integer.class)
                return Integer.parseInt(value.toString());
            if (type == Float.class)
                return Float.parseFloat(value.toString());
        } catch (NumberFormatException ignore) {
        }
        if (type == String.class)
            return value.toString();
        return Object.class;
    }

    public String view(String prefix, String format, int index) {
        StringBuilder output = new StringBuilder();
        output.append(String.format(prefix, index));
        for (int field = 0; field < data.length; field++)
            output.append(String.format(
                    format,
                    getString(field)
            ));
        return output.toString();
    }


    public Object get(int index) {
        if (!inBounds(index)) return null;
        return data[index];
    }

    public String getString(int index) {
        Object value = get(index);
        if (value == null || value.toString().isEmpty())
            return "(null)";
        return value.toString();
    }


    private boolean inBounds(int index) {
        return index >= 0 && index < data.length;
    }

}
