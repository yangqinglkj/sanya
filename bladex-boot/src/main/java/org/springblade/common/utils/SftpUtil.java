package org.springblade.common.utils;

import com.jcraft.jsch.*;
import com.qcloud.cos.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author yq
 * @Date 2020/10/15 11:38
 */

public class SftpUtil {
	/**
	 * 日志对象
	 **/
	private static final Logger logger = LoggerFactory.getLogger(SftpUtil.class);

	private ChannelSftp sftp;

	private Session session;
	/** SFTP 登录用户名*/
	private String username;
	/** SFTP 登录密码*/
	private String password;
	/** 私钥 */
	private String privateKey;
	/** SFTP 服务器地址IP地址*/
	private String host;
	/** SFTP 端口*/
	private int port;


	/**
	 * 构造基于密码认证的sftp对象
	 */
	public SftpUtil(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * 构造基于秘钥认证的sftp对象
	 */
	public SftpUtil(String username, String host, int port, String privateKey) {
		this.username = username;
		this.host = host;
		this.port = port;
		this.privateKey = privateKey;
	}

	public SftpUtil(){}


	/**
	 * 连接sftp服务器
	 */
	public void login(){
		try {
			JSch jsch = new JSch();
			if (privateKey != null) {
				// 设置私钥
				jsch.addIdentity(privateKey);
			}
			session = jsch.getSession(username, host, port);
			if (password != null) {
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接 server
	 */
	public void logout(){
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}


	/**
	 * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
	 * @param basePath  服务器的基础路径
	 * @param directory  上传到该目录
	 * @param sftpFileName  sftp端文件名
	 */
	public void upload(String basePath,String directory, String sftpFileName, InputStream input) throws SftpException{
		try {
			sftp.cd(basePath);
			sftp.cd(directory);
		} catch (SftpException e) {
			//目录不存在，则创建文件夹
			String [] dirs=directory.split("/");
			String tempPath=basePath;
			for(String dir:dirs){
				if(null== dir || "".equals(dir)){
					continue;
				}
				tempPath+="/"+dir;
				try{
					sftp.cd(tempPath);
				}catch(SftpException ex){
					sftp.mkdir(tempPath);
					sftp.cd(tempPath);
				}
			}
		}
		//上传文件
		sftp.put(input, sftpFileName);
	}


	/**
	 * 下载文件。
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile 存在本地的路径
	 */
	public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		File file = new File(saveFile);
		sftp.get(downloadFile, new FileOutputStream(file));
	}

	/**
	 * 下载文件
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件名
	 * @return 字节数组
	 */
	public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		InputStream is = sftp.get(downloadFile);
		return IOUtils.toByteArray(is);
	}


	/**
	 * 删除文件
	 * @param directory 要删除文件所在目录
	 * @param deleteFile 要删除的文件
	 */
	public void delete(String directory, String deleteFile) throws SftpException {
		sftp.cd(directory);
		sftp.rm(deleteFile);
	}


	/**
	 * 列出目录下的文件
	 * @param directory 要列出的目录
	 */
	public Vector listFiles(String directory) throws SftpException {
		return sftp.ls(directory);
	}

	//上传文件测试
	public static void main(String[] args) throws SftpException, IOException {
//		SftpUtil sftp = new SftpUtil("root", "qmyg,./<>?123", "129.28.198.149", 22);
		SftpUtil sftp = new SftpUtil("sywl_sftp", "qGI1x^Nc", "180.169.95.129", 22);
		sftp.login();
//		Pattern pattern = Pattern.compile("\\w+_\\w+_(\\d+)\\.\\w{2,4}");
//		Matcher matcher;
//
//		int maxIndex = 0;
//		int maxDate = 0;
//		for (int i=0; i<ls.size(); i++) {
//			String filename = ((ChannelSftp.LsEntry) ls.get(i)).getFilename();
//			matcher = pattern.matcher(filename);
//			if (matcher.find(1)) {
//				int date = Integer.parseInt(matcher.group(1));
//				if (date >= maxDate) {
//					date = maxDate;
//					maxIndex = i;
//				}
//			}
//		}
//
//		System.out.println("Last modified file is found on index: " + maxIndex);
//		Object o = ls.get(maxIndex);
//		System.out.println(o);


//		sftp.download("/root/java_avue_data/java","application.yml","D:\\test\\test.yml");
		sftp.download("/day","sanya_day2020-10-08.zip","D:\\test\\sanya_day2020-10-08.zip");
		sftp.logout();

	}
}
