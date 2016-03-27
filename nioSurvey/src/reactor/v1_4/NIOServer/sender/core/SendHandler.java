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
 * @create-time 2011-8-25
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.NIOServer.sender.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import reactor.v1_4.NIOServer.common.AbstractHandler;
import reactor.v1_4.NIOServer.common.Request;
import reactor.v1_4.NIOServer.common.Response;

/**
 * @author yaohw
 */
public abstract class SendHandler extends AbstractHandler implements Sender{
	// private Log log = LogFactory.getLog(this.getClass());
	private Response response;
	private Request request;
	private Object responseData;
	SocketChannel socketChannel = (SocketChannel) key.channel();
	private final ByteBuffer writeBuffer = ByteBuffer.allocate(1024 * 5);

	public SendHandler(SelectionKey key, Request request,Response response) {
		super(key);
		this.response = response;
		this.request = request;
	}

	@Override
	public void run() {
		// cancel the register before start Thread ,avoid the multi write
		key.interestOps(~SelectionKey.OP_WRITE & key.interestOps());
		responseData = getResponseData(response);
		scheduler.execute(new Runnable() {
			@Override
			public void run() {
				try {
					sendData();
				} catch (IOException e) {
					key.cancel();
					try {
						socketChannel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		});
	}

	private void sendData() throws IOException {
		
		if (responseData instanceof ReadableByteChannel) {
			
			//write the length of the file
			ByteBuffer temp = ByteBuffer.allocate(8);
			temp.asLongBuffer().put(response.getFileLength());
//			temp.flip();
			socketChannel.write(temp);
			
			while (true) {
				this.writeBuffer.clear();
				ReadableByteChannel channel = (ReadableByteChannel) responseData;
				int i = channel.read(writeBuffer);
				writeBuffer.flip();
				if (i <= 0) {
					/**
					 * ʵ�ֳ����� notice��1
					 */
//					socketChannel.close();// ���رգ��ͻ��˻�������ͨ����  
//					key.cancel();
					break;
				}
				// here attention! use while loop -yaohw
				try {
					/**
					 * ������ÿ�����һ���ж������Ƿ�д����߼�������һ��ѭ�������Ա�֤����ȫ�����ͳ�ȥ
					 */
					while (writeBuffer.hasRemaining()) {
						socketChannel.write(writeBuffer);// ���ͻ�������д���ݣ��������أ�0����д����ֽ���
						Thread.currentThread().yield();
					}
				} catch (IOException e) {
					// The remote forcibly closed the connection
					key.cancel();
					socketChannel.close();
					return;
				}
			}
		} else {
			ByteBuffer buffer = (ByteBuffer) responseData;
			while (buffer.hasRemaining()) {
				socketChannel.write(buffer);
			}
		}
		/**
		 * ʵ�ֳ����� notice��2
		 * ������һ�����������ע����¼�
		 */
//		key.cancel();// ȡ��ע�ᣬ���´ε���select()ʱɾ����key
		key.interestOps(key.interestOps()|SelectionKey.OP_READ);
		key.attach(request.getReader());
		key.selector().wakeup();
	}

}
