package reactor.v1_3.NIOServer.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * 主线程，搞定响应请求
 * @author yaohw
 *
 */
public class AcceptReactor extends AbstractReactor {
	// Selector selector;
	public AcceptReactor(int port) throws IOException {
		super();
		final ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port)/*,100*/);//设定第二个参数可以指定连接等待数backlog，默认为50
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor(serverSocket));// 附上接收处理对象，接收成功后在打开的通道的selectKey上添加read事件
	}
}