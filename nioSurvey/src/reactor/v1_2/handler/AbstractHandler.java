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
package reactor.v1_2.handler;

import java.nio.channels.SelectionKey;

/**
 * �������ԣ�SelectionKey
 * @author yaohw
 *
 */
public abstract class AbstractHandler implements Runnable{

	protected SelectionKey key;
	
	public AbstractHandler(SelectionKey key) {
		this.key = key;
	}
}
