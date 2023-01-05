import java.util.ArrayList;

public class Process extends Element {

    int maxQueue;
    ArrayList<Task> queue;

    Process(double delay, double deviation) {
        super(delay, deviation);
        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
    }

    Process(double delay) {
        super(delay);
        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
    }

    public void setQueue(ArrayList<Task> queue) {
        this.queue = queue;
    }

    @Override
    public ArrayList<Task> getQueue() {
        return queue;
    }

    @Override
    public void in(Task task) {
        int queueIsFull = 1;
        for (int i = 0; i < states.length; i++) {
            // System.out.println(super.tasks.length);
            if (super.states[i] == 0) {
                super.states[i] = 1;
                super.tasks[i] = task;
                super.nextTime[i] = super.currentTime + super.getDelay(i);
                super.spentTime[i] += super.nextTime[i] - super.currentTime;
                super.successTasks[i]++;
                queueIsFull = 0;
                super.allProcCount += 1;
                break;
            }
        }
        if (queueIsFull == 1) {
            if (queue.size() < maxQueue) {
                super.allProcCount += 1;
                if (task.taskType == 1) {
                    int t = -1;
                    for (int q = 0; q < queue.size(); q++) {
                        if (queue.get(q).taskType != 1) {
                            queue.add(q, task);
                            t = q;
                            break;
                        }
                    }
                    if (t == -1)
                        queue.add(task);
                } else
                    queue.add(task);
            } else {
                failCount++;
            }
        }
    }

    @Override
    public void out() {
        super.out();
        int idOut = 0;
        double minTime = Double.MAX_VALUE;
        for (int i = 0; i < nextTime.length; i++) {
            if (nextTime[i] < minTime) {
                minTime = nextTime[i];
                idOut = i;
            }
        }
        super.nextTime[idOut] = Double.MAX_VALUE;
        super.states[idOut] = 0;
        Task task = tasks[idOut];

        boolean isBack = false;

        if (queue.size() > 0) {
            tasks[idOut] = queue.get(0);
            queue.remove(0);
            super.states[idOut] = 1;
            super.nextTime[idOut] = super.currentTime + super.getDelay(idOut);
            super.spentTime[idOut] += super.nextTime[idOut] - super.currentTime;
            super.successTasks[idOut]++;
        }
        if (processName.equalsIgnoreCase("testAnalyses")) {
            task.taskType = 1;
            isBack = true;
        }
        if (nextElem != null && nextElem.size() > 0) {
            Element tmp = super.setNextElem(isBack);
            if (tmp != null) {
                tmp.in(task);
            } else {
                task.endTime = super.currentTime;
                Model.setNewTask(task);
            }
        } else {
            // System.out.println(task.endTime);
            task.endTime = super.currentTime;
            Model.setNewTask(task);
        }
    }
}
