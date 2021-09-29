package geneticprogram; 

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Data {
    private static Data singleton = null;
    private final ArrayList<double[]> data; 
    private int number_classes;
    private int number_attributes;
    private int number_instances;
    private Data(){
        data    = new ArrayList<>();
    }
    
    public static Data initialiseData() throws Exception{
        if(singleton == null){
            singleton = new Data();  
            singleton.readFile("/media/rudo/Storage/02 - Homework/Computer Science/Honours/COS 700/Project/COS700_Project/Data/dataset.csv");
            singleton.shuffle();
        }
        return singleton;
    }

    
    public List<double[]> getFold(int fold) throws Exception{
        int sample_size =  (int) Math.floor(number_instances/Parameters.getInstance().getK_folds()*1.0);
        return data.subList(fold * sample_size, (fold * sample_size) + sample_size);
    }
    /**
     * 
     * @param file
     * @throws FileNotFoundException 
     */
    private void readFile(String file) throws Exception{
        number_instances    = 0; 
        number_classes      = 0;
        Scanner sc          = new Scanner(new File(file));
        String[] line       = sc.nextLine().split(",");
        number_attributes   = line.length - 1;
        double [] instance;
        int cls;
        while(sc.hasNextLine()){
            instance = new double[number_attributes + 1];
            line = sc.nextLine().split(",");
            for (int i = 0; i < number_attributes + 1; i++) {
                instance[i] = Double.parseDouble(line[i]);
            } 
            data.add(instance); 
            cls = (int) instance[number_attributes];
            if(cls + 1 > number_classes){
                number_classes = cls + 1;
            }
            ++number_instances;
        }
        sc.close(); 
    }
    
        
    /**
     * @return 
     */
    public int getNumber_instances() {
        return number_instances;
    }
    
    /**
     * 
     */
    public void shuffle(){
        Randomness.getInstance().reseed();
        Randomness.getInstance().shuffle(data);
    }

    /**
     * @return number of classes
     */
    public int getNumberClasses() {
        return number_classes;
    }
    
    /**
     * @return number of attributes
     */
    public int getNumberAttributes() {
        return number_attributes;
    }
    
    
    
    
}
