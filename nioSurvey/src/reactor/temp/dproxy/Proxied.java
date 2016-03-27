package reactor.temp.dproxy;

import reactor.v1_3.NIOServer.reader.Request;
import reactor.v1_3.NIOServer.reader.Response;

//被代理类 需实现的 接口
/**
 * 
 */
public interface Proxied {
	 public void process(Request request,Response response);
}