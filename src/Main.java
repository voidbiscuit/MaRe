import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    static final String directory_name = "data/";
    static ArrayList<String> file_names;

    public static void main(String[] args) {
        initFiles();
        new DataMaRe(file_names.get(0));
    }


    private static boolean initFiles() {
        File directory = new File(directory_name);
        try {
            System.out.println("Loading Files from [" + directory.getCanonicalPath() + "]");
            file_names = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.list())));
            if (!file_names.isEmpty())
                for (int file = 0; file < file_names.size(); file++) {
                    System.out.println("-- " + file_names.get(file));
                    file_names.set(file, directory.getCanonicalPath() + "\\" + file_names.get(file));
                }
            return true;
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
