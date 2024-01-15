import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
    LineModel textLine;
    TerminalController terminalController;
    static final int KEY_ENTER = 13;
    static final int KEY_RIGHT_ARROW = 67;
    static final int KEY_LEFT_ARROW = 68;
    static final int KEY_HOME = 72;
    static final int KEY_INSERT = 50;
    static final int KEY_DELETE = 51;
    static final int KEY_BACKSPACE = 127;
    static final int KEY_ESCAPE = 27;
    static final int KEY_BRACKET = 91;
    static final int KEY_TILDE = 126;
    static final int KEY_END = 0;
    static final int ACTION_BACKSPACE = 127;
    static final int ACTION_INSERT = 2004;
    static final int ACTION_DELETE = 2005;
    static final int ACTION_ESCAPE = 2000;
    static final int ACTION_HOME = 2000;
    static final int ACTION_MOVE_RIGHT = 2001;
    static final int ACTION_MOVE_LEFT = 2002;
    static final int ACTION_END = 2003;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.textLine = new LineModel();
        this.terminalController = new TerminalController(this.textLine);
        this.textLine.addObserver(this.terminalController);
    }

    public int read() throws IOException {
        int charRead = super.read();
        if (charRead == KEY_ESCAPE) {
            charRead = super.read();
            if (charRead == KEY_BRACKET) {
                charRead = this.read();
                switch (charRead) {
                    case KEY_HOME:
                        return ACTION_HOME;
                    case KEY_LEFT_ARROW:
                        return ACTION_MOVE_LEFT;
                    case KEY_RIGHT_ARROW:
                        return ACTION_MOVE_RIGHT;
                    case KEY_END:
                        return ACTION_END;
                    case KEY_INSERT:
                        charRead = super.read();
                        if (charRead == KEY_TILDE) {
                            return ACTION_INSERT;
                        }
                        return -1;
                    case KEY_DELETE:
                        charRead = super.read();
                        if (charRead == KEY_TILDE) {
                            return ACTION_DELETE;
                        }
                        return -1;
                    default:
                        return -1;
                }
            }
        } else if (charRead == KEY_BACKSPACE) {
            return ACTION_BACKSPACE;
        } else {
            return charRead;
        }
        return -1;
    }

    public String readLine() throws IOException {
        int charRead;
        do {
            charRead = this.read();
            if (charRead >= ACTION_ESCAPE) {
                switch (charRead) {
                    case ACTION_MOVE_RIGHT:
                        this.textLine.moveRight();
                        break;
                    case ACTION_MOVE_LEFT:
                        this.textLine.moveLeft();
                        break;
                    case ACTION_HOME:
                        this.textLine.moveToStart();
                        break;
                    case ACTION_INSERT:
                        this.textLine.toggleInsertMode();
                        break;
                    case ACTION_DELETE:
                        this.textLine.deleteCharacter();
                        break;
                    case ACTION_BACKSPACE:
                        this.textLine.backspace();
                        break;
                }
            } else if (charRead != KEY_ENTER) {
                this.textLine.addCharacter((char) charRead);
            } else if (charRead == ACTION_BACKSPACE) {
                this.textLine.backspace();
            }
        } while (charRead != KEY_ENTER);
        this.textLine.confirmEntry();
        return textLine.toString();
    }
}
