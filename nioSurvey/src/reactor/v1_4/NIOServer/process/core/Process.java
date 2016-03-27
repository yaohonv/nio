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
package reactor.v1_4.NIOServer.process.core;

import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.common.Response;



/**
 * @author yaohw
 *
 */
public interface Process {
	public  void start();
	/**
	 * ������д���Զ��崦����
	 * 
	 * @param request
	 * @param response
	 */
	public void process(Request request, Response response);
}
