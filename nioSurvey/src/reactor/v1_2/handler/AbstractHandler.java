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

/**
 * 具有属性：SelectionKey
 * @author yaohw
 *
 */
public abstract class AbstractHandler implements Runnable{

	protected SelectionKey key;
	
	public AbstractHandler(SelectionKey key) {
		this.key = key;
	}
}
