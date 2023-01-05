import java.util.Vector;
import java.util.ArrayList;

public class Model {
    Vector<Element> model = new Vector<>();
    double time, nextTime;
    int processNum;
    static ArrayList<Task> tasks;

    Model(Vector<Element> l) {
        this.model = l;
        this.nextTime = 0.0;
        this.processNum = 0;
        this.time = 0.0;
        tasks = new ArrayList<>();
    }

    public void cleanup() {
        this.nextTime = 0.0;
        this.processNum = 0;
        this.time = 0.0;
        Element.countID = 0;
        tasks = new ArrayList<>();
    }

    static void setNewTask(Task task) {
        if (tasks.size() > 5000) {
            tasks.remove(0);
        }
        tasks.add(task);
    }

    // public void simulateCar(double max_time) {
    //     double timeCarOut = 0.0;
    //     int allCarCount = 0;
    //     double allCarCountTime = 0.0;
    //     int carChangeRoute = 0;
    //     double allClientCountTime = 0.0;
    //     while (time < max_time) {
    //         carChangeRoute = changeRouteQueue(carChangeRoute);
    //         nextStep(false);
    //         double timePrev = runStep();
    //         if (!model.get(processNum).processName.equalsIgnoreCase("Producer")) {
    //             allCarCountTime += (time - timeCarOut);
    //             timeCarOut = time;
    //             allCarCount++;
    //         }
    //         allCarCount = runStep(timePrev, allCarCount);
    //         carChangeRoute = changeRouteQueue(carChangeRoute);
    //         allClientCountTime += getCountPeople(timePrev) / max_time;
    //     }
    //     double timeClient = 0.0;
    //     for (Task client : tasks) {
    //         timeClient += (client.endTime - client.startTime) / (double) tasks.size();
    //     }
    //     // getCarData(carChangeRoute, allClientCountTime, allCarCountTime / (double) allCarCount, timeClient);
    //     show_final_data(max_time);
    // }

    public double getCountPeople(double timePrev) {
        double result = 0.0;
        for (int i = 0; i < model.get(1).states.length; i++)
            result += model.get(1).states[i];
        for (int i = 0; i < model.get(2).states.length; i++)
            result += model.get(2).states[i];
        result += model.get(1).getQueue().size() + model.get(1).getQueue().size();
        return result * ((time - timePrev));
    }

    // private Integer changeRouteQueue(int changeRoute) {
    //     if (-2 > (model.get(1).getQueue().size() - model.get(2).getQueue().size())) {
    //         Client el = model.get(2).getQueue().get(model.get(2).getQueue().size() - 1);
    //         model.get(1).getQueue().add(el);
    //         changeRoute += 1;
    //     } else if (2 < (model.get(1).getQueue().size() - model.get(2).getQueue().size())) {
    //         Client el = model.get(1).getQueue().get(model.get(1).getQueue().size() - 1);
    //         model.get(2).getQueue().add(el);
    //         changeRoute += 1;
    //     }
    //     return changeRoute;
    // }

    private void nextStep(Boolean show_inner_stat) {
        nextTime = Double.MAX_VALUE;
        for (Element element : model) {
            double nextTime_h = Double.MAX_VALUE;
            for (Double elem : element.nextTime) {
                if (elem < nextTime_h) {
                    nextTime_h = elem;
                }
            }
            if (nextTime_h < nextTime) {
                nextTime = nextTime_h;
                processNum = element.id;
            }
        }
        if (show_inner_stat) {
            System.out.println("\nIt's time for event in " +
                    model.get(processNum).processName +
                    ", time = " + nextTime);
        }
    }

    private void nextStep(double oldTime) {
        for (Element element : model) {
            for (Double ele : element.nextTime) {
                if (ele == time) {
                    element.out();
                }
            }
            if (time - oldTime != 0)
                element.queueObjTime += (double) element.getQueue().size() * (time - oldTime);
        }
    }

    private double runStep() {
        double oldTime = time;
        time = nextTime;
        for (Element element : model) {
            element.currentTime = time;
        }

        model.get(processNum).out();
        return oldTime;
    }

    // private int runStep(double time_old, int carCount) {
    //     for (Element element : model) {
    //         for (Double elem : element.nextTime) {
    //             if (elem == time) {
    //                 carCount++;
    //                 element.out();
    //             }
    //         }
    //         if (time - time_old != 0)
    //             element.queueObjTime += (double) element.getQueue().size() * (time - time_old);
    //     }
    //     return carCount;
    // }

    // private void getCarData(int num, double peopleCount, double carExit, double mean_in) {
    //     System.out.println("All tasks in average in bank was " + peopleCount + " persons");
    //     System.out.println("tasks spend in bank in average " + mean_in + " time(s)");
    //     System.out.println("All tasks, that left is: " + carExit);
    //     System.out.println("Cars changed queue " + num + " time(s)");

    // }

    // private void getHospitalData(double mean_in, double mean_enter) {
    //     System.out.println("tasks spend in hospital in average: " + mean_in + " s.");
    //     System.out.println("The time between tasks in laboratory " + mean_enter + " s.");
    // }

    private void show_final_data(double maxTime) {
        double successCount = 0.0, failCount = 0.0;
        for (Element element : model) {
            if (element.deviceCount < 10)
                for (int i = 0; i < element.deviceCount; i++) {
                    System.out.println(element.processName + ": device- " + (i + 1));
                    if (element.successTasks[i] > 0) {
                        System.out.println("Mean speed: " + (element.successTasks[i] / element.spentTime[i])
                                + " tasks for current session");
                        System.out.println("Mean use is: " + (element.successTasks[i] / time) + " for all sessions");
                    }
                    System.out.println();
                }
            if (element.processName.equalsIgnoreCase("Producer")) {
                System.out.println("Producer elements count is " + element.allProcCount);
                successCount += element.allProcCount;
            } else {
                failCount += element.failCount;
                System.out.println("Fail count is: " + element.failCount + ", or "
                        + (element.failCount / (double) (element.allProcCount + element.failCount) * 100) + "%");
                System.out.println(
                        "Mean queue is: " + element.queueObjTime / (Math.max(time, maxTime)) + " for all sessions");
            }
        }
        System.out.println("All fail elements is: " + failCount + ", or " + failCount / (successCount) * 100 + "%\n");
    }

    public void simulate(double maxTime, boolean show_inner_stat) {
        while (time < maxTime) {
            nextStep(show_inner_stat);
            double oldTime = runStep();
            nextStep(oldTime);
        }
        show_final_data(maxTime);
    }

    public void simulate(double maxTime) {
        double p_ex_old = 0.0;
        double p_ex_time = 0.0;
        int p_en_count = 0;
        while (time < maxTime) {
            nextStep(false);
            double time_old = runStep();
            if (!model.get(processNum).processName.equalsIgnoreCase("goToLaboratory") &&
                    model.get(processNum).taskBackToQueue) {
                p_ex_time += (time - p_ex_old);
                p_ex_old = time;
                p_en_count++;
                model.get(processNum).taskBackToQueue = false;
            }

            nextStep(time_old);
        }
        double taskTime = 0.0;
        for (Task task : tasks) {
            taskTime += (task.endTime - task.startTime) / (double) tasks.size();
        }
        // getHospitalData(clientTime, p_ex_time / p_en_count);
        show_final_data(maxTime);
    }
}
