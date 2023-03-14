package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp){
        if (!exp.contains(" ")) return Integer.parseInt(exp);

        exp = stripOuterBrackets(exp);

        boolean needToMulti = exp.contains("*");
        boolean needToPlus = exp.contains("+") || exp.contains("-");

        boolean needToCompound = exp.contains("*") && exp.contains("+");

        if(needToCompound) {
            String[] bits = exp.split(" \\+ ");
            String  newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)
                    .mapToObj(e -> e + "")
                    .collect(Collectors.joining(" + "));
            return run(newExp);
        } else if (needToMulti) {
            int mul = 1;
            String[] bits = exp.split(" \\* ");
            for (String value : bits){
                mul *= Integer.parseInt(value);
            }
            return mul;
        } else if (needToPlus) {
            int sum = 0;
            exp = exp.replaceAll("- ", "+ -");
            String[] bits = exp.split(" \\+ ");
            for (String value : bits){
                sum += Integer.parseInt(value);
            }
            return sum;
        }
        throw new RuntimeException("올바른 계산식이 아닙니다.");
    }

    private static String stripOuterBrackets(String exp) {
        int outerBracketsCount = 0;

        while ( exp.charAt(outerBracketsCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketsCount) == ')' ) {
            outerBracketsCount++;
        }

        if ( outerBracketsCount == 0 ) return exp;

        return exp.substring(outerBracketsCount, exp.length() - outerBracketsCount);
    }
}
