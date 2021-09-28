package geneticprogram;
 
import java.util.ArrayList;
import java.util.Stack;

public class FlyWeight {
    private static FlyWeight singleton = null;
    private final ArrayList<ArrayList<int[]>>   al_int_arr;
    private final ArrayList<Stack<Integer>>     stack_int;
    private final ArrayList<char[][]>           char_arr_2D_m;
    private final ArrayList<char[][]>           char_arr_2D_c;
    private final ArrayList<char[][][][]>       char_arr_4D;
    private final ArrayList<Program>            programs;
    private final ArrayList<Generation>         generations;
    
    private FlyWeight(){
        singleton       = new FlyWeight();
        al_int_arr      = new ArrayList<>();
        stack_int       = new ArrayList<>();
        char_arr_2D_m   = new ArrayList<>();
        char_arr_2D_c   = new ArrayList<>();
        char_arr_4D     = new ArrayList<>();
        programs        = new ArrayList<>();
        generations     = new ArrayList<>();
    } 
    
    
    
    /**
     * @return singleton.
     */
    public static FlyWeight getInstance(){
        if(singleton == null){
            singleton = new FlyWeight();
        }
        return singleton;
    }
    
    /**
     * @return a Generation
     * @throws Exception 
     */
    public Generation getGeneration() throws Exception{
        Generation gen = null;
        if(!generations.isEmpty()){
            gen = generations.remove(0);
        }else{
            gen = new Generation();
        }
        return gen;
    }
    
    /**
     * @param generation 
     */
    public void addGeneration(Generation generation) throws Exception{
        if(generation != null){
            generations.add(generation);
        }else
            throw new Exception("Cannot add a null Generation object to flyweight.");
    }
    
    /**
     * @return program
     * @throws Exception 
     */
    public Program getProgram() throws Exception{
        Program prog = null;
        if(!programs.isEmpty()){
            prog = programs.remove(0);
        }else{
            prog = new Program();
        }
        return prog;
    }
    
    
    public void addProgram(Program prog) throws Exception{
        if(prog != null){
            programs.add(prog);
        }else
            throw new Exception("Cannot add a null Program object to flyweight.");
    }
    
    /**
     * @return ArrayList<int[]>
     */
    public ArrayList<int[]> getArrayListIntArray(){
        ArrayList<int[]> obj = null;
        if(!al_int_arr.isEmpty()){
            obj = al_int_arr.remove(0);
            obj.clear();
        }else{
            obj = new ArrayList<int[]>();
        }
        return obj;
    }
    
    /**
     * @param obj
     * @throws Exception 
     */
    public void addArrayListIntArray(ArrayList<int[]> obj) throws Exception{ 
        if(obj != null){
            al_int_arr.add(obj);
        }else
            throw new Exception("Cannot add null ArrayList<int[]> object to flyweight."); 
    }
    
    /**
     * @return Stack<Integer>
     */
    public Stack<Integer> getStackInteger(){
        Stack<Integer> obj = null;
        if(!al_int_arr.isEmpty()){
            obj = stack_int.remove(0);
            obj.clear();
        }else{
            obj = new Stack<Integer>();
        }
        return obj;
    }
    
    /**
     * @param obj
     * @throws Exception 
     */
    public void addStackInteger(Stack<Integer> obj) throws Exception{
        if(obj != null){
            stack_int.add(obj);
        }else 
            throw new Exception("Cannot add null Stack<Integer> object to flyweight.");
    }
    
    /**
     * @return char[][]
     * @throws Exception 
     */
    public char[][] getCharArray2dMain() throws Exception{
        char[][] arr;
        if(!char_arr_2D_m.isEmpty()){
            arr = char_arr_2D_m.remove(0);
        }else{
            int power = 1;
            arr = new char[Parameters.getInstance().getMain_max_depth()][];
            for (int i = 0; i <Parameters.getInstance().getMain_max_depth() ; i++) {
                arr[i]  = new char[power];
                power   = power << 1;
            }
        }
        return arr;
    }
    
    /**
     * @return char[][]
     * @throws Exception 
     */
    public char[][] getCharArray2dCondtion() throws Exception{
        char[][] arr;
        if(!char_arr_2D_c.isEmpty()){
            arr = char_arr_2D_c.remove(0);
        }else{
            int power = 1;
            arr = new char[Parameters.getInstance().getMain_max_depth()][];
            for (int i = 0; i <Parameters.getInstance().getMain_max_depth() ; i++) {
                arr[i]  = new char[power];
                power   = power << 1;
            }
        }
        return arr;
    }
    
    /**
     * @param arr  
     */
    public void addCharArray2dMain(char[][] arr) throws Exception{
        if(arr != null){
            char_arr_2D_m.add(arr);
        }else 
            throw new Exception("Cannot add null char[][] object to flyweight.");
    }
    
    /**
     * @param arr  
     */
    public void addCharArray2dCondition(char[][] arr) throws Exception{
        if(arr != null){
            char_arr_2D_c.add(arr);
        }else 
            throw new Exception("Cannot add null char[][] object to flyweight.");
    }
    
    /**
     * @return char[][][][]
     * @throws Exception 
     */
    public char[][][][] getCharArray4d() throws Exception{
        char[][][][] arr;
        if(!char_arr_4D.isEmpty()){
            arr = char_arr_4D.remove(0);
        }else{
            int power   = 1;
            arr         = new char[Parameters.getInstance().getMain_max_depth()][][][];
            int i = 0;
            for (; i <Parameters.getInstance().getMain_max_depth() ; i++) {
                arr[i]  = new char[power][][];
                for (int j = 0; j < power; j++) {
                    int level_size = 1;
                    for (int k = 0; k < Parameters.getInstance().getCondition_max_depth(); k++) {
                        arr[i][j][k] = new char[level_size];
                        level_size = level_size << 1;
                    }
                }
                power   = power << 1;
            }
        }
        return arr;
    }
    
    /**
     * @param arr  
     * @throws Exception
     */
    public void addCharArray4D(char[][][][] arr) throws Exception{
        if(arr != null){
            char_arr_4D.add(arr);
        }else 
            throw new Exception("Cannot add null char[][][][] object to flyweight.");
    }
    
}
