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
					socketChannel.close();// 不关闭，客户端会阻塞在通道上
					key.cancel();
					break;
				}
				// here attention! use while loop -yaohw
				try {
					/**
					 * 这里可以有一个判断数据是否写完的逻辑。用了一个循环，可以保证数据全部发送出去
					 */
					while (writeBuffer.hasRemaining()) {
						socketChannel.write(writeBuffer);//向发送缓冲区中写数据，立即返回，0或者写入的字节数
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
		key.cancel();// 取消注册，在下次调用select()返回时删除
		// socketChannel.close();
	}

}
