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
package reactor.v1_3.NIOServer.selector;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import reactor.v1_3.Constant;
/**
 * 处理接收，打开一个新的socket，并添加到某个IOReactor中
 * @author yaohw
 */
public class Acceptor implements Runnable {

//	private Log log = LogFactory.getLog(this.getClass());
	private IOReactor[] ioReactors;
	final ServerSocketChannel serverSocket;
	static int next = 0;

	public Acceptor(ServerSocketChannel serverSocket) {
		this.serverSocket = serverSocket;
		initIOReactor();
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
	
	public void initIOReactor(){
		ioReactors = new IOReactor[Constant.NIO_reactor_num];
		for(int i=0;i<ioReactors.length;i++){
			try {
				ioReactors[i] = new IOReactor();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(ioReactors[i]).start();
		}
	}
}
