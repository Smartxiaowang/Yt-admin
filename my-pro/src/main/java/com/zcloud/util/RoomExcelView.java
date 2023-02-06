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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.zcloud.entity.PageData;

/**
 * 导出xlsx数据
 */
@Service
public class RoomExcelView extends AbstractXlsView {

	private final int MAX_ROW = 65535;

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
		
		// 房间面积   >=0的数，包含小数。
		DataValidationConstraint fjmjDVC = helper.createNumericConstraint(DVConstraint.ValidationType.DECIMAL,DVConstraint.OperatorType.GREATER_OR_EQUAL, "0", "0");
		CellRangeAddressList fjmjCRA = new CellRangeAddressList(1, MAX_ROW, 4, 4);
		DataValidation fjmjDV = helper.createValidation(fjmjDVC, fjmjCRA);
		fjmjDV.createErrorBox("输入值类型或大小有误！", "数值型，请输入大于0 的数值。");

		// 处理Excel兼容性问题
		if (fjmjDV instanceof XSSFDataValidation) {
			fjmjDV.setSuppressDropDownArrow(true);
			fjmjDV.setShowErrorBox(true);
		} else {
			fjmjDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(fjmjDV);
		
		
		// 房间类型
		String[] fjlxArray = { "出租", "自住", "空置" };
		DataValidationConstraint fjlxDVC = helper.createExplicitListConstraint(fjlxArray);
		CellRangeAddressList fjlxCRAL = new CellRangeAddressList(1, MAX_ROW, 5, 5);
		DataValidation fjlxDV = helper.createValidation(fjlxDVC, fjlxCRAL);
		fjlxDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (fjlxDV instanceof XSSFDataValidation) {
			fjlxDV.setSuppressDropDownArrow(true);
			fjlxDV.setShowErrorBox(true);
		} else {
			fjlxDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(fjlxDV);
		
		// 房间性质
		String[] fjxzArray = { "私有", "公有", "物业", "开发商" };
		DataValidationConstraint fjxzDVC = helper.createExplicitListConstraint(fjxzArray);
		CellRangeAddressList fjxzCRAL = new CellRangeAddressList(1, MAX_ROW, 6, 6);
		DataValidation fjxzDV = helper.createValidation(fjxzDVC, fjxzCRAL);
		fjxzDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (fjxzDV instanceof XSSFDataValidation) {
			fjxzDV.setSuppressDropDownArrow(true);
			fjxzDV.setShowErrorBox(true);
		} else {
			fjxzDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(fjxzDV);
		
		// 户型
		String[] hxArray = { "一室一厅", "一室两厅", "两室三厅" };
		DataValidationConstraint hxDVC = helper.createExplicitListConstraint(hxArray);
		CellRangeAddressList hxCRAL = new CellRangeAddressList(1, MAX_ROW, 7, 7);
		DataValidation hxDV = helper.createValidation(hxDVC, hxCRAL);
		hxDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (hxDV instanceof XSSFDataValidation) {
			hxDV.setSuppressDropDownArrow(true);
			hxDV.setShowErrorBox(true);
		} else {
			hxDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(hxDV);
		
		// 朝向
		String[] cxArray = { "朝南", "朝北", "朝东", "朝西" };
		DataValidationConstraint cxDVC = helper.createExplicitListConstraint(cxArray);
		CellRangeAddressList cxCRAL = new CellRangeAddressList(1, MAX_ROW, 8, 8);
		DataValidation cxDV = helper.createValidation(cxDVC, cxCRAL);
		cxDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (cxDV instanceof XSSFDataValidation) {
			cxDV.setSuppressDropDownArrow(true);
			cxDV.setShowErrorBox(true);
		} else {
			cxDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(cxDV);

	}

}