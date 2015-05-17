package smt.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
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
	
		@SuppressWarnings("unchecked")
		Iterable<PsychoSocialReport> reports = (Iterable<PsychoSocialReport>) model.get("reports");
		String beginReportDate = (String) model.get("beginReportDate");
		String endReportDate = (String) model.get("endReportDate");
		
		Sheet sheet =  workbook.getSheet("Sheet1");
		Row row2 = sheet.createRow(2);
		Cell cell0 = row2.createCell(14);
		cell0.setCellValue("ตั้งแต่วันที่ " + beginReportDate + "   ถึงวันที่ "+ endReportDate);

		
		PsychoSocialReport firstReport  = Iterables.getFirst(reports, null) ;
		if(firstReport !=null) {
			OrganizationNetwork org = firstReport.getOrganization();
			cell0 = row2.createCell(0);
			cell0.setCellValue("หน่วยงานที่เก็บข้อมูล ศูนย์สุขภาพจิตที่  " + org.getZone().getCode() + "    " + org.getZone().getName() );
	
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
					cell = row.createCell(1);
					cell.setCellValue(province.getName());
					order = 1;
				}
				
				row = sheet.createRow(currentRow++);
				Integer colNum = 0;
				cell = row.createCell(colNum++);
				cell.setCellValue(order++);
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getOrganization().getOrgName());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getTargetAge());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getPragnantService());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getPragnantCount());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getAlcoholService());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getAlcoholCount());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getViolenceService());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getViolenceCount());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getGamblingService());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getGamblingCount());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getDrugService());
				cell = row.createCell(colNum++);
				cell.setCellValue(report.getDrugCount());
				
			}
		} // end if firstReport == null	
	}

}
