

public class Random {
    public static double exponential(double p){
        double r = Math.random();
        while (r==0.0||r==1.0){
            r = Math.random();
        }
        return (-p)*Math.log(r);
    }
    public static double normal(double s, double a){
        double p=0;
        for(int i=0; i<12; i++){
            p+=Math.random();
        }
        p-=6;
        return s*p+a;
    }
    public static double even(double a, double b){
        double p = Math.random() * (b-a) + a;
        return p;
    }
    public static double erlang(double a, double b){
        double p =1.0;
        for (int i=0; i<b; i++){
            p = p*Math.random();
        }
        p = Math.log(p);
        p = -p*a/b;
        return p;
    }

    public static int getProbabilityOfFail() {
        int p = (int) (Math.random() * 100);
        return p;
    }

    public static int getTaskSizeBytes() {
        int p = (int) (Math.random() * (700 - 300) + 300);
        return p;
    }


}
