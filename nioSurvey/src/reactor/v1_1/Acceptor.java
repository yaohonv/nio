/********************************************************************
 *
 * [�ı���Ϣ]
 *
 * nioSamplesԴ���뿽��Ȩ�������Ĵ�ʱ����������ɷ����޹�˾���У�
 * �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-8-24
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_1;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yaohw
 *
 */
public class Acceptor implements Runnable{

	private Log log = LogFactory.getLog(this.getClass());
	final Selector selector;
	final ServerSocketChannel serverSocket;

	
	public Acceptor(Selector selector,ServerSocketChannel serverSocket) {
		this.selector=selector;
		this.serverSocket=serverSocket;
	}
	@Override
	public void run() {
		try {
			SocketChannel c = serverSocket.accept();
			log.info("a client comming!");
			if (c != null)
				new ReadHandler(selector, c);
		} catch (IOException ex) {
		}
		
	}
}
