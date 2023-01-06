import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        experiments();
    }



    public static void run(){
        Vector<Element> list = new Vector<>();

        Create producer = new Create(300, 100);
        producer.processName = "Producer";

        Process input = new Process(5, 2);
        input.processName = "Input";
        input.setDeviceCount(1);
        input.maxQueue = Integer.MAX_VALUE;

        Process processing = new Process(5, 2);
        processing.processName = "Processing";
        processing.setDeviceCount(1);
        processing.maxQueue = Integer.MAX_VALUE;

        Process output = new Process(5, 2);
        output.setDeviceCount(1);
        output.maxQueue = Integer.MAX_VALUE;
        output.processName = "Output";
        output.nextElemType = 3;

        producer.dis_type = "exponent";
        input.dis_type = "exponent";
        processing.dis_type = "exponent";
        output.dis_type = "exponent";

        producer.setNextElement(input, 1.0);
        input.setNextElement(processing, 1.0);
        processing.setNextElement(output, 1.0);
        output.setNextElement(null, 0.95);
        output.setNextElement(input, 0.05);

        producer.nextTime = new Double[] { 0.0 };

        list.add(producer);
        list.add(input);
        list.add(processing);
        list.add(output);

        Model model = new Model(list, false);
        model.simulate(1000000);
        model.cleanup();

    }

    public static void experiments(){
        Vector<Element> list = new Vector<>();

        Create producer = new Create(300, 100);
        producer.processName = "Producer";

        Process input = new Process(5, 2);
        input.processName = "Input";
        input.setDeviceCount(1);
        input.maxQueue = Integer.MAX_VALUE;

        Process processing = new Process(5, 2);
        processing.processName = "Processing";
        processing.setDeviceCount(1);
        processing.maxQueue = Integer.MAX_VALUE;

        Process output = new Process(5, 2);
        output.setDeviceCount(1);
        output.maxQueue = Integer.MAX_VALUE;
        output.processName = "Output";
        output.nextElemType = 3;

// Експеримент з експоненціальним розподілом

        // producer.dis_type = "exponent";
        // input.dis_type = "exponent";
        // processing.dis_type = "exponent";
        // output.dis_type = "exponent";

// Експеримент з нормальним розподілом

        // producer.dis_type = "normal";
        // input.dis_type = "normal";
        // processing.dis_type = "normal";
        // output.dis_type = "normal";

// Експеримент з рівномірним розподілом
// Найоптимальніший

        producer.dis_type = "even";
        input.dis_type = "even";
        processing.dis_type = "even";
        output.dis_type = "even";

// Експеримент з поєднанням розподілів

        // producer.dis_type = "exponent";
        // input.dis_type = "even";
        // processing.dis_type = "even";
        // output.dis_type = "even";

// Експеримент з поєднанням розподілів

        // producer.dis_type = "exponent";
        // input.dis_type = "even";
        // processing.dis_type = "exponent";
        // output.dis_type = "even";

// Експеримент з поєднанням розподілів

        // producer.dis_type = "even";
        // input.dis_type = "exponent";
        // processing.dis_type = "even";
        // output.dis_type = "even";

// Експеримент з поєднанням розподілів

        // producer.dis_type = "normal";
        // input.dis_type = "exponent";
        // processing.dis_type = "normal";
        // output.dis_type = "even";



        producer.setNextElement(input, 1.0);
        input.setNextElement(processing, 1.0);
        processing.setNextElement(output, 1.0);
        output.setNextElement(null, 0.95);
        output.setNextElement(input, 0.05);

        producer.nextTime = new Double[] { 0.0 };

        list.add(producer);
        list.add(input);
        list.add(processing);
        list.add(output);

        Model model = new Model(list, false);
        model.simulate(1000000);
        model.cleanup();
    }




}
