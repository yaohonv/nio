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
package reactor.v1_3.NIOServer.sender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.SelectionKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_3.NIOServer.reader.Response;
import util.LocalTestFile;

/**
 * @author yaohw
 * 
 */
public class MyDoResponseHandler extends SendHandler {
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @param key
	 * @param response
	 */
	public MyDoResponseHandler(SelectionKey key, Response response) {
		super(key, response);
	}

	/**
	 * 自己来书写返回数据形式
	 * @param response
	 * @param responseData
	 *            有Bytebuffer和ReadableChannel两种形式
	 */
	@Override
	public Object getResponseData(Response response) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(LocalTestFile.getBy_para((String) response.getResponseData()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fin.getChannel();
	}

}
