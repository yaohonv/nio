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
package reactor.v1_3.NIOServer.sender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import reactor.v1_3.NIOServer.AbstractHandler;
import reactor.v1_3.NIOServer.reader.Response;

/**
 * @author yaohw
 */
public abstract class SendHandler extends AbstractHandler implements Sender{
	// private Log log = LogFactory.getLog(this.getClass());
	private Response response;
	private Object responseData;
	private final ByteBuffer writeBuffer = ByteBuffer.allocate(1024 * 5);

	public SendHandler(SelectionKey key, Response response) {
		super(key);
		this.response = response;
	}

	@Override
	public void run() {
		responseData = getResponseData(response);
		try {
			sendData();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					sendData();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}

	private void sendData() throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (responseData instanceof ReadableByteChannel) {
			while (true) {
				this.writeBuffer.clear();
				ReadableByteChannel channel = (ReadableByteChannel) responseData;
				int i = channel.read(writeBuffer);
				writeBuffer.flip();
				if (i <= 0) {
					socketChannel.close();// ���رգ��ͻ��˻�������ͨ����
					key.cancel();
					break;
				}
				// here attention! use while loop -yaohw
				try {
					/**
					 * ���������һ���ж������Ƿ�д����߼�������һ��ѭ�������Ա�֤����ȫ�����ͳ�ȥ
					 */
					while (writeBuffer.hasRemaining()) {
						socketChannel.write(writeBuffer);//���ͻ�������д���ݣ��������أ�0����д����ֽ���
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
		key.cancel();// ȡ��ע�ᣬ���´ε���select()����ʱɾ��
		// socketChannel.close();
	}

}
