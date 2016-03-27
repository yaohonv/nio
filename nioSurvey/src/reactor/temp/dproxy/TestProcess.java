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
 * @create-time 2011-8-25
 * @revision    Id 1.0
 ********************************************************************/
package reactor.temp.dproxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_3.NIOServer.reader.Request;
import reactor.v1_3.NIOServer.reader.Response;

/**
 * @author yaohw
 * 
 */
public class TestProcess implements Proxied{
	private Log log = LogFactory.getLog(this.getClass());

	public void process(Request request,Response response){
		log.debug("my own process");
		String[] str = ((String)request.getRequestData()).split(",");
		int num = 0;
		System.out.println((++num)+"c "+(System.currentTimeMillis()-Long.parseLong(str[0])));
		response.setResponseData(str[1]);
		try {
			response.setHostAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
