package org.springblade.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author yq
 * @Date 2020/12/23 17:11
 */
@Slf4j
public class CSVUtils {
	private final static String CSV_LOWER = "csv";
	private final static String CSV = "CSV";

	/**
	 * 功能说明：获取UTF-8编码文本文件开头的BOM签名。
	 * BOM(Byte Order Mark)，是UTF编码方案里用于标识编码的标准标记。例：接收者收到以EF BB BF开头的字节流，就知道是UTF-8编码。
	 * @return UTF-8编码文本文件开头的BOM签名
	 */
	public static String getBom() {

		byte[] b = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
		return new String(b);
	}

	/**
	 * 生成CVS文件
	 * @param exportData
	 *       源数据List
	 * @param map
	 *       csv文件的列表头map
	 * @param outPutPath
	 *       文件路径
	 * @param fileName
	 *       文件名称
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static File createCsvFile(List exportData, LinkedHashMap map, String outPutPath,
									 String fileName) {
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			//定义文件名格式并创建
			String fileOpath = outPutPath + File.separator + fileName+".csv";
			csvFile =new File(fileOpath);
			file.createNewFile();
			// UTF-8使正确读取分隔符","
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				csvFile), "UTF-8"), 1024);

			//写入前段字节流，防止乱码
			csvFileOutputStream.write(getBom());
			// 写入文件头部
			for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				csvFileOutputStream.write((String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" );
				if (propertyIterator.hasNext()) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();
			// 写入文件内容
			for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
				Object row = (Object) iterator.next();
				for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
					.hasNext();) {
					java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
						.next();
					String str = "";
					if(row !=null && !"".equals( propertyEntry.getKey()) && null != propertyEntry.getKey() && ((Map)row).get(propertyEntry.getKey()) != null){
						str=((Map)row).get(propertyEntry.getKey()).toString();

					}
					if(StringUtils.isEmpty(str)){
						str="";
					}else{
						if(str.indexOf(",")>=0){
							str="\""+str+"\"";
						}
					}
					csvFileOutputStream.write(str);
					if (propertyIterator.hasNext()) {
						csvFileOutputStream.write(",");
					}
				}
				if (iterator.hasNext()) {
					csvFileOutputStream.newLine();
				}
			}
			csvFileOutputStream.flush();
		} catch (Exception e) {
			log.error("error：{}", e);
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				log.error("error：{}", e);
			}
		}
		return csvFile;
	}

	/**
	 *     生成并下载csv文件
	 * @param response
	 * @param exportData
	 * @param map
	 * @param outPutPath
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void exportDataFile(HttpServletResponse response, List exportData, LinkedHashMap map, String outPutPath, String fileName) throws IOException{
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			//定义文件名格式并创建
			csvFile =new File(outPutPath+fileName+".csv");
			if(csvFile.exists()){
				csvFile.delete();
			}
			csvFile.createNewFile();
			// UTF-8使正确读取分隔符","
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
			//写入前段字节流，防止乱码
			csvFileOutputStream.write(getBom());
			// 写入文件头部
			for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				csvFileOutputStream.write((String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" );
				if (propertyIterator.hasNext()) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();
			if(CollectionUtils.isNotEmpty(exportData)) {
				// 写入文件内容
				for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
					Object row = (Object) iterator.next();
					for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
						.hasNext(); ) {
						java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
							.next();
						String str = "";
						if (row != null && !"".equals(propertyEntry.getKey()) && null != propertyEntry.getKey() && ((Map) row).get(propertyEntry.getKey()) != null) {
							str = ((Map) row).get(propertyEntry.getKey()).toString();

						}
						if (StringUtils.isEmpty(str)) {
							str = "";
						} else {
							if (str.indexOf(",") >= 0) {
								str = "\"" + str + "\"";
							}
						}
						csvFileOutputStream.write(str);
						if (propertyIterator.hasNext()) {
							csvFileOutputStream.write(",");
						}
					}
					if (iterator.hasNext()) {
						csvFileOutputStream.newLine();
					}
				}
			}
			csvFileOutputStream.flush();
		} catch (Exception e) {
			log.error("error：{}", e);
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				log.error("error：{}", e);
			}
		}
		downFile(response,outPutPath,fileName);
		csvFile.delete();
	}

	private static void downFile(HttpServletResponse response, String outPutPath, String fileName)throws IOException{
		InputStream in = null;
		try {
			in = new FileInputStream(outPutPath+fileName+".csv");
			int len = 0;
			byte[] buffer = new byte[1024];

			OutputStream out = response.getOutputStream();
			response.setContentType("application/csv;charset=UTF-8");
			response.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(fileName+".csv", "UTF-8"));
			response.setCharacterEncoding("UTF-8");
			while ((len = in.read(buffer)) > 0) {
				out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
				out.write(buffer, 0, len);
			}
			out.close();
		} catch (FileNotFoundException e) {
			log.error("error：{}", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					log.error("error：{}", e);
				}
			}
		}
	}

	/**
	 *     生成并csv文件
	 * @param response
	 * @param exportData
	 * @param map
	 * @param outPutPath
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void exportFile(HttpServletResponse response, List exportData, LinkedHashMap map, String outPutPath, String fileName) throws IOException{
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			//定义文件名格式并创建
			csvFile =new File(outPutPath+fileName+".csv");
			if(csvFile.exists()){
				csvFile.delete();
			}
			csvFile.createNewFile();
			// UTF-8使正确读取分隔符","
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
			//写入前段字节流，防止乱码
			csvFileOutputStream.write(getBom());
			// 写入文件头部
			for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				csvFileOutputStream.write((String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" );
				if (propertyIterator.hasNext()) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();
			if(CollectionUtils.isNotEmpty(exportData)) {
				// 写入文件内容
				for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
					Object row = (Object) iterator.next();
					for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
						.hasNext(); ) {
						java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
							.next();
						String str = "";
						if (row != null && !"".equals(propertyEntry.getKey()) && null != propertyEntry.getKey() && ((Map) row).get(propertyEntry.getKey()) != null) {
							str = ((Map) row).get(propertyEntry.getKey()).toString();

						}
						if (StringUtils.isEmpty(str)) {
							str = "";
						} else {
							if (str.indexOf(",") >= 0) {
								str = "\"" + str + "\"";
							}
						}
						csvFileOutputStream.write(str);
						if (propertyIterator.hasNext()) {
							csvFileOutputStream.write(",");
						}
					}
					if (iterator.hasNext()) {
						csvFileOutputStream.newLine();
					}
				}
			}
			csvFileOutputStream.flush();
		} catch (Exception e) {
			log.error("error：{}", e);
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				log.error("error：{}", e);
			}
		}
	}





	/**
	 *     解析csv
	 * @param response
	 * @param exportData
	 * @param map
	 * @param outPutPath
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void analysisExportFile(HttpServletResponse response, List exportData, LinkedHashMap map, String outPutPath, String fileName) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(outPutPath+fileName+".csv");
			int len = 0;
			byte[] buffer = new byte[1024];

			OutputStream out = response.getOutputStream();
			response.setContentType("application/csv;charset=UTF-8");
			response.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(fileName+".csv", "UTF-8"));
			response.setCharacterEncoding("UTF-8");
			while ((len = in.read(buffer)) > 0) {
				out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
				out.write(buffer, 0, len);
			}
			out.close();
		} catch (FileNotFoundException e) {
			log.error("error：{}", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					log.error("error：{}", e);
				}
			}
		}
		File csvFile = null;
		csvFile =new File(outPutPath+fileName+".csv");
		if(csvFile.exists()){
			csvFile.delete();
		}
		csvFile.delete();
	}



	/**
	 * 读取csv文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readCsv(MultipartFile file) throws IOException {

		checkFile(file);

		List<String> list = new ArrayList<String>();
		if (!file.isEmpty()){
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(file.getInputStream());
				br = new BufferedReader(isr);
				String line = null;
				List<List<String>> strs = new ArrayList<List<String>>();
				while ((line = br.readLine()) != null){
					strs.add(Arrays.asList(line.split(",",-1)));
				}
				JSONArray array = toJsonArray(strs);
				list = array.toJavaList(String.class);
			} catch (IOException e) {
				log.error("error：{}", e);
			}finally {
				try {
					if (br != null){
						br.close();
					}
					if (isr != null){
						isr.close();
					}
				} catch (IOException e) {
					log.error("error：{}", e);
				}
			}
		}else {
			System.out.println("文件为空！");
		}
		return list;
	}

	/**
	 * 读取csv文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> readCsvFile(MultipartFile file ,String codeFormat) throws IOException {

		checkFile(file);

		List<String[]> strs = new ArrayList<String[]>();
		if (!file.isEmpty()){
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(file.getInputStream(),codeFormat);
				br = new BufferedReader(isr);
				String line = null;

				while ((line = br.readLine()) != null){
					strs.add(line.split(",",-1));
				}
			} catch (IOException e) {
				log.error("error：{}", e);
			}finally {
				try {
					if (br != null){
						br.close();
					}
					if (isr != null){
						isr.close();
					}
				} catch (IOException e) {
					log.error("error：{}", e);
				}
			}
		}else {
			System.out.println("文件为空！");
		}
		return strs;
	}


	private static JSONArray toJsonArray(List<List<String>> strs) {
		JSONArray array = new JSONArray();
		for (int i = 1; i < strs.size(); i++) {
			JSONObject object = new JSONObject();
			for (int j = 0; j < strs.get(0).size(); j++) {
				object.put(strs.get(0).get(j), strs.get(i).get(j));
			}
			array.add(object);
		}
		return array;
	}
	/**
	 * 检查文件格式
	 * @param file
	 * @throws IOException
	 */
	public static void checkFile(MultipartFile file) throws IOException {
		// 判断文件是否存在
		if (null == file) {
			throw new FileNotFoundException("判断文件不存在");
		}
		// 获得文件名
		String fileName = file.getOriginalFilename();
		// 判断文件是否是excel文件
		if (!fileName.endsWith(CSV_LOWER) && !fileName.endsWith(CSV)) {
			throw new IOException(fileName + "不是csv文件");
		}
	}
}
