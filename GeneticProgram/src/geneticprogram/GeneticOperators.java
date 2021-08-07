package geneticprogram; 

import static geneticprogram.CONDITION_TERMINALS.ATTRIBUTE;
import java.util.Stack;

public class GeneticOperators {  
    
    /**
     * @param prog          - program to create using the grow method.
     * @throws Exception 
     */
    public static void grow(Program prog) throws Exception{
        createMain(prog,Parameters.getInstance().getMain_max_depth(),0,0,false);
    }
    
    /**
     * @param prog          - program to create using the full method.
     * @throws Exception 
     */
    public static void full(Program prog) throws Exception{
        createMain(prog,Parameters.getInstance().getMain_max_depth(),0,0,true);
    }
    
    /**
     * @param prog          - program to create.
     * @param max_depth     - maximum depth of the program.
     * @param start_level   - the starting level of the creation.
     * @param start_pos     - the starting position of the creation.
     * @param full          - whether this should be full or grow method.
     * @throws Exception     
     */
    private static void createMain(Program prog,int max_depth,int start_level, int start_pos,boolean full) throws Exception{
        if(max_depth > 2 && max_depth <= Parameters.getInstance().getMain_max_depth()){ 
            char [][][][] condition_tree    = prog.getConditions();
            char [][]     main_tree         = prog.getMain();
            Stack<Integer> level_stack      = new Stack<Integer>();
            Stack<Integer> position_stack   = new Stack<Integer>();
            level_stack.push(start_level); 
            position_stack.push(start_pos);
            int level,
                position; 
            char to_add = 0;
            do{ 
                level       = level_stack.pop();
                position    = position_stack.pop();
                if(level <= max_depth){
                    if(level < max_depth){
                        if(full){
                            to_add  = Meta.main_functions.getMainFunctions()[Util.getInstance().getRandomInt(0, Meta.main_functions.getMainFunctions().length)];
                        }else{ 
                            int pos = Util.getInstance().getRandomInt(0,Meta.main_functions.getMainFunctions().length + Meta.main_terminals.getMainTerminals().length-1);
                            to_add  = pos < Meta.main_functions.getMainFunctions().length ? Meta.main_functions.getMainFunctions()[pos] : Meta.main_terminals.getMainTerminals()[pos - Meta.main_functions.getMainFunctions().length];
                        }
                    }else{
                        to_add = Meta.main_terminals.getMainTerminals()[Util.getInstance().getRandomInt(0, Meta.main_terminals.getMainTerminals().length)];
                    }
                    switch(to_add){
                        case MAIN_TERMINALS.CLASS:
                            main_tree[level][position]      = Factory.createClass(Util.getInstance().getRandomInt(0, Meta.CLASSES.length-1));
                            condition_tree[level][position] = null;
                            break;
                        default:
                            main_tree[level][position]      = to_add;
                            createCondition(condition_tree[level][position],Parameters.getInstance().getCondition_max_depth(),0,0,true);
                            for (int i = 0; i < 2; i++) {
                                level_stack.push(level+1); 
                                position_stack.push((2*position)+i);
                            }
                            break;
                    }
                }
            }while(!level_stack.empty() && !position_stack.empty()); 
        }else
            throw new Exception("Depth of main program to create is invalid.");
    }
    
    /**
     * @param condtion      - the condition branch to write to.
     * @param max_depth     - the maximum depth of the branch.
     * @param start_level   - the start level of the condition branch. 
     * @param start_pos     - the start position of the level.
     * @param full          - whether full or grow method should be used.
     * @return
     * @throws Exception 
     */
    private static void createCondition(char[][] condtion, int max_depth, int start_level, int start_pos,boolean full) throws Exception{
        if(max_depth > 2 && max_depth <= Parameters.getInstance().getCondition_max_depth()){
            Stack<Integer> level_stack      = new Stack<Integer>();
            Stack<Integer> position_stack   = new Stack<Integer>();
            level_stack.push(start_level); 
            position_stack.push(start_pos);
            int level,
                position; 
            char to_add = 0;
            do{
                level       = level_stack.pop();
                position    = position_stack.pop();
                if(level <= max_depth){
                    if(level < max_depth){
                        if(full){
                            to_add  = Meta.condition_functions.getConditionFunctions()[Util.getInstance().getRandomInt(0, Meta.condition_functions.getConditionFunctions().length-1)];
                        }else{
                            int pos = Util.getInstance().getRandomInt(0, Meta.condition_functions.getConditionFunctions().length + Meta.condition_terminals.getConditionTerminals().length - 1);
                            to_add  = pos < Meta.condition_functions.getConditionFunctions().length ? Meta.condition_functions.getConditionFunctions()[pos]:Meta.condition_terminals.getConditionTerminals()[pos - Meta.condition_functions.getConditionFunctions().length];  
                        }
                    }else{
                        to_add  = Meta.condition_terminals.getConditionTerminals()[Util.getInstance().getRandomInt(0, Meta.condition_terminals.getConditionTerminals().length-1)];
                    }
                    switch(to_add){
                        case CONDITION_TERMINALS.ATTRIBUTE:
                            condtion[level][position]   = to_add;
                            break;
                        default:
                            condtion[level][position]   = to_add;
                            for (int i = 0; i < 2; i++) {
                                level_stack.push(level+1);
                                position_stack.push((2*position)+i);
                            }
                            break;
                    }
                } 
            }while(!level_stack.empty() && !position_stack.empty());
        }else 
            throw new Exception("Depth of condition to create is invalid.");
        
    } 
}
