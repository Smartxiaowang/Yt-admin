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
public class CarportExcelView extends AbstractXlsView {

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
		
		
		// 房间类型
		String[] cwlxArray = { "产权车位", "出租车位", "出售车位", "临时车位" };
		DataValidationConstraint cwlxDVC = helper.createExplicitListConstraint(cwlxArray);
		CellRangeAddressList cwlxCRAL = new CellRangeAddressList(1, MAX_ROW, 0, 0);
		DataValidation cwlxDV = helper.createValidation(cwlxDVC, cwlxCRAL);
		cwlxDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (cwlxDV instanceof XSSFDataValidation) {
			cwlxDV.setSuppressDropDownArrow(true);
			cwlxDV.setShowErrorBox(true);
		} else {
			cwlxDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(cwlxDV);
		
		// 房间性质
		String[] cwflArray = { "地下车位", "地上车位", "车库" };
		DataValidationConstraint cwflDVC = helper.createExplicitListConstraint(cwflArray);
		CellRangeAddressList cwflCRAL = new CellRangeAddressList(1, MAX_ROW, 1, 1);
		DataValidation cwflDV = helper.createValidation(cwflDVC, cwflCRAL);
		cwflDV.createErrorBox("请选择下拉框中的选项！", "请选择下拉框中的选项！");
		// 处理Excel兼容性问题
		if (cwflDV instanceof XSSFDataValidation) {
			cwflDV.setSuppressDropDownArrow(true);
			cwflDV.setShowErrorBox(true);
		} else {
			cwflDV.setSuppressDropDownArrow(false);
		}
		sheet.addValidationData(cwflDV);
		

	}

}