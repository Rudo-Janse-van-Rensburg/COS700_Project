package geneticprogram;
import java.util.Random;


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
    
    public void reseed(long seed){
        random = new Random(seed);
    }
}
