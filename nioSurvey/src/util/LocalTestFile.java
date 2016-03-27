/********************************************************************
 *
 * [�ı���Ϣ]
 *
 * nioSamplesԴ���뿽��Ȩ�������Ĵ�ʱ����������ɷ����޹�˾���У�
 * �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����
 *
 * @copyright   Copyright: 2002-2009 Beijing Startimes
 *              Software Technology Co. Ltd.
 * @creator     yaohw yaohw@startimes.com.cn <br/>
 * @create-time 2011-8-18
 * @revision    Id 1.0
 ********************************************************************/
package util;

import java.io.File;

/**
 * @author yaohw
 *
 */
public class LocalTestFile {
	
	public static final String PATH =/*"/home/nio/down/"; windows :  */"C:/test/testCopyFile/";
	public static File getFileByPath(String pathname){
		return new File(pathname);
	}
	
	public static File getDefault_test1_txt(){
		return getFileByPath(PATH+"test1.txt");
	}

	public static File getBy_para(String filename){
		return getFileByPath(PATH+filename);
	}

	/**
	 * @return
	 */
	public static File getDefaultTo_to1_txt() {
		// TODO Auto-generated method stub
		return getFileByPath(PATH+"to1.txt");
	}

}
