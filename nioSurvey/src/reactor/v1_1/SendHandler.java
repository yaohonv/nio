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
package reactor.v1_1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.LocalTestFile;

/**
 * @author yaohw
 * 
 */
public class SendHandler extends AbstractHandler{
	private Log log = LogFactory.getLog(this.getClass());
	private String fileName;
	private final ByteBuffer writeBuffer = ByteBuffer.allocate(1024*5);
	public SendHandler(SelectionKey key,String fileName) {
		super(key);
		this.fileName = fileName;
	}

	
	@Override
	public void run() {
		try {
			send();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void send() throws IOException{
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
//				if(readBuffer.hasRemaining()){
//					n++;
////					System.out.println(readBuffer.limit()-readBuffer.position());
//				}
			}
			} catch (IOException e) {
				// The remote forcibly closed the connection
				key.cancel();
				socketChannel.close();
				return;
			}
//			j += i;
		}
//		System.out.println("GOD:"+n);
//		long endTime = System.currentTimeMillis();
//		System.out.println(endTime + "����������ʱ��" + (endTime - startTime));
		key.cancel();
//		socketChannel.close();
	}
}
