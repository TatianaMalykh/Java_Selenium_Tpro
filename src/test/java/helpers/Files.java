package helpers;

import java.io.*;

public class Files {
    public void write(String fileName, String text) {
        File file = new File(fileName);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file.getAbsoluteFile(), true);// если перезаписывать - true не забыть убрать
            fr.write(text);
            fr.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        fr.close();
    }
}