package geneticprogram;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class FlyWeight {

    private static FlyWeight singleton = null;
    private final ArrayList<ArrayList<int[]>> al_int_arr;
    private final ArrayList<ArrayList<Integer>> al_int;
    private final ArrayList<Stack<Integer>> stack_int;
    private final ArrayList<byte[][]> shrt_arr_2D_m;
    private final ArrayList<byte[][]> shrt_arr_2D_c;
    private final ArrayList<byte[][][][]> shrt_arr_4D;
    private final ArrayList<Program> programs;
    private final ArrayList<Generation> generations;
    private final ArrayList<GeneticOperatorThread> go_threads;
    private final ArrayList<ArrayList<GeneticOperatorThread>> go_arr_threads;
    private final ArrayList<Random> random_arr;
    private final ArrayList<CyclicBarrier> cb_arr;

    private FlyWeight() {
        go_arr_threads = new ArrayList<>();
        cb_arr = new ArrayList<>();
        random_arr = new ArrayList<>();
        go_threads = new ArrayList<>();
        al_int_arr = new ArrayList<>();
        al_int = new ArrayList<>();
        stack_int = new ArrayList<>();
        shrt_arr_2D_m = new ArrayList<>();
        shrt_arr_2D_c = new ArrayList<>();
        shrt_arr_4D = new ArrayList<>();
        programs = new ArrayList<>();
        generations = new ArrayList<>();
    }

    /**
     * @return singleton.
     */
    public static FlyWeight getInstance() {
        if (singleton == null) {
            singleton = new FlyWeight();
        }
        return singleton;
    }

    public void addCyclicBarrier(CyclicBarrier obj) throws Exception {
        if (obj != null) {
            cb_arr.add(obj);
        } else {
            throw new Exception("Cannot add null CyclicBarrier object");
        }
    }

    public CyclicBarrier getCyclicBarrier() {
        if (cb_arr.isEmpty()) {
            return new CyclicBarrier(0);
        } else {
            return cb_arr.remove(0);
        }
    }

    public void addGeneticOperatorThread(GeneticOperatorThread thread) {
        go_threads.add(thread);
    }

    public GeneticOperatorThread getGeneticOperatorThread() {
        if (go_threads.isEmpty()) {
            return new GeneticOperatorThread();
        } else {
            GeneticOperatorThread thread = go_threads.remove(0);
            return thread;
        }
    }

    public void addGeneticOperatorThread(ArrayList<GeneticOperatorThread> obj) throws Exception {
        if (obj != null) {
            go_arr_threads.add(obj);
        } else {
            throw new Exception("Cannot add null ArrayList<GeneticOperatorThread> obj ");
        }
    }

    public ArrayList<GeneticOperatorThread> getGeneticOperatorThreads() {
        if (go_arr_threads.isEmpty()) {
            return new ArrayList<GeneticOperatorThread>();
        } else {
            ArrayList<GeneticOperatorThread> thread = go_arr_threads.remove(0);
            thread.clear();
            return thread;
        }
    }

    public void addRandom(Random obj) throws Exception {
        if (obj != null) {
            random_arr.add(obj);
        } else {
            throw new Exception("Cannot add null Random obj");
        }
    }

    public Random getRandom() {
        synchronized (singleton) { 
            if (random_arr.isEmpty()) {
                return new Random();
            } else {
                return random_arr.remove(0);
            }
        }
    }

    /**
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getArrayListInt() {
        ArrayList<Integer> al;
        if (!al_int.isEmpty()) {
            al = al_int.remove(0);
            al.clear();
        } else {
            al = new ArrayList<>();
        }

        return al;
    }

    /**
     * @param al
     * @throws Exception
     */
    public void addArrayListInt(ArrayList<Integer> al) throws Exception {
        if (al != null) {
            al_int.add(al);
        } else {
            throw new Exception("Cannot add a null ArrayList<Integer> object to flyweight. ");
        }

    }

    /**
     * @return a Generation
     * @throws Exception
     */
    public Generation getGeneration() throws Exception {
        Generation gen = null;
        if (!generations.isEmpty()) {
            gen = generations.remove(0);
        } else {
            gen = new Generation();
        }
        return gen;
    }

    /**
     * @param generation
     * @throws Exception
     */
    public void addGeneration(Generation generation) throws Exception {
        if (generation != null) {
            generations.add(generation);
        } else {
            throw new Exception("Cannot add a null Generation object to flyweight.");
        }
    }

    /**
     * @return program
     * @throws Exception
     */
    public Program getProgram() throws Exception {
        Program prog = null;
        if (!programs.isEmpty()) {
            prog = programs.remove(0);
        } else {
            prog = new Program();
        }
        return prog;
    }

    public void addProgram(Program prog) throws Exception {
        if (prog != null) {
            programs.add(prog);
        } else {
            throw new Exception("Cannot add a null Program object to flyweight.");
        }
    }

    /**
     * @return ArrayList<int[]>
     */
    public ArrayList<int[]> getArrayListIntArray() {
        ArrayList<int[]> obj = null;
        if (!al_int_arr.isEmpty()) {
            obj = al_int_arr.remove(0);
            obj.clear();
        } else {
            obj = new ArrayList<>();
        }
        return obj;
    }

    /**
     * @param obj
     * @throws Exception
     */
    public void addArrayListIntArray(ArrayList<int[]> obj) throws Exception {
        if (obj != null) {
            al_int_arr.add(obj);
        } else {
            throw new Exception("Cannot add null ArrayList<int[]> object to flyweight.");
        }
    }

    /**
     * @return Stack<Integer>
     */
    public Stack<Integer> getStackInteger() {
        Stack<Integer> obj = null;
        if (!stack_int.isEmpty()) {
            obj = stack_int.remove(0);
            obj.clear();
            obj.setSize(0);
        } else {
            obj = new Stack<>();
        }
        return obj;
    }

    /**
     * @param obj
     * @throws Exception
     */
    public void addStackInteger(Stack<Integer> obj) throws Exception {
        if (obj != null) {
            stack_int.add(obj);
        } else {
            throw new Exception("Cannot add null Stack<Integer> object to flyweight.");
        }
    }

    /**
     * @return byte[][]
     * @throws Exception
     */
    public byte[][] addByteArray2dMain() throws Exception {
        byte[][] arr;
        if (!shrt_arr_2D_m.isEmpty()) {
            arr = shrt_arr_2D_m.remove(0);
        } else {
            arr = new byte[Parameters.getInstance().getMain_max_depth()][2 << (Parameters.getInstance().getMain_max_depth() - 1)];
        }
        return arr;
    }

    /**
     * @return byte[][]
     * @throws Exception
     */
    public byte[][] getByteArray2dCondtion() throws Exception {
        byte[][] arr;
        if (!shrt_arr_2D_c.isEmpty()) {
            arr = shrt_arr_2D_c.remove(0);
        } else {
            arr = new byte[Parameters.getInstance().getCondition_max_depth()][2 << (Parameters.getInstance().getCondition_max_depth() - 1)];
        }
        return arr;
    }

    public byte[][] getByteArray2dMain() throws Exception {
        byte[][] arr;
        if (!shrt_arr_2D_m.isEmpty()) {
            arr = shrt_arr_2D_m.remove(0);
        } else {
            arr = new byte[Parameters.getInstance().getMain_max_depth()][2 << (Parameters.getInstance().getMain_max_depth() - 1)];
        }
        return arr;
    }

    /**
     * @param arr
     */
    public void addByteArray2dMain(byte[][] arr) throws Exception {
        if (arr != null) {
            shrt_arr_2D_m.add(arr);
        } else {
            throw new Exception("Cannot add null byte[][] object to flyweight.");
        }
    }

    /**
     * @param arr
     */
    public void addByteArray2dCondition(byte[][] arr) throws Exception {
        if (arr != null) {
            shrt_arr_2D_c.add(arr);
        } else {
            throw new Exception("Cannot add null byte[][] object to flyweight.");
        }
    }

    /**
     * @return byte[][][][]
     * @throws Exception
     */
    public byte[][][][] addByteArray4d() throws Exception {
        byte[][][][] arr;
        if (!shrt_arr_4D.isEmpty()) {
            arr = shrt_arr_4D.remove(0);
        } else {
            int main = 2 << (Parameters.getInstance().getMain_max_depth() - 1);
            int cond = 2 << (Parameters.getInstance().getCondition_max_depth() - 1);
            arr = new byte[Parameters.getInstance().getMain_max_depth()][main][Parameters.getInstance().getCondition_max_depth()][cond];
        }
        return arr;
    }

    /**
     * @param arr
     * @throws Exception
     */
    public void addByteArray4D(byte[][][][] arr) throws Exception {
        if (arr != null) {
            shrt_arr_4D.add(arr);
        } else {
            throw new Exception("Cannot add null byte[][][][] object to flyweight.");
        }
    }

}
