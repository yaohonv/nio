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
	 * ��������������ʵ��
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
	 * ����Class������ʵ��
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
	 * ���ݴ��������������AOP�������
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