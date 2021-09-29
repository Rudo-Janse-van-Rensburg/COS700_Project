package geneticprogram;

public class Meta {  
    
    public static boolean debug                     = true;    
    public static final char    /*IF*/
                                IF                  = (char) 0;
    
                                /*FUNCTION IN THE MAIN BRANCH*/
    public static final char[]  MAINS               = new char[]{
                                                        IF
                                                    };
    
    public static final char    /*RELATION*/ 
                                GREATER_THAN        = (char) 0,
                                LESS_THAN           = (char) 1,
                                GREATER_OR_EQUAL    = (char) 2,
                                LESS_OR_EQUAL       = (char) 3,
                                EQUAL               = (char) 4,
                                NOT_EQUAL           = (char) 5,
                                /*ARITHMETIC*/
                                ADDITION            = (char) 6,
                                SUBTRACTION         = (char) 7,
                                DIVISION            = (char) 8,
                                MULTIPLICATION      = (char) 9,
                                /*BITWISE*/         
                                BITWISE_AND         = (char) 10,
                                BITWISE_OR          = (char) 11,
                                BITWISE_XOR         = (char) 12, 
                                /*LOGICAL*/
                                LOGICAL_AND         = (char) 13,
                                LOGICAL_OR          = (char) 14;
                                
                                /*FUNCTIONS IN THE CONDITION BRANCH*/
    public static final char[]  CONDITIONS          = new char[]{
                                                        GREATER_THAN,
                                                        LESS_THAN,
                                                        GREATER_OR_EQUAL,
                                                        LESS_OR_EQUAL,
                                                        EQUAL,
                                                        NOT_EQUAL,
                                                        ADDITION,
                                                        SUBTRACTION,
                                                        DIVISION,
                                                        MULTIPLICATION,
                                                        BITWISE_AND,
                                                        BITWISE_OR,
                                                        BITWISE_XOR,
                                                        LOGICAL_AND,
                                                        LOGICAL_OR
                                                    }; 
    
    
    
    
}
 