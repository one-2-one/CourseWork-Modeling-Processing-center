import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Element {
    static int idCount = 0;
    double currentTime;
    Double[] nextTime;
    double delayMean;
    double delayDev;
    String dis_type;
    String processName;
    int state;
    int id;
    int deviceCount;
    Integer[] states;
    Task[] tasks;
    int failCount;
    int successfulTasks;
    Vector<Element> nextElem;
    Vector<Double> elemProb;

    Integer[] doneTask;
    Double[] usedTime;
    Double queueObjects;
    int nextElemType;
    boolean isBack=false;

    public Element(){
        currentTime = 0.0;
        nextTime = new Double[]{Double.MAX_VALUE};
        delayMean = 1.0;
        delayDev = 1.0;
        dis_type = "normal";
        nextElemType = 2;
        id = idCount;
        deviceCount=1;
        states = new Integer[]{0};
        tasks = new Task[]{new Task()};
        state = 0;
        queueObjects = 0.0;

        failCount = 0;
        successfulTasks = 0;
        nextElem = null;

        doneTask = new Integer[]{0};
        usedTime = new Double[]{0.0};

        processName = "Default proccess name";

        nextElem = new Vector<>();
        elemProb = new Vector<>();

        idCount++;
    }

    public Element(double deviation){
        currentTime = 0.0;
        nextTime = new Double[]{Double.MAX_VALUE};
        delayDev = deviation;
        dis_type = "";
        nextElemType = 2;
        id = idCount;
        state = 0;
        deviceCount=1;
        states = new Integer[]{0};
        tasks = new Task[]{new Task()};
        failCount = 0;
        successfulTasks = 0;
        nextElem = null;
        queueObjects = 0.0;

        processName = "Default proccess name";

        doneTask = new Integer[]{0};
        usedTime =  new Double[]{0.0};

        nextElem = new Vector<>();
        elemProb = new Vector<>();

        idCount++;
    }

    public Element(double delay, double devitation){
        currentTime = 0.0;
        nextTime = new Double[]{Double.MAX_VALUE};
        delayMean = delay;
        delayDev = devitation;
        dis_type = "";
        nextElemType = 2;
        id = idCount;
        state = 0;
        deviceCount=1;
        states = new Integer[]{0};
        tasks = new Task[]{new Task()};
        failCount = 0;
        successfulTasks = 0;
        nextElem = null;
        queueObjects = 0.0;

        processName = "Default proccess name";

        doneTask = new Integer[]{0};
        usedTime =  new Double[]{0.0};

        nextElem = new Vector<>();
        elemProb = new Vector<>();

        idCount++;
    }

    public Element setNextElement(boolean isReturn){
        int id = 0;
        isBack = isReturn;

        switch (nextElemType){
            case 0:
            {
                id = setNextPriority();
                break;
            }
            case 1:
            {
                id = setNextRandom();
                break;
            }
            case 2:
            {
                id = setNextPriorityQueue();
                break;
            }
            case 3:
            {
                id = setNextReturnWay(isReturn);
                break;
            }
        }
        return nextElem.get(id);
    }

    private int setNextReturnWay(boolean isReturn){
        int ind=0;
        if (isReturn)
        {
            ind = 1;
        } 
        return ind;
    }

    private int setNextPriority(){
        Double min = 1000000.0;
        int ind = 0;
        for (int i=0; i<elemProb.size();i++){
            if (Arrays.asList(nextElem.get(i).states).contains(0) && elemProb.get(i)<min){
                min = elemProb.get(i);
                ind = i;
            }
        }

        return ind;
    }

    private int setNextPriorityQueue(){
        Double min = 1000000.0;
        Double minP = 1000000.0;
        int ind = 0;
        for (int i=0; i<elemProb.size();i++){
            if ((nextElem.get(i).getQueue().size() == min && elemProb.get(i) < minP) ||nextElem.get(i).getQueue().size()<min){
                min = (double)(nextElem.get(i).getQueue().size());
                minP=elemProb.get(i);
                ind = i;
            }
        }
        return ind;
    }

    private int setNextRandom(){
        Double sum = 0.0;
        for (Double elem: elemProb)
        {
            sum += elem;
        } 

        Double res = Math.random()*sum;
        Double tmp = 0.0;
        int ind = 0;

        for (int i=0; i<elemProb.size();i++)
        {
            tmp += elemProb.get(i);
            if (res <= tmp) {
                ind = i;
                break;
            }
        }
        return ind;
    }

    public void setNextElement(Element el, Double probability)
    {
        nextElem.add(el);
        elemProb.add(probability);
    }

    public void setDeviceCount(int count)
    {
        deviceCount=count;
        states = new Integer[count];
        tasks = new Task[count];
        nextTime = new Double[count];
        usedTime = new Double[count];
        doneTask = new Integer[count];

        for (int i=0; i<count;i++) 
        {
            states[i]=0;
            nextTime[i]=Double.MAX_VALUE;
            usedTime[i]=0.0;
            doneTask[i]=0;
        }
        nextTime[0] = Double.MAX_VALUE;
    }

    public double getDelay(int proc){
        double delay = this.delayMean;
        switch (dis_type)
        {
            case "normal":
                delay = Random.normal(delayMean,delayDev);
                break;
            case "exponent":
                delay = Random.exponential(delayMean);
                break;
            case "even":
                delay = Random.even(delayMean,delayDev);
                break;
            default:
                delay = delayMean;
                break;
        }
        return delay;
    }

    public ArrayList<Task> getQueue()
    {
        return new ArrayList<>(){};
    }

    public void setQueue(ArrayList<Task> q)
    {

    }

    public void in(Task task)
    {

    }
    public void out()
    {

    }
}
