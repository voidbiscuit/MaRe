package mapreduce.jobs.tasks;

import data.DataMaRe;

import java.util.Random;

public class Reducer implements Runnable {
    DataMaRe dataMaRe;

    public Reducer(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (!Thread.interrupted()) {
            int start = r.nextInt(100);
            dataMaRe.displayData(start, start + 1);
        }
    }
}
