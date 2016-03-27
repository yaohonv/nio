package reactor.v1_1.client;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import reactor.v1_1.common.Constant;

/**
 * 模拟下载的客户端
 * @author yaohw
 *
 */
public class Client implements Runnable{
//	private Log log = LogFactory.getLog(this.getClass());
	private FileChannel fileChannel;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024*5);
	private String serverFileName;// 服务器上的文件
	private String localFileName;// 下载到客户端的文件名
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

		// 判断此通道上是否正在进行连接操作
		if (channel.isConnectionPending())
			channel.finishConnect();
		channel.write(ByteBuffer.wrap((startTime+","+serverFileName).getBytes()));
//		channel.write(ByteBuffer.wrap((startTime+","+serverFileName).getBytes()));
//		log.info("start down");
		// socketChannel.configureBlocking(true);
		byteBuffer.clear();
		// 向本机下载文件创建文件channel
		if (fileChannel == null)
			fileChannel = new RandomAccessFile(localFileName,"rw").getChannel();
		// fileChannel.transferFrom(position, count, target)
//long j=0;
		while (true) {
			byteBuffer.clear();
			int r = channel.read(byteBuffer);
			/**
			 * 文件流会有一个结束符标志，client的read操作会读到这个标志后下次读时直接返回-1，不阻塞
			 * 
			 * 这里返回0只有在non-block状态下才会发生
			 */
			if(r==-1){
				System.out.println("server's socket has closed");
			}
			if (r <= 0) {
				// key.cancel();
				channel.close();
				fileChannel.close();
//				log.info("download A success");
				break;
			}
			byteBuffer.flip();
			// 写到下载文件中
			fileChannel.write(byteBuffer);
//j+=r;
		}
		}catch(IOException e){
//			e.printStackTrace();
//			System.out.println("server shutdown");
			fail_num++;
			return;
//			System.exit(-1);
		}
			long tem = System.currentTimeMillis()-startTime;
			average_time += tem;
			succ_num++;
			/*log.info*/System.out.println(Thread.currentThread().getName()+" waster time ,"+tem+" fail ,"+fail_num+" average_time,"+average_time/succ_num);
	}

	// 客户端用10个线程向服务器端下载文件,并保存为不同的文件 10个客户端,并行执行下载任务
	public static void main(String[] args) {
		// ExecutorService executorService =
		// Executors.newSingleThreadExecutor();
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
			
												//换行符 readline
				new Thread(new Client("test.zip"+"\n", "d:/down/" + filename1),"client"+i).start();
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			
		}
	}
}
