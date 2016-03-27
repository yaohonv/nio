package reactor.v1_4.NIOServer.selector.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;



/**
 * reactor 父类，负责初始化一个Selector,默认轮询方法实现
 * @author yaohw
 *
 */
public abstract class AbstractReactor implements Reactor{
	protected Selector selector;
	protected AbstractReactor() throws IOException{
		selector = Selector.open();
	}
	/**
	 * 通用的分发方法
	 */
	@Override
	public	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());// 取出附在此key上面的处理对象
		if (r != null) {
			r.run();
		}
	}
	/**
	 * 通用循环调用selector选择方法，并进行分发
	 */
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// System.out.println("prepare for servering!");
				int n = selector.select();
				if (n == 0) {
					continue;
				}
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
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