package reactor.v1_4.client;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_4.NIOServer.common.Constant;

/**
 * ģ�����صĿͻ���
 * 
 * @author yaohw
 * 
 */
public class MultClient implements Runnable {
	private Log log = LogFactory.getLog(this.getClass());
	private FileChannel fileChannel;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 5);
	private String serverFileName;// �������ϵ��ļ�
	private String localFileName;// ���ص��ͻ��˵��ļ���
	SocketChannel socketChannel;
	static int succ_num = 0;
	static int fail_num = 0;
	long startTime = System.currentTimeMillis();
	static long average_time = 0;

	public MultClient(String serverFileName, String localFileName) {
		this.serverFileName = serverFileName;
		this.localFileName = localFileName;
	}

	public void run() {
		try {
			socketChannel = SocketChannel.open();
			// channel.configureBlocking(false);
			// startTime =System.currentTimeMillis();
			// socketChannel.socket().setReceiveBufferSize(1024);
			socketChannel.connect(new InetSocketAddress("192.168.249.77", 18080));

			// �жϴ�ͨ�����Ƿ����ڽ������Ӳ���
			// if (socketChannel.isConnectionPending())
			if (socketChannel.finishConnect())
				log.debug(Thread.currentThread().getName() + " con.");
			for(int i = 0; i<Constant.IO_request_num;i++){
				doGet(i);
			}
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail_num++;
			return;
		}
		long tem = System.currentTimeMillis() - startTime;
		average_time += tem;
		succ_num++;
		/* log.info */System.out.println(Thread.currentThread().getName() + " wasteTime " + tem
				+ " ,fail " + fail_num + ", average_time " + average_time / succ_num);
	}

	public void doGet(int i) throws IOException{
		// channel.write(ByteBuffer.wrap((/*startTime+","+serverFileName*/"2").getBytes()));
		socketChannel.write(ByteBuffer.wrap((startTime + "," + serverFileName).getBytes()));
		// log.info("start down");
		byteBuffer.clear();
		// �򱾻������ļ������ļ�channel
		if (fileChannel==null||!fileChannel.isOpen())
			fileChannel = new RandomAccessFile(localFileName+"_"+ i+".zip", "rw").getChannel();
		
		ByteBuffer mesLengthBuffer = ByteBuffer.allocate(8);
		socketChannel.read(mesLengthBuffer);
		mesLengthBuffer.flip();
		long fileLength = mesLengthBuffer.asLongBuffer().get();
		long mesLength = 0;
		while (true) {
			byteBuffer.clear();
			int r = socketChannel.read(byteBuffer);
			mesLength += r;
			
			byteBuffer.flip();
			// д�������ļ���
			fileChannel.write(byteBuffer);
			/**
			 * ����r==0ֻ����non-block״̬�²Żᷢ��
			 */
			if (mesLength >= fileLength) {
//				socketChannel.close();
				fileChannel.close();
				break;
			}
		}
	}

	// �ͻ�����10���߳���������������ļ�,������Ϊ��ͬ���ļ� 10���ͻ���,����ִ����������
	public static void main(String[] args) {
		for (int i = 0; i < Constant.client_num; i++) {
			String filename1 =
			// "test1.zip"
			// "test.zip"
			// "to1.txt"
			// "test1.txt"
			"test" + i
			// "test"+i+".txt"
			;
			// String filename2 =
			// "test1.zip"
			// // "test.zip"
			// // "to1.txt"
			// // "test1.txt"
			// ;

			// ���з� readline
			new Thread(new MultClient("test.zip" + "\r\n", "d:/down/" + filename1), "client" + i)
					.start();
			// try {
			// // if(i>100){
			// // Thread.sleep(100);
			// // }
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

		}
	}
}