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
package reactor.v1_4.NIOServer.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.SelectionKey;

import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.common.Response;
import reactor.v1_4.NIOServer.sender.core.SendHandler;
import util.LocalTestFile;

/**
 * @author yaohw
 * 
 */
public class MyDoResponseHandler extends SendHandler {
	// private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @param key
	 * @param response
	 */
	public MyDoResponseHandler(SelectionKey key, Request request , Response response) {
		super(key, request,response);
	}

	/**
	 * �Լ�����д����������ʽ
	 * 
	 * @param response
	 * @param responseData
	 *            ��Bytebuffer��ReadableChannel������ʽ
	 */
	@Override
	public Object getResponseData(Response response) {
		FileInputStream fin = null;
		try {
			File file = LocalTestFile.getBy_para((String) response.getResponseData());
			response.setFileLength(file.length());
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fin.getChannel();
	}

}
