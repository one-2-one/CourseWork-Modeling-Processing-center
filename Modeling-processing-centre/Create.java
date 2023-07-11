

public class Create extends Element{

    Create(double delay){
        super(delay);
    }

    Create (double delay, double deviation)
    {
        super(delay,deviation);
    }

    @Override
    public void out() {
        super.out();
        super.successfulTasks += 1;
        super.nextTime[0] = super.currentTime + super.getDelay(0);
        super.usedTime[0] += super.nextTime[0]-super.currentTime;
        Task tsk = new Task(currentTime);
        tsk = new Task(currentTime);
        super.setNextElement(false).in(tsk);
        super.doneTask[0]++;
    }
}
