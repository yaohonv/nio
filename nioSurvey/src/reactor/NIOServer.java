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
	private int port1 = 18080;// �����������
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024*5);
	
	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Constant.NIO_thread_num);

	public NIOServer() {
		initSelector();
	}

	/**
	 * ʵ����selector��ע��ServerSocketChannel ׼����������
	 */
	public void initSelector() {
		try {
			// selector = SelectorProvider.provider().openSelector();
			selector = Selector.open();
			this.serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			InetSocketAddress isa = new InetSocketAddress(this.port1);
			serverChannel.socket().bind(isa);//���ﻹ����ָ�����ӵȴ����е������Ŀ��JDKĬ��Ϊ50
			//ע������¼�
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
		//��һ���µ�socketChannel��ע����¼�
		socketChannel.register(this.selector, SelectionKey.OP_READ);
		selector.wakeup();//û�д˲������ڸ�socketChannelͨ����������ʱ����,�������´�select()���ܷ���0

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
		
		//��ͨ���ж������ݣ�ע��д�¼�,����ԭ���Ķ��¼�������û�е���selector.wakeup()
//		key = socketChannel.register(this.selector, SelectionKey.OP_WRITE);
		key.attach(fileName);
		scheduler.execute(new Process(key));
	}

	public void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
//		log.info("write data to client!");
//		long startTime = System.currentTimeMillis();
		// �����ļ�д������׷�ӵ���ʽ
		RandomAccessFile raf = new RandomAccessFile(LocalTestFile.getBy_para(key.attachment().toString()),
				"rw");
//		log.info("size of the file being downloaded : " + raf.length());
//		int j = 0;//���� �ֽ�
//		int n = 0;//д����ʧ�ܴ���
		
//		raf.getChannel().transferTo(0, raf.length(), socketChannel);
		
		while (true) {
			this.readBuffer.clear();
			int i = raf.getChannel().read(readBuffer);
			readBuffer.flip();
			if (i <= 0) {
				socketChannel.close();//���رգ��ͻ��˻�������ͨ����
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
//		System.out.println(endTime + "����������ʱ��" + (endTime - startTime));
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
			key.selector().wakeup();//�ǵõ��ô˷��� �Ա���һ����д�϶�����ִ��
		}
	}

}
