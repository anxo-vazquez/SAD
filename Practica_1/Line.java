import java.util.ArrayList;
import java.util.Observable;

public class Line extends Observable {
    static final int MOVE_RIGHT = 2001;
    static final int MOVE_LEFT = 2002;
    static final int MOVE_START = 2000;
    static final int TOGGLE_INSERT = 2004;
    static final int REMOVE_CHARACTER = 2005;
    static final int BACKSPACE_ACTION = 127;
    static final int ESCAPE_ACTION = 2000;
    static final int MOVE_END = 2003;
    static final int ADD_CHARACTER = 2006;
    static final int FINALIZE_INPUT = 2007;

    private int cursorPosition;
    private boolean isInInsertMode;
    private ArrayList<Integer> characterBuffer;
    private char lastChar;

    public Line() {
        this.cursorPosition = 0;
        this.isInInsertMode = false;
        this.characterBuffer = new ArrayList<>();
    }

    public void finalizeInput() {
        this.setChanged();
        this.notifyObservers(FINALIZE_INPUT);
    }

    public char getLastChar() {
        return this.lastChar;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public boolean isInInsertMode() {
        return this.isInInsertMode;
    }

    public void moveCursorRight() {
        if (this.cursorPosition < this.characterBuffer.size()) {
            this.cursorPosition++;
        }
        this.setChanged();
        this.notifyObservers(MOVE_RIGHT);
    }

    public void moveCursorLeft() {
        if (this.cursorPosition > 0) {
            this.cursorPosition--;
        }
        this.setChanged();
        this.notifyObservers(MOVE_LEFT);
    }

    public void moveToStart() {
        this.cursorPosition = 0;
        this.setChanged();
        this.notifyObservers(MOVE_START);
    }

    public void moveToEnd() {
        this.cursorPosition = this.characterBuffer.size();
        this.setChanged();
        this.notifyObservers(MOVE_END);
    }

    public void backspace() {
        if (this.cursorPosition > 0 && !this.characterBuffer.isEmpty()) {
            this.characterBuffer.remove(this.cursorPosition - 1);
            this.moveCursorLeft();
        }
        this.setChanged();
        this.notifyObservers(BACKSPACE_ACTION);
    }

    public void deleteCharacter() {
        if (this.cursorPosition < this.characterBuffer.size()) {
            this.characterBuffer.remove(this.cursorPosition);
        }
        this.setChanged();
        this.notifyObservers(REMOVE_CHARACTER);
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for (int charCode : this.characterBuffer) {
            text.append((char) charCode);
        }
        return text.toString();
    }

    public void addCharacter(int characterCode) {
        if (this.isInInsertMode && this.cursorPosition < characterBuffer.size()) {
            this.characterBuffer.set(this.cursorPosition, characterCode);
        } else {
            this.characterBuffer.add(this.cursorPosition, characterCode);
        }
        this.lastChar = (char) characterCode;
        this.cursorPosition++;
        this.setChanged();
        this.notifyObservers(ADD_CHARACTER);
    }

    public void toggleInsertMode() {
        this.isInInsertMode = !this.isInInsertMode;
        this.setChanged();
        this.notifyObservers(TOGGLE_INSERT);
    }
}
