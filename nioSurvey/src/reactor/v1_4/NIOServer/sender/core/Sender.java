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
 * @create-time 2011-9-1
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.sender.core;

import reactor.v1_4.NIOServer.common.Response;
import reactor.v1_4.NIOServer.common.SendThreadPool;

/**
 * @author yaohw
 *
 */
public interface Sender extends SendThreadPool{
	/**
	 * 
	 * @param response
	 * @param responseData
	 *            ��Bytebuffer��ReadableChannel������ʽ
	 */
	public Object getResponseData(Response response);
}
