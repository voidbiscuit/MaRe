import java.io.*;
import java.util.ArrayList;

public class DataMaRe {
    // Constants
    private static final String regex_split = ",";
    // Individual Data
    private String file_path;
    private ArrayList<String> lines;
    private String[] types;

    public DataMaRe(String file_path) {
        // Update the filepath
        this.file_path = file_path;
        // Read the file in by lines
        readData();


    }

    private String[] readData() {
        // Clear Data
        ArrayList<String> lines = new ArrayList<>();
        try {
            // Prepare to Read file
            BufferedReader r = new BufferedReader(new FileReader(new File(file_path)));
            String line;
            // Read file to end
            while ((line = r.readLine()) != null)
                lines.add(line);
            return Arrayslines;
        } catch (IOException e) {
            // Catch if fucked
            e.printStackTrace();
        }
    }

    private void putIntoColumns(String... types) {
        this.types = types;
        String[] line_fragments;
        for (String line : lines) {
            line_fragments = line.split(regex_split);
            for (int fragment = 0; fragment < line_fragments.length; fragment++) {
                switch (types[fragment]) {
                    case :
                    stuff.add(Strin types[fragment]line_fragments[fragment].)
                }
            }
        }
    }
}
