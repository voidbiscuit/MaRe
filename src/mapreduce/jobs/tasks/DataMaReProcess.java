package mapreduce.jobs.tasks;

import data.DataMaRe;

public abstract class DataMaReProcess implements Runnable {
    DataMaRe dataMaRe;
    private volatile boolean
            running = false,
            paused = true,
            finished = false;
    private final Object pauselock = new Object();

    public DataMaReProcess(DataMaRe dataMaRe) {
        this.dataMaRe = dataMaRe;
    }

    @Override
    public void run() {
        if (running || !paused) return;
        running = true;
        paused = false;
        finished = false;
        while (running) {
            synchronized (pauselock) {
                if (!running) break;
                if (paused)
                    try {
                        pauselock.wait();
                    } catch (InterruptedException ignore) {
                        break;
                    }
                if (!running) break;
            }
            process();
        }
    }

    public void stop() {
        running = false;
        paused = false;
    }

    public void pause() {
        if (!running) return;
        paused = true;
    }

    public void resume() {
        if (!running || !paused)
            synchronized (pauselock) {
                paused = false;
                pauselock.notifyAll();
            }
    }

    void finished() {
        finished = true;
        stop();
    }

    void process() {
        //debug
        if (true) System.out.println(finished ? "finished" : "working");

    }


    public Object getResult() {
        if (!finished) return null;
        return finishedResult();
    }

    Object finishedResult() {
        return null;
    }
}
