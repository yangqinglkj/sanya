package org.springblade.common.utils;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @Author yq
 * @Date 2020/10/15 11:38
 */
@Slf4j
public class SftpUtil2 {
	/**
	 * FTPClient对象
	 **/
	private static ChannelSftp sftp = null;
	/**
	 *
	 */
	private static Session session = null;

	/**
	 * 连接服务器
	 *
	 * @param host     服务器IP地址
	 * @param port     端口号
	 * @param userName 用户名
	 * @param password 密码
	 */
	public static ChannelSftp getConnect(String host, String port, String userName, String password)
		throws Exception {
		try {
			JSch jsch = new JSch();
			// 获取sshSession
			session = jsch.getSession(userName, host, Integer.parseInt(port));
			// 添加s密码
			session.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			session.setConfig(sshConfig);
			// 开启session链接
			session.connect();
			// 获取sftp通道
			sftp = (ChannelSftp) session.openChannel("sftp");
			// 开启
			sftp.connect();
		} catch (Exception e) {
			e.printStackTrace();
			InterfaceLogUtils.saveLog("下载ftp出错",null,e.getMessage());
			throw new Exception("连接sftp服务器异常。。。。。。。。");
		}
		return sftp;
	}

	/**
	 * 下载文件。
	 *
	 * @param directory    下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile     存在本地的路径
	 */
	public static void download(ChannelSftp sftp, String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		File file = new File(saveFile);
		sftp.get(downloadFile, new FileOutputStream(file));
	}

	/**
	 * 关闭连接 server
	 */
	public static void logout() {
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


}
