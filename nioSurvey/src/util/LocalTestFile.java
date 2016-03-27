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
