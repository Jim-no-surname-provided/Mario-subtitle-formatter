package formatter.inout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InOut {

    public static final char eof = '\uffff';

    public static String readFile(String pathStr) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathStr))) {
            StringBuilder sb = new StringBuilder();
            char c = (char) reader.read();

            while (c != eof) {
                sb.append(c);
                c = (char) reader.read();
            }

            return sb.toString();

        } catch (IOException e) {
            System.err.println("The file was not found: " + e.getMessage());
            return null;
        }
    }

    public static boolean writeFile(String pathStr, String text) {

        try {
            Files.write(Paths.get(pathStr), text.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String[] readLines(String pathStr) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathStr))) {
            return toStringArray(reader.lines().toArray());
        } catch (IOException e) {
            System.err.println("The file was not found: " + e.getMessage());
            return null;
        }
    }

    public static String[] toStringArray(Object[] a) {
        String[] result = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i].toString();
        }
        return result;
    }
}