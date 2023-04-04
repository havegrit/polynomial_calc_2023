package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static boolean debug = true;
    public static int runCallCount = 0;
    public static int run(String exp){
        runCallCount++;
        if (!exp.contains(" ")) return Integer.parseInt(exp);
        int[] pos = null;

        while ((pos = findCaseMinusBracket(exp)) != null) {
            exp = changeMinusBracket(exp, pos[0], pos[1]);
        }
        exp = stripOuterBrackets(exp);

        if (debug) {
            System.out.printf("exp(%d) = %s\n", runCallCount, exp);
        }
        exp.trim();
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

    private static String changeMinusBracket(String exp, int startPos, int endPos) {
        String head = exp.substring(0, startPos);
        String body = "(" + exp.substring(startPos + 1, endPos + 1) + " * -1)";
        String tail = exp.substring(endPos + 1);

        exp = head + body + tail;

        return exp;
    }

    private static int[] findCaseMinusBracket(String exp) {
        for (int i = 0; i < exp.length() - 1; i++) {
            if (exp.charAt(i) == '-' && exp.charAt(i + 1) == '(') {
                int bracketsCount = 1;

                for (int j = i + 2; j < exp.length(); j++) {
                    char c = exp.charAt(j);

                    if (c == '(') {
                        bracketsCount++;
                    } else if (c == ')') {
                        bracketsCount--;
                    }
                    if (bracketsCount == 0) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
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
        if (exp.charAt(0) == '(' && exp.charAt(exp.length() - 1) == ')') {
            int bracketsCount = 0;

            for (int i = 0; i < exp.length(); i++) {
                if (exp.charAt(i) == '(') {
                    bracketsCount++;
                } else if (exp.charAt(i) == ')') {
                    bracketsCount--;
                }

                if (bracketsCount == 0) {
                    if (exp.length() == i + 1) {
                        return stripOuterBrackets(exp.substring(1, exp.length() - 1));
                    }
                    return exp;
                }
            }
        }
        return exp;
    }
}
