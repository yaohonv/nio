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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yaohw
 * 
 */
public class AOPHandler implements InvocationHandler {

	private Log logger = LogFactory.getLog(this.getClass());
	private List<Interceptor> interceptors = null;
	private Object originalObject;

	/**
	 * ���ض�̬����ʵ��
	 * 
	 * @param obj
	 * @return
	 */
	public Object bind(Object obj) {
		this.originalObject = obj;
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass()
				.getInterfaces(), this);
	}

	/**
	 * ��Invoke�����У����ض�Ӧ��Interceptor��������
	 * Ԥ����(before)������(after)�Լ��쳣����exceptionThrow������
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		logger.debug("Invoking Before Intercetpors!");
		invokeInterceptorsBefore();

		try {
			logger.debug("Invoking Proxy Method!");
			result = method.invoke(originalObject, args);
			logger.debug("Invoking After Method!");
			invokeInterceptorsAfter();
		} catch (Throwable tr) {
			logger.debug("Invoking exceptionThrow Method!");
//			 invokeInterceptorsExceptionThrow();
//			throw new AOPRuntimeException(tr);
		}
		return result;
	}

	/**
	 * ����Interceptor
	 * 
	 * @return
	 */
	private synchronized List<Interceptor> getIntercetors() {
		if (null == interceptors) {
			interceptors = new ArrayList();
			// Todo����ȡ���ã�����Interceptorʵ��
			// interceptors.add(new MyInterceptor());
		}
		return interceptors;
	}

	/**
	 * ִ��Ԥ������
	 * 
	 * @param invInfo
	 */
	private void invokeInterceptorsBefore() {
		List<Interceptor> interceptors = getIntercetors();
		int len = interceptors.size();
		for (int i = 0; i < len; i++) {
			((Interceptor) interceptors.get(i)).before();
		}
	}

	/**
	 * ִ�к�����
	 * 
	 * @param invInfo
	 */
	private void invokeInterceptorsAfter() {
		List<Interceptor> interceptors = getIntercetors();
		int len = interceptors.size();
		for (int i = len - 1; i >= 0; i--) {
			(interceptors.get(i)).after();
		}
	}
	
	void addIntercetor(Interceptor i){
		if (null == interceptors) {
			interceptors = new ArrayList<Interceptor>();
			// Todo����ȡ���ã�����Interceptorʵ��
			// interceptors.add(new MyInterceptor());
		}
		this.interceptors.add(i);
	}
}