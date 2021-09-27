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
    
    private static Program _fitnessProportionate(Generation generation) throws Exception{
        double total_fitness    = generation.getTotal_fitness();
        Generation gen          = new Generation();
        double [] fitnesses     = new double[Parameters.getInstance().getPopulation_size()];
        boolean has_capacity    = true;
        int pos                 = 0 ;
        do{
            Program prog    = generation.getIndividual(pos % generation.getCapacity());
            double fitness  = generation.getFitness(pos % generation.getCapacity()); 
            int i = 0;
            while(
                has_capacity 
                && 
                i++ < Math.round((1.0*fitness/1.0*total_fitness)*Parameters.getInstance().getPopulation_size())
            ){
                has_capacity = has_capacity && gen.add(prog, fitness);
            }
            ++pos;
            
        }while(has_capacity);
        return gen.getIndividual(Util.getInstance().getRandomInt(0, Parameters.getInstance().getPopulation_size()-1));
    }
    
    private static Program _inverseFitnessProportionate(Generation generation) throws Exception{
        double total_fitness    = generation.getTotal_fitness();
        Generation gen          = new Generation();
        double [] fitnesses     = new double[Parameters.getInstance().getPopulation_size()];
        boolean has_capacity    = true;
        int pos                 = 0 ;
        do{
            Program prog    = generation.getIndividual(pos % generation.getCapacity());
            double fitness  = generation.getFitness(pos % generation.getCapacity()); 
            int i = 0;
            while(
                has_capacity 
                && 
                i++ < Math.round((1 - (1.0*fitness/1.0*total_fitness))*Parameters.getInstance().getPopulation_size())
            ){
                has_capacity = has_capacity && gen.add(prog, fitness);
            }
            ++pos;
            
        }while(has_capacity);
        return gen.getIndividual(Util.getInstance().getRandomInt(0, Parameters.getInstance().getPopulation_size()-1));
    }
 
    

}
