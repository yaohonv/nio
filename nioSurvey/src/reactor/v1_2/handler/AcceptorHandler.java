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
package reactor.v1_2.handler;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_2.reactor.IOReactor;

/**
 * ������գ���һ���µ�socket������ӵ�ĳ��IOReactor��
 * @author yaohw
 */
public class AcceptorHandler implements Runnable {

	private Log log = LogFactory.getLog(this.getClass());
	private IOReactor[] ioReactors;
	final ServerSocketChannel serverSocket;
	static int next = 0;

	public AcceptorHandler(IOReactor[] ioReactors,ServerSocketChannel serverSocket) {
		this.serverSocket = serverSocket;
		this.ioReactors = ioReactors;
	}

	@Override
	public void run() {
		try {
			SocketChannel c = serverSocket.accept();
//			log.info("a client comming!");
			if (c != null) {
				ioReactors[next].addSocket(c);
			}
			if (++next == ioReactors.length)
				next = 0;
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
