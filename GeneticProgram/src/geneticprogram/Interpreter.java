package geneticprogram; 

import java.util.Stack;

public class Interpreter {  
    private static Interpreter singleton = null;

    private  Interpreter() {
    }
    
    public static Interpreter  getInstance(){
        if(singleton  == null){
            singleton = new Interpreter();
        }
        return singleton;
    }
    
    public int Interpret(Program p) throws Exception{
        char result = 0;
        if(p != null){
            Stack<Integer> row  = new Stack<Integer>();
            Stack<Integer> pos  = new Stack<Integer>();
            row.push(0);
            pos.push(0);
            return interpretMain(p,row,pos);
        }else 
            throw new Exception("Cannot interpret a null program.");

    }
        
    /***
     * @param prog  - a program to interpret.
     * @param row   - a stack for maintaining row position.
     * @param pos   - a stack for maintaining col position.
     * @return      - an int representing a class.
     * @throws Exception 
     */
    
    private int _interpretMain(Program prog,Stack<Integer> row,Stack<Integer> pos) throws Exception{
        if(row != null && pos != null && !row.empty() && !pos.empty()){
            int r,p;
            do{
                r = row.pop();
                if(r <= Parameters.getInstance().getMain_max_depth()){
                    /*Still not at the leaf of a main sub-branch*/
                    p = pos.pop();
                    switch(prog.getMain()[r][p]){
                        case MAIN_FUNCTIONS.IF:
                            /*Main Function*/
                            Stack<Integer>  c_row   = new Stack<>();
                            Stack<Integer>  c_pos   = new Stack<>();
                            c_row.push(0);
                            c_pos.push(0);
                            if(_interpretCondition(
                                        prog.getConditions()[r][p],
                                        c_row,
                                        c_pos
                                    ) != 0
                            ){
                                row.push(r + 1);
                                pos.push(2*p);
                            }else{
                                row.push(r + 1);
                                pos.push(2*p + 1);
                            }
                            break;
                        default:
                            /*A class*/
                            return prog.getMain()[r][p];
                    }
                }else
                    throw new Exception("Main program did not terminate within the maximum depth.");
            }while(!row.empty() && !pos.empty());
            throw new Exception ("Program completed without producing a result.");
        }else 
            throw new Exception("Cannot interpret empty main program.");
        
    }
    
    private int _interpretCondition(char[][] cond,Stack<Integer> row,Stack<Integer> pos) throws Exception{
        if(cond != null){
            if(row != null && pos != null && !row.empty() && !pos.empty()){
                int r,p;
                do{
                    r = row.peek();
                    if(r <= Parameters.getInstance().getCondition_max_depth()){
                        p = pos.peek();
                        switch(cond[r][p]){
                            case 
                        }
                    }else 
                        throw new Exception("Program condition did not terminate within the maximum depth.");
                }while(!row.empty() && !pos.empty());
            }else
                throw new Exception("Cannot interpret empty condition.");
        }else
            throw new Exception("Cannot interpet null condition.");
    }
    
    
}
