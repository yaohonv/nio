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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yaohw
 * 
 */
public class ProcessHandler extends AbstractHandler {
	private Log log = LogFactory.getLog(this.getClass());
	private String fileName;

	ProcessHandler(String fileName, SelectionKey key) {
		super(key);
		this.fileName = fileName;

	}

	@Override
	public void run() {
		log.debug("process");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		key.attach(new SendHandler(key, fileName));
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}
}
