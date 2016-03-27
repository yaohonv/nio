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
 * @create-time 2011-9-1
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.reader.core;

import java.nio.ByteBuffer;

import reactor.v1_4.NIOServer.common.ReadProcessThreadPool;
import reactor.v1_4.NIOServer.common.Request;


/**
 * @author yaohw
 *
 */
public interface Reader extends ReadProcessThreadPool{

	/**
	 * ��������д�Լ����������ݽ�����ʽ,��װ�������
	 * 
	 * @param buffer
	 *            : the data from the socket
	 * @param request
	 *            : request Object
	 */
	public void parseData(ByteBuffer buffer, Request request);
}
