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
            int depth   = m.length;
            main        = FlyWeight.getInstance().getCharArray2dMain();
            conditions  = FlyWeight.getInstance().getCharArray4d();
            //_allocateArrays(depth);  
            int main_level  = 0,
                level_size  = 1;
            for (; main_level < (depth - 1); main_level++) {
                for (int position_level = 0; position_level < level_size; position_level++) {
                    this.main[main_level][position_level]   = m[main_level][position_level];
                    for (int sub_level = 0; sub_level < Parameters.getInstance().getCondition_max_depth(); sub_level++) {
                        int sub_level_size = 1;
                        for (int sub_position_level = 0; sub_position_level < sub_level_size; sub_position_level++) {
                            this.conditions[main_level][position_level][sub_level][sub_position_level]  = c[main_level][position_level][sub_level][sub_position_level];
                        }
                        sub_level_size = sub_level_size << 1;
                    }
                }
                level_size = level_size << 1;
            }
            for (int position_level = 0; position_level < level_size; position_level++)
                    this.main[main_level][position_level]   = m[main_level][position_level];
        }else
            throw new Exception("Program could not be instantiated correctly.");
    }
 
    /**
     * 
     * @param copy
     * @throws Exception 
     */
    public void copy(Program copy) throws Exception{
        _copy(copy.main,copy.conditions);
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

