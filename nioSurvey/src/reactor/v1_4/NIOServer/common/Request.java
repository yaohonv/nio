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
package reactor.v1_4.NIOServer.common;

import java.net.InetAddress;

import reactor.v1_4.NIOServer.reader.core.Reader;

/**
 * @author yaohw
 *
 */
public class Request {
	
	private InetAddress hostAddress;
	
	private InetAddress remoteAddress;
	
	private Object requestData;
	
	private Reader reader;

	
	
	/**
	 * @return the reader
	 */
	public Reader getReader() {
		return reader;
	}

	/**
	 * @param reader the reader to set
	 */
	public void setReader(Reader reader) {
		this.reader = reader;
	}

	/**
	 * @return the remoteAddress
	 */
	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * @param remoteAddress the remoteAddress to set
	 */
	public void setRemoteAddress(InetAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	/**
	 * @return the requestData
	 */
	public Object getRequestData() {
		return requestData;
	}

	/**
	 * @param requestData the requestData to set
	 */
	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}

	/**
	 * @return the hostAddress
	 */
	public InetAddress getHostAddress() {
		return hostAddress;
	}

	/**
	 * @param hostAddress the hostAddress to set
	 */
	public void setHostAddress(InetAddress hostAddress) {
		this.hostAddress = hostAddress;
	}
	
	

}
