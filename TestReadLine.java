package Practica_1;

import java.io.*;
import static java.lang.System.in;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anxovazquez
 */
public class TestReadLine {

    public static void main(String[] args) {
        EditableBufferedReader in = new EditableBufferedReader(new InputStreamReader(System.in));

        String str = null;

        try {
            str = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(
                "\nline is: " + str);
    }
}
