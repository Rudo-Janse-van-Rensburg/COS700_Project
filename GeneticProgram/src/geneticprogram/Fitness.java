package geneticprogram;
 
import java.util.Arrays;
import java.util.List;

public class Fitness {
    public static final byte 
                      f1                = 0,
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
    
    private static double _f1(Program prog) throws Exception{ 
        int number_classes  = Data.initialiseData().getNumberClasses(),
                target,
                output,
                fold_instances = (int) Math.ceil(Data.initialiseData().getNumber_instances()/Parameters.getInstance().getK_folds()*1.0); 
        double  avg_f1 = 0,prec = 0, rec = 0;
        int[] tp, fp, fn; 
        double[] instance;
        for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {
            

            List<double[]> data = Data.initialiseData().getFold(k);
            tp  = new int[number_classes];
            fp  = new int[number_classes];
            fn  = new int[number_classes];
            /*for (int i = 0; i < number_classes; i++) {
                tp[i] = 0;
                fp[i] = 0;
                fn[i] = 0;
            }*/
            for (int i = 0; i < fold_instances; i++) {
                instance    = data.get(i);
                target      = (int)instance[Data.initialiseData().getNumberAttributes()];
                output      = (int)Interpreter.getInstance().Interpret(prog,instance);
                
                if(target == output) 
                    tp[output] += 1;
                else{
                    fn[target] += 1;
                    fp[output] += 1;
                } 
            } 
            prec    = 0;
            rec     = 0;
            for (int c = 0; c < number_classes; c++) {
                if(tp[c] > 0  || fp[c] > 0){
                    prec +=  tp[c] / (1.0*(tp[c]+fp[c]));     
                }
                if(tp[c] > 0  || fn[c] > 0){
                    rec +=  tp[c] / (1.0*(tp[c]+fn[c]));     
                }
            } 
            prec /= (1.0*number_classes); 
            rec /= (1.0*number_classes); 
            avg_f1 += (2.0 * (prec * rec)/(1.0*(prec + rec))); 
        }  
        avg_f1 /= (Parameters.getInstance().getK_folds()*1.0);
        
        return avg_f1;
    }
    
    private static double _precision(Program prog) throws Exception{
        int number_classes  = Data.initialiseData().getNumberClasses();
        double target,output, avg_precision = 0,prec = 0;
        double[] instance, tp, fp;
        
        for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {
            List<double[]> data =Data.initialiseData().getFold(k);
            tp  = new double[number_classes];
            fp  = new double[number_classes];
            for (int i = 0; i < (int) Math.floor(Data.initialiseData().getNumber_instances()/Parameters.getInstance().getK_folds()); i++) {
                instance = data.get(i);
                target = instance[Data.initialiseData().getNumberAttributes()];
                output = Interpreter.getInstance().Interpret(prog,instance);
                if(target == output) 
                    tp[(int) target] += 1;
                else 
                    fp[(int) output] += 1;
            } 
            for (int c = 0; c < number_classes; c++) {
                if(tp[c] > 0  || fp[c] > 0){
                    prec =  tp[c] / (tp[c]+fp[c]);     
                }
            }
            prec /= number_classes; 
            avg_precision += prec;
        }  
        return avg_precision / Parameters.getInstance().getK_folds();
    }
    
    private static double _recall(Program prog) throws Exception{
        int number_classes  = Data.initialiseData().getNumberClasses();
        double target,output, avg_recall = 0,rec = 0;
        double[] instance, tp, fn;
        
        for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {
            List<double[]> data =Data.initialiseData().getFold(k);
            tp  = new double[number_classes];
            fn  = new double[number_classes];
            for (int i = 0; i < (int) Math.floor(Data.initialiseData().getNumber_instances()/Parameters.getInstance().getK_folds()); i++) {
                instance = data.get(i);
                target = instance[Data.initialiseData().getNumberAttributes()];
                output = Interpreter.getInstance().Interpret(prog,instance);
                if(target == output) 
                    tp[(int) target] += 1;
                else 
                    fn[(int) target] += 1;
            } 
            for (int c = 0; c < number_classes; c++) {
                if(tp[c] > 0  || fn[c] > 0){
                    rec =  tp[c] / (tp[c]+fn[c]);     
                }
            }
            rec /= number_classes; 
            avg_recall += rec;
        }  
        return avg_recall / Parameters.getInstance().getK_folds();
    }
    
    private static double _accuracy(Program prog)throws Exception{
        int number_classes  = Data.initialiseData().getNumberClasses();
        double target,output, avg_accuracy = 0, acc = 0;
        double[] instance, tp;
        
        for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {
            List<double[]> data =Data.initialiseData().getFold(k);
            tp  = new double[number_classes];
            for (int i = 0; i < (int) Math.floor(Data.initialiseData().getNumber_instances()/Parameters.getInstance().getK_folds()); i++) {
                instance = data.get(i);
                target = instance[Data.initialiseData().getNumberAttributes()];
                output = Interpreter.getInstance().Interpret(prog,instance);
                if(target == output) 
                    acc += 1;
            }  
            acc /= Math.floor(Data.initialiseData().getNumber_instances()/Parameters.getInstance().getK_folds());
            avg_accuracy += acc;
        }  
        return avg_accuracy / Parameters.getInstance().getK_folds();
    }
}
