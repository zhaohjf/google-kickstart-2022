import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InterestingIntegers {

    /**
     * dp[L][p2][p3][p5][p7][S]
     */
    private static long[][][][][][] dp = new long[14][8][5][3][3][118];

    private static Map<Integer, int[]> breakUp = new HashMap<>();

    static {
        // initial
        for (int L = 0; L < 14; L++) {
            for (int p2 = 0; p2 < 8; p2++) {
                for (int p3 = 0; p3 < 5; p3++) {
                    for (int p5 = 0; p5 < 3; p5++) {
                        for (int p7 = 0; p7 < 3; p7++) {
                            for (int S = 0; S < 118; S++) {
                                dp[L][p2][p3][p5][p7][S] = -1L;
                            }
                        }
                    }
                }
            }
        }
        double product = 1;
        for (int p2 = 0; p2 < 8; p2++) {
            for (int p3 = 0; p3 < 5; p3++) {
                for (int p5 = 0; p5 < 3; p5++) {
                    for (int p7 = 0; p7 < 3; p7++) {
                        dp[0][p2][p3][p5][p7][0] = 0;
                        if (p2 == 0) {
                            product = 0;
                        } else {
                            product = Math.pow(2, p2 - 1) * Math.pow(3, p3) * Math.pow(5, p5) * Math.pow(7, p7);
                        }
                        for (int S = 1; S < 118; S++) {
                            if ((product % S) == 0) {
                                dp[0][p2][p3][p5][p7][S] = 1;
                            } else {
                                dp[0][p2][p3][p5][p7][S] = 0;
                            }
                        }
                    }
                }
            }
        }

        /**
         * arrays represents the p2,p3,p5,p7
         *
         * key of the map of breakUp equals 2^p2 * 3^p3 * 5^p5 * 7^p7;
         *
         */
        breakUp.put(0, new int[]{0, -1, -1, -1});
        breakUp.put(1, new int[]{1, 0, 0, 0});
        breakUp.put(2, new int[]{2, 0, 0, 0});
        breakUp.put(3, new int[]{1, 1, 0, 0});
        breakUp.put(4, new int[]{3, 0, 0, 0});
        breakUp.put(5, new int[]{1, 0, 1, 0});
        breakUp.put(6, new int[]{2, 1, 0, 0});
        breakUp.put(7, new int[]{1, 0, 0, 1});
        breakUp.put(8, new int[]{4, 0, 0, 0});
        breakUp.put(9, new int[]{1, 2, 0, 0});
    }

    private static Map<Long, Long> resultMap = new HashMap<>();

    private static long f1(int L, int p2, int p3, int p5, int p7, int S) {

        p2 = Math.min(p2, 7); // p2加+1了
        p3 = Math.min(p3, 4);
        p5 = Math.min(p5, 2);
        p7 = Math.min(p7, 2);

        if (p2 == 0) {
            return S > 0 ? (long) (Math.pow(10, L)) : 0;
        }

        if (dp[L][p2][p3][p5][p7][S] != -1L) {
            return dp[L][p2][p3][p5][p7][S];
        }

        long count = 0;
        for (int digit = 0; digit < 10; digit++) {
            int pp2 = breakUp.get(digit)[0];
            int pp3 = breakUp.get(digit)[1];
            int pp5 = breakUp.get(digit)[2];
            int pp7 = breakUp.get(digit)[3];
            if (p2 == 0 || pp2 == 0) {
                count += f1(L - 1, 0, p3 + pp3, p5 + pp5, p7 + pp7, S + digit);
            } else {
                count += f1(L - 1, p2 + pp2 - 1, p3 + pp3, p5 + pp5, p7 + pp7, S + digit);
            }
        }
        dp[L][p2][p3][p5][p7][S] = count;
        return count;
    }

    private static long countInterestingIntegersWithNumberOfDigits(int L) {
        if (L == 1) {
            return 9;
        }
        long count = 0;
        for (int digit = 1; digit < 10; digit++) {
            int p2 = breakUp.get(digit)[0];
            int p3 = breakUp.get(digit)[1];
            int p5 = breakUp.get(digit)[2];
            int p7 = breakUp.get(digit)[3];
            count += f1(L - 1, p2, p3, p5, p7, digit);
        }

        return count;
    }

    private static long countInterestingIntegersWithPrefixOfN(long N, int p2, int p3, int p5, int p7, int S, int digitIndex, boolean isFirstDigit) {

        String strN = String.valueOf(N);
        int l = strN.length();
        if (digitIndex == l) {
            long product = (long) (Math.pow(2, p2 - 1) * Math.pow(3, p3) * Math.pow(5, p5) * Math.pow(7, p7));
            if (p2 == 0 || (S > 0 && product % S == 0)) {
                return 1;
            } else {
                return 0;
            }
        }
        int digitStart = 0;
        if (isFirstDigit) {
            digitStart = 1;
        }
        long count = 0;
        for (int digit = digitStart; digit < (strN.charAt(digitIndex) - '0'); digit++) {
            int pp2 = breakUp.get(digit)[0];
            int pp3 = breakUp.get(digit)[1];
            int pp5 = breakUp.get(digit)[2];
            int pp7 = breakUp.get(digit)[3];
            if (p2 == 0 || pp2 == 0) {
                count += f1(l - digitIndex - 1, 0, p3 + pp3, p5 + pp5, p7 + pp7, S + digit);
            } else {
                count += f1(l - digitIndex - 1, p2 + pp2 - 1, p3 + pp3, p5 + pp5, p7 + pp7, S + digit);
            }
        }
        int currentDigit = strN.charAt(digitIndex) - '0';
        int pp2 = breakUp.get(currentDigit)[0];
        int pp3 = breakUp.get(currentDigit)[1];
        int pp5 = breakUp.get(currentDigit)[2];
        int pp7 = breakUp.get(currentDigit)[3];
        int newP2 = p2 + pp2 - 1;
        if (p2 == 0 || pp2 == 0) {
            newP2 = 0;
        }
        count += countInterestingIntegersWithPrefixOfN(N, newP2, p3 + pp3, p5 + pp5, p7 + pp7,
                S + currentDigit, digitIndex + 1, false);
        return count;
    }

    private static long countInterestingIntegers(long N) {

        if (resultMap.containsKey(N)) {
            return resultMap.get(N);
        }

        if (N == 0) {
            return 0;
        }

        int numberLength = String.valueOf(N).length();
        long count = 0;
        for (int l = 1; l < numberLength; l++) {
            count += countInterestingIntegersWithNumberOfDigits(l);
        }
        count += countInterestingIntegersWithPrefixOfN(N, 1, 0, 0, 0, 0, 0, true);
        resultMap.put(N, count);
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int nCases = Integer.valueOf(scanner.nextLine());

        for (int i = 1; i <= nCases; ++i) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            long res2 = countInterestingIntegers(Long.valueOf(split[1]));
            long res1 = countInterestingIntegers(Long.valueOf(split[0]) - 1);
            System.out.println("Case #" + i + ": " + (res2 - res1));
        }
    }
}
