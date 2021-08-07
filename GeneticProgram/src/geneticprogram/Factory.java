package geneticprogram; 
public class Factory {
    
    
    
    public static char createIf(){
        return Meta.main_functions.IF;
    } 
    
    public static char createClass(int c){
        return (char) (((char) 0x0) + c);
    }
    
    
}
