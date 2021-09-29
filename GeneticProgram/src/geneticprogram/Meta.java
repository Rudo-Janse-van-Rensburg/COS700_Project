package geneticprogram;

public class Meta {  
    
    public static boolean debug                     = true;    
    public static final char    /*IF*/
                                IF                  = 0;
    
                                /*FUNCTION IN THE MAIN BRANCH*/
    public static final char[]  MAINS               = new char[]{
                                                        IF
                                                    };
    
    public static final char    /*RELATION*/ 
                                GREATER_THAN        = 0,
                                LESS_THAN           = 1,
                                GREATER_OR_EQUAL    = 2,
                                LESS_OR_EQUAL       = 3,
                                EQUAL               = 4,
                                NOT_EQUAL           = 5,
                                /*ARITHMETIC*/
                                ADDITION            = 6,
                                SUBTRACTION         = 7,
                                DIVISION            = 8,
                                MULTIPLICATION      = 9,
                                /*BITWISE*/         
                                BITWISE_AND         = 10,
                                BITWISE_OR          = 11,
                                BITWISE_XOR         = 12, 
                                /*LOGICAL*/
                                LOGICAL_AND         = 13,
                                LOGICAL_OR          = 14;
                                
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
 