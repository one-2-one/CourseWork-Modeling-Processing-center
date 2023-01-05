public class Task {
    double startTime;
    double endTime;
    int taskType;

    public Task() {
        this.startTime = 0.0;
        this.endTime = 0.0;
        this.taskType = 0;
    }

    public Task(double time) {
        this.startTime = time;
        this.endTime = 0.0;
        this.taskType = 0;
    }

    public Task(double time, int type) {
        this.startTime = time;
        this.endTime = 0.0;
        this.taskType = type;
    }

}
