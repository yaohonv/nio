/********************************************************************
 *
 * [文本信息]
 *
 * nioSamples源代码拷贝权属北京四达时代软件技术股份有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
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
 * 处理接收，打开一个新的socket，并添加到某个IOReactor中
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
