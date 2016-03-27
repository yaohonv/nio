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

	//�ٶ���Ϣ�Ի��н���
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
			return "��������" + Thread.currentThread().getName();
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
//		int n = socketChannel.write(bb);//���Է���
		/**
		 * ���һ��channel�����ݲ����꣬���ԼӴ󻺳���������ת��ԭ�������������ݺ���ռ����������
		 * ����һ�ζ��귵���ˣ��ǲ���ȷ�������Ѿ����꣬��Ҫһ��Լ�����жϽ����������߸�����ʱʱ��
		 */
		socketChannel.read(input);// �����channel������û���꣬�´�selector��ѯʱ���������ش�key
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
		 * ���ͻ���д���ݣ�TCPʱ�ͻ����ǲ�����ʲôʱ�����ݽ�����ϣ�������ҪӦ�ò���Լ����eg.��һ��������־
		 * д�ļ�ʱ ����һ��������־
		 */
		while (output.hasRemaining()) {
			socketChannel.write(output);
		}
		System.out.println("send : " + mes + " end!");
		if (outputIsComplete()) {
//			socketChannel.close();
			sk.cancel();// ȡ����
		}
	}
}