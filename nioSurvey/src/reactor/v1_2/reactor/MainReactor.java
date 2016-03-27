package reactor.v1_2.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import reactor.v1_2.common.Constant;
import reactor.v1_2.handler.AcceptorHandler;

/**
 * 主线程，搞定响应请求
 * @author yaohw
 *
 */
class MainReactor extends Reactor {
	private IOReactor[] ioReactors;
	// Selector selector;
	MainReactor(int port) throws IOException {
		super();
		initIOReactor();
		final ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new AcceptorHandler(ioReactors,serverSocket));// 附上接收处理对象，接收成功后在打开的通道的selectKey上添加read事件
	}
	public void initIOReactor() throws IOException{
		ioReactors = new IOReactor[Constant.NIO_reactor_num];
		for(int i=0;i<ioReactors.length;i++){
			ioReactors[i] = new IOReactor();
			new Thread(ioReactors[i]).start();
		}
	}
	
	public static void main(String[] args) {
		try {
			new MainReactor(18080).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}