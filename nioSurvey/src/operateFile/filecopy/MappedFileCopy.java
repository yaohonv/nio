package operateFile.filecopy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFileCopy extends CommonCopy {

    public MappedFileCopy(String fromFile, String toFile){
        super(fromFile, toFile);
        System.out.println("map ����");
    }

    @Override
    public void run() {
        try {

            File fileIn = new File(fromFile);
            File fileOut = new File(toFile);

            RandomAccessFile raf = new RandomAccessFile(fileIn, "rw");
            // FileInputStream fin = new FileInputStream(fileIn);
            FileOutputStream fout = new FileOutputStream(fileOut);

            FileChannel fcIn = raf.getChannel();
            FileChannel fcOut = fout.getChannel();
            MappedByteBuffer mbb = fcIn.map(FileChannel.MapMode.READ_WRITE, 0, fcIn.size());//�洢��ӳ���ļ�
            while (/*fcIn.read(mbb) != -1*/mbb.hasRemaining()) {
//                mbb.flip();
                fcOut.write(mbb);
//                mbb.clear();
            }
            mbb.clear();
//            mbb.put(1, (byte)'a');//���ļ����������

        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = true;

    }

}
