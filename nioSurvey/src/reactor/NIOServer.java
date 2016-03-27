package reactor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_1.common.Constant;
import util.LocalTestFile;
/**
 * 
 * @author yaohw
 */
public class NIOServer implements Runnable {

	private Log log = LogFactory.getLog(this.getClass());
	private int port1 = 18080;// 负责完成下载
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024*5);
	
	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Constant.NIO_thread_num);

	public NIOServer() {
		initSelector();
	}

	/**
	 * 实例化selector并注册ServerSocketChannel 准备接收连接
	 */
	public void initSelector() {
		try {
			// selector = SelectorProvider.provider().openSelector();
			selector = Selector.open();
			this.serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			InetSocketAddress isa = new InetSocketAddress(this.port1);
			serverChannel.socket().bind(isa);//这里还可以指定连接等待队列的最大数目，JDK默认为50
			//注册接收事件
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (true) {
			try {
//				log.info("prepare for servering");
				int n = this.selector.select();
				if (n <= 0) {
					continue;
				}
				Iterator selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		log.info("a client connect!");
		socketChannel.socket().close();
		socketChannel.configureBlocking(false);
		//打开一个新的socketChannel后注册读事件
		socketChannel.register(this.selector, SelectionKey.OP_READ);
		selector.wakeup();//没有此操作则在该socketChannel通道上有数据时触发,调用则下次select()可能返回0

	}

	public void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		StringBuffer fileName = new StringBuffer("");
		// Attempt to read off the channel
		int numRead;
		while (true) {
			this.readBuffer.clear();
			try {
				numRead = socketChannel.read(this.readBuffer);
				// data from the channel is null
				if (numRead <= 0) {
					break;
				}
			} catch (IOException e) {
				// The remote forcibly closed the connection
				key.cancel();
				socketChannel.close();
				return;
			}

			fileName.append(new String(readBuffer.array()).substring(0, numRead).trim());
		}
//		System.out.println("server get data from client:" + fileName);
		
		//从通道中读完数据，注册写事件,覆盖原来的读事件，这里没有调用selector.wakeup()
//		key = socketChannel.register(this.selector, SelectionKey.OP_WRITE);
		key.attach(fileName);
		scheduler.execute(new Process(key));
	}

	public void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
//		log.info("write data to client!");
//		long startTime = System.currentTimeMillis();
		// 这种文件写入是以追加的形式
		RandomAccessFile raf = new RandomAccessFile(LocalTestFile.getBy_para(key.attachment().toString()),
				"rw");
//		log.info("size of the file being downloaded : " + raf.length());
//		int j = 0;//传送 字节
//		int n = 0;//写操作失败次数
		
//		raf.getChannel().transferTo(0, raf.length(), socketChannel);
		
		while (true) {
			this.readBuffer.clear();
			int i = raf.getChannel().read(readBuffer);
			readBuffer.flip();
			if (i <= 0) {
				socketChannel.close();//不关闭，客户端会阻塞在通道上
				key.cancel();
				break;
			}
			// here attention! use while loop -yaohw
			try {
			while (readBuffer.hasRemaining()) {
				socketChannel.write(readBuffer);
//				if(readBuffer.hasRemaining()){
//					n++;
////					System.out.println(readBuffer.limit()-readBuffer.position());
//				}
			}
			} catch (IOException e) {
				// The remote forcibly closed the connection
				key.cancel();
				socketChannel.close();
				return;
			}
//			j += i;
		}
//		System.out.println("GOD:"+n);
//		long endTime = System.currentTimeMillis();
//		System.out.println(endTime + "传送数据用时：" + (endTime - startTime));
		key.cancel();
		socketChannel.close();
	}

	public static void main(String[] args) {
		NIOServer server = new NIOServer();
		Thread t = new Thread(server);
		t.start();
	}
	
	class Process implements Runnable{
		SelectionKey key;
		Process(SelectionKey key){
			this.key= key;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			key.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();//记得调用此方法 以便下一步的写肯定可以执行
		}
	}

}
