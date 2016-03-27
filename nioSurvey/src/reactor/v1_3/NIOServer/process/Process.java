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
package reactor.v1_3.NIOServer.process;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import reactor.v1_3.Constant;
import reactor.v1_3.NIOServer.reader.Request;
import reactor.v1_3.NIOServer.reader.Response;



/**
 * @author yaohw
 *
 */
public interface Process {
	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Constant.NIO_Business_thread_num);
	public  void startWithThread();
	
	/**
	 * ������д���Զ��崦����
	 * 
	 * @param request
	 * @param response
	 */
	public void process(Request request, Response response);
}
