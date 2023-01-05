import java.util.Vector;


public class Main {

    public static void main(String[] args) {
        Stack stack = new Stack(15.0);
        stack.processName = "Producer";
        stack.isTask = true;
        // stack.isRandom = 0;

        Process input = new Process(1.0);
        input.processName = "Input";
        input.setDeviceCount(1);
        input.maxQueue = Integer.MAX_VALUE;

        Process processing = new Process(5.5, 2.5);
        processing.processName = "Processing";
        processing.setDeviceCount(1);
        processing.maxQueue = Integer.MAX_VALUE;

        Process output = new Process(3.5, 1.5);
        output.processName = "Output";
        output.setDeviceCount(1);

        stack.distributionType = "exponential";
        input.distributionType = "exponential";
        processing.distributionType = "uniform";
        output.distributionType = "uniform";

        stack.setNextElem(input, 1.0);
        input.setNextElem(processing, 1.0);
        processing.setNextElem(output, 1.0);
        output.setNextElem(null, 0.95);
        output.setNextElem(input, 0.05);

        stack.nextTime = new Double[] {0.0};

        Vector<Element> list = new Vector<>();
        list.add(stack);
        list.add(input);
        list.add(processing);
        list.add(output);

        Model model = new Model(list);
        model.simulate(10000.0);
        model.cleanup();

    }

}
