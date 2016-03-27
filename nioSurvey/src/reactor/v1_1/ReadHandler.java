package reactor.v1_1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final class ReadHandler extends AbstractHandler {
	private Log log = LogFactory.getLog(this.getClass());
	final private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	final SocketChannel socket;

	ReadHandler(Selector sel, SocketChannel c) throws IOException {
		super(null);
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		key = socket.register(sel, 0);
		key.attach(this);
		key.interestOps(SelectionKey.OP_READ);
//		sel.wakeup();//客户端写数据了，才可以读
	}

	// class Handler continued
	@Override
	public void run() {
		try {
			read();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	void read() throws IOException {
		log.debug("read");
		String fileName =null;
//		synchronized (readBuffer) {
			int numRead;
//			while (true) {
				this.readBuffer.clear();
				try {
					numRead = socket.read(this.readBuffer);
					// data from the channel is null
					if (numRead <= 0) {
//						break;
						return;
					}
				} catch (IOException e) {
					// The remote forcibly closed the connection
					key.cancel();
					socket.close();
					return;
				}
				fileName = new String(readBuffer.array()).substring(0, numRead).trim();
				String[] str = fileName.split(",");
				System.out.println("c "+(System.currentTimeMillis()-Long.parseLong(str[0])));
				fileName = str[1];
//			}
//		}
		Reactor.scheduler.execute(new ProcessHandler(fileName, key));
	}
}