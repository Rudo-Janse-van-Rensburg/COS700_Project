package geneticprogram;
  
public class Program {
    private char[][]        main;                //[level in main brach][position in level]
    private char[][][][]    conditions; //[level in main branch rooted from][level in sub-branch][sub-branch level][position in level in sub-branch]

    /**
     * @throws Exception 
     */
    public Program() throws Exception{
        main        = FlyWeight.getInstance().getCharArray2dMain();
        conditions  = FlyWeight.getInstance().getCharArray4d();
        //_allocateArrays(main_depth);
    }
    
    /**
     * @param m
     * @param c
     * @throws Exception 
     */
    private void _copy(char[][] m, char[][][][] c) throws Exception{
        if(m != null && c != null){  
            for (int main_depth = 0; main_depth < Parameters.getInstance().getMain_max_depth(); main_depth++) {
                System.arraycopy(m[main_depth], 0, this.main[main_depth], 0, (1 << main_depth));
                for (int condition = 0; condition < (1 << main_depth); condition++) {
                    for (int condition_depth = 0; condition_depth < Parameters.getInstance().getCondition_max_depth(); condition_depth++) {
                        System.arraycopy(c[main_depth][condition][condition_depth], 0, this.conditions[main_depth][condition][condition_depth], 0, (1 << condition_depth));
                    }
                }
            } 
        }else
            throw new Exception("Program could not be instantiated correctly.");
    }
 
    /**
     * 
     * @param copy
     * @throws Exception 
     */
    public void copy(Program copy) throws Exception{
        if(copy != null){
            _copy(copy.main,copy.conditions);
        }else 
            throw new Exception("Cannot copy null Program");
        
    }
    
    /**
     * @return 
     */
    public char[][][][] getConditions() {
        return conditions;
    }

    /**
     * @return 
     */
    public char[][] getMain() {
        return main;
    }
    
}

