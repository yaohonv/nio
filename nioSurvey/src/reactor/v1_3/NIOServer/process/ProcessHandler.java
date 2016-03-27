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
package reactor.v1_3.NIOServer.process;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_3.NIOServer.AbstractHandler;
import reactor.v1_3.NIOServer.reader.Request;
import reactor.v1_3.NIOServer.reader.Response;
import reactor.v1_3.NIOServer.sender.Sender;

/**
 * @author yaohw
 * 
 */
public abstract class ProcessHandler extends AbstractHandler implements Process {
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
	 * 
	 * @param reqData
	 *            ��ȡ����������
	 * @param key
	 *            ע���selectorKey
	 */
	protected ProcessHandler(Request req, SelectionKey key) {
		super(key);
		this.request = req;
		response = new Response();
	}

	@Override
	public void run() {
		log.debug("kernel process");
		initResponse();
		/* proxied. */process(request, response);
		
		/**
		 * �������֮��ע����д�¼�������д�����handler
		 */
		key.attach(instanceSender());
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}

	private Sender instanceSender() {
		try {
			return sender.getConstructor(SelectionKey.class, Response.class).newInstance(key,
					response);
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

	}

	/*
	 * @see
	 * reactor.v1_3.handler.spi.Process#startThread(reactor.v1_3.common.Request,
	 * java.nio.channels.SelectionKey)
	 */
	@Override
	public void startWithThread() {
		scheduler.execute(this);
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
