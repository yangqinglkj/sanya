package org.springblade.common.utils;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	/**

	 * zip解压
	 * @param srcFile        zip源文件
	 * @param destDirPath     解压后的目标文件夹
	 * @throws RuntimeException 解压失败会抛出运行时异常
	 */

	public static void unZip2(File srcFile, String destDirPath) throws RuntimeException {
		long start = System.currentTimeMillis();
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
		}
		// 开始解压
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(srcFile);
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				System.out.println("解压" + entry.getName());
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = destDirPath + "/" + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					File targetFile = new File(destDirPath + "/" + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if(!targetFile.getParentFile().exists()){
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();
					// 将压缩文件内容写入到这个文件中
					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len;
					byte[] buf = new byte[6*1024];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流顺序，先打开的后关闭
					fos.close();
					is.close();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("解压完成，耗时：" + (end - start) +" ms");
		} catch (Exception e) {
			throw new RuntimeException("unzip error from ZipUtils", e);
		} finally {
			if(zipFile != null){
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	/**
	 * zip解压
	 * @param srcFile        zip源文件
	 * @param destDirPath     解压后的目标文件夹
	 * @throws RuntimeException 解压失败会抛出运行时异常
	 */
	public static Map<String,Object> unZip(File srcFile, String destDirPath) throws RuntimeException {
		long start = System.currentTimeMillis();
		Map<String,Object> map = new HashMap<>();
		if (!srcFile.exists()) {// 判断源文件是否存在
			map.put("boolean",false);
			map.put("mes","所指文件不存在");
			return map;
		}
		// 开始解压
		ZipFile zipFile = null;
		try {
			//zipFile = new ZipFile(zipFilePath,Charset.forName("GBK"));
			zipFile = new ZipFile(srcFile, Charset.forName("GBK"));
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				map.put("name",entry.getName());
				//System.out.println("解压" + entry.getName());
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = destDirPath + "/" + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					File targetFile = new File(destDirPath + "/" + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if(!targetFile.getParentFile().exists()){
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();
					// 将压缩文件内容写入到这个文件中
					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len;
					byte[] buf = new byte[6*1024];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流顺序，先打开的后关闭
					fos.close();
					is.close();
				}
			}
			long end = System.currentTimeMillis();
			//System.out.println("解压完成，耗时：" + (end - start) +" ms");
			map.put("boolean",true);
			map.put("mes","解压完成");
			return map;
		} catch (Exception e) {
			map.put("boolean",false);
			map.put("mes",e);
			return map;
		} finally {
			if(zipFile != null){
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 根据参数读取文件并把写入到地址中
	 * @param path
	 * @param dir
	 * @param fileAddress
	 * @throws Exception
	 */
	public static void methodInAndPut(String path,String dir,String fileAddress) throws Exception{
		String outPath = fileAddress.substring(fileAddress.lastIndexOf("/"), fileAddress.length());
		File file = new File(path);
		if(!file.exists()){//如果文件夹不存在
			file.mkdirs();//创建文件夹
		}else {
			deleteDir(path);
		}
		FileInputStream fileIn = new FileInputStream(fileAddress);//输入(读取)流
		FileOutputStream out = new FileOutputStream(path+outPath);//输出，写出流
		byte[] by = new byte[6*1024];
		int len;
		while ((len=fileIn.read(by))!=-1){//，如果读取到了末尾，则返回-1；
			out.write(by,0,len);//写入文件
		}
		out.close();
		fileIn.close();
	}

	/**
	 * 根据参数读取文件并把写入到地址中(有文件夹不删除)
	 * @param path
	 * @param dir
	 * @param fileAddress
	 * @throws Exception
	 */
	public static void methodInAndPutNull(String path,String dir,String fileAddress) throws Exception{
		String outPath = fileAddress.substring(fileAddress.lastIndexOf("/"), fileAddress.length());
		File file = new File(path);
		if(!file.exists()){//如果文件夹不存在
			file.mkdirs();//创建文件夹
		}
		FileInputStream fileIn = new FileInputStream(fileAddress);//输入(读取)流
		FileOutputStream out = new FileOutputStream(path+outPath);//输出，写出流
		byte[] by = new byte[6*1024];
		int len;
		while ((len=fileIn.read(by))!=-1){//，如果读取到了末尾，则返回-1；
			out.write(by,0,len);//写入文件
		}
		out.close();
		fileIn.close();
	}


	/**
	 * 根据参数读取文件并把写入到地址中(给文件命名{aaaa.jap})
	 * @param path
	 * @param dir
	 * @param fileAddress
	 * @throws Exception
	 */
	public static void methodInAndPutNullPhoto(String path,String dir,String fileAddress,String URL) throws Exception{
		String outPath = fileAddress.substring(fileAddress.lastIndexOf("/"), fileAddress.length());
		File file = new File(path);
		if(!file.exists()){//如果文件夹不存在
			file.mkdirs();//创建文件夹
		}
		FileInputStream fileIn = new FileInputStream(fileAddress);//输入(读取)流
		//FileOutputStream out = new FileOutputStream(path+outPath);//输出，写出流
		FileOutputStream out = new FileOutputStream(path+"/"+URL);//输出，写出流
		byte[] by = new byte[6*1024];
		int len;
		while ((len=fileIn.read(by))!=-1){//，如果读取到了末尾，则返回-1；
			out.write(by,0,len);//写入文件
		}
		out.close();
		fileIn.close();
	}


	/**
	 * 删除文件夹
	 * @param path
	 * @return
	 */
	public static boolean deleteDir(String path) {
		File file = new File(path);
		if (!file.exists()) {//判断是否待删除目录是否存在
			return false;
		}
		String[] content = file.list();//取得当前目录下所有文件和文件夹
		for (String name : content) {
			File temp = new File(path, name);
			if (temp.isDirectory()) {//判断是否是目录
				deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
				temp.delete();//删除空目录
			} else {
				if (!temp.delete()) {//直接删除文件
					System.err.println("Failed to delete " + name);
				}
			}
		}
		return true;
	}



	/**
	 * 压缩文件目录
	 * @param source 源文件目录（单个文件和多层目录）
	 * @param destit 目标目录
	 */
	public static void zipFiles(String source,String destit) {
		File file = new File( source );
		ZipOutputStream zipOutputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream( destit );
			zipOutputStream = new ZipOutputStream( fileOutputStream );
			if (file.isDirectory()) {
				directory( zipOutputStream, file, "" );
			} else {
				zipFile( zipOutputStream, file, "" );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zipOutputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 压缩文件
	 * @param zipOutputStream
	 * @param file
	 * @param parentFileName
	 */
	private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName){
		FileInputStream in = null;
		try {
			ZipEntry zipEntry = new ZipEntry( parentFileName );
			//ZipEntry zipEntry = new ZipEntry( parentFileName+file.getName() );
			zipOutputStream.putNextEntry( zipEntry );
			in = new FileInputStream( file);
			int len;
			byte [] buf = new byte[8*1024];
			while ((len = in.read(buf)) != -1){
				zipOutputStream.write(buf, 0, len);
			}
			zipOutputStream.closeEntry(  );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归压缩目录结构
	 * @param zipOutputStream
	 * @param file
	 * @param parentFileName
	 */
	private static void directory(ZipOutputStream zipOutputStream,File file,String parentFileName){
		File[] files = file.listFiles();
		String parentFileNameTemp = null;
		if (files!=null&&files.length>0){
			for (File fileTemp: files) {
				parentFileNameTemp =  StringUtils.isEmpty(parentFileName)?fileTemp.getName():parentFileName+"/"+fileTemp.getName();
				if(fileTemp.isDirectory()){
					directory(zipOutputStream,fileTemp, parentFileNameTemp);
				}else{
					zipFile(zipOutputStream,fileTemp,parentFileNameTemp);
				}
			}
		}else {
			try {
				if (parentFileNameTemp!=null){
					zipOutputStream.putNextEntry(new ZipEntry( parentFileNameTemp+file.getName()+"/"));
				}else {
					zipOutputStream.putNextEntry(new ZipEntry( parentFileName+"/"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 将string 写入xml文件
	 * @param doc
	 * @param filename
	 */
	public static void OutputXml(Document doc, String filename){
		OutputFormat format = OutputFormat.createPrettyPrint();
		/** 指定XML编码 */
		format.setEncoding("UTF-8");
		/** 将document中的内容写入文件中 */
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(new File(filename)), format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
