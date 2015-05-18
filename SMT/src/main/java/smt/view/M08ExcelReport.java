package smt.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

import com.google.common.collect.Iterables;

import smt.model.OrganizationNetwork;
import smt.model.PsychoSocialReport;
import smt.model.glb.Province;

public class M08ExcelReport extends AbstractPOIExcelView {

	
	
	@Override
	protected Workbook createWorkbook() {
		XSSFWorkbook wb = null;
		try {
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("excelTemplates/m08report1.xlsx");
			
			wb = new  XSSFWorkbook(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

	
	
	@Override
	protected String getFileName() {
		return "รายงานผลการดำเนินงานศูนย์ให้คำปรึกษาคุณภาพ.xlsx";
	}

	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short)14);
		font.setFontName("TH SarabunPSK");
		
		
		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short)14);
		boldFont.setFontName("TH SarabunPSK");
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		
		CellStyle topRightStyle = workbook.createCellStyle();
		topRightStyle.setFont(font);
		topRightStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        
		CellStyle topLeftStyle = workbook.createCellStyle();
		topLeftStyle.setFont(font);
		topLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);
		
		CellStyle rowCenterStyle = workbook.createCellStyle();
		rowCenterStyle.setFont(font);
		rowCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
		rowCenterStyle.setBorderBottom(CellStyle.BORDER_THIN);
		rowCenterStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rowCenterStyle.setBorderLeft(CellStyle.BORDER_THIN);
		rowCenterStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rowCenterStyle.setBorderRight(CellStyle.BORDER_THIN);
		rowCenterStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rowCenterStyle.setBorderTop(CellStyle.BORDER_THIN);
		rowCenterStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
		
		CellStyle rowLeftStyle = workbook.createCellStyle();
		rowLeftStyle.setFont(font);
		rowLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);
		rowLeftStyle.setBorderBottom(CellStyle.BORDER_THIN);
		rowLeftStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftStyle.setBorderLeft(CellStyle.BORDER_THIN);
		rowLeftStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftStyle.setBorderRight(CellStyle.BORDER_THIN);
		rowLeftStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftStyle.setBorderTop(CellStyle.BORDER_THIN);
		rowLeftStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
		CellStyle rowLeftBoldStyle = workbook.createCellStyle();
		rowLeftBoldStyle.setFont(boldFont);
		rowLeftBoldStyle.setAlignment(CellStyle.ALIGN_LEFT);
		rowLeftBoldStyle.setBorderBottom(CellStyle.BORDER_THIN);
		rowLeftBoldStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftBoldStyle.setBorderLeft(CellStyle.BORDER_THIN);
		rowLeftBoldStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftBoldStyle.setBorderRight(CellStyle.BORDER_THIN);
		rowLeftBoldStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		rowLeftBoldStyle.setBorderTop(CellStyle.BORDER_THIN);
		rowLeftBoldStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());


		
		@SuppressWarnings("unchecked")
		Iterable<PsychoSocialReport> reports = (Iterable<PsychoSocialReport>) model.get("reports");
		String beginReportDate = (String) model.get("beginReportDate");
		String endReportDate = (String) model.get("endReportDate");
		
		Sheet sheet =  workbook.getSheet("Sheet1");
		Row row2 = sheet.createRow(2);
		Cell cell0 = row2.createCell(14);
		cell0.setCellValue("ตั้งแต่วันที่ " + beginReportDate + "   ถึงวันที่ "+ endReportDate);
		cell0.setCellStyle(topRightStyle);

		
		PsychoSocialReport firstReport  = Iterables.getFirst(reports, null) ;
		if(firstReport !=null) {
			OrganizationNetwork org = firstReport.getOrganization();
			cell0 = row2.createCell(0);
			cell0.setCellValue("หน่วยงานที่เก็บข้อมูล ศูนย์สุขภาพจิตที่  " + org.getZone().getCode() + "    " + org.getZone().getName() );
			cell0.setCellStyle(topLeftStyle);
	
			Iterator<PsychoSocialReport> iter = reports.iterator();
			Integer currentRow = 7;
			Row row;
			Cell cell;
			Integer order = 1;
			Province province = null;
			while(iter.hasNext()) {
				PsychoSocialReport report = iter.next();
				Province reportProvince = report.getOrganization().getProvince();
				if(province == null || reportProvince.getId() != province.getId() ) {
					province = reportProvince;
					row = sheet.createRow(currentRow++);
					cell = row.createCell(0);
					cell.setCellStyle(rowCenterStyle);
					cell = row.createCell(1);
					cell.setCellValue("จังหวัด"+province.getName());
					cell.setCellStyle(rowLeftBoldStyle);
					for(int i=2; i<15; i++ ) {
						cell = row.createCell(i);
						cell.setCellStyle(rowLeftStyle);
					}
					
					order = 1;
				}
				
				row = sheet.createRow(currentRow++);
				Integer colNum = 0;
				cell = row.createCell(colNum++);
				cell.setCellValue(order++);
				cell.setCellStyle(rowCenterStyle);
				
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getOrganization().getOrgName());
				cell.setCellStyle(rowLeftStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getTargetAge());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getPragnantService());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getPragnantCount());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getAlcoholService());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getAlcoholCount());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getViolenceService());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getViolenceCount());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getGamblingService());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getGamblingCount());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getDrugService());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(report.getDrugCount());
				cell.setCellStyle(rowCenterStyle);

				cell = row.createCell(colNum++);
				cell.setCellValue(sdf.format(report.getReportDate()));
				cell.setCellStyle(rowCenterStyle);
				
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getReportUser().getUsername());
				cell.setCellStyle(rowCenterStyle);
				
			}
		} // end if firstReport == null	
	}

}
