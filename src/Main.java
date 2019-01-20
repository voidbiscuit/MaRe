import data.DataMaRe;
import data.Series.Series;

public class Main {


    public static void main(String[] args) {

        //TestSeries();
        TestDataFrame();
    }

    static void TestDataFrame() {
        FileLoader files = new FileLoader();
        DataMaRe dm = new DataMaRe(files.getFile(0));
        dm.displayData();
    }


}
