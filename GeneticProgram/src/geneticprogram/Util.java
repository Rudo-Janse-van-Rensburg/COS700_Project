package geneticprogram;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Util {
    private static Util utilities = null;
    private static Random random;
    private Util(){
        random = new Random(42069);
    }
    
    public static Util getInstance(){
        if(Util.utilities == null )
            Util.utilities = new Util();
        return Util.utilities;
    }
    
    public int getRandomInt(int min, int max){
        return random.nextInt(max + 1 - min) + min;
    }
    
    public boolean getRandomBoolean(){
        return random.nextBoolean();
    }
    
    public void reseed(long seed){
        random = new Random(seed);
    }
    
    /**
     * 
     * @param tree
     * @param main
     * @return
     * @throws Exception 
     */
    public ArrayList<int[]> getPoints(char[][] tree,boolean main) throws Exception{
        if(tree != null){
            ArrayList<int[]> points     = new ArrayList<>();
            Stack<Integer> levels       = new Stack();
            Stack<Integer> positions    = new Stack();
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
                   case Meta.EQUAL:
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
            return points;
        }else
            throw new Exception("Can not get points of an empty tree.");
        
    } 
    
    public String toString(Program prog){
        return "";
    }
    
}
