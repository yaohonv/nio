package operateFile.fileLock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import util.LocalTestFile;

public class FileWriteThread implements Runnable {

	public void run() {

		Thread curr = Thread.currentThread();

		System.out.println("Current executing thread is " + curr.getName());

		RandomAccessFile raf = null;

		FileChannel fc = null;

		try {

			raf = new RandomAccessFile(/* url.getPath() */LocalTestFile.getDefault_test1_txt(), "rw");

			fc = raf.getChannel();

			System.out.println(curr.getName() + " ready");

			fc.position(fc.size());

			//如果另一个线程锁住文件，则抛出”java.io.IOException: 另一个程序已锁定文件的一部分，进程无法访问。“
			fc.write(ByteBuffer.wrap((curr.getName() + " write data. \n").getBytes()));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}