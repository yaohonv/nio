package reactor.v1_0;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

final class Handler implements Runnable {
	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	final SocketChannel socketChannel;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(20);
	ByteBuffer output = ByteBuffer.allocate(10);
	String mes;
	static final int READING = 0, SENDING = 1;
	int state = READING;

	Handler(Selector sel, SocketChannel c) throws IOException {
		socketChannel = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socketChannel.register(sel, 0);
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}

	//假定消息以换行结束
	boolean inputIsComplete() {
		int position = input.position();
		if(position>0){
			input.position(position-1);
			if(input.get()==(byte)'\n')
				return true;
		}
		return false;
	}

	boolean outputIsComplete() {
		return true;
	}

	String process() { /* ... */
		try {
			Thread.sleep(1000);
			return "处理数据" + Thread.currentThread().getName();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	// class Handler continued
	public void run() {
		System.out.println("handler");
		try {
			if (state == READING)
				read();
			else if (state == SENDING)
				send();
		} catch (IOException ex) { /* ... */
		}
	}

	void read() throws IOException {
		input.clear();
//		ByteBuffer bb = ByteBuffer.wrap("1111".getBytes());
//		int n = socketChannel.write(bb);//可以发送
		/**
		 * 解决一次channel中数据不读完，可以加大缓冲区，或者转存原来缓冲区中数据后清空继续朝里面读
		 * 另外一次读完返回了，是不是确认数据已经读完，需要一个约定，判断结束符，或者给个超时时间
		 */
		socketChannel.read(input);// 如果是channel中数据没读完，下次selector轮询时，继续返回此key
		System.out.println("read the request!");

		if (inputIsComplete()) {

			scheduler.execute(new Runnable() {

				@Override
				public void run() {
					mes = process();
					state = SENDING;//
					// Normally also do first write now
					sk.interestOps(SelectionKey.OP_WRITE);
					sk.selector().wakeup();
					System.out.println(Thread.currentThread().getName() + " finished!");
				}
			});
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// mes = process();
			// state = SENDING;//
			// // Normally also do first write now
			// sk.interestOps(SelectionKey.OP_WRITE);
			// sk.selector().wakeup();
			// }
			// },input.getInt()+"").start();
		}
	}

	void send() throws IOException {

		/**
		 * 朝客户端写数据，TCP时客户端是不晓得什么时候数据接收完毕，所以需要应用层来约定。eg.给一个结束标志
		 * 写文件时 会有一个结束标志
		 */
		while (output.hasRemaining()) {
			socketChannel.write(output);
		}
		System.out.println("send : " + mes + " end!");
		if (outputIsComplete()) {
//			socketChannel.close();
			sk.cancel();// 取消绑定
		}
	}
}