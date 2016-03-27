/********************************************************************
 *
 * [�ı���Ϣ]
 *
 * nioSamplesԴ���뿽��Ȩ�������Ĵ�ʱ����������ɷ����޹�˾���У�
 * �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-8-23
 * @revision    Id 1.0
 ********************************************************************/
package operateFile.filecopy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author yaohw
 * 
 */
public class TransferFileCopy extends CommonCopy {

	TransferFileCopy(String from, String to) {
		super(from, to);
		System.out.println("file transfer����");
	}
@Override
	public void run() {

		// fromFile = "test1.zip";
		// toFile = "d:/down/" + filename;
		long i = 0;
		try {
			// File file = LocalTestFile.getBy_para(filename);
			RandomAccessFile raf = new RandomAccessFile(fromFile, "rw");
			FileChannel fileChannel = new FileOutputStream(toFile).getChannel();
			i = raf.getChannel().transferTo(0, raf.length(), fileChannel);
//			if (raf.length() - i == 0) {
//				System.out.println("size:" + raf.length() + " byte copy successly!");
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		flag=true;
	}
}
