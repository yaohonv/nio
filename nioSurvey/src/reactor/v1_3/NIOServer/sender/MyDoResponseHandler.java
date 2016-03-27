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
	 * �Լ�����д����������ʽ
	 * @param response
	 * @param responseData
	 *            ��Bytebuffer��ReadableChannel������ʽ
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
