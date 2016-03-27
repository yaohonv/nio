package reactor.v1_3.NIOServer.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * ���̣߳��㶨��Ӧ����
 * @author yaohw
 *
 */
public class AcceptReactor extends AbstractReactor {
	// Selector selector;
	public AcceptReactor(int port) throws IOException {
		super();
		final ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port)/*,100*/);//�趨�ڶ�����������ָ�����ӵȴ���backlog��Ĭ��Ϊ50
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor(serverSocket));// ���Ͻ��մ�����󣬽��ճɹ����ڴ򿪵�ͨ����selectKey�����read�¼�
	}
}