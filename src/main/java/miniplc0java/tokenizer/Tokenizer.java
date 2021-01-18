package tokenizer;



import error.ErrorCode;
import error.TokenizeError;
import util.Pos;

import java.util.HashMap;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return Number();
        } else if (Character.isAlphabetic(peek)) {
            return Ident();
        } else if(peek=='\''){
            return Char();
        } else if(peek=='\"'){
            return String();
        }else{
            return Operator();
        }
    }

    private Token Char() throws TokenizeError {
        char ch=it.nextChar();
        char chal='\0';
        if(ch!='\''){
            throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());
        }
        ch = it.nextChar();
        Pos startPos = it.currentPos();
        if(ch=='\\')
            chal=getescape();
        else
            chal=ch;
        ch = it.nextChar();
        if(ch!='\'')
            throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());
        Pos endPos = it.currentPos();
        return new Token(TokenType.UINT_LITERAL,(long)chal,startPos,endPos);
    }

    private Token Number() throws TokenizeError
    {
        String arr=new String();
        arr+=it.nextChar();
        Pos startPos=it.currentPos();
        TokenType type=TokenType.UINT_LITERAL;
        while(Character.isDigit(it.peekChar())||it.peekChar()=='.'||it.peekChar()=='e'||it.peekChar()=='E'||it.peekChar()=='+'||it.peekChar()=='-')
        {
            if(it.peekChar()=='.')
            {
                type=TokenType.DOUBLE_LITERAL;
            }
            else if(it.peekChar()=='e'||it.peekChar()=='E'||it.peekChar()=='-'||it.peekChar()=='+')
            {
                if(type == TokenType.UINT_LITERAL){
                    throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
                }
            }
            arr+=it.nextChar();
        }
        Pos endPos=it.currentPos();
        if(type==TokenType.UINT_LITERAL)
        {
            return new Token(TokenType.UINT_LITERAL,Long.valueOf(arr),startPos,endPos);
        }
        else if(type==TokenType.DOUBLE_LITERAL)
        {
            return new Token(TokenType.DOUBLE_LITERAL,Double.doubleToLongBits(Double.valueOf(arr)), startPos, endPos);
        }
        else
            throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());

    }

    private Token String() throws TokenizeError {
        String arr = "";
        char ch=it.nextChar();
        if(ch!='\"'){
            throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());
        }
        Pos startPos=it.currentPos();
        boolean flag;
        do{
            flag=true;
            ch=it.nextChar();
            if(ch==0){
                throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());
            }
            if(ch=='\\'){
                ch=getescape();
                flag=false;
                arr=arr+ch;
            }
            if(ch!='\t'&&ch!='\n'&&ch!='\r'&&ch!='\"'&&ch!='\''&&ch!='\\'){
                arr=arr+ch;
            }
        }while(!(ch=='\"'&&flag));
        if(ch!='\"'){
            throw new TokenizeError(ErrorCode.InvalidInput,it.previousPos());
        }
        Pos endPos = it.currentPos();
        return new Token(TokenType.STRING_LITERAL,arr,startPos,endPos);
    }
    private char getescape() throws TokenizeError{
        char ch;
        ch=it.nextChar();
        if(ch=='\''){
            return '\'';
        }
        else if(ch=='\"'){
            return'\"';
        }
        else if(ch=='t'){
            return'\t';
        }else if(ch=='n'){
            return'\n';
        }else if(ch=='r'){
            return'\r';
        }else if(ch=='\\'){
            return '\\';
        }
        else{
            throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }


    private Token Ident() throws TokenizeError {
        String[] keyword = {"FN_KW", "LET_KW", "CONST_KW", "AS_KW", "WHILE_KW", "IF_KW", "ELSE_KW", "RETURN_KW", "BREAK_KW", "CONTINUE_KW"};
        String[] word = {"fn", "let", "const", "as", "while", "if", "else", "return", "break", "continue"};
        String arr = "";
        arr+=it.nextChar();
        Pos startPos=it.currentPos();
        while(Character.isDigit(it.peekChar())||Character.isAlphabetic(it.peekChar())||it.peekChar()=='_'){
            arr+=it.nextChar();
        }
        Pos endPos=it.currentPos();

        for(int i = 0; i < word.length; i++){
            if(arr.equals(word[i])){
                return new Token(TokenType.valueOf(keyword[i]), arr,startPos,endPos);

            }
        }
        return new Token(TokenType.IDENT, arr, startPos, endPos);

    }
    private Token Operator() throws TokenizeError{
        Pos prePos=it.previousPos();
        char ch=it.nextChar();
        if(ch=='+')
            return new Token(TokenType.PLUS, '+', it.previousPos(), it.currentPos());
        else if(ch=='(')
            return new Token(TokenType.L_PAREN,'(',it.previousPos(),it.currentPos());
        else if(ch==')')
            return new Token(TokenType.R_PAREN,')',it.previousPos(),it.currentPos());
        else if(ch=='{')
            return new Token(TokenType.L_BRACE,'{',it.previousPos(),it.currentPos());
        else if(ch=='}')
            return new Token(TokenType.R_BRACE,'}',it.previousPos(),it.currentPos());
        else if(ch==',')
            return new Token(TokenType.COMMA,',',it.previousPos(),it.currentPos());
        else if(ch==':')
            return new Token(TokenType.COLON,':',it.previousPos(),it.currentPos());
        else if(ch==';')
            return new Token(TokenType.SEMICOLON,';',it.previousPos(),it.currentPos());
        else if(ch=='=')
        {
            if(it.peekChar()=='=')
            {
                ch=it.nextChar();
                Pos curPos=it.currentPos();
                return new Token(TokenType.EQ, "==",prePos,curPos);
            }
            else
                return new Token(TokenType.ASSIGN, '=', it.previousPos(), it.currentPos());
        }
        else if(ch=='-')
        {
            if(it.peekChar()=='>')
            {
                ch=it.nextChar();
                Pos curPos=it.currentPos();
                return new Token(TokenType.ARROW,"->",prePos,curPos);
            }
            else
                return new Token(TokenType.MINUS, '-', it.previousPos(), it.currentPos());
        }
        else if(ch=='*')
            return new Token(TokenType.MUL, '*', it.previousPos(), it.currentPos());
        else if(ch=='/')
        {
            //注释
            if(it.peekChar()=='/')
            {
                while(ch!='\n')
                    ch=it.nextChar();
                return nextToken();
            }
            else
                return new Token(TokenType.DIV, '/', it.previousPos(), it.currentPos());
        }

        else if(ch=='<')
        {
            if(it.peekChar()=='=')
            {
                ch=it.nextChar();
                Pos curPos=it.currentPos();
                return new Token(TokenType.LE, "<=", prePos, curPos);
            }
            else
                return new Token(TokenType.LT, '<', it.previousPos(), it.currentPos());
        }
        else if(ch=='!'&&it.peekChar()=='=')
        {
            ch=it.nextChar();
            Pos cuPos=it.currentPos();
            return new Token(TokenType.NEQ, "!=", prePos, cuPos);
        }
        else if(ch=='>')
        {
            if(it.peekChar()=='=')
            {
                ch=it.nextChar();
                Pos curPos=it.currentPos();
                return new Token(TokenType.GE, ">=", prePos, curPos);
            }
            else
                return new Token(TokenType.GT, '>', it.previousPos(), it.currentPos());

        }

        else
            throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
