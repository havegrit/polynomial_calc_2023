package org.example;

public class Calc {
    public static int run(String exp){
        String[] bits = exp.split(" ");
        int a = Integer.parseInt(bits[0]);
        int b = Integer.parseInt(bits[2]);
        String op = bits[1];
        if(op.equals("+")){
            return a+b;
        }
        return a-b;
    }
}
