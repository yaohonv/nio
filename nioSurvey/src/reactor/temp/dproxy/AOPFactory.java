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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yaohw
 * 
 */
public class AOPFactory {
	private static Log logger = LogFactory.getLog(AOPFactory.class);

	/**
	 * 根据类名创建类实例
	 * @param clzName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object getClassInstance(String clzName) {
		Class cls;
		try {
			cls = Class.forName(clzName);
			return (Object) cls.newInstance();
		} catch (ClassNotFoundException e) {
			logger.debug(e);
			// throw new AOPRuntimeException(e);
		} catch (InstantiationException e) {
			logger.debug(e);
			// throw new AOPRuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.debug(e);
			// throw new AOPRuntimeException(e);
		}
		return null;
	}
	/**
	 * 根据Class创建类实例
	 * 
	 * @param clzName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object getClassInstance(Class clazz) {
		try {
			return (Object) clazz.newInstance();
		} catch (InstantiationException e) {
			logger.debug(e);
			// throw new AOPRuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.debug(e);
			// throw new AOPRuntimeException(e);
		}
		return null;
	}

	/**
	 * 根据传入的类名，返回AOP代理对象
	 * @param clazz
	 * @param interceptors
	 * @return
	 */
	public static Proxied getProxied(Class clazz,List<Interceptor> interceptors) {
		AOPHandler txHandler = new AOPHandler();
		for (Interceptor interceptor : interceptors) {
			txHandler.addIntercetor(interceptor);
		}
		Object obj = getClassInstance(clazz);
		
		return (Proxied)txHandler.bind(obj);
	}
}