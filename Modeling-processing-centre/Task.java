

public class Task {
    double startTime;
    double finishTime;
    int taskType;
    int taskSizeBytes;

    public Task(){
        startTime=0;
        finishTime=0;
        taskType=0;
        this.taskSizeBytes = Random.getTaskSizeBytes();
    }
    public Task(double start, int type) {
        startTime = start;
        finishTime = 0.0;
        taskType = type;
        this.taskSizeBytes = Random.getTaskSizeBytes();
    }
    public Task(double start){
        startTime=start;
        finishTime=0;
        taskType=0;
        this.taskSizeBytes = Random.getTaskSizeBytes();
    }


}
