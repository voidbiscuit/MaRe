package data.Series;

import java.util.ArrayList;

public class Series {

    // Data
    private String name;
    private Class type;
    private ArrayList<Object> data;

    // Constructors
    public Series(String name) {
        this(name, (Class) null);
    }

    public Series(String name, Class type) {
        this(name, type, new ArrayList<>());
    }

    public Series(String name, ArrayList<Object> data) {
        this(name, null, data);
    }


    public Series(String name, Class type, ArrayList<Object> data) {
        this.data = new ArrayList<>();
        this.type = type;
        this.name = name;
        for (Object o : data)
            add(o);
    }


    public void view() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("\n" +
                        "\nSeries Name :\t%s" +
                        "\nData Type   :\t%s" +
                        "\nData        |",
                name, type
        ));
        for (int i = 0; i < data.size(); i++)
            if (inBounds(i))
                output.append(String.format(
                        "\n# %3d\t\t|\t%s",
                        i + 1, data.get(i)
                ));
        System.out.println(output.toString());
    }

    private boolean checkType(Object val) {
        return val.getClass() == type;
    }

    private Object convertVal(Object val) {
        // Check if Integer
        try {
            return Integer.parseInt(val.toString());
        } catch (NumberFormatException e) {
        }
        // Check if Float
        try {
            return Float.parseFloat(val.toString());
        } catch (NumberFormatException e) {
        }
        // Return String
        String val_string = val.toString();
        if (val_string.matches("\\s") || val_string.isEmpty())
            return null;
        return val;

    }

    public String header() {
        return name;
    }


    public String getType() {
        String[] classtype = type.toString().split("\\.");
        return classtype[classtype.length - 1];
    }

    public Object get(int index) {
        if (!inBounds(index)) return null;
        return data.get(index);
    }

    public String getString(int index) {
        Object value = get(index);
        if (value == null || value.toString().isEmpty())
            return "(null)";
        return value.toString();

    }

    public void set(int index, Object val) {
        if (!inBounds(index)) return;
        if (!checkType(val)) return;
        data.set(index, val);
    }

    public void add(Object val) {
        // Set a type
        if (type == null)
            type = convertVal(val).getClass();
        // If the type is correct, add to list
        if (checkType(val)) {
            this.data.add(val);
            return;
        }
        // Otherwise, try to convert
        else {
            val = convertVal(val);
            // Check the new type of the attempted convert
            if (checkType(val)) {
                this.data.add(val);
                return;
            }
        }
        // If all fails, it's null
        this.data.add(null);
    }


    private boolean inBounds(int index) {
        return index >= 0 && index < data.size();
    }

}
