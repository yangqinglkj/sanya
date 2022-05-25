package org.springblade.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import java.io.*;
import java.net.SocketException;

/**
 * @Author yq
 * @Date 2020/10/15 11:07
 */
@Slf4j
public class FTPUtil {
//	/**
//	 * Description: 向FTP服务器上传文件
//	 * @param host FTP服务器hostname
//	 * @param port FTP服务器端口
//	 * @param username FTP登录账号
//	 * @param password FTP登录密码
//	 * @param basePath FTP服务器基础目录
//	 * @param filePath FTP服务器文件存放路径。文件的路径为basePath+filePath
//	 * @param filename 上传到FTP服务器上的文件名
//	 * @param input 输入流
//	 * @return 成功返回true，否则返回false
//	 */
//	public static boolean uploadFile(String host, int port, String username, String password, String basePath,
//									 String filePath, String filename, InputStream input) {
//		boolean result = false;
//		FTPClient ftp = new FTPClient();
//		try {
//			int reply;
//			// 连接FTP服务器
//			ftp.connect(host, port);
//			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
//			ftp.login(username, password);// 登录
//			reply = ftp.getReplyCode();
//			if (!FTPReply.isPositiveCompletion(reply)) {
//				ftp.disconnect();
//				return result;
//			}
//			//切换到上传目录
//			if (!ftp.changeWorkingDirectory(basePath+filePath)) {
//				//如果目录不存在创建目录
//				String[] dirs = filePath.split("/");
//				String tempPath = basePath;
//				for (String dir : dirs) {
//					if (null == dir || "".equals(dir)) continue;
//					tempPath += "/" + dir;
//					if (!ftp.changeWorkingDirectory(tempPath)) {  //进不去目录，说明该目录不存在
//						if (!ftp.makeDirectory(tempPath)) { //创建目录
//							//如果创建文件目录失败，则返回
//							System.out.println("创建文件目录"+tempPath+"失败");
//							return result;
//						} else {
//							//目录存在，则直接进入该目录
//							ftp.changeWorkingDirectory(tempPath);
//						}
//					}
//				}
//			}
//			//设置上传文件的类型为二进制类型
//			ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			//上传文件
//			if (!ftp.storeFile(filename, input)) {
//				return result;
//			}
//			input.close();
//			ftp.logout();
//			result = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (ftp.isConnected()) {
//				try {
//					ftp.disconnect();
//				} catch (IOException ioe) {
//				}
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * Description: 从FTP服务器下载文件
//	 * @param host FTP服务器hostname
//	 * @param port FTP服务器端口
//	 * @param username FTP登录账号
//	 * @param password FTP登录密码
//	 * @param remotePath FTP服务器上的相对路径
//	 * @param fileName 要下载的文件名
//	 * @param localPath 下载后保存到本地的路径
//	 * @return
//	 */
//	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
//									   String fileName, String localPath) {
//		boolean result = false;
//		FTPClient ftp = new FTPClient();
//		try {
//			int reply;
//			ftp.connect(host, port);
//			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
//			// 登录
//			ftp.login(username, password);
//			reply = ftp.getReplyCode();
//			if (!FTPReply.isPositiveCompletion(reply)) {
//				ftp.disconnect();
//				return result;
//			}
//			// 转移到FTP服务器目录
//			ftp.changeWorkingDirectory(remotePath);
//			FTPFile[] fs = ftp.listFiles();
//			for (FTPFile ff : fs) {
//				if (ff.getName().equals(fileName)) {
//					File localFile = new File(localPath + "/" + ff.getName());
//
//					OutputStream is = new FileOutputStream(localFile);
//					ftp.retrieveFile(ff.getName(), is);
//					is.close();
//				}
//			}
//			ftp.logout();
//			result = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (ftp.isConnected()) {
//				try {
//					ftp.disconnect();
//				} catch (IOException ioe) {
//				}
//			}
//		}
//		return result;
//	}

	/**
	 * 获取FTPClient对象
	 * @param ftpHost       FTP主机服务器
	 * @param ftpPassword   FTP 登录密码
	 * @param ftpUserName   FTP登录用户名
	 * @param ftpPort       FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			// 连接FTP服务器
			ftpClient.connect(ftpHost, ftpPort);
			// 登陆FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);
			// 中文支持
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				log.info("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				log.info("FTP连接成功。");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			log.info("FTP的IP地址可能错误，请正确配置。");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("FTP的端口错误,请正确配置。");
		}
		return ftpClient;
	}


	//ftp上传文件测试main函数
	public static void main(String[] args) throws IOException {
//		try {
//			FileInputStream in=new FileInputStream(new File("D:\\Tomcat 5.5\\pictures\\t0176ee418172932841.jpg"));
//			boolean flag = uploadFile("192.168.111.128", 21, "用户名", "密码", "/www/images","/2017/11/19", "hello.jpg", in);
//			System.out.println(flag);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		//boolean result = FTPUtil.downloadFile("180.169.95.129", 22, "sywl_sftp", "qGI1x^Nc", "/day", "sanya_day2020-10-08.zip", "D:\\test\\sanya_day2020-10-08.zip");
		String host = "180.169.95.129";
		Integer port = 21;
		String username = "sywl_sftp";
		String password = "qGI1x^Nc";
		FTPClient ftpClient = FTPUtil.getFTPClient(host, username, password, port);
		FTPFile[] ftpFiles = ftpClient.listFiles();
		System.out.println(ftpFiles);
	}
}
