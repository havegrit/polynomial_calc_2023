package org.example;

public class Calc {
    public static int run(String exp){
        exp = exp.replaceAll("- ", "+ -");
        String[] bits = exp.split(" \\+ ");
        int sum = 0;
        for (String value : bits){
            sum += Integer.parseInt(value);
        }
        return sum;
    }
}
