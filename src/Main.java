import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.edit.RemoveEXIST;
import DataMaRe.jobs.tasks.edit.RemoveFORMAT;
import DataMaRe.jobs.tasks.edit.RemoveNULL;
import DataMaRe.jobs.tasks.edit.Sort;

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

        // Remove Null Values then wait
        dataMaRe.processFragment_Edit(new RemoveNULL());
        dataMaRe.waitForFragments();

        // Remove badly formatted values, then wait
        dataMaRe.processFragment_Edit(new RemoveFORMAT("" +
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
        dataMaRe.processFragment_Edit(new RemoveEXIST(airports, "Airport", "Origin"));
        dataMaRe.waitForFragments();
        // Remove Invalid Values
        dataMaRe.processFragment_Edit(new RemoveEXIST(airports, "Airport", "Destination"));
        dataMaRe.waitForFragments();


        // Sort
        dataMaRe.processFragment_Edit(new Sort("Destination"));
        dataMaRe.displayData();
    }

}

