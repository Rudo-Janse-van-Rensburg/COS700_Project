package geneticprogram; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class GeneticOperators {  
    
    /**
     * @param prog      - program to full
     * @param depth     - depth to grow to
     * @throws Exception 
     */
    public static void grow(Program prog,int depth) throws Exception{
        if(prog != null){
            _createMain(prog,depth,0,0,false);
        }else
            throw new Exception("Cannot grow null program."); 
    }
    
    /**
     * @param prog      - program to full
     * @param depth     - depth to grow to
     * @throws Exception 
     */
    public static void full(Program prog, int depth) throws Exception{
        if(prog != null){
            _createMain(prog,depth,0,0,true);
        }else 
            throw new Exception("Cannot full null program.");
    }
    
    /**
     * @param prog          - program to mutate.
     * @throws Exception 
     */
    public static void mutate(Program prog) throws Exception{
        if(prog != null){
            ArrayList<int[]> points;
            int [] position;
            if(Randomness.getInstance().getRandomBoolean()){
                /*MUTATE MAIN TREE*/
                points      = Util.getInstance().getPoints(prog.getMain(),true);
                position    = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                if(false && Meta.debug){
                    System.out.print("    main points : [" );
                    for(int[] point : points){
                        System.out.print(""+Arrays.toString(point));
                    }
                    System.out.print("]\n");
                }
                
                _createMain(prog, Parameters.getInstance().getMain_max_depth(), position[0], position[1], false);
            }else{
                /*MUTATE CONDITION SUB-TREE*/
                points      = Util.getInstance().getMains(prog.getMain());
                position    = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                char[][] ptr_condition  = prog.getConditions()[position[0]][position[1]];
                points                  = Util.getInstance().getPoints(ptr_condition,false);
                if(false && Meta.debug){
                    System.out.print("    cond points : [" );
                    for(int[] point : points){
                        System.out.print(""+Arrays.toString(point));
                    }
                    System.out.print("]\n");
                } 
                position                = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                
                _createCondition(ptr_condition, Parameters.getInstance().getCondition_max_depth(), position[0], position[1], false);
            } 
        }else 
            throw new Exception("Cannot mutate null program.");
    }
    
    
    /**
     * @param prog          - program to mutate.
     * @throws Exception 
     */
    public static void mutate_old(Program prog) throws Exception{
        if(prog != null){
            ArrayList<int[]> points = Util.getInstance().getPoints(prog.getMain(),true);
            boolean mutate_main     = Randomness.getInstance().getRandomBoolean(); 
            int [] position         = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
            if(mutate_main){
                /*MUTATE MAIN TREE*/
                if(false && Meta.debug){
                    System.out.print("    main points : [" );
                    for(int[] point : points){
                        System.out.print(""+Arrays.toString(point));
                    }
                    System.out.print("]\n");
                }
                
                _createMain(prog, Parameters.getInstance().getMain_max_depth(), position[0], position[1], false);
            }else{
                /*MUTATE CONDITION SUB-TREE*/
                char[][] ptr_condition  = prog.getConditions()[position[0]][position[1]];
                points                  = Util.getInstance().getPoints(ptr_condition,false);
                if(false && Meta.debug){
                    System.out.print("    cond points : [" );
                    for(int[] point : points){
                        System.out.print(""+Arrays.toString(point));
                    }
                    System.out.print("]\n");
                } 
                position                = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                
                _createCondition(ptr_condition, Parameters.getInstance().getCondition_max_depth(), position[0], position[1], false);
            } 
        }else 
            throw new Exception("Cannot mutate null program.");
    }
    
    /**
     * @param prog
     * @throws Exception 
     */
    public static void hoist(Program prog) throws Exception{
        if(prog != null){
            char[][] tree_prog      = prog.getMain();
            ArrayList<int[]> points = Util.getInstance().getMains(tree_prog);
            int [] pos              = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
            char[][] main           = prog.getMain();
            char[][][][] cond       = prog.getConditions();
            int pow                 = 1;
            for (int level = pos[0]; level < main.length; level++) {
                for (int position = pow*pos[1]; position < (pow*pos[1] + pow); position++) {
                    main[level-pos[0]][position-pow*pos[1]] = main[level][position];
                    cond[level-pos[0]][position-pow*pos[1]] = cond[level][position];
                }
                pow = pow << 1; //time 2 
            }
        }else 
            throw new Exception("Cannot hoist null program.");
    }
    
    /**
     * @param a             - parent A.
     * @param b             - parent B.
     * @throws Exception 
     */
    public static void crossover(Program a,Program b) throws Exception{
        if(a != null && b != null){
            boolean     crossover_main  = Randomness.getInstance().getRandomBoolean();
            int[]       pos_A,pos_B; 
            ArrayList<int[]> points;
            if(Meta.debug){
                System.out.println("Main    : "+crossover_main);
            }
            Program temp = FlyWeight.getInstance().getProgram();
            temp.copy(b);
            if(crossover_main){
                /*CROSSOVER MAIN TREE*/
                points                  = Util.getInstance().getPoints(a.getMain(),true);
                pos_A                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                points                  = Util.getInstance().getPoints(b.getMain(),true);
                pos_B                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                
                int a_level             = pos_A[0],
                    a_position          = pos_A[1],
                    b_level             = pos_B[0],
                    b_position          = pos_B[1],
                    a_levels            = Parameters.getInstance().getMain_max_depth() - a_level,
                    b_levels            = Parameters.getInstance().getMain_max_depth() - b_level;
                
                /*
                    Copy A to B
                */
                int level = 0;
                do{
                    if(b_level + level < Parameters.getInstance().getMain_max_depth() - 1){
                        for (int position = 0; position < (1 << level); position++) {
                            b.getMain()[b_level+level][(b_position << level) + position]  = a.getMain()[a_level+level][(a_position << level) + position];
                            b.getConditions()[b_level + level][(b_position << level) + position] = a.getConditions()[a_level + level][(a_position << level) + position];
                        }
                    }else{
                        for (int position = 0; position < (1 << level); position++) {
                            if(a.getMain()[a_level + level][(a_position << level) + position] < Meta.MAINS.length ){
                                b.getMain()[b_level + level][(b_position << level) + position] = (char) (Meta.MAINS.length + Randomness.getInstance().getRandomIntExclusive(0, Data.initialiseData().getNumberClasses()));
                            }else{
                                b.getMain()[b_level + level][(b_position << level) + position] = a.getMain()[a_level + level][(a_position << level) + position];
                            }
                            b.getConditions()[b_level + level][(b_position<< level) + position] = a.getConditions()[a_level + level][(a_position << level) + position];
                        }
                    }
                    ++level;
                }while(b_level +  level < Parameters.getInstance().getMain_max_depth() && level < a_levels);
                 
                /*
                    Copy B to A
                */
                level = 0;
                do{
                    if(a_level + level < Parameters.getInstance().getMain_max_depth() - 1){
                        for (int position = 0; position < (1 << level); position++) {
                            a.getMain()[a_level+level][(a_position << level) + position]       = temp.getMain()[b_level + level][(b_position << level) + position];
                            a.getConditions()[a_level+level][(a_position << level) + position] = temp.getConditions()[b_level + level][(b_position << level) + position];
                        }
                    }else{
                        for (int position = 0; position < (1 << level); position++) {
                            if(temp.getMain()[b_level + level][(b_position << level) + position] < Meta.MAINS.length ){
                                a.getMain()[a_level+level][(a_position << level) + position] = (char) (Meta.MAINS.length + Randomness.getInstance().getRandomIntExclusive(0, Data.initialiseData().getNumberClasses()));
                            }else{
                                a.getMain()[a_level+level][(a_position << level) + position] = temp.getMain()[b_level + level][(b_position << level) + position];
                            }
                            a.getConditions()[a_level+level][(a_position << level) + position] = temp.getConditions()[b_level + level][(b_position << level) + position];
                        }
                    }
                    ++level;
                }while(a_level + level < Parameters.getInstance().getMain_max_depth() && level < b_levels);
            }else{
                /*CROSSOVER CONDITION SUB-TREES*/ 
                //pick a condition sub-branch in tree A
                points                  = Util.getInstance().getMains(a.getMain());
                pos_A                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                char [][] tree_A        = a.getConditions()[pos_A[0]][pos_A[1]];
                points                  = Util.getInstance().getPoints(tree_A,false);
                pos_A                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                
                //pick a condition sub-branch in tree B
                points                  = Util.getInstance().getMains(b.getMain());
                pos_B                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                char [][] tree_B        = b.getConditions()[pos_B[0]][pos_B[1]];
                char [][] tree_temp     = temp.getConditions()[pos_B[0]][pos_B[1]];
                points                  = Util.getInstance().getPoints(tree_B,false);
                pos_B                   = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                
                
                int a_level             = pos_A[0],
                    a_position          = pos_A[1],
                    b_level             = pos_B[0],
                    b_position          = pos_B[1],
                    a_levels            = Parameters.getInstance().getCondition_max_depth()- a_level,
                    b_levels            = Parameters.getInstance().getCondition_max_depth() - b_level;
                
                /*
                    Copy A to B
                */
                int level = 0;
                do{
                    if(b_level + level < Parameters.getInstance().getCondition_max_depth()- 1){
                        for (int position = 0; position < (1 << level); position++) {
                            tree_B[b_level+level][(b_position << level) + position]  = tree_A[a_level+level][(a_position << level) + position]; 
                        }
                    }else{
                        for (int position = 0; position < (1 << level); position++) {
                            if(tree_A[a_level + level][(a_position << level) + position] < Meta.CONDITIONS.length ){
                                tree_B[b_level + level][b_position + position] = (char) (Meta.CONDITIONS.length + Randomness.getInstance().getRandomIntExclusive(0, Data.initialiseData().getNumberAttributes()));
                            }else{
                                tree_B[b_level+level][(b_position << level) + position]  = tree_A[a_level+level][(a_position << level) + position]; 
                            } 
                        }
                    }
                    ++level;
                }while((b_level +  level) < Parameters.getInstance().getCondition_max_depth() && level < a_levels);
                
                /*
                    Copy B to A
                */
                level = 0;
                do{
                    if(a_level + level < Parameters.getInstance().getCondition_max_depth()- 1){
                        for (int position = 0; position < (1 << level); position++) {
                            tree_A[a_level+level][(a_position << level) + position]  = tree_temp[b_level+level][(b_position << level) + position];  
                        }
                    }else{
                        for (int position = 0; position < (1 << level); position++) {
                            if(tree_temp[b_level+level][b_position + position] < Meta.CONDITIONS.length ){
                                tree_A[a_level+level][(a_position << level) + position] = (char) (Meta.CONDITIONS.length + Randomness.getInstance().getRandomIntExclusive(0, Data.initialiseData().getNumberAttributes()));
                            }else{
                                tree_A[a_level+level][(a_position << level) + position]  = tree_temp[b_level+level][(b_position << level) + position];  
                            } 
                        }
                    }
                    ++level;
                }while((a_level +  level) < Parameters.getInstance().getCondition_max_depth() && level < b_levels);
            }   
        }else 
            throw new Exception("Cannot crossover null programs.");
        
    }
    
    /** 
     * @param prog          - program to edit.
     * @throws Exception 
     */
    public static void edit(Program prog) throws Exception{
        if(prog != null){ 
            char[][] tree           = null;
            ArrayList<int[]> points;
            int [] position;
            points                      = Util.getInstance().getMains(prog.getMain());
            int depth;
            boolean main = Randomness.getInstance().getRandomBoolean(); 
            if(main){
                
                tree        = prog.getMain();
                depth       = Parameters.getInstance().getMain_max_depth();
            }else{
                position    = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));
                tree        = prog.getConditions()[position[0]][position[1]];
                points      = Util.getInstance().getConditions(tree);
                depth       = Parameters.getInstance().getCondition_max_depth();
            } 
            
            position                = points.get(Randomness.getInstance().getRandomIntExclusive(0, points.size()));  
            if(false && Meta.debug){
                System.out.println("main    : "+main);
                System.out.print("points    : [" );
                for(int[] point : points){
                    System.out.print("("+Arrays.toString(point)+","+tree[point[0]][point[1]]+")");
                }
                System.out.print("]\n");
                System.out.println(""+Arrays.toString(position));
            }
            for (int level = position[0] + 1 ; level < depth; level++) {
                int pow = level - position[0] - 1; 
                for (int pos = position[1] * (1 << pow); pos < (position[1] + 1 )*(1 << pow); pos++) {
                    char temp           = tree[level][pos];
                    tree[level][pos]    = tree[level][pos + ((position[1] + 1 )*(1 << pow))];
                    tree[level][pos + pos + ((position[1] + 1 )*(1 << pow))]  = temp;
                }
            } 
       } else 
            throw new Exception("Cannot edit null program.");
    }
    
    /**
     * @param prog          - program to create.
     * @param max_depth     - maximum depth of the program.
     * @param start_level   - the starting level of the creation.
     * @param start_pos     - the starting position of the creation.
     * @param full          - whether this should be full or grow method.
     * @throws Exception     
     */
    private static void _createMain(Program prog,int max_depth,int start_level, int start_pos,boolean full) throws Exception{
        if(max_depth >= 2 && max_depth <= Parameters.getInstance().getMain_max_depth()){ 
            char [][][][] condition_tree    = prog.getConditions();
            char [][]     main_tree         = prog.getMain();
            Stack<Integer> level_stack      = FlyWeight.getInstance().getStackInteger();
            Stack<Integer> position_stack   = FlyWeight.getInstance().getStackInteger();
            level_stack.push(start_level); 
            position_stack.push(start_pos);
            int level,
                position; 
            char to_add = 0;
            do{ 
                level       = level_stack.pop();
                position    = position_stack.pop();
                if(level >= 0 && level < max_depth){
                    if(level == 0)
                        to_add  = Meta.MAINS[Randomness.getInstance().getRandomIntExclusive(0, Meta.MAINS.length)]; 
                    else if(level > 0 && level < max_depth -1){
                        if(full || Randomness.getInstance().getRandomBoolean()){
                            to_add = Meta.MAINS[Randomness.getInstance().getRandomIntExclusive(0, Meta.MAINS.length)]; 
                        }else{
                             to_add = (char) Randomness.getInstance().getRandomIntExclusive(0, Meta.MAINS.length + Data.initialiseData().getNumberClasses()); 
                        }
                    }else {
                        to_add = (char) (Meta.MAINS.length + Randomness.getInstance().getRandomIntExclusive(0, Data.initialiseData().getNumberClasses())); 
                    }
                    
                    switch(to_add){
                        case Meta.IF:
                            main_tree[level][position]      = to_add;
                            _createCondition(condition_tree[level][position],Parameters.getInstance().getCondition_max_depth(),0,0,full);
                            for (int i = 0; i < 2; i++) {
                                level_stack.push(level+1); 
                                position_stack.push((position << 1)+i);
                            }
                            break;
                        default:
                            main_tree[level][position]      = to_add; 
                            break;
                    }
                }else
                    throw new Exception("Invalid Main dimensions."); 
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
     * @throws Exception 
     */
    private static void _createCondition(char[][] condtion, int max_depth, int start_level, int start_pos,boolean full) throws Exception{
        if(max_depth > 0 && max_depth <= Parameters.getInstance().getCondition_max_depth() ){
            Stack<Integer> level_stack      = FlyWeight.getInstance().getStackInteger();
            Stack<Integer> position_stack   = FlyWeight.getInstance().getStackInteger();
            level_stack.push(start_level); 
            position_stack.push(start_pos);
            int level,
                position;  
            char to_add; 
            do{
                level           = level_stack.pop();
                position        = position_stack.pop(); 
                if(level >= 0 && level <  max_depth && position <= (1 << level)){
                    if(level == 0 ){
                        if(Parameters.getInstance().getCondition_max_depth() > 1){
                            to_add  =  Meta.CONDITIONS[Randomness.getInstance().getRandomIntExclusive(0, Meta.CONDITIONS.length)]; 
                        }else{
                            to_add =  (char) (Meta.CONDITIONS.length + Randomness.getInstance().getRandomIntExclusive(0,Data.initialiseData().getNumberAttributes()));
                        }
                    }else if(level > 0 && level < max_depth - 1){
                        if(full){
                            to_add  =  Meta.CONDITIONS[Randomness.getInstance().getRandomIntExclusive(0, Meta.CONDITIONS.length)]; 
                        }else{
                            to_add  = (char) Randomness.getInstance().getRandomIntExclusive(0, Meta.CONDITIONS.length + Data.initialiseData().getNumberAttributes());
                        }
                    }else{
                        to_add  = (char) (Meta.CONDITIONS.length + Randomness.getInstance().getRandomIntExclusive(0,Data.initialiseData().getNumberAttributes()));
                    } 
                    
                    if(to_add >= 0 && to_add  < Meta.CONDITIONS.length ){
                        condtion[level][position]   = to_add;
                        for(int i = 0; i < 2; i++) {
                            level_stack.push(level+1);
                            position_stack.push((position << 1)+i);
                        }
                    }else if(to_add >= Meta.CONDITIONS.length && to_add < Meta.CONDITIONS.length + Data.initialiseData().getNumberAttributes()){ 
                        condtion[level][position]   = to_add;
                    }else{
                        throw new Exception("Invalid Condition primitive.");
                    }
                }else
                    throw new Exception("Invalid Condition dimensions.");
            }while(!level_stack.empty() && !position_stack.empty());
        }else 
            throw new Exception("Depth of condition to create is invalid.");
    } 
}
