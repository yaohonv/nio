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
	 * 数据的业务逻辑处理部分 eg.
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
