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

/**
 * @author yaohw
 *
 */
public interface Interceptor {

	public void before();
	public void after();
	public void exceptionThrow();
}
