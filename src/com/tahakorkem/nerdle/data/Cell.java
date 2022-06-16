package com.tahakorkem.nerdle.data;

import java.io.Serializable;

/**
 * Oyunda yer alan her bir hücre için bilgileri tutan sınıf.
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 3L;

    /**
     * Hücrenin etiket bilgisi.
     */
    private State state;
    /**
     * Hücrenin içinde yer alan metin bilgisi.
     */
    private char text;

    public Cell() {
        state = State.INITIAL;
        text = ' ';
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public char getText() {
        return text;
    }

    public void setText(char text) {
        this.text = text;
    }

    public void clear() {
        text = ' ';
    }

    public boolean isEmpty() {
        return text == ' ';
    }

    @Override
    public String toString() {
        return "nerdle.data.Cell{" +
                "state=" + state +
                ", text=" + text +
                '}';
    }
}
