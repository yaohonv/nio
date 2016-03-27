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
