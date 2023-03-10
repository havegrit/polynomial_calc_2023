package org.example;

public class Calc {
    public static int run(String exp){
        exp = exp.replaceAll("- ", "+ -");
        String[] bits = exp.split(" ");
        int a = Integer.parseInt(bits[0]);
        int b = Integer.parseInt(bits[2]);
        int c = 0;
        if(bits.length > 3){
            c = Integer.parseInt(bits[4]);
        }
        return a+b+c;
    }
}
