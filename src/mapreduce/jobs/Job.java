package mapreduce.jobs;

class Job {
    private String name;
    private Thread thread;

    Job(String name, Runnable job) {
        this.name = name;
        this.thread = new Thread(job);
        printStatus("created");
    }

    String getName() {
        return name;
    }


    void startJob() {
        printStatus("starting");
        thread.start();
        printStatus("started");
    }

    void stopJob() {
        printStatus("interrupting");
        thread.interrupt();
        printStatus("interrupted");
    }

    void printStatus(String status) {
        System.out.println(String.format("[%s] %s", name, status));
    }
}
