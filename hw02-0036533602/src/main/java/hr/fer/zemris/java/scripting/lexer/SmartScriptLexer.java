package hr.fer.zemris.java.scripting.lexer;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of lexer
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SmartScriptLexer {
    /**
     * Data represented as Array of characters
     */
    private final char[] data;
    /**
     * Current token
     */
    private Token currentToken;
    /**
     * Current index, values from 0 to data.length - 1
     */
    private int currentIndex;
    /**
     * Opening tag after which lexer changes state to TAG
     */
    private final String  OPEN_TAG = "{$";
    /**
     * Closing tag after which lexer changes state to BASIC
     */
    private final String CLOSE_TAG = "$}";
    /**
     * Current lexer state, BASIC state by default
     */
    private LexerState state;

    /**
     * Set of operands
     */

    private static Set<Character> operands;
    static {
        operands = new HashSet<>();
        operands.add('+');
        operands.add('-');
        operands.add('*');
        operands.add('/');
        operands.add('^');
    }

    /**
     *
     * Constructor which takes {@param text} as argument and turns it into an array of characters
     *
     * @param text text that we will turn to array of characters
     * @throws NullPointerException if {@param text} is null
     */
    public SmartScriptLexer(String text){
        if(text == null) throw new NullPointerException("Text can't be null");

        this.data = text.toCharArray();
        this.state = LexerState.BASIC;
        this.currentIndex = 0;
        this.currentToken = null;

    }

    /**
     * Returns current token
     * @return Token current token
     */
    public Token getToken(){
        if(currentToken == null) throw new LexerException("Can't get token because current token is null");

        return currentToken;
    }

    /**
     * Sets state of lexer
     * @param state in which state we want him to work
     * @throws NullPointerException when state is null
     */

    public void setState(LexerState state){
        if(state == null) throw new NullPointerException("State can't be null");

        this.state = state;
    }

    /**
     * Finds next token and sets as new token
     */

    public void nextToken(){

        if(currentToken != null
                && currentToken.getType() == TokenType.EOF)
            throw new LexerException("There is no more tokens");

        if(currentIndex >= data.length){
            currentToken = new Token(TokenType.EOF, null);
            return;
        }

        if(state == LexerState.BASIC){
            if(startsWithOpenTag()){
                currentIndex += 2;
                currentToken = new Token(TokenType.TAG_START,OPEN_TAG);
            }
            else currentToken = new Token(TokenType.STRING, getText());

        }else if(state == LexerState.TAG){
            removeWhiteSpaces();
            if(isClosingTag()){
                currentIndex += 2;
                currentToken = new Token(TokenType.TAG_END, CLOSE_TAG);
            }else
                getTokenize();

        }

    }

    /**
     * Chechs if starts with {$
     *
     * @return true starts with {$ or else false
     */

    private boolean startsWithOpenTag(){
        if(data[currentIndex] != '{') return false;

        return data[currentIndex + 1] == '$';
    }

    /**
     * Chechs if next 2 characters are {$
     *
     * @return true if next 2 characters are {$ or else false
     */

    private boolean isClosingTag(){
        if(data[currentIndex] != '$') return false;

        return data[currentIndex + 1] == '}';
    }

    /**
     * Returns token for string when lexer is in BASIC state
     *
     * @return String text until opening tag
     */

    private String getText(){
        StringBuilder sb = new StringBuilder();

        while(currentIndex < data.length && !startsWithOpenTag()){

            //if(data[currentIndex] == '{') throw new LexerException("Invalid opening tag");
            if(data[currentIndex] == '\\') checkSlashBasic();

            sb.append(data[currentIndex++]);
        }

        return sb.toString();
    }

    /**
     * Checks if escape expression is valid or not
     * when lexer is in state BASIC
     */

    private void checkSlashBasic(){
        if(currentIndex + 1 >= data.length)
            throw new LexerException("Invalid escape expression");

        if(data[currentIndex + 1] != '{')
            if(data[currentIndex + 1] != '\\')
                throw new LexerException("Invalid escape expression");

        currentIndex++;
    }

    /**
     * When lexer is in TAG state then we have multiple options,
     * so we must check the first character, so that we know
     * which tag is appropriate
     */

    private void getTokenize(){
        char character = data[currentIndex];
        if(character == '-' && Character.isDigit(data[currentIndex + 1])){
            currentIndex++;
            getNumberTag(true);
        }
        else if(Character.isLetter(character) || character == '=')  getIDNTag();
        else if (character == '@') {
            if(Character.isLetter(data[currentIndex + 1])) getFunctionTag();
            else throw new LexerException("Invalid function expression");
        }

        else if (character == '\"') getStringTag();
        else if(operands.contains(character)) currentToken = new Token(TokenType.OPERATOR, data[currentIndex++]);
        else if(Character.isDigit(character))  getNumberTag(false);
        else throw new LexerException("Invalid expression");
    }

    /**
     * String tag when lexer is in state TAG
     * At the end if everything is alright set new token as current token
     */
    private void getStringTag(){
        StringBuilder sb = new StringBuilder();
        currentIndex++;

        while(currentIndex < data.length){
            sb.append(addWhiteSpaces());
            if(data[currentIndex] == '\"') break;
            if(data[currentIndex] == '\\') checkSlashTag();
            sb.append(data[currentIndex++]);
        }

        currentIndex++;

        currentToken = new Token(TokenType.STRING,sb.toString());
    }

    /**
     * Checks if escape expression is valid or not
     * when lexer state is TAG
     */

    private void checkSlashTag(){
        if(currentIndex + 1 >= data.length)
            throw new LexerException("Invalid escape expression");

        if(data[currentIndex + 1] != '\\')
            if(data[currentIndex + 1] != '\"')
                throw new LexerException("Invalid escape expression");

        currentIndex++;

    }

    /**
     * Creates function tag
     * At the end if everything is alright set new token as current token
     */

    private void getFunctionTag(){
        StringBuilder sb = new StringBuilder();

        sb.append(data[currentIndex++]);
        sb.append(data[currentIndex++]);

        while(currentIndex < data.length){
            if(!validateFunctionAndIDNNextChar())break;
            sb.append(data[currentIndex++]);
        }

        currentToken = new Token(TokenType.FUNCTION, sb.toString());

    }

    /**
     * Creates identifier tag
     *   At the end if everything is alright set this newly created token as current token
     */

    private void getIDNTag(){
        StringBuilder sb = new StringBuilder();

        while (currentIndex < data.length && !isClosingTag()) {
            if(data[currentIndex] == '=') {
                sb.append(data[currentIndex++]);
                break;
            }
            if(!validateFunctionAndIDNNextChar()) break;

            sb.append(data[currentIndex++]);
        }

        String finalValue = sb.toString();

        if(finalValue.startsWith("@")) currentToken = new Token(TokenType.FUNCTION, finalValue);
        else currentToken = new Token(TokenType.IDN, finalValue);

    }

    /**
     * Creates number tag
     * At the end if everything is alright set this newly created token as current token
     *
     * @param negative if true then it's negative number or else it's positive number
     */


    private void getNumberTag(boolean negative) {
        StringBuilder sb = new StringBuilder();
        int numOfDots = 0;
        while(currentIndex < data.length &&
                (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {

            if(data[currentIndex] == '.') numOfDots++;

            sb.append(data[currentIndex++]);
        }

        String finalValue = sb.toString();

        if(numOfDots > 1) throw new LexerException("Invalid number");
        if(numOfDots == 1) {
            Double num = negative ? -Double.parseDouble(finalValue) : Double.parseDouble(finalValue);
            currentToken = new Token(TokenType.DOUBLE, num);
        }
        if(numOfDots == 0){
            Integer num = negative ? -Integer.parseInt(finalValue) : Integer.parseInt(finalValue);
            currentToken = new Token(TokenType.INTEGER, num);
        }

    }

    /**
     * Removes white spaces when lexer state is BASIC
     */

    private void removeWhiteSpaces(){
        while(currentIndex < data.length){
            if(!isWhiteSpace()) break;
            currentIndex++;
        }
    }

    /**
     * Checks if character is whitespace
     *
     * @return boolean if it is a whitespaces then true else false
     */
    private boolean isWhiteSpace(){
        char character = data[currentIndex];
        return Character.isSpaceChar(character) || character == '\r' || character == '\n' || character == '\t';
    }

    /**
     * Checks if character can be part of a function or identifier
     *
     * @return true if it can be a part of a function or identifier or else false
     */

    private boolean validateFunctionAndIDNNextChar(){
        return Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_';
    }

    /**
     * Returns all whitespaces collected between 2 strings
     *
     * @return String all whitespaces collected between 2 strings
     */

    public String addWhiteSpaces(){
        StringBuilder sb = new StringBuilder();
        while(currentIndex < data.length && data[currentIndex] == '\\'){
            if(data[currentIndex + 1] == 'n' || data[currentIndex + 1] == 't' || data[currentIndex + 1] == 'r'){
                sb.append("\\" + data[currentIndex++]);
            }else break;
        }

        return sb.toString();
    }

}
