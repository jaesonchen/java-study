package com.asiainfo.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月17日  下午1:39:53
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ExcelUtil {
	
	public static List<List<String>> getExcelData(InputStream inputStream) throws Exception {

		try {
			
			Workbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			
			List<List<String>> result = new ArrayList<List<String>>();
			List<String> list;
			XSSFRow row;
			for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
				
				row = sheet.getRow(i);
				if (row != null) {
					
					list = new ArrayList<String>();
					
					list.add(formatString(row.getCell(1)));
					list.add(row.getCell(2).getStringCellValue());
					list.add(row.getCell(6).getStringCellValue());
					result.add(list);
				}
			}
			workbook.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@SuppressWarnings("deprecation")
	private static String formatString(Cell cell) {
		
		DecimalFormat df = new DecimalFormat("0");
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return df.format(cell.getNumericCellValue());
		}
		return cell.toString();
	}
	
	public static void main(String[] args) throws Exception {
		
		String url = "d:\\test\\test.xlsx";
		List<List<String>> result = getExcelData(new FileInputStream(new File(url)));
		
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(new String[] {"classpath:application-core-configure.xml"});
		LoadExcelService service = ac.getBean(LoadExcelService.class);
		
		Set<String> planids = new HashSet<String>();
		List<List<String>> dup = new ArrayList<List<String>>();
		for (List<String> list : result) {
			
			if (planids.contains(list.get(0))) {
				dup.add(list);
				continue;
			}
			
			planids.add(list.get(0));
			//System.out.println(list);
			service.insert(list.get(0), list.get(1), "就高".equals(list.get(2)) ? "2" : "1");
		}
		
		for (List<String> list : dup) {
			System.out.println(list);
		}
		
		ac.close();
	}
}
