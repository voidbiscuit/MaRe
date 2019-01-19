import data.Series.Series;

public class Main {


    public static void main(String[] args) {

        TestSeries();
    }

    static void TestSeries() {
        Series s = new Series("Numbers", Integer.class, 2, 3, 4, 5, 6);
        Series t = new Series("Names", String.class, "Jaffa Cakes", "Mushrooms", "Bananas");
        Series u = new Series("Chars", Character.class, 'a', 'b', 'c');
        s.view();
        t.view();
        u.view();
    }


}
