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
 * @create-time 2011-8-31
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_3;

import java.io.IOException;

import reactor.v1_3.NIOServer.process.MyProcessHandler;
import reactor.v1_3.NIOServer.process.ProcessHandler;
import reactor.v1_3.NIOServer.reader.MyReadHandler;
import reactor.v1_3.NIOServer.reader.ReadHandler;
import reactor.v1_3.NIOServer.selector.AcceptReactor;
import reactor.v1_3.NIOServer.selector.IOReactor;
import reactor.v1_3.NIOServer.sender.MyDoResponseHandler;

/**
 * @author yaohw
 *
 */
public class StartNIOServer {
	public static void main(String[] args) {
		registerHandlers();
		try {
			new AcceptReactor(18080).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void registerHandlers(){
		IOReactor.setReader(MyReadHandler.class);
		ReadHandler.setProcess(MyProcessHandler.class);
		ProcessHandler.setSender(MyDoResponseHandler.class);
	}
}
