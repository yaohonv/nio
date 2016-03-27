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
