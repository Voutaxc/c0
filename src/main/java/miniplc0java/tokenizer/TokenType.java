package tokenizer;

public enum TokenType {
    None,
    FN_KW     ,
    LET_KW    ,
    CONST_KW  ,
    AS_KW     ,
    WHILE_KW  ,
    IF_KW     ,
    ELSE_KW   ,
    RETURN_KW ,
    BREAK_KW,
    CONTINUE_KW,
    UINT_LITERAL,
    DOUBLE_LITERAL,
    STRING_LITERAL,
    IDENT,


    PLUS    ,// -> '+'
    MINUS   , //-> '-'
    MUL     , //-> '*'
    DIV     , //-> '/'
    ASSIGN  ,// -> '='
    EQ      ,// -> '=='
    NEQ     , //-> '!='
    LT      , //-> '<'
    GT      , //-> '>'
    LE      , //-> '<='
    GE      , //-> '>='
    L_PAREN ,
    R_PAREN ,
    L_BRACE ,
    R_BRACE ,
    ARROW   ,
    COMMA   ,
    COLON   ,
    SEMICOLON,
    EOF,
    INT,
    DOUBLE,
    VOID;


    @Override
    public String toString() {
        switch (this) {
            case None:
                return "NullToken";
            case FN_KW:
                return "FN_KW";
            case LET_KW:
                return "LET_KW";
            case CONST_KW:
                return "CONST_KW";
            case AS_KW:
                return "AS_KW";
            case WHILE_KW:
                return "WHILE_KW";
            case IF_KW:
                return "IF_KW";
            case ELSE_KW:
                return "ELSE_KW";
            case RETURN_KW:
                return "RETURN_KW";
            case BREAK_KW:
                return "BREAK_KW";
            case CONTINUE_KW:
                return "CONTINUE_KW";
            case UINT_LITERAL:
                return "UINT_LITERAL";
            case DOUBLE_LITERAL:
                return "DOUBLE_LITERAL";
            case STRING_LITERAL:
                return "STRING_LITERAL";
            case IDENT:
                return "IDENT";
            case PLUS:
                return "PLUS";
            case MINUS:
                return "MINUS";
            case MUL:
                return "MUL";
            case DIV:
                return "DIV";
            case ASSIGN:
                return "ASSIGN";
            case EQ:
                return "EQ";
            case NEQ:
                return "NEQ";
            case LT:
                return "LT";
            case GT:
                return "GT";
            case LE:
                return "LE";
            case GE:
                return "GE";
            case L_PAREN:
                return "L_PAREN";
            case R_PAREN:
                return "R_PAREN";
            case L_BRACE:
                return "L_BRACE";
            case R_BRACE:
                return "R_BRACE";
            case ARROW:
                return "ARROW";
            case COMMA:
                return "COMMA";
            case COLON:
                return "COLON";
            case SEMICOLON:
                return "SEMICOLON";
            case EOF:
                return "EOF";
            case INT:
                return "INT";
            case VOID:
                return "VOID";
            case DOUBLE:
                return "DOUBLE";

            default:
                return "InvalidToken";
        }
    }
}
