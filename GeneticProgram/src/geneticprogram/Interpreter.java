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
    
    /**
     * @param p 
     * @return  
     * @throws Exception 
     */
    public int Interpret(Program p) throws Exception{
        char result = 0;
        if(p != null){
            Stack<Integer> row  = FlyWeight.getInstance().getStackInteger();
            Stack<Integer> pos  = FlyWeight.getInstance().getStackInteger();
            row.push(0);
            pos.push(0);
            return _interpretMain(p,row,pos);
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
                        case Meta.IF:
                            /*Main Function*/ 
                            if(_interpretCondition(
                                        prog.getConditions()[r][p],
                                        0,
                                        0
                                    ) != 0 
                            ){
                                /*Condition Evaluated True*/
                                row.push(r + 1);
                                pos.push(p << 1);
                            }else{
                                /*Condition Evaluated False*/
                                row.push(r + 1);
                                pos.push((p << 1) + 1);
                            }
                            break;
                        default:
                            /*A class*/
                            return prog.getMain()[r][p] - Meta.MAINS.length;
                    }
                }else
                    throw new Exception("Main program did not terminate within the maximum depth.");
            }while(!row.empty() && !pos.empty());
            throw new Exception ("Program completed without producing a result.");
        }else 
            throw new Exception("Cannot interpret empty main program.");
        
    }
    
    private int _interpretCondition(char[][] cond,int row,int pos) throws Exception{
        if(cond != null){ 
            if(row < cond.length && pos < cond[row].length){
                switch(cond[row][pos]){
                    case Meta.GREATER_THAN:  
                        return _interpretCondition(cond, row+1, pos << 1 ) > _interpretCondition(cond, row+1, (pos << 1) +1 )? 1 : 0;
                    case Meta.LESS_THAN:
                        return _interpretCondition(cond, row+1, pos << 1 ) < _interpretCondition(cond, row+1, (pos << 1) +1 )? 1 : 0;
                    case Meta.GREATER_OR_EQUAL:
                        return _interpretCondition(cond, row+1, pos << 1 ) >= _interpretCondition(cond, row+1, (pos << 1) +1 )? 1 : 0;
                    case Meta.EQUAL:
                        return _interpretCondition(cond, row+1, pos << 1 ) == _interpretCondition(cond, row+1, (pos << 1) +1 )? 1 : 0;
                    case Meta.ADDITION:
                        return _interpretCondition(cond, row+1, pos << 1 ) + _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.SUBTRACTION:
                        return _interpretCondition(cond, row+1, pos << 1 ) - _interpretCondition(cond, row+1,(pos << 1) +1 );
                    case Meta.DIVISION:
                        return _interpretCondition(cond, row+1, pos << 1 ) / _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.MULTIPLICATION:
                        return _interpretCondition(cond, row+1, pos << 1 ) * _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.BITWISE_AND:
                        return _interpretCondition(cond, row+1, pos << 1 ) & _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.BITWISE_OR:
                        return _interpretCondition(cond, row+1, pos << 1 ) | _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.BITWISE_XOR:
                        return _interpretCondition(cond, row+1, pos << 1 ) ^ _interpretCondition(cond, row+1, (pos << 1) +1 );
                    case Meta.LOGICAL_AND:
                        return (_interpretCondition(cond, row+1, pos << 1 ) != 0) && (_interpretCondition(cond, row+1, (pos << 1) +1 ) != 0) ? 1 : 0;
                    case Meta.LOGICAL_OR:
                        return (_interpretCondition(cond, row+1, pos << 1 ) != 0) || (_interpretCondition(cond, row+1, (pos << 1) +1 ) != 0) ? 1 : 0;
                    default:
                        return cond[row][pos] - Meta.CONDITIONS.length;
                }
            }else 
                throw new Exception("Cannot interpret invalid condition.");
        }else
            throw new Exception("Cannot interpet null condition.");
    }
    
    
}
