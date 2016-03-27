package reactor.v1_2.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * reactor ���࣬�����ʼ��һ��Selector
 * @author yaohw
 *
 */
public class Reactor implements Runnable {
	Selector selector;
	Reactor() throws IOException{
		selector = Selector.open();
	}
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());// ȡ�����ڴ�key����Ĵ������
		if (r != null) {
			r.run();
		}
	}
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
				selected.clear();//���selector��selectedKeys
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}