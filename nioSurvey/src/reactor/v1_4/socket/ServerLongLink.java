/********************************************************************
 *
 * [文本信息]
 *
 * javaStudy源代码拷贝权属北京四达时代软件技术股份有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-8-5
 * @revision    Id 1.0
 ********************************************************************/
package reactor.v1_4.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import reactor.v1_4.NIOServer.common.Constant;
import util.LocalTestFile;

/**
 * @author yaohw
 * 
 */
public class ServerLongLink {
	// private Log log = LogFactory.getLog(this.getClass());
	static int num = 0;

	public static void main(String[] args) {
		try {
			new ServerLongLink().server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final static ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(Constant.IO_thread_num);

	public void server() throws Exception {
		ServerSocket server = new ServerSocket(18080);
		while (true) {
			final Socket socket = server.accept();
			final BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			final OutputStream out = socket.getOutputStream();
			// log.info("a client comming!");
			// one client two threads
			// send message thread
			scheduler.execute(new Runnable() {
				@Override
				public synchronized void run() {
					while (true) {
						String data = read(in);
						if(data==null)
							return;
						process();
						send(data, in, out);
					}
				}
			});
		}
	}

	public void process() {
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public void send(String filepath, BufferedReader in, OutputStream out) {
		try {
			// log.debug("send");
			byte[] buffer = new byte[1024 * 5];
			File file = LocalTestFile.getBy_para(filepath);
			FileInputStream local_in = new FileInputStream(file);
			ByteBuffer temp = ByteBuffer.allocate(8);
			temp.asLongBuffer().put(file.length());
			out.write(temp.array());
			while (local_in.read(buffer) > 0) {
				out.write(buffer);
			}
			out.flush();
			// out.close();
			// socket.close();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public String read(BufferedReader in) {
		try {
			// log.debug("read");

			String mes = "";
//			while (!mes.endsWith("\r\n")) {
				mes = in.readLine().trim();
//			}
			String[] str = mes.split(",");
			// in.close();
			// scheduler.shutdown();
			System.out.println((++num) + "c "
					+ (System.currentTimeMillis() - Long.parseLong(str[0])));
			mes = str[1];
			return mes;

		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
}
