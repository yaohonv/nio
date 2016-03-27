package operateFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import util.LocalTestFile;

public class NIOFile {

	//abnormally copy file
	public static void copyFile() throws Exception {
//		String infile = "C:\\src.txt";
//		String outfile = "C:\\copy0.txt";

		// 获取源文件和目标文件的输入输出流
		FileInputStream fin = new FileInputStream(LocalTestFile.getDefault_test1_txt());
		FileOutputStream fout = new FileOutputStream(LocalTestFile.getDefaultTo_to1_txt());

		// 获取输入输出通道
		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();

		// 创建缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		long startTime = System.currentTimeMillis();
		
		//测重复写入很多数据
		for (int i = 0; i < 2000; i++) {
			while (true) {
				// clear方法重设缓冲区，使它可以接受读入的数据
				buffer.clear();

				// 从输入通道中将数据读到缓冲区 读一次后输入通道position至数据尾
				int r = fcin.read(buffer);

				// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
				if (r == -1) {
					fcin.position(0);//复位
					break;
				}

				// flip方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();

				// 从缓冲区将数据写入输出通道中
				fcout.write(buffer);
			}
		}
		System.out.println("NIO waste time:"+(System.currentTimeMillis()-startTime));
		
		fin.close();
		fout.close();
	}
}