package smt.view;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.AbstractView;


public abstract class AbstractPOIExcelView extends AbstractView {
	  /** The content type for an Excel response */
    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    
    protected static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");

    @Autowired
	protected DataSource dataSource;
    
	protected static final SimpleDateFormat printTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", new Locale("th", "TH"));

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    
    /**
     * Default Constructor. Sets the content type of the view for excel files.
     */
    public AbstractPOIExcelView() {
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    protected final String getIndent(int level) {
    	String indent = " ";
    	for(int i=0; i<level; i++) {
    		indent += "          ";
    	}
    	return indent;
    }
    
    /**
     * Renders the Excel view, given the specified model.
     */
    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Workbook workbook = createWorkbook();

        if (workbook instanceof XSSFWorkbook) {
            setContentType(CONTENT_TYPE_XLSX);
        } else {
            setContentType(CONTENT_TYPE_XLS);
        }

        buildExcelDocument(model, workbook, request, response);

        String fileName = URLEncoder.encode(getFileName(),"UTF-8");
        
        // Set the content type.
        response.setContentType(getContentType());
        response.addCookie(new Cookie("fileDownload", "true"));
        response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+fileName);
        
        // Flush byte array to servlet output stream.
        ServletOutputStream out = response.getOutputStream();
        out.flush();
        workbook.write(out);
        out.flush();

        // Don't close the stream - we didn't open it, so let the container
        // handle it.
        // http://stackoverflow.com/questions/1829784/should-i-close-the-servlet-outputstream
    }
    
    protected abstract String getFileName();

    /**
     * Subclasses must implement this method to create an Excel Workbook.
     * HSSFWorkbook and XSSFWorkbook are both possible formats.
     */
    protected abstract Workbook createWorkbook();

    /**
     * Subclasses must implement this method to create an Excel HSSFWorkbook
     * document, given the model.
     * 
     * @param model
     *            the model Map
     * @param workbook
     *            the Excel workbook to complete
     * @param request
     *            in case we need locale etc. Shouldn't look at attributes.
     * @param response
     *            in case we need to set cookies. Shouldn't write to it.
     */
    protected abstract void buildExcelDocument(Map<String, Object> model, Workbook workbook,
            HttpServletRequest request, HttpServletResponse response) throws Exception;

}
