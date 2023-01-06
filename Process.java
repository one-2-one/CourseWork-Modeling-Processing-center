

import java.util.ArrayList;

public class Process extends Element{
    int maxQueue;

    ArrayList<Task> queue;

    Process(double delay){
        super(delay);
        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
    }

    Process(double delay, double devitation) {
        super(delay, devitation);
        queue = new ArrayList<>();
        maxQueue = Integer.MAX_VALUE;
    }

    public void setQueue(ArrayList<Task> q) {
        queue = q;
    }

    @Override
    public ArrayList<Task> getQueue() {
        return queue;
    }

    @Override
    public void in(Task task){

        int emptyQueue = 1;
        for (int i=0; i<states.length;i++) {
            if (super.states[i] == 0) {
                super.tasks[i]=task;
                super.states[i] = 1;
                super.nextTime[i] = super.currentTime + super.getDelay(i);
                super.usedTime[i] += super.nextTime[i] - super.currentTime;
                super.doneTask[i]++;
                emptyQueue = 0;
                super.successfulTasks +=1;
                break;
            }
        }
        if (emptyQueue ==1){
            if (queue.size() < maxQueue) {
                super.successfulTasks += 1;
                if (task.taskType == 1) {
                    int t=-1;
                    for (int i = 0; i < queue.size(); i++) {
                        if (queue.get(i).taskType != 1) 
                        {
                            queue.add(i,task);
                            t=i;
                            break;
                        }
                    }
                    if (t == -1)
                    {
                     queue.add(task);
                    }
                } else
                    queue.add(task);
            } else {
                failCount++;
            }
        }
    }

    @Override
    public void out(){
        super.out();
        int idOut = 0;
        double minT = Double.MAX_VALUE;
        for (int i=0; i<nextTime.length;i++)
        {
            if (nextTime[i]<minT)
            {
                minT = nextTime[i];
                idOut = i;
            }
        }
        super.nextTime[idOut] = Double.MAX_VALUE;
        super.states[idOut]= 0;
        Task tmpTask = tasks[idOut];
        boolean isBack = false;
        if (queue.size() > 0) 
        {
            tasks[idOut] = queue.get(0);
            queue.remove(0);
            super.states[idOut] = 1;
            super.nextTime[idOut] = super.currentTime + super.getDelay(idOut);
            super.usedTime[idOut] += super.nextTime[idOut] - super.currentTime;
            super.doneTask[idOut]++;
        }
        if (processName.equalsIgnoreCase("Output") && Random.getProbabilityOfFail()<=5)
        {
            failCount++;
            tmpTask.taskType=1;
            isBack=true;

        }
        if (nextElem != null && nextElem.size()>0) 
        {
            Element tmp = super.setNextElement(isBack);
            if (tmp!=null) 
            {
                tmp.in(tmpTask);
            }
            else{
                tmpTask.finishTime=currentTime;
                Model.addTask(tmpTask);
            }
        }
        else{
            tmpTask.finishTime=currentTime;
            Model.addTask(tmpTask);
        }
    }
}
