import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FileLoader {
    // Directory name and Directory
    private String directory_name;
    private File directory;
    // Loaded and Stored Files
    private ArrayList<String> file_names;
    private ArrayList<String> files;

    /**
     * Constructor
     */
    public FileLoader() {
        // Load Files with using dir "DataMaRe.data"
        this("data");
    }

    /**
     * Constructor
     * @param directory directory to load data from
     */
    public FileLoader(String directory) {
        updateDirectory(directory);
        loadFiles();
    }

    /**
     * Update the directory to use
     * @param directory_name new directory name
     */
    public void updateDirectory(String directory_name) {
        directory_name.replace("/", "\\");
        if (!(directory_name.endsWith("\\")))
            directory_name += "/";
        this.directory_name = directory_name;
        this.directory = new File(directory_name);
        updateDirectoryListing();
    }

    /***
     * Get files from new directory
     */
    public void updateDirectoryListing() {
        file_names = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.list())));
    }

    /**
     * Return files in the directory
     * @return filenames
     */
    public ArrayList<String> listDirectory() {
        return file_names;
    }

    /**
     * Load all files from the directory
     */
    public void loadFiles() {
        // Load File Names
        files = new ArrayList<>();
        updateDirectoryListing();
        // Load files as ArrayList, containing ArrayList of lines
        for (String file_name : file_names)
            files.add(readFile(file_name));
    }

    /**
     * Read each file from the directory.
     * @param file_name the name of the file
     * @return String contents of the file
     */
    private String readFile(String file_name) {
        StringBuilder data = new StringBuilder();
        data.append(file_name);
        try {
            BufferedReader r = new BufferedReader(new FileReader(new File(directory_name + file_name)));
            String buffer;
            while (true) {
                buffer = r.readLine();
                if (buffer == null) break;
                data.append("\n");
                data.append(buffer);
            }
            return data.toString();
        } catch (IOException e) {
            System.err.println("Problem reading file " + file_name);
            return "";
        }
    }

    /**
     * Get the file contents of the specified file
     * @param filename name of file.
     * @return file
     */
    public String getFile(String filename) {
        for (int filename_id = 0; filename_id < files.size(); filename_id++)
            if (filename.equals(file_names.get(filename_id))) return getFile(filename_id);
        return null;
    }

    /**
     * Get the file contents of the specified file
     * @param index file index
     * @return file
     */
    public String getFile(int index) {
        if (index >= 0 && index < files.size())
            return files.get(index);
        return null;
    }
}
