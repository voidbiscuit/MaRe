package DataMaRe.jobs.tasks;

import DataMaRe.data.DataMaRe;

public abstract class DataMaReProcess implements Runnable {
    // Data
    protected DataMaRe dataMaRe;
    private volatile boolean
            running = false,
            paused = true,
            finished = false;
    private final Object pauselock = new Object();

    protected DataMaReProcess() {

    }

    public void setDataMaRe(DataMaRe dataMaRe) {
        if (this.dataMaRe != null)
            return;
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

    protected void finished() {
        finished = true;
        stop();
    }

    protected void process() {
        if (dataMaRe == null) {
            System.err.println("Null DataMaRe - Exiting");
            stop();
        }
    }


    public Object getResult() {
        if (!finished) return null;
        return finishedResult();
    }

    Object finishedResult() {
        return dataMaRe;
    }
}
