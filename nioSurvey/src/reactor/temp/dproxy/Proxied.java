package reactor.temp.dproxy;

import reactor.v1_3.NIOServer.reader.Request;
import reactor.v1_3.NIOServer.reader.Response;

//�������� ��ʵ�ֵ� �ӿ�
/**
 * 
 */
public interface Proxied {
	 public void process(Request request,Response response);
}