package geneticprogram; 

public class Data {
    private static Data data = null;
    private int numberClasses;
    private int numberAttributes;
    private Data(){
        
    }
    
    public static Data initialiseData(){
        if(data == null)
            Data.data = new Data();  
        return Data.data;
    }

    public int getNumberClasses() {
        return numberClasses;
    }

    public int getNumberAttributes() {
        return numberAttributes;
    }
    
    
    
    
}
