package com.wistron.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.wistron.globaldata.Student;
import com.wistron.globaldata.Teacher;

public class ExcelUtil {

	/** 总行数 */
	private int totalRows = 0;
	/** 总列数 */
	private int totalCells = 0;
	/** 错误信息 */
	private String errorInfo;

	/**
	 * @描述：得到总行数
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@return
	 * @返回值：int
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * @描述：得到总列数
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@return
	 * @返回值：int
	 */
	public int getTotalCells() {
		return totalCells;
	}

	/**
	 * @描述：得到错误信息
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@return
	 * @返回值：String
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * @描述：验证excel文件
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@param filePath　文件完整路径
	 * @参数：@return
	 * @返回值：boolean
	 */
	public boolean validateExcel(String filePath) {
		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		if (filePath == null
				|| !(WDWUtil.isExcel2003(filePath) || WDWUtil
						.isExcel2007(filePath))) {
			errorInfo = "文件名不是excel格式";
			return false;
		}
		/** 检查文件是否存在 */
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			errorInfo = "文件不存在";
			return false;
		}
		return true;

	}

	/**
	 * 
	 * @描述：根据文件名读取excel文件
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@param filePath 文件完整路径
	 * @参数：@return
	 * @返回值：List
	 */
	public List<List<String>> read(String filePath) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		InputStream is = null;
		try {
			/** 验证文件是否合法 */
			if (!validateExcel(filePath)) {
				System.out.println(errorInfo);
				return null;
			}
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(filePath)) {
				isExcel2003 = false;
			}
			/** 调用本类提供的根据流读取的方法 */
			File file = new File(filePath);
			is = new FileInputStream(file);
			dataLst = read(is, isExcel2003);
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		/** 返回最后读取的结果 */
		return dataLst;
	}

	/**
	 * 
	 * @描述：根据文件名读取excel文件
	 * @时间：2014-08-29 下午16:27:15
	 * @参数：@param file 文件
	 * @参数：@return
	 * @返回值：List
	 */
	public List<List<String>> read(File file) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		InputStream is = null;
		try {
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(file.getName())) {
				isExcel2003 = false;
			}
			is = new FileInputStream(file);
			dataLst = read(is, isExcel2003);
			is.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		/** 返回最后读取的结果 */
		return dataLst;
	}

	/**
	 * @描述：根据流读取Excel文件
	 * @时间：2014-08-29 下午16:40:15
	 * @参数：@param inputStream
	 * @参数：@param isExcel2003
	 * @参数：@return
	 * @返回值：List
	 */
	public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {
		List<List<String>> dataLst = null;
		try {
			/** 根据版本选择创建Workbook的方式 */
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(inputStream);
			} else {
				wb = new XSSFWorkbook(inputStream);
			}
			dataLst = read(wb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataLst;

	}

	/**
	 * @描述：读取数据
	 * @时间：2014-08-29 下午16:50:15
	 * @参数：@param Workbook
	 * @参数：@return
	 * @返回值：List<List<String>>
	 */
	private List<List<String>> read(Workbook wb) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的行数 */
		this.totalRows = sheet.getPhysicalNumberOfRows();
		/** 得到Excel的列数 */
		if (this.totalRows >= 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		/** 循环Excel的行 */
		for (int r = 0; r < this.totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			List<String> rowLst = new ArrayList<String>();
			/** 循环Excel的列 */
			for (int c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				int count = 0;
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						DecimalFormat df = new DecimalFormat("0");
						cellValue = df.format(cell.getNumericCellValue());
						break;

					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;

					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;

					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;

					case HSSFCell.CELL_TYPE_BLANK: // 空值
						count += 1;
						cellValue = "";
						break;

					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;

					default:
						cellValue = "未知类型";
						break;
					}
				}
				if(count!=1){
					rowLst.add(cellValue);
				}
			}
			/** 保存第r行的第c列 */
			if(rowLst.size()!=0){
				dataLst.add(rowLst);
			}
		}
		return dataLst;
	}
	//导出生成Excel文件
	public static String exportExcel(String tablename,List<String> cells,List dataList,String type){
		 // 第一步，创建一个webbook，对应一个Excel文件    
		XSSFWorkbook wb = new XSSFWorkbook();    
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet    
		XSSFSheet sheet = wb.createSheet(tablename);    
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short    
		XSSFRow row = sheet.createRow(0); 
        // 第四步，创建单元格，并设置值表头 设置表头居中    
		XSSFCellStyle style = wb.createCellStyle();    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式    
        XSSFCell cell = null;
        Student student = null;
        Teacher teacher = null;
        String folder = System.getProperty("java.io.tmpdir");
        String filename = "";
        for(int i=0;i<cells.size();i++){
        	 cell = row.createCell(i);   
             cell.setCellValue(cells.get(i));    
             cell.setCellStyle(style);  
        }
        for (int j = 0; j < dataList.size(); j++)    
        {   
            row = sheet.createRow((int) j + 1);    
            // 第四步，创建单元格，并设置值 
            for(int k=0; k<cells.size(); k++){
            	if("0".equals(type)){
            		student = (Student)dataList.get(j);
            		row.createCell(0).setCellValue(student.getGrade());    
                    row.createCell(1).setCellValue(student.getCno());    
                    row.createCell(2).setCellValue(student.getSname());    
                    row.createCell(3).setCellValue(student.getSno());
                    row.createCell(4).setCellValue(student.getSeatNum());
                    row.createCell(5).setCellValue(student.getPassword());
            	}else{
            		teacher = (Teacher)dataList.get(j);
            		row.createCell(0).setCellValue(teacher.getNumber());    
                    row.createCell(1).setCellValue(teacher.getSchool());    
                    row.createCell(2).setCellValue(teacher.getGrade());    
                    row.createCell(3).setCellValue(teacher.getTclass());
                    row.createCell(4).setCellValue(teacher.getName());
                    row.createCell(5).setCellValue(teacher.getSubject());
                    row.createCell(6).setCellValue(teacher.getPassword());
            	}
            }
            
        }    
        // 第六步，将文件存到指定位置    
        try    
        {   
        	Date d = new Date();
    		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        	filename = df.format(d);
        	String path = folder+filename+".xlsx";
            FileOutputStream fout = new FileOutputStream(path);    
            wb.write(fout);
            fout.close();  
        }    
        catch (Exception e)    
        {    
            e.printStackTrace();    
        } 
        return filename+".xlsx";
	}
	/**
	 * @描述：main测试方法
	 * @时间：2014-08-29 下午17:12:15
	 * @参数：@param args
	 * @参数：@throws Exception
	 * @返回值：void
	 */
	public static void main(String[] args) throws Exception {
//		ExcelUtil poi = new ExcelUtil();
//		List<List<String>> list = poi.read("C:\\Users\\s1710001\\Desktop\\劲歌贝尔小学教师学生账号.xlsx");
//		if (list != null) {
//			for (int i = 0; i < list.size(); i++) {
//				System.out.print("第" + (i) + "行");
//				List<String> cellList = list.get(i);
//				for (int j = 0; j < cellList.size(); j++) {
//					System.out.print("    " + cellList.get(j));
//				}
//				System.out.println();
//			}
//		}
		/*List<Object> list = new ArrayList<Object>();
		Student s1 = new Student(21,"324354345", "wang sda", 2, 3, "321432");
		Student s2 = new Student(21,"324354345", "wang sda", 2, 3, "321432");
		Student s3 = new Student(21,"324354345", "wang sda", 2, 3, "321432");
		Student s4 = new Student(21,"324354345", "wang sda", 2, 3, "321432");
		list.add(s1);
		list.add(s2);
		list.add(s3);
		list.add(s4);
		List<String> cells = new ArrayList<String>(); 
		cells.add("年级");
		cells.add("班级");
		cells.add("姓名");
		cells.add("学号");
		cells.add("密码");
		exportExcel("学生表",cells,list,"0");*/
	}
}
