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
 * @create-time 2011-9-1
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.selector.core;

import java.nio.channels.SelectionKey;

/**
 * @author yaohw
 *
 */
public interface Reactor extends Runnable{
	/**
	 * 事件分发
	 * @param k
	 */
	void dispatch(SelectionKey k);
}
