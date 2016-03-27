package reactor.v1_3.client;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import reactor.v1_3.Constant;

/**
 * ģ�����صĿͻ���
 * @author yaohw
 *
 */
public class Client implements Runnable{
//	private Log log = LogFactory.getLog(this.getClass());
	private FileChannel fileChannel;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024*5);
	private String serverFileName;// �������ϵ��ļ�
	private String localFileName;// ���ص��ͻ��˵��ļ���
	SocketChannel channel;
	static int succ_num =0;
	static int fail_num =0;
	long startTime;
	static long average_time=0;
	
	public Client(String serverFileName, String localFileName) {
		this.serverFileName = serverFileName;
		this.localFileName = localFileName;
	}

	public void run(){
		try{
		channel = SocketChannel.open();
//		channel.configureBlocking(false);
		startTime =System.currentTimeMillis();
		channel.connect(new InetSocketAddress("127.0.0.1", 18080));

		// �жϴ�ͨ�����Ƿ����ڽ������Ӳ���
		if (channel.isConnectionPending())
			channel.finishConnect();
//		channel.write(ByteBuffer.wrap((/*startTime+","+serverFileName*/"2").getBytes()));
		channel.write(ByteBuffer.wrap((startTime+","+serverFileName).getBytes()));
//		log.info("start down");
		byteBuffer.clear();
		// �򱾻������ļ������ļ�channel
		if (fileChannel == null)
			fileChannel = new RandomAccessFile(localFileName,"rw").getChannel();
		while (true) {
			byteBuffer.clear();
			int r = channel.read(byteBuffer);
			/**
			 * �ļ�������һ����������־��client��read��������������־���´ζ�ʱֱ�ӷ���-1��������
			 * �����������������ʱ������
			 * ���ﷵ��0ֻ����non-block״̬�²Żᷢ��
			 */
			if (r <= 0) {
				channel.close();
				fileChannel.close();
				break;
			}
			byteBuffer.flip();
			// д�������ļ���
			fileChannel.write(byteBuffer);
		}
		}catch(IOException e){
			fail_num++;
			return;
		}
			long tem = System.currentTimeMillis()-startTime;
			average_time += tem;
			succ_num++;
			/*log.info*/System.out.println(Thread.currentThread().getName()+" waster time ,"+tem+" fail ,"+fail_num+" average_time,"+average_time/succ_num);
	}

	// �ͻ�����10���߳���������������ļ�,������Ϊ��ͬ���ļ� 10���ͻ���,����ִ����������
	public static void main(String[] args) {
		for (int i = 0; i < Constant.client_num; i++) {
			String filename1 = 
//				"test1.zip"
//				"test.zip"
//				"to1.txt"
//				"test1.txt"
				"test"+i+".zip"
//				"test"+i+".txt"
				;
//			String filename2 = 
//				"test1.zip"
////				"test.zip"
////				"to1.txt"
////				"test1.txt"
//				;
			
												//���з� readline
				new Thread(new Client("test.zip"+"\n", "d:/down/" + filename1),"client"+i).start();
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			
		}
	}
}
