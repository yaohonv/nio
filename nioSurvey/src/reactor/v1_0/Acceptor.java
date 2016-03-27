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
package reactor.v1_0;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yaohw
 *
 */
public class Acceptor implements Runnable{

	final Selector selector;
	final ServerSocketChannel serverSocket;

	
	public Acceptor(Selector selector,ServerSocketChannel serverSocket) {
		this.selector=selector;
		this.serverSocket=serverSocket;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			SocketChannel c = serverSocket.accept();
			if (c != null)
				new Handler(selector, c);
		} catch (IOException ex) { /* ... */
		}
		
	}
}
