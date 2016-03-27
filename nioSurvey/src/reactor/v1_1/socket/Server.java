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
package reactor.v1_1.socket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.v1_1.common.Constant;
import util.LocalTestFile;

/**
 * @author yaohw
 * 
 */
public class Server {
	private Log log = LogFactory.getLog(this.getClass());
	public static void main(String[] args) {
		try {
			new Server().server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Constant.IO_thread_num);

	public void server() throws Exception {
		ServerSocket server = new ServerSocket(18080);
		while (true) {
			final Socket socket = server.accept();
			log.info("a client comming!");
			// one client two threads
			// send message thread
			scheduler.execute(new Runnable() {
				@Override
				public synchronized void run() {
					String data = read(socket);
					process();
					send(data,socket);
					
				}
			});
		}
	}
	
	public void process() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String filepath,Socket socket) {
		try {
			log.debug("send");
			OutputStream out = socket.getOutputStream();
			byte[] buffer = new byte[1024*5];
			FileInputStream in = new FileInputStream(LocalTestFile.getBy_para(filepath));
			while(in.read(buffer)>0){
				out.write(buffer);
			}
			out.flush();
			out.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String read(Socket socket) {
		try {
			log.debug("read");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String mes = "";
			while(!mes.trim().endsWith(".zip")){
				mes +=in.readLine().trim();
			}
//			in.close();
			// scheduler.shutdown();
			return mes;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
