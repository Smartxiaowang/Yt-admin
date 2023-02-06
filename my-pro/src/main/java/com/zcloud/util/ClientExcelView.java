package com.zcloud.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.zcloud.entity.PageData;
import com.zcloud.entity.system.Dictionaries;
import com.zcloud.service.system.DictionariesService;

/**
 * 导出xlsx数据
 */
@Component
public class ClientExcelView extends AbstractXlsView {

	private final int MAX_ROW = 65535;
	
	@Autowired
	private DictionariesService dictionariesService;
	

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		String filename = DateUtil.date2Str(date, "yyyyMMddHHmmss");
		HSSFSheet sheet;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");

		HSSFWorkbook book = (HSSFWorkbook) workbook;
		sheet = book.createSheet("sheet1");

		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		HSSFCellStyle headerStyle = book.createCellStyle(); // 标题样式
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		HSSFFont headerFont = book.createFont(); // 标题字体
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerStyle.setFont(headerFont);
		short height = 25 * 20;
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < len; i++) { // 设置标题
			String title = titles.get(i);
			row.setRowStyle(headerStyle);
			row.createCell(i).setCellValue(title);
		}
		sheet.getRow(0).setHeight(height);

		HSSFCellStyle contentStyle = book.createCellStyle(); // 内容样式
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		for (int i = 0; i < varCount; i++) {
			PageData vpd = varList.get(i);
			HSSFRow rows = sheet.createRow(i + 1);
			for (int j = 0; j < len; j++) {
				String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
				rows.setRowStyle(contentStyle);
				rows.createCell(j).setCellValue(varstr);
			}

		}
		// 普通格式组装完毕，开始设置下拉框
		DataValidationHelper helper = sheet.getDataValidationHelper();
		
		// 单元 整数
		DataValidationConstraint dyDVC = helper.createNumericConstraint(DVConstraint.ValidationType.INTEGER,DVConstraint.OperatorType.GREATER_OR_EQUAL, "0", "0");
		CellRangeAddressList dyCRA = new CellRangeAddressList(1, MAX_ROW, 2, 2);
		DataValidation dyDV = helper.createValidation(dyDVC, dyCRA);
		dyDV.createErrorBox("输入值类型或大小有误！", "数值型，请输入大于0 的整数。");
		
		
		// 处理Excel兼容性问题
		if (dyDV instanceof XSSFDataValidation) {
			dyDV.setSuppressDropDownArrow(true);
			dyDV.setShowErrorBox(true);
		} else {
			dyDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(dyDV);
		
		// 楼层   整数
		DataValidationConstraint lcDVC = helper.createNumericConstraint(DVConstraint.ValidationType.INTEGER,DVConstraint.OperatorType.GREATER_OR_EQUAL, "0", "0");
		CellRangeAddressList lcCRA = new CellRangeAddressList(1, MAX_ROW, 3, 3);
		DataValidation lcDV = helper.createValidation(lcDVC, lcCRA);
		lcDV.createErrorBox("输入值类型或大小有误！", "数值型，请输入大于0 的整数。");

		// 处理Excel兼容性问题
		if (lcDV instanceof XSSFDataValidation) {
			lcDV.setSuppressDropDownArrow(true);
			lcDV.setShowErrorBox(true);
		} else {
			lcDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(lcDV);
		
		// 证件类型
		String[] zjlxArray = { "身份证", "其他"};
		DataValidationConstraint zjlxDVC = helper.createExplicitListConstraint(zjlxArray);
		CellRangeAddressList zjlxCRAL = new CellRangeAddressList(1, MAX_ROW, 6, 6);
		DataValidation zjlxDV = helper.createValidation(zjlxDVC, zjlxCRAL);
		zjlxDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (zjlxDV instanceof XSSFDataValidation) {
			zjlxDV.setSuppressDropDownArrow(true);
			zjlxDV.setShowErrorBox(true);
		} else {
			zjlxDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(zjlxDV);
		
		// 民族
		List<Dictionaries>	mzListMap = dictionariesService.listSubDictByParentId("109b9d8b92b44dc1a21eb2642eb2125f");
		String[] mzArray = new String[56];
		for(int i = 0; i <mzListMap.size();i++ ) {
			mzArray[i] = mzListMap.get(i).getNAME();
		}
		
		DataValidationConstraint mzDVC = helper.createExplicitListConstraint(mzArray);
		CellRangeAddressList mzCRAL = new CellRangeAddressList(1, MAX_ROW, 9, 9);
		DataValidation mzDV = helper.createValidation(mzDVC, mzCRAL);
		mzDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (mzDV instanceof XSSFDataValidation) {
			mzDV.setSuppressDropDownArrow(true);
			mzDV.setShowErrorBox(true);
		} else {
			mzDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(mzDV);

	}

}