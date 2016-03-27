package reactor.v1_2.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import reactor.v1_2.handler.ReadHandler;
/**
 * IO 方面的事件轮询
 * @author yaohw
 *
 */
public class IOReactor extends Reactor {
	private final List<SocketChannel> sockets = new LinkedList<SocketChannel>();
	IOReactor() throws IOException {
		super();
	}

	public void addSocket(SocketChannel s) {
		synchronized (sockets) {
			this.sockets.add(s);
		}
		selector.wakeup();//that is !!!
	}

	public void register() throws IOException{
		synchronized (sockets) {
			Iterator<SocketChannel>  iterator = sockets.iterator();
			while(iterator.hasNext()) {
				SocketChannel socket = iterator.next();
				new ReadHandler(selector, socket);
				iterator.remove();
			}
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// System.out.println("prepare for servering!");
				register();
				int n = selector.select();
				if (n == 0) {
					continue;
				}
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) (it.next());
					// it.remove();
					dispatch(key);
				}
				selected.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}