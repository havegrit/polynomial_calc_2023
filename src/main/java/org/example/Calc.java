package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp){
        if (!exp.contains(" ")) return Integer.parseInt(exp);

        exp = stripOuterBrackets(exp);

        boolean needToMulti = exp.contains("*");
        boolean needToPlus = exp.contains("+") || exp.contains("-");
        boolean needToSplit = exp.contains("(") || exp.contains(")");

        boolean needToCompound = exp.contains("*") && exp.contains("+");

        if (needToSplit) {
            int splitPointIndex = findSplitPointIndex(exp);
            String firstExp = exp.substring(0, splitPointIndex-1);
            String operationCode = exp.substring(splitPointIndex-1, splitPointIndex + 2);
            String SecondExp = exp.substring(splitPointIndex + 2);

            return Calc.run(Calc.run(firstExp) + operationCode + Calc.run(SecondExp));
        } else if(needToCompound) {
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
    private static int findSplitPointIndexBy(String exp, char findChar) {
        int bracketsCount = 0;
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == '(') {
                bracketsCount++;
            } else if (c == ')') {
                bracketsCount--;
            } else if (c == findChar) {
                if (bracketsCount == 0) return i;
            }
        }
        return -1;
    }

    private static int findSplitPointIndex(String exp) {
        int index = findSplitPointIndexBy(exp, '+');
        if(index >= 0) return index;
        return findSplitPointIndexBy(exp, '*');
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
