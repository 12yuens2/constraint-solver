package util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CSVWriter {

    private PrintWriter writer;
    
    public CSVWriter(String filename) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(
                                          new OutputStreamWriter(
                                          new FileOutputStream(filename), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
    }
    
    public void writeRow(Object... data) {
        StringBuilder builder = new StringBuilder();
        
        for (Object o: data) {
            builder.append(o + ",");
        }
        
        String row = builder.toString();
        writer.write(row + "\n");
        writer.flush();
    }
    
    public void close() {
        writer.flush();
        writer.close();
    }
}
