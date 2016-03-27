package reactor.v1_3.NIOServer.selector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import reactor.v1_3.NIOServer.reader.Reader;

/**
 * IO 方面的事件轮询
 * 
 * @author yaohw
 * 
 */
public class IOReactor extends AbstractReactor {
	private final List<SocketChannel> sockets = new ArrayList<SocketChannel>();
	private static Class<? extends Reader> reader;

	public IOReactor() throws IOException {
		super();
	}

	public void addSocket(SocketChannel s) {
		synchronized (sockets) {
			this.sockets.add(s);
		}
		selector.wakeup();// that is !!!
	}

	public void register() throws IOException {
		synchronized (sockets) {
			Iterator<SocketChannel> iterator = sockets.iterator();
			while (iterator.hasNext()) {
				SocketChannel socket = iterator.next();
				// 获取请求数据
				// new MyConnection(selector, socket);
				instanceConnector(socket);
				iterator.remove();
			}
		}
	}

	private void instanceConnector(SocketChannel socket) {
		try {
			reader.getConstructor(Selector.class, SocketChannel.class).newInstance(selector,
					socket);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
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
	
	/**
	 * @param reader the reader to set
	 */
	public static void setReader(Class<? extends Reader> reader) {
		IOReactor.reader = reader;
	}
}