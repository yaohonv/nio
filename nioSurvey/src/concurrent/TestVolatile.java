package concurrent;

import java.util.ArrayList;

public class TestVolatile {
	    public volatile int inc = 0;
	     
	    public /*synchronized*/ void increase() {
	        inc++;
	    }
	     
	    public static void main(String[] args) {
	        final TestVolatile test = new TestVolatile();
	        for(int i=0;i<10;i++){
	            new Thread(){
	                public void run() {
	                    for(int j=0;j<1000;j++)
	                        test.increase();
	                };
	            }.start();
	        }
	         
	        while(Thread.activeCount()>1)  //保证前面的线程都执行完
	            Thread.yield();
	        System.out.println(test.inc);
	        
	        ArrayList<Long> as = new ArrayList<Long>();
	        as.add(1l);
	        as.add(2l);
	        System.out.println("ids:"+as);
	    }
}
