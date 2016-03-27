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
 * @create-time 2011-8-30
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.process;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.common.Response;
import reactor.v1_4.NIOServer.process.core.ProcessHandler;

/**
 * @author yaohw
 * 
 */
public class MyProcessHandler extends ProcessHandler {
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @param reqData
	 * @param key
	 */
	public MyProcessHandler(Request req, SelectionKey key) {
		super(req, key);
	}

	/**
	 * ���ݵ�ҵ���߼������� eg.
	 */
	@Override
	public void process(Request request, Response response) {
		log.debug("my own process");
		response.setResponseData(request.getRequestData());
		try {
			response.setHostAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
