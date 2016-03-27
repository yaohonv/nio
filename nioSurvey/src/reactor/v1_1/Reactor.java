package reactor.v1_1;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import reactor.v1_1.common.Constant;

class Reactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	final static ScheduledExecutorService scheduler = Executors
	.newScheduledThreadPool(Constant.NIO_thread_num);
	
	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor(selector,serverSocket));// 附上接收处理对象，接收成功后在打开的通道的selectKey上添加read事件
	}

	public void run() { // normally in a new
		try {
			while (!Thread.interrupted()) {
//				System.out.println("prepare for servering!");
				int n = selector.select();
				if(n==0){
					continue;
				}
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) (it.next());
					it.remove();
					dispatch(key);
				}
				selected.clear();
			}
		} catch (IOException ex) { /* ... */
		}
	}

	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());// 取出附在此key上面的处理对象
		if (r != null){
			r.run();
//			scheduler.execute(r);
//			new Thread(r).start();
//			scheduler.execute(r);
		}
	}

	public static void main(String[] args) {
		try {
			new Reactor(18080).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}