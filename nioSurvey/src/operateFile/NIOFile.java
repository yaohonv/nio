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

		// ��ȡԴ�ļ���Ŀ���ļ������������
		FileInputStream fin = new FileInputStream(LocalTestFile.getDefault_test1_txt());
		FileOutputStream fout = new FileOutputStream(LocalTestFile.getDefaultTo_to1_txt());

		// ��ȡ�������ͨ��
		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();

		// ����������
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		long startTime = System.currentTimeMillis();
		
		//���ظ�д��ܶ�����
		for (int i = 0; i < 2000; i++) {
			while (true) {
				// clear�������軺������ʹ�����Խ��ܶ��������
				buffer.clear();

				// ������ͨ���н����ݶ��������� ��һ�κ�����ͨ��position������β
				int r = fcin.read(buffer);

				// read�������ض�ȡ���ֽ���������Ϊ�㣬�����ͨ���ѵ�������ĩβ���򷵻�-1
				if (r == -1) {
					fcin.position(0);//��λ
					break;
				}

				// flip�����û��������Խ��¶��������д����һ��ͨ��
				buffer.flip();

				// �ӻ�����������д�����ͨ����
				fcout.write(buffer);
			}
		}
		System.out.println("NIO waste time:"+(System.currentTimeMillis()-startTime));
		
		fin.close();
		fout.close();
	}
}