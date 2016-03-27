package operateFile.filecopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StreamFileCopy extends CommonCopy {

    public StreamFileCopy(String fromFile, String toFile){
        super(fromFile, toFile);
        System.out.println("Á÷¸´ÖÆ");
    }

    @Override
    public void run() {
        try {

            File fileIn = new File(fromFile);
            File fileOut = new File(toFile);
            FileInputStream fin = new FileInputStream(fileIn);
            FileOutputStream fout = new FileOutputStream(fileOut);
            byte[] buffer = new byte[8192];
            while (fin.read(buffer) != -1) {
                fout.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = true;

    }

}
