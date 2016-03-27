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
package reactor.v1_4.NIOServer.process.core;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_4.NIOServer.common.AbstractHandler;
import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.common.Response;
import reactor.v1_4.NIOServer.sender.core.Sender;

/**
 * @author yaohw
 * 
 */
public abstract class ProcessHandler extends AbstractHandler implements Process/*,ThreadPool*/{
	private Log log = LogFactory.getLog(this.getClass());
	private static Class<? extends Sender> sender;
	// private static Proxied proxied;
	private Request request;
	private Response response;

	/*
	 * static { List<Interceptor> interceptors = new ArrayList<Interceptor>();
	 * interceptors.add(new ProcessInterceptor()); proxied =
	 * AOPFactory.getProxied(TestProcess.class, interceptors); }
	 */

	/**
	 * @param reqData
	 *            ��ȡ����������
	 * @param key
	 *            ע���selectorKey
	 */
	protected ProcessHandler(Request req, SelectionKey key) {
		super(key);
		this.request = req;
		initResponse();
	}

	@Override
	public void run() {
		log.debug("kernel process");
		/* proxied. */process(request, response);
		
		/**
		 * �������֮��ע����д�¼�������д�����handler
		 */
		key.attach(instanceSender());
		key.interestOps(SelectionKey.OP_WRITE|key.interestOps());
		key.selector().wakeup();
	}

	private Sender instanceSender() {
		try {
			return sender.getConstructor(SelectionKey.class, Request.class,Response.class).newInstance(key,
					request,response);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ����request����ʼ��response��һЩ��������
	private void initResponse() {
		response = new Response();
	}

	/*
	 * @see
	 * reactor.v1_4.handler.spi.Process#startThread(reactor.v1_4.common.Request,
	 * java.nio.channels.SelectionKey)
	 */
	@Override
	public void start() {
//		scheduler.execute(this);
		run();
	}

	public Request getRequest() {
		return request;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}
	/**
	 * @param sender the sender to set
	 */
	public static void setSender(Class<? extends Sender> sender) {
		ProcessHandler.sender = sender;
	}
}
