package geneticprogram;
 
import java.lang.reflect.Array;

public class Program {
    private char[][]        main;                //[level in main brach][position in level]
    private char[][][][]    conditions; //[level in main branch rooted from][level in sub-branch][sub-branch level][position in level in sub-branch]

    public Program(int main_depth) {
        _allocateArrays(main_depth);
    }
    
    public Program(char[][] m, char[][][][] c) throws Exception{
        if(m != null && c != null){
            int depth   = m.length;
            _allocateArrays(depth);  
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
                        sub_level_size *= 2;
                    }
                }
                level_size  *= 2;
            }
            for (int position_level = 0; position_level < level_size; position_level++)
                    this.main[main_level][position_level]   = m[main_level][position_level];
        }else
            throw new Exception("Program could not be instantiated correctly.");
    }
    
    public Program(Program copy) throws Exception{
        this(copy.main,copy.conditions);
    }
      
    private void _allocateArrays(int main_depth){
        this.main       = new char[main_depth][];
        this.conditions = new char[main_depth][][][];
        int power       = 1;
        int main_level  = 0;
        for (; main_level < main_depth; main_level++) {
            this.main[main_level]       = new char[power];
            this.conditions[main_level] = new char[power][][];
            for (int pos_in_level = 0; pos_in_level < power; pos_in_level++) {
                int sub_level_size          = 1;
                for (int sub_branch_level = 0; sub_branch_level < Parameters.getInstance().getCondition_max_depth(); sub_branch_level++) {
                    this.conditions[main_level][pos_in_level][sub_branch_level] = new char[sub_level_size];
                    sub_level_size  *= 2;
                }
            }
            power           *= 2;
        } 
    }

    public char[][][][] getConditions() {
        return conditions;
    }

    public char[][] getMain() {
        return main;
    }
    
}

