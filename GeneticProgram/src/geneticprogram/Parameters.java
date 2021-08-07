package geneticprogram; 

public class Parameters {
    private static int main_max_depth           = 10;         // the maximum dpeth of the main program tree
    private static int condition_max_depth      = 5;    //
    private static Parameters singleton   = null;
    private Parameters(int mmd, int cmd) {
        this.main_max_depth         = mmd;
        this.condition_max_depth    = cmd;
    }
    
    private Parameters(){
        
    }
    
    public static Parameters setParameters(int mmd, int cmd){
        if(singleton != null){
            singleton.main_max_depth        = mmd;
            singleton.condition_max_depth   = cmd;
        }else
            singleton = new Parameters(mmd,cmd);
        return singleton;
    }
    
    public static Parameters getInstance(){
        if(singleton == null){
            singleton = new Parameters();
        }
        return singleton;
    }
    
    public int getCondition_max_depth() {
        return condition_max_depth;
    }

    public int getMain_max_depth() {
        return main_max_depth;
    }  
    
    
}
