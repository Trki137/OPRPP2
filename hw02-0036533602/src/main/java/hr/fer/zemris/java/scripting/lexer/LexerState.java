package hr.fer.zemris.java.scripting.lexer;

/**
 * In which states lexer can be
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public enum LexerState {
    /**
     * Mode before opening tag or after closing tab
     */
    BASIC,
    /**
     * Mode after opening tag
     */
    TAG;
}
