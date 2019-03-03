import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.edit.RemoveEXIST;
import DataMaRe.jobs.tasks.edit.RemoveFORMAT;
import DataMaRe.jobs.tasks.edit.RemoveNULL;
import DataMaRe.jobs.tasks.edit.Sort;
import DataMaRe.jobs.tasks.mapreduce.Reduce;

public class Main {
    static FileLoader files;
    static DataMaRe dataMaRe;

    public static void main(String[] args) {
        files = new FileLoader();
        TestDataMaRe();
    }

    static DataMaRe getFile(int file) {
        return new DataMaRe(files.getFile(file));
    }


    static void TestDataMaRe() {
        // Set the Main File as Erroneous Data
        dataMaRe = getFile(1);

        // PROCESSING

        // Remove Null Values then wait
        dataMaRe.processFragment(new RemoveNULL());
        dataMaRe.waitForFragments();

        // Remove badly formatted values, then wait
        dataMaRe.processFragment(new RemoveFORMAT("" +
                                                  "^[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]{1}$",
                                                  "^[A-Z]{3}[0-9]{4}[A-Z]{1}$",
                                                  "^[A-Z]{3}$",
                                                  "^[A-Z]{3}$",
                                                  "^[0-9]+$",
                                                  "^[0-9]+$"
        ));
        dataMaRe.waitForFragments();

        // Remove Invalid Values
        DataMaRe airports = getFile(4);
        dataMaRe.processFragment(new RemoveEXIST(airports, "Airport", "Origin"));
        dataMaRe.waitForFragments();
        // Remove Invalid Values
        dataMaRe.processFragment(new RemoveEXIST(airports, "Airport", "Destination"));
        dataMaRe.waitForFragments();


        // SEARCHING AND SORTING

        // Search - Airports by Origin
        dataMaRe.processFragment(new Reduce(airports, "Airport", "Origin"));
        dataMaRe.waitForFragments();

        dataMaRe.processFragment(new Sort("Flight Number"));
        dataMaRe.displayData();

        // Search - Passengers per Flight
        dataMaRe.processFragment(new Reduce("Flight Number"));
        dataMaRe.waitForFragments();


    }

}

