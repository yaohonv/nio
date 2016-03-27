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
				socketChannel.close();//不关闭，客户端会阻塞在通道上
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
//		System.out.println(endTime + "传送数据用时：" + (endTime - startTime));
		key.cancel();
//		socketChannel.close();
	}
}
