package data.Series;

import java.util.ArrayList;
import java.util.Arrays;

public class Series {

    // Data
    private String name;
    private Class type;
    private ArrayList<Object> data;

    // Constructors
    public Series(String name, Class type) {
        Constructor(name, type, new ArrayList<>());
    }

    public Series(String name, Class type, ArrayList<Object> data) {
        Constructor(name, type, data);
    }

    public Series(String name, Class type, Object... data) {
        Constructor(name, type, new ArrayList<>(Arrays.asList(data)));
    }

    // Actual Constructor
    private void Constructor(String name, Class type, ArrayList<Object> data) {
        this.data = new ArrayList<>();
        this.name = name;
        this.type = type;
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


    public Object get(int index) {
        if (!inBounds(index)) return null;
        return data.get(index);

    }

    public void set(int index, Object val) {
        if (!inBounds(index)) return;
        if (!checkType(val)) return;
        data.set(index, val);
    }

    public void add(Object val) {
        if (!checkType(val)) return;
        this.data.add(val);
    }


    private boolean inBounds(int index) {
        return index >= 0 && index < data.size();
    }

}
