package operateFile.fileLock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import util.LocalTestFile;

public class FileWriteThreadWithLock implements Runnable {

	public static void main(String[] args) {
		new Thread(new FileWriteThreadWithLock()).start();
	}

	public void run() {

		Thread curr = Thread.currentThread();

		System.out.println("Current executing thread is " + curr.getName());

		RandomAccessFile raf = null;

		FileChannel fc = null;

		FileLock fileLock = null;

		try {

			raf = new RandomAccessFile(/* url.getPath() */LocalTestFile.getDefault_test1_txt(), "rw");

			fc = raf.getChannel();

			System.out.println(curr.getName() + " ready");

			fileLock = fc.lock(0,fc.size(),true);// 不同进程间lock锁定同一文件的同一区域或是有重叠时会阻塞

			if (fileLock != null) {
				fc.position(fc.size());

				fc.write(ByteBuffer.wrap((curr.getName() + " write data. \n").getBytes()));
			} else {
				System.out.println(curr.getName() + " get filelock fail");
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (fileLock != null && fileLock.isValid()) {
				try {
					fileLock.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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