package operateFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * write data to file
 * @author loci
 */
public class IOFile {
	
	
	
	public static void main(String[] args) {
		try {
			copyFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile() throws Exception{
		FileOutputStream fout = new FileOutputStream("C:\\copy1.txt");
		BufferedInputStream finb = new BufferedInputStream(new FileInputStream("C:\\src.txt"));
		finb.mark(Integer.MAX_VALUE);
		byte [] data = new byte[1024]; 
		int tempByteNum = 0;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 2000; i++) {
			
			while((tempByteNum = finb.read(data)) >=0){
				fout.write(data,0,tempByteNum);
			}
			tempByteNum  = 0 ;
		
			finb.reset();
		}
		System.out.println("FIO waste time:"+(System.currentTimeMillis()-startTime));
		fout.close();
		finb.close();
		
	}
	
	
	
	
	public static void writeFile(String args) {
		
		

		File f = new File("C:/src.txt");
//		File f = new File("C:/src.txt");
//	    System.out.println(f.mkdir());//
	    try {
	    	if(!f.exists())
			System.out.println(f.createNewFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter fw= null;
		try {
			FileOutputStream fo = new FileOutputStream(f);
			try {
				fw = new FileWriter(f);
				fw.write(args);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			fo.write();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
