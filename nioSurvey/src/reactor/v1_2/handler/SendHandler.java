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
package reactor.v1_2.handler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import util.LocalTestFile;

/**
 * @author yaohw
 */
public class SendHandler extends AbstractHandler{
//	private Log log = LogFactory.getLog(this.getClass());
	private String fileName;
	private final ByteBuffer writeBuffer = ByteBuffer.allocate(1024*5);
	public SendHandler(SelectionKey key,String fileName) {
		super(key);
		this.fileName = fileName;
	}

	
	@Override
	public void run() {
		try {
			SocketChannel socketChannel = (SocketChannel) key.channel();
			RandomAccessFile raf = new RandomAccessFile(LocalTestFile.getBy_para(fileName),	"rw");
			while (true) {
				this.writeBuffer.clear();
				int i = raf.getChannel().read(writeBuffer);
				writeBuffer.flip();
				if (i <= 0) {
					socketChannel.close();//���رգ��ͻ��˻�������ͨ����
					key.cancel();
					break;
				}
				// here attention! use while loop -yaohw
				try {
				while (writeBuffer.hasRemaining()) {
					socketChannel.write(writeBuffer);
				}
				} catch (IOException e) {
					// The remote forcibly closed the connection
					key.cancel();
					socketChannel.close();
					return;
				}
			}
			key.cancel();//ȡ��ע�ᣬ���´ε���select()����ʱɾ��
//			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
