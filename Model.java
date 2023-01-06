

import java.util.ArrayList;
import java.util.Vector;

public class Model {
    Vector<Element> model = new Vector<>();
    double time, nextTime;
    int processNum;
    int currentBuffer;
    boolean showAnalytics = false;
    static ArrayList<Task> tasksBase;

    Model(Vector<Element> l, boolean showAnalytics)
    {
        model = l;
        nextTime = 0.0;
        time = 0.0;
        processNum = 0;
        currentBuffer = 0;
        tasksBase = new ArrayList<>();
        this.showAnalytics=showAnalytics;
    }

    static void addTask(Task tsk){
        if (tasksBase.size() > 500000)
        { 
            tasksBase.remove(0);
        }
        tasksBase.add(tsk);
    }

    public void cleanup(){
        nextTime = 0.0;
        processNum = 0;
        time = 0.0;
        Element.idCount=0;
        tasksBase = new ArrayList<>();
    }

    public void simulate(double maxTime){
        double prExOld = 0.0;
        double prExTime = 0.0;
        int prEnCount = 0;
        int inputStep = 0;
        int processingStep = 0;
        int outputStep = 0;

        if (showAnalytics) 
        {
            System.out.println("\nprocessName;bufferSize");
        }

        while(time < maxTime)
        {
            step(false);
            double oldTime = execStep();
            if (!model.get(processNum).processName.equalsIgnoreCase("Producer")){
                prExTime += (time - prExOld);
                prExOld = time;
                prEnCount ++;
            }

            additionalStep(oldTime, inputStep,processingStep,outputStep);
        }

        double cli_time=0.0;
        for(Task tsk: tasksBase)
        {
            cli_time += (tsk.finishTime-tsk.startTime)/(double) tasksBase.size();
        }
        if(showAnalytics)
        {
            showAnalyticsData(maxTime);
        }
        else
        {
            show_final_data(maxTime);
            getDataResults(cli_time, prExTime / prEnCount);
        }


    }

    private void showAnalyticsData(double maxTime)
    {
        System.out.println("\ndeviceName;deviceN;doneTasks;busyness(%);meanQueueTasks;failCount");

        for (Element el : model) {
            for (int i=0; i<el.deviceCount;i++) {
                System.out.println(el.processName+";"+i+1+";"+ el.doneTask[i]+";" + (el.doneTask[i] / time)
                        / Math.max((el.doneTask[i] / el.usedTime[i]), (el.doneTask[i] / time))
                        * 100 +";"+ el.queueObjects / (Math.max(time, maxTime))+";"+ el.failCount);
            }
        }
    }

    private void additionalStep(double oldTime, int inputStep, int processingStep, int outputStep){
        for (Element el : model) 
        {
            for (Double ele: el.nextTime){
                if (ele  == time) {
                    el.out();

                }
            }
            if (time - oldTime != 0)
            {

                el.queueObjects += (double) el.getQueue().size() * (time - oldTime);
                if (showAnalytics)
                {

                    ArrayList<Task> currQueue = el.getQueue();
                    int bufferSize = 0;
                    for (Task task : currQueue) {
                        bufferSize += task.taskSizeBytes;
                    }
                    if (bufferSize != 0) {
                        if (el.processName.equalsIgnoreCase("Input"))
                        {
                            System.out.println( "input;"+ bufferSize);

                        }
                        else if (el.processName.equalsIgnoreCase("Processing"))
                        {
                            System.out.println( "processing;"+ bufferSize);
                        }
                        else if (el.processName.equalsIgnoreCase("Output"))
                        {
                            System.out.println( "output;"+ bufferSize);
                        }
                    }
                }



            }
        }
    }

    private void step(Boolean getStats){
        nextTime = Double.MAX_VALUE;
        for (Element el : model) {
            double nextTime_h = Double.MAX_VALUE;
            for (Double ele: el.nextTime){
                if (ele < nextTime_h) {
                    nextTime_h = ele;
                }
            }
            if (nextTime_h < nextTime) {
                nextTime = nextTime_h;
                processNum = el.id;
            }
        }
        if(getStats) {
            System.out.println("\nTime for event in " + model.get(processNum).processName + ", time = " + nextTime);
        }
    }

    private double execStep(){
        double oldTime = time;
        time = nextTime;
        for (Element el : model) {
            el.currentTime = time;
        }
        model.get(processNum).out();
        return oldTime;
    }


    private void getDataResults(double meanTimeIn, double meanTimeEnter){
        System.out.println("Mean time task spend while processing- "+ meanTimeIn +" minutes");
    }

    private void show_final_data(double maxTime){
        double successCount=0.0, failCount=0.0;
        for (Element el : model) {

            for (int i=0; i<el.deviceCount;i++) {
                System.out.println("\nFor " + el.processName + " device N" + (i + 1));
                if (el.doneTask[i] > 0) {
                    System.out.println("Tasks: " + el.doneTask[i] + " done");
                    System.out.println("Mean use- " + (el.doneTask[i]/el.usedTime[i]) + " task(s) while using");
                    System.out.println("Mean use- " + (el.doneTask[i]/time) + " task(s) during all time");
                    System.out.println("Busyness- " +
                            (el.doneTask[i] / time) / Math.max((el.doneTask[i] / el.usedTime[i]), (el.doneTask[i] / time))* 100 + " % of all time");
                }
                else 
                {
                    System.out.println("Device didn't using at all");
                }
            }
            if (el.processName.equalsIgnoreCase("Producer")){
                System.out.println("Produced count is: "+el.successfulTasks);
                successCount+=el.successfulTasks;
            }
            failCount += el.failCount;
            System.out.println("Fail count: " + el.failCount + ", or "
                    + (el.failCount / (double) (el.successfulTasks + el.failCount) * 100) + "%");
            System.out.println(
                    "Mean queue: " + el.queueObjects / (Math.max(time, maxTime)) + " task(s) for 1 worker");

        }
        System.out.println();
        System.out.println("Total fail count: "+failCount+", or "+failCount/(successCount+ failCount)*100 +"%");
    }
}
