package geneticprogram;
 
public class Fitness {
    public static final byte f1         = 0,
                      recall            = 1,
                      precision         = 2,
                      accuracy          = 3;
    
    private byte measure                = f1;
    private static  Fitness singleton   = null;
    
    private Fitness(byte measure){
        this.measure = measure;
    }

    /***
     * @param measure
     * @return
     * @throws Exception 
     */
    public static Fitness getInstance(byte measure) throws Exception{
        if(measure>= 0 && measure <= 43){
            if(singleton == null){
                singleton = new Fitness(measure);
            }
            singleton.measure = measure;
            return singleton;
        }else
            throw new Exception("Invalid fitness measure.");
        
    }
    
    /**
     * @param prog
     * @return
     * @throws Exception 
     */
    public double evaluate(Program prog) throws Exception{
        if(prog != null){
            switch(this.measure){
                case f1:
                    return _f1(prog);
                case recall:
                    return _recall(prog);
                case precision:
                    return _precision(prog);
                case accuracy:
                    return _accuracy(prog);
                default:
                    return _f1(prog);
            }
        }else 
            throw new Exception("Cannot evaluate null program.");
    } 
    
    private static double _f1(Program prog){
        return 0;
    }
    
    private static double _recall(Program prog){
        return 0;
    }
    
    private static double _precision(Program prog){
        return 0;
    }
    
    private static double _accuracy(Program prog){
        return 0;
    }
}
