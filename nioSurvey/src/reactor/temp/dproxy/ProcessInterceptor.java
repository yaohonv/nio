/********************************************************************
 *
 * [�ı���Ϣ]
 *
 * testspringԴ���뿽��Ȩ�������Ĵ�ʱ����������ɷ����޹�˾���У�
 * �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-7-27
 * @revision    Id 1.0
 ********************************************************************/
package reactor.temp.dproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yaohw
 * 
 */
public class ProcessInterceptor implements Interceptor {
	private static Log logger = LogFactory.getLog(ProcessInterceptor.class);

	public void before(/*InvocationInfo invInfo*/) {
		logger.debug("Pre-processing");
//		try {
//			response.setHostAddress(InetAddress.getLocalHost());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
	}

	public void after(/*InvocationInfo invInfo*/) {
		logger.debug("Post-processing");
	}

	public void exceptionThrow(/*InvocationInfo invInfo*/) {
		logger.debug("Exception-processing");
	}
}