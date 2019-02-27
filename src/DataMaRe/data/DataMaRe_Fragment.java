package DataMaRe.data;

import DataMaRe.jobs.Job;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

public class DataMaRe_Fragment {

    private DataMaRe fragment;
    private int start, end;
    private Job process;

    DataMaRe_Fragment(DataMaRe data, int start, int end) {
        this.start = start < 0 ? 0 : start > data.getRows() - 1 ? data.getRows() - 1 : start;
        this.end = end > data.getRows() ? data.getRows() : end < start ? start : end;
        this.fragment = new DataMaRe(data.getName(), data.getHeaders(), data.getTypes(), data.getData(this.start, this.end));
    }

    int getStart() {
        return start;
    }

    int getEnd() {
        return end;
    }

    void shift(int delta) {
        this.start -= delta;
        this.end -= delta;
        this.process.updateName(String.format("%s [%d-%d]", fragment.getName(), this.start, this.end));
    }


    void runProcess(DataMaReProcess_Edit process) {
        if (this.process != null || this.fragment == null) {
            System.err.println("Process Creation Failed");
            return;
        }
        this.process = new Job(String.format("%s [%d-%d]", fragment.getName(), this.start, this.end), process, this.fragment);
        this.process.getJob().run();
    }


    DataMaRe getFragment() {
        if (process == null)
            return null;
        DataMaRe result = (DataMaRe) process.getJob().getResult();
        if (result == null) return null;
        return result;
    }

}
