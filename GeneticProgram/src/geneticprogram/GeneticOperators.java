package geneticprogram;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class GeneticOperators {

    /**
     * @param prog - program to full
     * @param depth - depth to grow to
     * @throws Exception
     */
    public static void grow(Program prog, int depth, long seed) throws Exception {
        if (prog != null) { 
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            Helper._createMain(prog, depth, 0, 0, false, rand);
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot grow null program.");
        }
    }

    /**
     * @param prog - program to full
     * @param depth - depth to grow to
     * @throws Exception
     */
    public static void full(Program prog, int depth, long seed) throws Exception {
        if (prog != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            Helper._createMain(prog, depth, 0, 0, true, rand);
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot full null program.");
        }
    }

    /**
     * @param prog - program to mutate.
     * @throws Exception
     */
    public static void mutate(Program prog, long seed) throws Exception {
        if (prog != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            if (rand.nextBoolean() || !Helper._mutateCondition(prog, rand)) {
                /*MUTATE MAIN TREE*/
                Helper._mutateMain(prog, rand);
            }
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot mutate null program.");
        }
    }

    /**
     * @param prog
     * @throws Exception
     */
    public static void hoist(Program prog, long seed) throws Exception {
        if (prog != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            int[] pos = Helper.getMainFunction(prog.getMain(), true, rand);
            FlyWeight.getInstance().addRandom(rand);
            int level = pos[0];
            int position = pos[1];
            byte[][] main = prog.getMain();
            byte[][][][] cond = prog.getConditions();
            for (int l = 0; l < Parameters.getInstance().getMain_max_depth() - level; l++) {
                int level_offset = level;
                int position_offset = (l * position); //starting position in level

                for (int p = 0; p < (1 << l); p++) {
                    main[l][p] = main[level + l][position_offset + p];
                    for (int cl = 0; cl < Parameters.getInstance().getCondition_max_depth(); cl++) {
                        System.arraycopy(cond[level + l][position_offset + p][cl], 0, cond[l][p][cl], 0, 1 << cl);
                    }
                }
            }
        } else {
            throw new Exception("Cannot hoist null program.");
        }
    }

    /**
     * @param a - parent A.
     * @param b - parent B.
     * @throws Exception
     */
    public static void crossover(Program a, Program b, long seed) throws Exception {
        if (a != null && b != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            if (rand.nextBoolean() || !Helper._crossoverCondition(a, b, rand)) {
                Helper._crossoverMain(a, b, rand);
            }
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot crossover null programs.");
        }
    }

    /**
     * @param prog - program to edit.
     * @throws Exception
     */
    public static void edit(Program prog, long seed) throws Exception {
        if (prog != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            byte[][] tree = null;
            ArrayList<int[]> points;
            int[] position = Helper.getMainFunction(prog.getMain(), true, rand);
            int depth;
            if (Randomness.getInstance().getRandomBoolean()) {
                /*Edit Main Tree*/
                tree = prog.getMain();
                depth = Parameters.getInstance().getMain_max_depth();
            } else {
                /*Edit Condition Sub-tree*/
                position = Helper.getConditionFunction(prog.getConditions()[position[0]][position[1]], true);
                depth = Parameters.getInstance().getCondition_max_depth();
            }
            if (false && Meta.debug) {
                System.out.print("points    : [");
                for (int[] point : points) {
                    System.out.print("(" + Arrays.toString(point) + "," + tree[point[0]][point[1]] + ")");
                }
                System.out.print("]\n");
                System.out.println("" + Arrays.toString(position));
            }
            for (int level = position[0] + 1; level < depth; level++) {
                int pow = level - position[0] - 1;
                for (int pos = position[1] * (1 << pow); pos < (position[1] + 1) * (1 << pow); pos++) {
                    byte temp = tree[level][pos];
                    tree[level][pos] = tree[level][pos + ((position[1] + 1) * (1 << pow))];
                    tree[level][pos + pos + ((position[1] + 1) * (1 << pow))] = temp;
                }
            }
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot edit null program.");
        }
    }

}
