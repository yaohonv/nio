package reactor.v1_3.NIOServer.reader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_3.NIOServer.AbstractHandler;
import reactor.v1_3.NIOServer.process.Process;

public abstract class ReadHandler extends AbstractHandler implements Reader {
	private Log log = LogFactory.getLog(this.getClass());
	private static Class<? extends Process> process;
	static int num;

	/**
	 * 请求数据缓存
	 */
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 2);
	final SocketChannel socketChannel;
	private Request request;

	public ReadHandler(Selector sel, SocketChannel c) throws IOException {
		super(null);
		request = new Request();
		socketChannel = c;
		socketChannel.configureBlocking(false);
		// Optionally try first read now
		key = socketChannel.register(sel, 0);
		key.attach(this);
		key.interestOps(SelectionKey.OP_READ);
		// sel.wakeup();//客户端写数据了，才可以读

	}

	private Process instanceProcess() {
		try {
			return process.getConstructor(Request.class, SelectionKey.class).newInstance(request,
					key);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	// class Handler continued
	@Override
	public void run() {
		try {
			read();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		parseData(readBuffer, request);
		Process process = instanceProcess();
		if(process!=null){
			process.startWithThread();
		}else{
			log.error("error Instantiate the Process");
		}
	}

	private void read() throws IOException, UnknownHostException {
		log.debug("read");
		// synchronized (readBuffer) {
		int numRead;
		// while (true) {
		
		/**
		 * 这里需要添加一个判断数据是否读完的逻辑
		 */
		this.readBuffer.clear();
		try {
			numRead = socketChannel.read(this.readBuffer);// no blocking , return 0 if
													// null
			// data from the channel is null
			if (numRead <= 0) {
				return;
			}
		} catch (IOException e) {
			// The remote forcibly closed the connection
			key.cancel();
			socketChannel.close();
			return;
		}
		// have problem temp!!!

		// }
		// }
		readBuffer.flip();
		readBuffer = readBuffer.slice();
	}

	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}
	/**
	 * @param process the process to set
	 */
	public static void setProcess(Class<? extends Process> process) {
		ReadHandler.process = process;
	}
}