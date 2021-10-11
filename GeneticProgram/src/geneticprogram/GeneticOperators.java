package geneticprogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
            for (int hoist_level = 0; hoist_level < (Parameters.getInstance().getMain_max_depth() - level) ; hoist_level++) {
                int number_positions = 1 << hoist_level;
                int start_position = position <<  hoist_level;
                for (int hoist_position = 0; hoist_position < number_positions; hoist_position++) {
                    main[hoist_level][hoist_position] = main[level + hoist_level][start_position+hoist_position] ;
                    
                    for (int cd = 0; cd < Parameters.getInstance().getCondition_max_depth();cd++) {
                        System.arraycopy(cond[level + hoist_level][start_position+hoist_position][cd], 0, cond[hoist_level][hoist_position][cd], 0, 1 << cd);
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
     * @param seed
     * @throws Exception
     */
    public static void crossover(Program a, Program b, long seed) throws Exception {
        if (a != null && b != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            /*if (rand.nextBoolean() || !Helper._crossoverCondition(a, b, rand)) {
                Helper._crossoverMain(a, b, rand);
            }*/
            Helper._crossoverMain(a, b, rand);
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot crossover null programs.");
        }
    }

    /**
     * @param prog - program to edit.
     * @param seed
     * @throws Exception
     */
    public static void edit(Program prog, long seed) throws Exception {
        if (prog != null) {
            Random rand = FlyWeight.getInstance().getRandom();
            rand.setSeed(seed);
            byte[][] tree = null;
            int[] position = null;
            int depth;
            if (rand.nextBoolean()) {
                /*Edit Main Tree*/
                position = Helper.getMainFunction(prog.getMain(), true, rand);
                tree = prog.getMain();
                depth = Parameters.getInstance().getMain_max_depth();
            } else {
                /*Edit Condition Sub-tree*/
                 position = Helper.getMainFunction(prog.getMain(), true, rand);
                tree = prog.getConditions()[position[0]][position[1]];
                position = Helper.getConditionFunction(tree, true,rand);
                depth = Parameters.getInstance().getCondition_max_depth();
            } 
            int start_level = position[0],
                    start_position = position[1];
            for (int level = start_level ; level < depth; level++) {
                int begin_pos = start_position << (level - start_level); 
                int end_pos     = begin_pos + (1 << (level - start_level));
                for (int pos = begin_pos; pos < end_pos; pos++) {
                     byte temp = tree[level][pos]; 
                     tree[level][pos] =  tree[level][pos + (end_pos-begin_pos)];
                    tree[level][pos + (end_pos-begin_pos)]= temp;
                }
            }
            
            /*
            for (int level_offset = 1; level_offset < depth - start_level; level_offset++) {
                for (int position_offset = 0; position_offset < (1 << (level_offset-1)); position_offset++) {
                    int curr_level = start_level + level_offset;
                    int left_pos = (start_position << (1 << (level_offset-1))) + position_offset;
                    int right_pos = left_pos + (1 << (level_offset-1));
                    byte temp = tree[curr_level][right_pos]; 
                     tree[curr_level][right_pos] =  tree[curr_level][left_pos];
                      tree[curr_level][left_pos]= temp;
                }
            }
            */
            /*
            for (int level = position[0] + 1; level < depth; level++) {
                int pow = level - position[0] - 1;
                for (int pos = position[1] * (1 << pow); pos < (position[1] + 1) * (1 << pow); pos++) {
                    byte temp = tree[level][pos];
                    tree[level][pos] = tree[level][pos + ((position[1] + 1) * (1 << pow))];
                    tree[level][pos + pos + ((position[1] + 1) * (1 << pow))] = temp;
                }
            }*/
            FlyWeight.getInstance().addRandom(rand);
        } else {
            throw new Exception("Cannot edit null program.");
        }
    }

}
