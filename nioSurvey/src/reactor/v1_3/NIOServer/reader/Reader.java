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
package reactor.v1_3.NIOServer.reader;

import java.nio.ByteBuffer;


/**
 * @author yaohw
 *
 */
public interface Reader {

	/**
	 * 在子类中写自己的请求数据解析方式,封装请求对象
	 * 
	 * @param buffer
	 *            : the data from the socket
	 * @param request
	 *            : request Object
	 */
	public void parseData(ByteBuffer buffer, Request request);
}
