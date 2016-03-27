/********************************************************************
 *
 * [文本信息]
 *
 * testspring源代码拷贝权属北京四达时代软件技术股份有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
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
	 * 返回动态代理实例
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
	 * 在Invoke方法中，加载对应的Interceptor，并进行
	 * 预处理(before)、后处理(after)以及异常处理（exceptionThrow）过程
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
	 * 加载Interceptor
	 * 
	 * @return
	 */
	private synchronized List<Interceptor> getIntercetors() {
		if (null == interceptors) {
			interceptors = new ArrayList();
			// Todo：读取配置，加载Interceptor实例
			// interceptors.add(new MyInterceptor());
		}
		return interceptors;
	}

	/**
	 * 执行预处理方法
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
	 * 执行后处理方法
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
			// Todo：读取配置，加载Interceptor实例
			// interceptors.add(new MyInterceptor());
		}
		this.interceptors.add(i);
	}
}