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
package reactor.v1_4.NIOServer.common;

import java.net.InetAddress;

import reactor.v1_4.NIOServer.sender.core.Sender;

/**
 * @author yaohw
 *
 */
public class Response {
	
	private InetAddress hostAddress;
	
	private InetAddress remoteAddress;
	
	private Object responseData;
	
	private long fileLength;
	
	/**
	 * 保存是哪个handler来发送这个响应
	 */
	private Sender sender;
	
	
	/**
	 * @return the sender
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}

	/**
	 * @return the fileLength
	 */
	public long getFileLength() {
		return fileLength;
	}

	/**
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
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
	 * @return the responseData
	 */
	public Object getResponseData() {
		return responseData;
	}

	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
	
	

}
