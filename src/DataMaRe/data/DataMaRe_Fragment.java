package DataMaRe.data;

import DataMaRe.jobs.Job;
import DataMaRe.jobs.tasks.DataMaReProcess_Edit;

public class DataMaRe_Fragment {

    private DataMaRe fragment;
    private int start, end;
    private Job process;

    DataMaRe_Fragment(DataMaRe data, int start, int end) {
        start = start < 0 ? 0 : start > data.getRows() - 1 ? data.getRows() - 1 : start;
        end = end > data.getRows() - 1 ? data.getRows() - 1 : end <= start ? start : end;
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

    void shift(int delta) {
        this.start += delta;
        this.end += delta;
        this.process.updateName(String.format("%s [%d-%d]", fragment.getName(), getStart(), getEnd()));
    }


    void runProcess(DataMaReProcess_Edit process) {
        if (this.process != null || this.fragment == null) {
            System.err.println("Process Creation Failed");
            return;
        }
        this.process = new Job(String.format("%s [%d-%d]", fragment.getName(), getStart(), getEnd()), process, this.fragment);
    }

    void exitProcess() {
        this.process.kill();
    }


    DataMaRe getResult() {
        if (process == null)
            return null;
        DataMaRe result = (DataMaRe) process.getReturnValue();
        if (result == null) return null;
        this.fragment = result;
        exitProcess();
        return getFragment();
    }

    DataMaRe getFragment() {
        return this.fragment;
    }

}
