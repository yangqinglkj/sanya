package org.springblade.modules.data.config.controller;

import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.entity.yl.TravelDayIndex1;
import org.springblade.common.entity.yl.TravelDayIndex2;
import org.springblade.common.entity.yl.TravelDayIndex4;
import org.springblade.common.entity.yl.TravelDayIndex5;
import org.springblade.common.mapper.yl.TravelDayIndex1Mapper;
import org.springblade.common.mapper.yl.TravelDayIndex2Mapper;
import org.springblade.common.mapper.yl.TravelDayIndex4Mapper;
import org.springblade.common.mapper.yl.TravelDayIndex5Mapper;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.common.utils.SftpUtil2;
import org.springblade.common.utils.ZipUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @Author yq
 * @Date 2021/2/18 9:18
 */
@Slf4j
@RestController
@RequestMapping("/yl")
public class AddYlController {
	@Resource
	private TravelDayIndex1Mapper travelDayIndex1Mapper;
	@Resource
	private TravelDayIndex2Mapper travelDayIndex2Mapper;
	@Resource
	private TravelDayIndex4Mapper travelDayIndex4Mapper;
	@Resource
	private TravelDayIndex5Mapper travelDayIndex5Mapper;

	private final static String USER_NAME = "sywl_sftp";
	private final static String PASSWORD = "U!jtAf2^";
	private final static String HOST = "180.169.95.129";
	private final static String PORT = "22";
	private final static String TRAVEL_DAY_INDEX1 = "bm_sanya_travel_day_index1.csv";
	private final static String TRAVEL_DAY_INDEX2 = "bm_sanya_travel_day_index2.csv";
	private final static String TRAVEL_DAY_INDEX4 = "bm_sanya_travel_day_index4.csv";
	private final static String TRAVEL_DAY_INDEX5 = "bm_sanya_travel_day_index5.csv";

	@GetMapping("/csv")
	public void updateFile() {
		LocalDate localDate = LocalDate.of(2021, 1, 24);
//		int count = 0;
//		for (int i = 0; i < 24; i++) {
			try {
				//????????????
//				String date = localDate.plusDays(count).toString();
				String date = "2021-02-14";
				String name = "sanya_day" + date + ".zip";
				ChannelSftp sftp;
				List<ChannelSftp.LsEntry> files;
				String filename = null;
				//??????ftp?????????
				sftp = SftpUtil2.getConnect(HOST, PORT, USER_NAME, PASSWORD);
				//??????day?????????????????????
				files = sftp.ls("/day");
				if (CollectionUtils.isNotEmpty(files)) {
					for (ChannelSftp.LsEntry file : files) {
						//?????????????????????
						if (name.equals(file.getFilename())) {
							filename = file.getFilename();
						}
					}
					//?????????????????????????????????C:\Users\legin\AppData\Local\Temp\???
					String fileAddress = System.getProperty("java.io.tmpdir");
					if (StringUtil.isNotBlank(fileAddress)) {
						UUID uuid = UUID.randomUUID();
						if (StringUtil.isNotBlank(filename)) {
							//????????????????????????
							SftpUtil2.download(sftp, "/day", filename, fileAddress + filename);
							SftpUtil2.logout();
							//??????
							ZipUtil.unZip2(new File(fileAddress + filename), fileAddress + uuid);
							//???????????????????????????
							File folder = new File(fileAddress + uuid);
							//??????????????????????????????
							File[] listFiles = folder.listFiles();
							if (listFiles != null) {
								for (File file : listFiles) {
									String fileName = file.getName();
									importCsv(file, fileName);
								}
							} else {
								log.error("================????????????????????????====================");
							}
						} else {
							log.error("================????????????????????????====================");
						}
					} else {
						log.error("================????????????????????????====================");
					}
				} else {
					log.error("================day????????????====================");
				}
			} catch (Exception e) {
				log.error("================??????ftp??????====================");
				e.printStackTrace();
			}
//			count++;
//		}
	}


	/**
	 * ??????csv??????
	 *
	 * @param file     ??????
	 * @param fileName ?????????
	 */
	private void importCsv(File file, String fileName) {
		FileReader fileReader = null;
		try {
			CsvReader reader = CsvUtil.getReader();
			if (TRAVEL_DAY_INDEX1.equals(fileName)) {
				fileReader = new FileReader(file);
				List<TravelDayIndex1> dayIndex1List = reader.read(fileReader, TravelDayIndex1.class);
				if (CollectionUtils.isNotEmpty(dayIndex1List)) {
					dayIndex1List.forEach(entity -> travelDayIndex1Mapper.insert(entity));
				}
			}
			if (TRAVEL_DAY_INDEX2.equals(fileName)) {
				fileReader = new FileReader(file);
				List<TravelDayIndex2> dayIndex2List = reader.read(fileReader, TravelDayIndex2.class);
				if (CollectionUtils.isNotEmpty(dayIndex2List)) {
					dayIndex2List.forEach(entity -> travelDayIndex2Mapper.insert(entity));
				}
			}
			if (TRAVEL_DAY_INDEX4.equals(fileName)) {
				fileReader = new FileReader(file);
				List<TravelDayIndex4> dayIndex4List = reader.read(fileReader, TravelDayIndex4.class);
				if (CollectionUtils.isNotEmpty(dayIndex4List)) {
					dayIndex4List.forEach(entity -> travelDayIndex4Mapper.insert(entity));
				}
			}
			if (TRAVEL_DAY_INDEX5.equals(fileName)) {
				fileReader = new FileReader(file);
				List<TravelDayIndex5> dayIndex5List = reader.read(fileReader, TravelDayIndex5.class);
				if (CollectionUtils.isNotEmpty(dayIndex5List)) {
					dayIndex5List.forEach(entity -> travelDayIndex5Mapper.insert(entity));
				}
			}
		} catch (FileNotFoundException e) {
			log.error("================??????csv????????????====================");
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
