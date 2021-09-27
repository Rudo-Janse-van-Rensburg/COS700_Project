package geneticprogram; 
import java.util.ArrayList;

public class Selection {
    public static final byte tournament                     = 0,
                            inverse_tournament              = 1,
                            fitness_proportionate           = 2,
                            inverse_fitness_proportionate   = 3;
    
    private byte method                                     = tournament;
    private static Selection singleton                      = null; 
    private Selection(byte method){
        this.method = method;
    }
    
    public static Selection getInstance(byte method) throws Exception{
        if(method >=0 && method <= 3){
            if(singleton == null){
                singleton   = new Selection(method);
            }
            singleton.method = method;
            return singleton;
        }else 
            throw new Exception("Invalid selection method.");
        
    }
    
    public Program select(Generation generation) throws Exception{
        if(generation != null && !generation.isEmpty()){
            switch(method){
                case tournament:
                    return _tournament(generation);
                case inverse_tournament:
                    return _inverseTournament(generation);
                case fitness_proportionate:
                    return _fitnessProportionate(generation);
                case inverse_fitness_proportionate:
                    return _inverseFitnessProportionate(generation);
                default:
                    return _tournament(generation);
            }
        }else 
            throw new Exception("Cannot perform selection from a null or empty generation.");
    }
    
    private static Program _tournament(Generation generation) throws Exception{
        ArrayList<Integer> competitors = new ArrayList<>();
        do{
            Integer pos = Util.getInstance().getRandomInt(0, generation.getCapacity()-1);
            if(!competitors.contains(pos)) 
                competitors.add(pos);
        }while(competitors.size() < Parameters.getInstance().getTournament_size());
        double fitness  = 0;
        Program prog    = null;
        for(int competitor: competitors) 
            if(generation.getFitness(competitor) > fitness)
                prog = generation.getIndividual(competitor);
        return prog;
    }
    
    private static Program _inverseTournament(Generation generation) throws Exception{
        ArrayList<Integer> competitors = new ArrayList<>();
        do{
            Integer pos = Util.getInstance().getRandomInt(0, generation.getCapacity()-1);
            if(!competitors.contains(pos)) 
                competitors.add(pos);
        }while(competitors.size() < Parameters.getInstance().getTournament_size());
        double fitness  = Integer.MAX_VALUE;
        Program prog    = null;
        for(int competitor: competitors)
            if(generation.getFitness(competitor) < fitness)
                prog = generation.getIndividual(competitor);
        return prog;
    }
    
    private static Program _fitnessProportionate(Generation generation){
        
        return null;
    }
    
    private static Program _inverseFitnessProportionate(Generation generation){
        return null;
    }
 
    

}
