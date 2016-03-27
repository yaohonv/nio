package operateFile.filecopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ��NIOFileCopy.java��ʵ��������ʹ��NIO�����ļ���д
 * 
 * @author yblin 2010-10-17 ����02:09:31
 */
public class NIOFileCopy extends CommonCopy {

    public NIOFileCopy(String fromFile, String toFile){
        super(fromFile, toFile);
        System.out.println("NIO����");
    }

    @Override
    public void run() {
        try {

            File fileIn = new File(fromFile);
            File fileOut = new File(toFile);
            FileInputStream fin = new FileInputStream(fileIn);
            FileOutputStream fout = new FileOutputStream(fileOut);

            FileChannel fcIn = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(1024*5);
            FileChannel fcOut = fout.getChannel();
            while (fcIn.read(bf) != -1) {
                bf.flip();//for read from buffer
                fcOut.write(bf);
                bf.clear();//for write into buffer
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = true;

    }


}
