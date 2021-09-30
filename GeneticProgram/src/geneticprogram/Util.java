package geneticprogram;
import java.util.ArrayList;
import java.util.Stack;


public class Util {
    private static Util utilities = null; 
    private Util(){
         
    }
    
    public static Util getInstance(){
        if(Util.utilities == null )
            Util.utilities = new Util();
        return Util.utilities;
    } 
    /**
     * @param tree
     * @param main
     * @return
     * @throws Exception 
     */
    public ArrayList<int[]> getPoints(char[][] tree,boolean main) throws Exception{
        if(tree != null){
            ArrayList<int[]> points     = FlyWeight.getInstance().getArrayListIntArray();
            Stack<Integer> levels       = FlyWeight.getInstance().getStackInteger();
            Stack<Integer> positions    = FlyWeight.getInstance().getStackInteger();
            levels.add(0);
            positions.add(0);
            do{
               int level    = levels.pop();
               int position = positions.pop(); 
               points.add(new int[]{level,position});
               int ch = tree[level][position];
               if(main){
                   if(ch < Meta.MAINS.length){
                       for (int i = 1; i >=0; i--) {
                           levels.push(level + 1);
                           positions.push((position << 1) + i);
                       }
                   }
               }else{
                   if(ch < Meta.CONDITIONS.length){
                       for (int i = 1; i >=0; i--) {
                           levels.push(level + 1);
                           positions.push((position << 1) + i);
                       }
                   }
               } 
            }while(!levels.empty() && !positions.empty());
            FlyWeight.getInstance().addStackInteger(levels);
            FlyWeight.getInstance().addStackInteger(positions); 
            return points;
        }else
            throw new Exception("Can not get points of an empty tree.");
    } 
    
    public String toString(Program prog) throws Exception{
        return toStringMain("",prog,prog.getMain(), 0, 0);
    }
    
    private String toStringMain(String level,Program prog,char[][] main, int row, int pos) throws Exception{
        if(row >=0 && row < Parameters.getInstance().getMain_max_depth()){
            String line = "";
            char ch = main[row][pos]; 
            switch(ch){
                case Meta.IF:
                    line += level + "IF" +toStringCondition(prog,row,pos,0, 0) + "{";
                    line += "\n";
                    line += level + toStringMain(level + "  ",prog,main,row+1,(pos << 1)  + 0);
                    line += "\n";
                    line += level + "}ELSE { ";
                    line += "\n";
                    line += level + toStringMain(level + "  ",prog,main,row+1,(pos << 1) + 1);
                    line += "\n";
                    line += level + "}";
                    line += "\n";
                    break;
                default:
                    line = level + (ch - Meta.MAINS.length); 
            }
            return line;
        }else 
            throw new Exception("Main row out of bounds.");
    }
    
    private String toStringCondition(Program prog,int m_row, int m_pos, int row, int pos) throws Exception{
        if(row >=0 && row <  Parameters.getInstance().getCondition_max_depth()){
            String line = "("; 
            char ch     = prog.getConditions()[m_row][m_pos][row][pos]; 
            switch(ch){
                case Meta.GREATER_THAN:  
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " > " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.LESS_THAN:  
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " < " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.GREATER_OR_EQUAL:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " >= " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.LESS_OR_EQUAL:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " <= " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.EQUAL:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " == " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.NOT_EQUAL:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " != " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.ADDITION:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " + " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.SUBTRACTION:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " - " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.DIVISION:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " / " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.MULTIPLICATION:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " * " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.BITWISE_AND:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " & " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.BITWISE_OR:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " | " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.BITWISE_XOR:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " ^ " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.LOGICAL_AND:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " && " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                case Meta.LOGICAL_OR:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " || " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
                    break;
                default:
                    int attribute = ch - Meta.CONDITIONS.length;
                    line += attribute;
            }
            return line+")";
        }else
            throw new Exception("Condition row out of bounds.");
    }
    
    
    
}
