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
 * @create-time 2011-9-5
 * @revision    Id 1.0
 ********************************************************************/
package datagramChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * @author yaohw
 * 
 */
public class TimeServer {
	private static final int DEFAULT_TIME_PORT = 37;
	private static final long DIFF_1900 = 2208988800L;
	protected DatagramChannel channel;

	public TimeServer(int port) throws Exception {
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(new InetSocketAddress(port));
		System.out.println("Listening on port " + port + " for time requests");
	}

	public void listen() throws Exception {
		// allocate a buffer to hold a long value
		ByteBuffer longBuffer = ByteBuffer.allocate(8);
		// assure big-endian (network) byte order
		longBuffer.order(ByteOrder.BIG_ENDIAN);
		// zero the whole buffer to be sure
		longBuffer.putLong(0, 0);
		// position to first byte of the low-order 32 bits
		longBuffer.position(4);
		// slice the buffer, gives view of the low-order 32 bits
		ByteBuffer buffer = longBuffer.slice();
		while (true) {
			buffer.clear();
			SocketAddress sa = this.channel.receive(buffer);//接收到一个数据报，获取他的地址信息
			if (sa == null) {
				continue; // defensive programming
			}
			// ignore content of received datagram per rfc 868
			System.out.println("Time request from " + sa);
			buffer.clear(); // sets pos/limit correctly
			// set 64-bit value, slice buffer sees low 32 bits
			longBuffer.putLong(0, (System.currentTimeMillis() / 1000) + DIFF_1900);
			this.channel.send(buffer, sa);//发给sa
		}
	}

	public static void main(String[] argv) throws Exception {
		int port = DEFAULT_TIME_PORT;
		if (argv.length > 0) {
			port = Integer.parseInt(argv[0]);
		}
		try {
			TimeServer server = new TimeServer(port);
			server.listen();
		} catch (SocketException e) {
			System.out.println("Can't bind to port " + port + ", try a different one");
		}
	}
}