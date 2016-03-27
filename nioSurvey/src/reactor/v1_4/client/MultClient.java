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
 * 模拟下载的客户端
 * 
 * @author yaohw
 * 
 */
public class MultClient implements Runnable {
	private Log log = LogFactory.getLog(this.getClass());
	private FileChannel fileChannel;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 5);
	private String serverFileName;// 服务器上的文件
	private String localFileName;// 下载到客户端的文件名
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

			// 判断此通道上是否正在进行连接操作
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
		// 向本机下载文件创建文件channel
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
			// 写到下载文件中
			fileChannel.write(byteBuffer);
			/**
			 * 这里r==0只有在non-block状态下才会发生
			 */
			if (mesLength >= fileLength) {
//				socketChannel.close();
				fileChannel.close();
				break;
			}
		}
	}

	// 客户端用10个线程向服务器端下载文件,并保存为不同的文件 10个客户端,并行执行下载任务
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

			// 换行符 readline
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
