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
 * @create-time 2011-8-30
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.reader.core.ReadHandler;


/**
 * @author yaohw
 * 
 */
public class MyReadHandler extends ReadHandler {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public MyReadHandler(Selector sel, SocketChannel c) throws IOException {
		super(sel, c);
	}
	/**
	 * 写自己的请求数据解析方式,封装请求对象
	 * @param buffer : the data from the socket
	 * @param request : request Object
	 */ 
	@Override
	public void parseData(ByteBuffer buffer,Request request) {
		log.debug("own parseData method");
		String requestStr = new String(buffer.array()).trim();
		String[] str = requestStr.split(",");
		request.setRequestData(str[1]);
		System.out.println("c "+(System.currentTimeMillis()-Long.parseLong(str[0])));
	}
}
