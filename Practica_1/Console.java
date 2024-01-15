import java.io.*;
import java.util.*;

public class Console implements Observer {

    static final String START_SEQUENCE = "\033[1~";
    static final String END_SEQUENCE = "\033[4~";
    static final String SPACE_SEQUENCE = "\033[@";
    static final String MOVE_RIGHT = "\033[C";
    static final String MOVE_LEFT = "\033[D";
    static final String TOGGLE_INSERT = "\033[4h";
    static final String ERASE_CHARACTER = "\b";
    static final String DELETE_CHARACTER = "\033[P";

    private LineModel currentLine;
    static final int KEY_BACKSPACE = 127;
    static final int KEY_ESCAPE = 2000;
    static final int KEY_START = 2000;
    static final int KEY_RIGHT = 2001;
    static final int KEY_LEFT = 2002;
    static final int KEY_END = 2003;
    static final int KEY_INSERT = 2004;
    static final int KEY_DELETE = 2005;
    static final int KEY_CHARACTER = 2006;
    static final int KEY_FINAL = 2007;

    public Console(LineModel line) {
        this.enableRawMode();
        this.currentLine = line;
    }

    public void update(Observable observable, Object data) {
        int actionCode = (int) data;
        switch (actionCode) {
            case KEY_CHARACTER:
                boolean isInsertMode = this.currentLine.isInsertMode();
                this.displayCharacter(isInsertMode, this.currentLine.getCharacter());
                break;
            case KEY_BACKSPACE:
                this.performBackspace();
                break;
            case KEY_START:
                this.moveCursor(this.currentLine.getCursorPosition());
                break;
            case KEY_RIGHT:
                this.moveCursorRight();
                break;
            case KEY_LEFT:
                this.moveCursorLeft();
                break;
            case KEY_END:
                this.moveCursor(this.currentLine.getCursorPosition());
                break;
            case KEY_INSERT:
                this.toggleInsertMode();
                break;
            case KEY_DELETE:
                this.performDelete();
                break;
            case KEY_FINAL:
                this.disableRawMode();
                break;
            default:
                break;
        }
    }

    public void enableRawMode() {
        String[] command = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disableRawMode() {
        String[] command = {"/bin/sh", "-c", "stty -echo cooked </dev/tty"};
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveCursor(int position) {
        int adjustedPosition = position + 1;
        System.out.print("\033[" + adjustedPosition + "G");
    }

    public void displayCharacter(boolean insert, char character) {
        if (insert) {
            System.out.print(character);
        } else {
            System.out.print(SPACE_SEQUENCE);
            System.out.print(character);
        }
    }

    public void performBackspace() {
        System.out.print(DELETE_CHARACTER);
    }

    public void moveCursorRight() {
        System.out.print(MOVE_RIGHT);
    }

    public void moveCursorLeft() {
        System.out.print(MOVE_LEFT);
    }

    public void toggleInsertMode() {
        System.out.print(TOGGLE_INSERT);
    }

    public void performDelete() {
        System.out.print(DELETE_CHARACTER);
    }
}
