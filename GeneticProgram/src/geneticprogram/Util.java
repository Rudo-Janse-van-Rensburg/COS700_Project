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
               switch(tree[level][position]){
                   case Meta.IF:
                       /*IF or GREATER THAN*/
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break; 
                   case Meta.LESS_THAN:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.GREATER_OR_EQUAL:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.LESS_OR_EQUAL:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.EQUAL:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.NOT_EQUAL:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.ADDITION:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.SUBTRACTION:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.DIVISION:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.MULTIPLICATION:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.BITWISE_AND:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.BITWISE_OR:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.BITWISE_XOR:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.LOGICAL_AND:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   case Meta.LOGICAL_OR:
                       points.add(new int[]{level,position});
                       for (int i = 0; i < 2; i++) {
                           levels.add(level + 1);
                           positions.add(2*position + i);
                       }
                       break;
                   default:
                       points.add(new int[]{level,position}); 
                       break;
                       
               }
            }while(levels != null && !levels.empty() && positions != null && positions.empty());
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
                    line += level + "IF" +toStringCondition(prog,row,pos,0, 0) + "{\n";
                    line += level + toStringMain(level + " ",prog,main,row+1,(pos << 1)  + 0) + "\n";
                    line += level + "} ELSE {\n";
                    line += level + toStringMain(level + " ",prog,main,row+1,(pos << 1) + 1) + "\n";
                    line += level + "}";
                    break;
                default:
                    line = level + (ch - Meta.MAINS.length); 
            }
            return line;
        }else 
            throw new Exception("Main row out of bounds.");
    }
    
    private String toStringCondition(Program prog,int m_row, int m_pos, int row, int pos) throws Exception{
        if(row >=0 && row < Parameters.getInstance().getCondition_max_depth()){
            String line = "("; 
            char ch = prog.getConditions()[m_row][m_pos][row][pos]; 
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
                case Meta.EQUAL:
                    line += toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 0) + " == " + toStringCondition(prog,m_row,m_pos, row+1, (pos << 1)  + 1);
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
