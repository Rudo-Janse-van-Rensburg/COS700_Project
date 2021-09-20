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
    
    public ArrayList<int[]> getPoints(char[][] tree) throws Exception{
        return new ArrayList<int[]>();
    }
    
    public ArrayList<int[]> WRONGgetPoints(char[][] tree,char type) throws Exception{
        if(tree != null){
            ArrayList<int[]> points = null; 
            Stack<Integer> level, position;
            int l,p;
            switch(type){
                case Meta.condition:
                    points      = new ArrayList<int[]>();
                    level       = new Stack<Integer>();
                    position    = new Stack<Integer>();
                    level.push(0);
                    position.push(0);
                    do{
                        l = level.pop();
                        p = level.pop();
                        points.add(new int[]{l,p});
                        switch(tree[l][p]){
                            case CONDITION_TERMINALS.ATTRIBUTE: 
                                break;
                            default:
                                for (int i = 0; i < 2; i++) {
                                    level.push(l+1);
                                    position.push(2*p+i);
                                }
                                break;
                        }
                    }while(!level.empty() && !position.empty());
                    break;
                case Meta.main:
                    points = new ArrayList<int[]>();
                    level       = new Stack<Integer>();
                    position    = new Stack<Integer>();
                    level.push(0);
                    position.push(0);
                    do{
                        l = level.pop();
                        p = level.pop();
                        points.add(new int[]{l,p});
                        switch(tree[l][p]){
                            case MAIN_TERMINALS.CLASS: 
                                break;
                            default:
                                for (int i = 0; i < 2; i++) {
                                    level.push(l+1);
                                    position.push(2*p+i);
                                }
                                break;
                        }
                    }while(!level.empty() && !position.empty());
                    break;
                default:
                    throw new Exception("Cannot get points of invalid type of node."); 

            }
            return points;
        }else
            throw new Exception("Cannot get points of an empty tree.");
        
    } 
    
    public String toString(Program prog){
        return "";
    }
    
}
