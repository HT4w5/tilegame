package edu.tilegame.utils;

import java.util.Random;

public class RandomUtils {
    /**
     * Returns a random integer within [0, n).
     */
    public static int uniform(Random random, int n) {
        return random.nextInt(n);
    }

    /**
     * Returns a random integer within [a, b).
     */
    public static int uniform(Random random, int a, int b) {
        return a + random.nextInt(b - a);
    }

    /**
     * Randomly rearranges elements in an array.
     */
    public static void uniformShuffle(Random random, Object[] objs) {
        if (objs != null) {
            for (int i = objs.length - 1; i > 0; --i) {
                int j = uniform(random, i + 1);

                // Swap i-th and j-th element.
                Object tmp = objs[i];
                objs[i] = objs[j];
                objs[j] = tmp;
            }
        }
    }
}
