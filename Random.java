public class Random {
    
    public static double normal(double s, double a) {
        double p = 0;
        for (int i = 0; i < 12; i++) {
            p += Math.random();
        }
        p -= 6;
        return s * p + a;
    }

    public static double uniform(double a, double b) {
        double p = Math.random() * (b - a) + a;
        return p;
    }

    public static double exponential(double param) {
        double p = Math.random();
        while (p == 1.0 || p == 0.0) {
            p = Math.random();
        }
        return (-param) * Math.log(p);
    }

    public static double erlang(double a, double b) {
        double p = 1.0;
        for (int i = 0; i < b; i++) {
            p = p * Math.random();
        }
        p = Math.log(p);
        p = -p * a / b;
        return p;
    }

}
