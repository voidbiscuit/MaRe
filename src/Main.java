import DataMaRe.data.DataMaRe;
import DataMaRe.jobs.tasks.edit.Sort;

public class Main {
    static FileLoader files;
    static DataMaRe dataMaRe;

    public static void main(String[] args) {
        getFile();
        TestDataMaRe();
    }

    static void getFile() {
        files = new FileLoader();
        dataMaRe = new DataMaRe(files.getFile(1));
    }

    static void TestDataMaRe() {
        dataMaRe.displayData();
        for (int i = 0; i < dataMaRe.getRows() / 4; i++)
            dataMaRe.processFragment_Edit(new Sort("Some Thing"), 4 * i, 4 * (i + 1)-1);
        while (dataMaRe.isProcessing()) {
            if (dataMaRe.updateFragments())
                System.out.println("Process Done");
        }
        System.err.println("\n\nDone\n\n");
        dataMaRe.displayData();
    }


}
