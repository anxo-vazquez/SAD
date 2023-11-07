package Practica_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author anxovazquez
 */
public class EditableBufferedReader extends BufferedReader {

    private static int defaultCharBufferSize = 8192;

    protected Line linea;
    
    public EditableBufferedReader(Reader in) {
        super(in, defaultCharBufferSize);
        this.linea = new Line();
    }

    public void setRaw() throws IOException {
        String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
        Runtime.getRuntime().exec(cmd);
    }

    public void unsetRaw() throws IOException {
        String[] cmd = {"/bin/sh", "-c", "stty cooked </dev/tty"};
        Runtime.getRuntime().exec(cmd);
    }

    @Override
    public int read() throws IOException {
        int temp = super.read();
        System.out.println(temp);
        return temp;

    }

    @Override
    public String readLine() throws IOException {
        int userInput = -1;
        while (userInput != 10) {
            userInput = this.read();
            switch (userInput) {
                case 27:
                    break;
                case 8:
                    this.linea.remove();
                    break;
                default:
                    linea.insert((char)userInput);
                    break;
            }

            System.out.println(linea.toString());
        }
        return linea.toString();

    }
}
