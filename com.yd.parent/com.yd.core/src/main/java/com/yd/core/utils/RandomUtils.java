package com.yd.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
public class RandomUtils {
    public static final Random JVM_RANDOM = new Random();

    public static String randNumber(int len) {
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int loc = 0;
        for (int i = 0; i < 7; i++) {
            loc = (int) (Math.random() * 9);
            int temp = a[i];
            a[i] = a[loc];
            a[loc] = temp;
        }
        String randomString = "";
        for (int i = 0; i < len; i++) {
            randomString += a[i];
        }
        return randomString;
    }

    public static List<String> randoms(int n) {
        List<String> res = new ArrayList<String>();
        Random r = new Random();

        int temp1, temp2;
        int send[] = new int[n];
        for (int i = 1; i <= n; i++) {
            send[i - 1] = i;
        }
        int len = send.length;
        int returnValue[] = new int[n];
        for (int i = 0; i < n; i++) {
            temp1 = Math.abs(r.nextInt()) % len;
            returnValue[i] = send[temp1];
            res.add(intToString(returnValue[i]));
            temp2 = send[temp1];
            send[temp1] = send[len - 1];
            send[len - 1] = temp2;
            len--;
        }

        return res;
    }

    public static List<Integer> randomList(List<Integer> list) {
        List<Integer> res = new ArrayList<Integer>();
        Random r = new Random();
        int len = list.size();
        int temp1, temp2;
        int send[] = new int[len];
        for (int i = 1; i <= len; i++) {
            send[i - 1] = list.get(i - 1);
        }
        for (int i = 0; i < list.size(); i++) {
            temp1 = Math.abs(r.nextInt()) % len;
            res.add(send[temp1]);
            temp2 = send[temp1];
            send[temp1] = send[len - 1];
            send[len - 1] = temp2;
            len--;
        }

        return res;
    }

    public static List<Integer> getGiveVipActivePartNumber(int needNumber, int partNumber) {
        List<Integer> result = new ArrayList<>();
        int totalNumber = needNumber;
        int current = partNumber;
        for (int i = 1; i <= partNumber - 1; i++) {
            int temp = totalNumber - current;
            temp = temp / 2;
            Random random = new Random();
            int randomNumber = 0;
            if (temp > 1) {
                randomNumber = (int) random.nextInt(temp);
            }
            result.add(randomNumber + 1);
            totalNumber = totalNumber - randomNumber - 1;
            current--;
        }
        result.add(totalNumber);
        result = randomList(result);
        return result;

    }

    public static int getScopeRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static int getScopeRanom(int scope) {
        Random r = new Random();
        return Math.abs(r.nextInt()) % scope;
    }

    private static String intToString(int value) {
        String temp = new String(value + "");
        String result = "";
        for (int i = 8; i > temp.length(); i--) {
            if (i == 8) {
                result += 1;
            } else {
                result += 0;
            }
        }

        return result + temp;
    }

    public static void main(String[] args) {
        int j = 1;

        while (j++ < 10) {
            int total = 57;
            int partNumber = 56;
            System.out.println("总参与人数 ：" + total + " 夺宝人数 ：" + partNumber);
            List<Integer> lsit = getGiveVipActivePartNumber(total, partNumber);
            for (int i = 0; i <= lsit.size() - 1; i++) {
                System.out.println("第" + (i + 1) + "个人参与 ：" + lsit.get(i) + "人次");
            }
            System.out.println("----------------------------------");
        }

    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value
     * between <code>0</code> (inclusive) and the specified value
     * (exclusive), from the Math.random() sequence.</p>
     *
     * @param n the specified exclusive max-value
     * @return the random int
     */
    public static int nextInt(int n) {
        return nextInt(JVM_RANDOM, n);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed int value
     * from the given <code>random</code> sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random int
     */
    public static int nextInt(Random random) {
        return random.nextInt();
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value
     * between <code>0</code> (inclusive) and the specified value
     * (exclusive), from the given Random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @param n      the specified exclusive max-value
     * @return the random int
     */
    public static int nextInt(Random random, int n) {
        // check this cannot return 'n'
        return random.nextInt(n);
    }
}
