import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    // Data
    private static final String directory_name = "data/";

    // Values
    private static File directory;
    private static ArrayList<String> file_names;
    private static ArrayList<DataMaRe> files;

    public static void main(String[] args) {
        findFiles();
        if (file_names.size() > 0) {
            System.out.println(String.format("%d files have been found!", file_names.size()));
            loadFiles();
            displayFiles();
        } else
            System.out.println("No files found.");
    }


    private static void findFiles() {
        directory = new File(directory_name);
        try {
            System.out.println("Loading Files from [" + directory.getCanonicalPath() + "]");
            file_names = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.list())));
            if (!file_names.isEmpty())
                for (String file_name : file_names) System.out.println("-- " + file_name);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFiles() {
        files = new ArrayList<>();
        for (String file_name : file_names)
            files.add(new DataMaRe(directory_name, file_name));
    }

    private static void displayFiles() {
        for (DataMaRe file : files)
            file.displayData();
    }
}
