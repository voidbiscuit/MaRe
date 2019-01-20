import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileLoader {
    // Directory name and Directory
    private String directory_name;
    private File directory;
    // Loaded and Stored Files
    private String[] file_names;
    private ArrayList<ArrayList> files;

    public FileLoader() {
        // Load Files with using dir "data"
        this("data");
    }

    public FileLoader(String directory) {
        // Load directory oas files
        setDirectory(directory);
        updateDir();
        loadFiles();
    }

    public void setDirectory(String directory) {
        directory.replace("/", "\\");
        if (!(directory.endsWith("\\")))
            directory += "/";
        directory_name = directory;
        this.directory = new File(directory);
    }

    public void updateDir() {
        // Return all files and folders in directory
        file_names = directory.list();
    }

    public String[] listDir() {
        return file_names;
    }

    public void loadFiles() {
        // Load File Names
        files = new ArrayList<>();
        updateDir();
        // Load files as ArrayList, containing ArrayList of lines
        for (String file_name : file_names)
            files.add(readFile(file_name));
    }

    private ArrayList<String> readFile(String file_name) {
        ArrayList<String> data = new ArrayList<>();
        data.add(file_name);
        try {
            BufferedReader r = new BufferedReader(new FileReader(new File(directory_name + file_name)));
            String buffer;
            while (true) {
                buffer = r.readLine();
                if (buffer == null) break;
                data.add(buffer);
            }
            System.out.println("Read File " + file_name);
            return data;
        } catch (IOException e) {
            System.err.println("Problem reading file " + file_name);
            return new ArrayList<>();
        }
    }

    public ArrayList getFile(int index) {
        if (index >= 0 && index < files.size())
            return files.get(index);
        return null;
    }
}
