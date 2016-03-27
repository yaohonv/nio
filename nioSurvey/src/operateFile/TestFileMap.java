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
 * @create-time 2011-8-1
 * @revision    Id 1.0
 ********************************************************************/
package operateFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * @author yaohw
 * 
 */
public class TestFileMap {

	/**
	 * Dummy HTTP server using MappedByteBuffers. Given a filename on the
	 * command line, pretend to be a web server and generate an HTTP response
	 * containing the file content preceded by appropriate headers. The data is
	 * sent with a gathering write.
	 ** 
	 * @author Ron Hitchens (ron@ronsoft.com)
	 */
	private static final String OUTPUT_FILE = "MappedHttp.out";
	private static final String LINE_SEP = "\r\n";
	private static final String SERVER_ID = "Server: XXXXXXX Server";
	private static final String HTTP_HDR = "HTTP/1.0 200 OK" + LINE_SEP + SERVER_ID + LINE_SEP;
	private static final String HTTP_404_HDR = "HTTP/1.0 404 Not Found" + LINE_SEP + SERVER_ID
			+ LINE_SEP;
	private static final String MSG_404 = "Could not open file: ";

	public static void main(String[] argv) throws Exception {
//		if (argv.length < 1) {
//			System.err.println("Usage: filename");
//			return;
//		}
		String file = "test.txt";
		ByteBuffer header = ByteBuffer.wrap(bytes(HTTP_HDR));
		ByteBuffer dynhdrs = ByteBuffer.allocate(128);
		ByteBuffer[] gather = { header, dynhdrs, null };
		String contentType = "unknown/unknown";
		long contentLength = -1;
		try {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			MappedByteBuffer filedata = fc.map(MapMode.READ_ONLY, 0, fc.size());
			gather[2] = filedata;
			contentLength = fc.size();
			contentType = URLConnection.guessContentTypeFromName(file);
		} catch (IOException e) {
			// file could not be opened, report problem
			ByteBuffer buf = ByteBuffer.allocate(128);
			String msg = MSG_404 + e + LINE_SEP;
			buf.put(bytes(msg));
			buf.flip();
			// use the HTTP error response
			gather[0] = ByteBuffer.wrap(bytes(HTTP_404_HDR));
			gather[2] = buf;
			contentLength = msg.length();
			contentType = "text/plain";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("Content-Length: " + contentLength);
		sb.append(LINE_SEP);
		sb.append("Content-Type: ").append(contentType);
		sb.append(LINE_SEP).append(LINE_SEP);
		dynhdrs.put(bytes(sb.toString()));
		dynhdrs.flip();
		FileOutputStream fos = new FileOutputStream(OUTPUT_FILE);
		FileChannel out = fos.getChannel();
		MappedByteBuffer mb = out.map(MapMode.READ_WRITE, 0, out.size());
		
		// all the buffers have been prepared, write 'em out
		while (out.write(gather) > 0) {
			// empty body, loop until all buffers empty
		}
		mb.put("123".getBytes());
		mb.force();//写入磁盘
		
		out.close();
		System.out.println("output written to " + OUTPUT_FILE);
	}

	// convert a string to its constituent bytes
	// from the ascii character set.
	private static byte[] bytes(String string) throws Exception {
		return (string.getBytes("US-ASCII"));
	}
}
