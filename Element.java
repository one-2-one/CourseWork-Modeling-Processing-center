import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;

public class Element {
    static int countID = 0;
    double currentTime;
    Double[] nextTime;
    double delayMean;
    double retentionDeviation;
    String distributionType;
    String processName;
    int id;
    int deviceCount;
    Integer[] states;
    Task[] tasks;
    int state;
    int failCount;
    int allProcCount;
    Vector<Element> nextElem;
    Vector<Double> elemProb;

    Integer[] successTasks;
    Double[] spentTime;
    Double queueObjTime;
    int isRandom;
    boolean taskBackToQueue = false;

    public Element() {
        this.currentTime = 0.0;
        this.nextTime = new Double[] { Double.MAX_VALUE };
        this.delayMean = 1.0;
        this.retentionDeviation = 1.0;
        this.distributionType = "normal";
        this.isRandom = 1;
        this.id = countID;
        this.deviceCount = 1;
        this.states = new Integer[] { 0 };
        this.tasks = new Task[] {
                new Task()
        };
        this.state = 0;
        this.queueObjTime = 0.0;

        this.failCount = 0;
        this.allProcCount = 0;
        this.nextElem = null;

        this.successTasks = new Integer[] { 0 };
        this.spentTime = new Double[] { 0.0 };

        this.processName = "PROC";

        this.nextElem = new Vector<>();
        this.elemProb = new Vector<>();

        countID++;
    }

    public Element(double del) {
        this.currentTime = 0.0;
        this.nextTime = new Double[] { Double.MAX_VALUE };
        this.delayMean = del;
        this.retentionDeviation = 1.0;
        this.distributionType = "";
        this.isRandom = 1;
        this.id = countID;
        this.state = 0;
        this.deviceCount = 1;
        this.states = new Integer[] { 0 };
        this.tasks = new Task[] { new Task() };
        this.failCount = 0;
        this.allProcCount = 0;
        this.nextElem = null;
        this.queueObjTime = 0.0;

        this.processName = "Default process name";

        this.successTasks = new Integer[] { 0 };
        this.spentTime = new Double[] { 0.0 };

        this.nextElem = new Vector<>();
        this.elemProb = new Vector<>();

        countID++;
    }

    public Element(double del, double deviation) {
        this.currentTime = 0.0;
        this.nextTime = new Double[] { Double.MAX_VALUE };
        this.delayMean = del;
        this.retentionDeviation = deviation;
        this.distributionType = "";
        this.isRandom = 1;
        this.id = countID;
        this.state = 0;
        this.deviceCount = 1;
        this.states = new Integer[] { 0 };
        this.tasks = new Task[] { new Task() };
        this.failCount = 0;
        this.allProcCount = 0;
        this.nextElem = null;
        this.queueObjTime = 0.0;

        this.processName = "Default process name";

        this.successTasks = new Integer[] { 0 };
        this.spentTime = new Double[] { 0.0 };

        this.nextElem = new Vector<>();
        this.elemProb = new Vector<>();

        countID++;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    public Element setNextElem(boolean taskBackToQueue) {
        int id = 0;
        this.taskBackToQueue = taskBackToQueue;

        if (this.isRandom == 0) {
            id = setNextPrior();
        } else if (this.isRandom == 1) {
            id = setNextRand();
        } else if (this.isRandom == 2) {
            id = this.setNextPriorQueue();
        } else if (this.isRandom == 3) {
            id = setNextReturn(taskBackToQueue);
        }

        return nextElem.get(id);
    }

    private int setNextReturn(boolean taskBackToQueue) {
        int id = 0;
        if (taskBackToQueue) {
            id = 1;
        }
        return id;
    }

    private int setNextPrior() {
        Double min = 1000000.0;
        int index = 0;
        for (int i = 0; i < elemProb.size(); i++) {
            if (Arrays.asList(nextElem.get(i).states).contains(0) && elemProb.get(i) < min) {
                min = elemProb.get(i);
                index = i;
            }
        }

        return index;
    }

    private int setNextPriorQueue() {
        Double min = 1000000.0;
        Double minPrior = 1000000.0;
        int ind = 0;
        for (int i = 0; i < elemProb.size(); i++) {
            if (nextElem.get(i).getQueue().size() < min
                    || (nextElem.get(i).getQueue().size() == min && elemProb.get(i) < minPrior)) {
                min = (double) (nextElem.get(i).getQueue().size());
                minPrior = elemProb.get(i);
                ind = i;
            }
        }
        return ind;
    }

    private int setNextRand() {
        Double sum = 0.0;
        for (Double element : elemProb) {
            sum += element;
        }

        Double res = Math.random() * sum;
        Double a = 0.0;
        int id = 0;

        for (int i = 0; i < elemProb.size(); i++) {
            a += elemProb.get(i);
            if (res <= a) {
                id = i;
                break;
            }
        }
        return id;
    }

    public void setNextElem(Element elem, Double prob) {
        this.nextElem.add(elem);
        this.elemProb.add(prob);
    }

    public void setDeviceCount(int count) {
        this.deviceCount = count;
        this.states = new Integer[count];
        this.tasks = new Task[count];
        this.nextTime = new Double[count];
        this.spentTime = new Double[count];
        this.successTasks = new Integer[count];

        for (int i = 0; i < count; i++) {
            this.states[i] = 0;
            this.nextTime[i] = Double.MAX_VALUE;
            this.spentTime[i] = 0.0;
            this.successTasks[i] = 0;
        }
        this.nextTime[0] = Double.MAX_VALUE;
    }

    public double getDelay(int proc) {
        double delay = delayMean;
            if (distributionType == "normal") {
                delay = Random.normal(delayMean, retentionDeviation);
            } else if (distributionType == "exponential") {
                delay = Random.exponential(delayMean);
            } else if (distributionType == "uniform") {
                delay = Random.uniform(delayMean, retentionDeviation);
            } else if (distributionType == "erlang") {
                delay = Random.erlang(delayMean, retentionDeviation);
            } 
            // else {
            //     if (tasks[proc].taskType == 1) {
            //         delay = Random.exponential(15.0);
            //     } else if (tasks[proc].taskType == 2) {
            //         delay = Random.exponential(40.0);
            //     } else if (tasks[proc].taskType == 3) {
            //         delay = Random.exponential(30.0);
            //     } else {
            //         delay = delayMean;
            //     }

            // }
        return delay;
    }

    public ArrayList<Task> getQueue() {
        return new ArrayList<>() {
        };
    }

    public void setQueue(ArrayList<Task> q) {
    }

    public void in(Task task) {
    }

    public void out() {
    }
}
