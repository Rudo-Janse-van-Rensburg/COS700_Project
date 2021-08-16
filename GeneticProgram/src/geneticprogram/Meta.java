package geneticprogram;
 
public class Meta { 
    public static final char condition                          = 'c';
    public static final char main                               = 'm';
    public static final char[] node_types                       = new char[]{condition,main};
    public static final CONDITION_FUNCTIONS condition_functions = new CONDITION_FUNCTIONS();
    public static final CONDITION_TERMINALS condition_terminals = new CONDITION_TERMINALS();
    public static final MAIN_FUNCTIONS main_functions           = new MAIN_FUNCTIONS();
    public static final MAIN_TERMINALS main_terminals           = new MAIN_TERMINALS();
    public static final int[] CLASSES                           = new int[]{0,1,2,};
}

/**
 * Functions in the main program tree.
 */
class MAIN_FUNCTIONS{
    public final static char IF = 'I';
    
    public static char[] getMainFunctions(){
        return new char[]{
            IF
        };
    }
}

/**
 * Functions in the condition program sub-tree.
 */
class CONDITION_FUNCTIONS{
    public final static char 
            RELATION    = 'R',
            ARITHMETIC  = 'A',
            LOGICAL     = 'L';
    
    public static char[] getConditionFunctions(){
        return new char[]{
            RELATION,
            ARITHMETIC,
            LOGICAL
        };
    }
} 

/**
 * Terminals of the main program tree.
 */
class MAIN_TERMINALS{
    public final static char CLASS   = 'c';
    public static char[] getMainTerminals(){
        return new char[]{
            CLASS
        };
    }
}

/**
 * Terminals of the condition program tree.
 */
class CONDITION_TERMINALS{
    public final static char ATTRIBUTE   = 'a';
    public static char[] getConditionTerminals(){
        return new char[]{
            ATTRIBUTE
        };
    }
}