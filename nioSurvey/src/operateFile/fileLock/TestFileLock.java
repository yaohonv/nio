package operateFile.fileLock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import util.LocalTestFile;

public class TestFileLock implements Runnable {
	public static void main(String[] args) {

		Thread t1 = new Thread(new TestFileLock());
		t1.setName("t1");
		Thread t2 = new Thread(new TestFileLock());
		t2.setName("t2");
		Thread t3 = new Thread(new FileWriteThread());
		t3.setName("t3");
		t1.start();
		t2.start();
		t3.start();
	}

	
	public void run() {

		Thread curr = Thread.currentThread();
		System.out.println("Current executing thread is " + curr.getName());
		RandomAccessFile raf = null;
		FileChannel fc = null;
		FileLock lock = null;
		try {
			raf = new RandomAccessFile(/* url.getPath() */LocalTestFile.getDefault_test1_txt(), "rw");
			fc = raf.getChannel();
			System.out.println(curr.getName() + " ready");
			// ��������ļ���ռ����
			while (true) {
				try {
					lock = fc.lock(0,fc.size(),false);// �̼߳� �ظ���ȡ��ռ�����߹��������׳��쳣
					System.out.println(lock.isShared());
//					if(lock.overlaps(0,fc.size())){
//						System.out.println(curr.getName() + "�ظ���ȡ���쳣");
//						Thread.sleep((int) (Math.random() * 2000));
//						continue;
//					}
					break;
				} catch (OverlappingFileLockException e) {
					// ��ͬһ�������������ͬһ�ļ���ĳ�ļ����ͬһ����
					// tryLock��lock����׳�OverlappingFileLockException�쳣��
					System.out.println(curr.getName() + "�ظ���ȡ���쳣");
					Thread.sleep((int) (Math.random() * 2000));
				}
			}
			if (null != lock) {
				System.out.println(curr.getName() + " get filelock success");
				fc.position(fc.size());
				fc.write(ByteBuffer.wrap((curr.getName() + " write data. ").getBytes()));
			} else {
				System.out.println(curr.getName() + " get filelock fail");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// ע�⣺���ͷ������ٹر�ͨ����
			if (null != lock && lock.isValid()) {
				try {
					lock.release();
					System.out.println(curr.getName() + " release filelock");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}