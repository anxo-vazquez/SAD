package Practica_1;

/**
 *
 * @author anxovazquez
 */
public class Line {

    protected StringBuilder line;
    protected int cursor;
    
    public Line() {
        this.line = new StringBuilder();
        this.cursor = 0;
    }

    public StringBuilder getLine() {
        return this.line;
    }

    public StringBuilder put(char c) {
        this.cursor= this.line.length();
        this.line.append(c);
        return this.line;
    }

    public StringBuilder remove() {
        this.line.deleteCharAt(this.cursor);
        return this.line;
    }

    public StringBuilder insert(char c) {
        this.line.insert(c,this.cursor);
        return this.line;
    }
    public StringBuilder moveCursor(int position){
        this.cursor+=position;
        return this.line;
    }
    @Override
    public String toString() {
        return this.line.toString();
    }
}
