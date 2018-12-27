import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataMaRe {

    // Constants
    private static final String regex_split = ",";

    // Individual Data
    private String
            file_path,
            file_name;
    private ArrayList<ArrayList<String>> data;
    private String[] types;

    /**
     * Object to represent a CSV file, loaded in.
     *
     * @param file_path Path of the CSV File
     */
    public DataMaRe(String file_path, String file_name) {
        // Update the filepath
        setFilePath(file_path, file_name);
        // Read new data
        readData();
    }

    private void setFilePath(String file_path, String file_name) {
        this.file_path = file_path;
        this.file_name = file_name;
    }


    private void readData() {
        // Clear Data
        data = new ArrayList<>();
        try {
            // Initialise a reader to open the file
            BufferedReader r = new BufferedReader(new FileReader(new File(this.file_path + this.file_name)));
            // Load in each line
            String buffer;
            while ((buffer = r.readLine()) != null) {
                buffer = buffer.replace("\\s+", " "); // remove trash whitespace
                data.add(new ArrayList<>(Arrays.asList(buffer.split(regex_split)))); // Load as arraylists
            }
        } catch (IOException e) {
            // Catch if fucked
            e.printStackTrace();
        }
    }

    public void displayData() {
        int max = 5;
        StringBuilder output = new StringBuilder();
        // Header
        output.append(String.format("\n\n-- %s\n%d records\n", file_name, data.size()));
        // For every stored record
        for (ArrayList<String> record : data) {
            if (max-- < 0) break; // dirty max
            // Print a new line
            output.append("\n\t|");
            // Print out each fragment, 20 wide
            for (String fragment : record)
                output.append(String.format("%20s", fragment));
        }
        System.out.print(String.format("%150s", output.toString()));
    }




}
