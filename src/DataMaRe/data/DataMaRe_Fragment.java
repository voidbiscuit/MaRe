package DataMaRe.data;

import DataMaRe.jobs.Job;
import DataMaRe.jobs.tasks.DataMaReProcess;

public class DataMaRe_Fragment {

    private DataMaRe fragment;
    private int start, end;
    private Job process;

    /**
     * Constructor for DataMaRe fragment
     * @param data the DataMaRe to get data from
     * @param start start index
     * @param end end index
     */
    DataMaRe_Fragment(DataMaRe data, int start, int end) {
        // If start is less than 0 shift to 0, if greater than end shift to end
        start = start < 0 ? 0 : start > data.getRows() - 1 ? data.getRows() - 1 : start;
        // If end is bigger than end, shift to end, if end is smaller or equal to start, end = start.
        end = end > data.getRows() - 1 ? data.getRows() - 1 : end <= start ? start : end;
        // Set start end end to replace to, then get the fragment data.
        this.start = start;
        this.end = end;
        this.fragment = new DataMaRe(data.getName(), data.getHeaders(), data.getTypes(), data.getData(start, end));

    }

    int getStart() {
        return start;
    }

    int getEnd() {
        return end;
    }

    /**
     * Shift the pointers by delta
     * @param delta amount to shift by
     */
    void shift(int delta) {
        this.start += delta;
        this.end += delta;
        this.process.updateName(String.format("%s [%d-%d]", fragment.getName(), getStart(), getEnd()));
    }

    /**
     * Run a process on the fragment
     * @param process process to run
     */
    void runProcess(DataMaReProcess process) {
        if (this.process != null || this.fragment == null) {
            System.err.println("Process Creation Failed");
            return;
        }
        this.process = new Job(String.format("%s [%d-%d]", fragment.getName(), getStart(), getEnd()), process, this.fragment);
    }

    /**
     * Kill Fragment
     */
    void exitProcess() {
        this.process.kill();
    }

    /**
     * Get the result, if it's null, it hasn't finished
     * @return result
     */
    Object getResult() {
        if (process == null)
            return null;
        Object result = process.getReturnValue();
        if (process.getReturnValue().getClass() == DataMaRe.class) {
            if (result == null) return null;
            this.fragment = (DataMaRe) result;
        }
        exitProcess();
        return result;
    }

    /**
     * Get the fragment's data
     * @return fragment data
     */
    DataMaRe getFragment() {
        return this.fragment;
    }

}
