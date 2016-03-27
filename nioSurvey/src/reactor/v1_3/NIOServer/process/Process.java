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
	 * 子类重写来自定义处理方案
	 * 
	 * @param request
	 * @param response
	 */
	public void process(Request request, Response response);
}
