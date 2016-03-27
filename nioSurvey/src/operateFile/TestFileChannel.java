/********************************************************************
 *
 * [文本信息]
 *
 * nioSamples源代码拷贝权属北京四达时代软件技术股份有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-8-18
 * @revision    Id 1.0
 ********************************************************************/
package operateFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import util.LocalTestFile;

/**
 * @author yaohw
 *
 */
public class TestFileChannel {
	private static Charset charset = Charset.forName("GBK");
	
	public static void main(String[] args) throws Exception {
		File file = LocalTestFile.getDefault_test1_txt();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		
		FileChannel fc = raf.getChannel();
		System.out.println("file size:"+fc.size()+" p: "+fc.position());
		
		ByteBuffer buffer = ByteBuffer.allocate(50);
		buffer.clear();
		fc.read(buffer);
		buffer.flip();
		System.out.println(charset.decode(buffer));
		fc.truncate(26);//截断文件
		System.out.println("file size:"+fc.size()+" p: "+fc.position());
		
		fc.close();
	}

}
