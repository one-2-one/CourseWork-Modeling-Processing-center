public class Stack extends Element {
    boolean isTask;

    Stack(double delay) {
        super(delay);
        isTask = false;
    }

    Stack(double delay, double deviation) {
        super(delay, deviation);
        isTask = false;
    }

    @Override
    public void out() {
        super.out();
        super.allProcCount += 1;
        super.nextTime[0] = super.currentTime + super.getDelay(0);
        super.spentTime[0] += super.nextTime[0] - super.currentTime;
        Task task = new Task(currentTime);
        if (isTask) {
            double random = Math.random();
            if (random <= 0.5)
                task = new Task(currentTime, 1);
            else if (random <= 0.6)
                task = new Task(currentTime, 2);
            else
                task = new Task(currentTime, 3);
        } else {
            task = new Task(currentTime);
        }

        super.setNextElem(false).in(task);
        super.successTasks[0]++;
    }
}
