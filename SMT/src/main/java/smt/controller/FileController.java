package smt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mysema.query.types.expr.BooleanExpression;

import smt.model.FileHistoryRecord;
import smt.model.FileMeta;
import smt.model.QFileMeta;
import smt.repository.FileHistoryRecordRepo;
import smt.repository.FileMetaRepo;

@Controller
@RequestMapping("/FILES")
public class FileController {
	public static Logger logger = LoggerFactory.getLogger(FileController.class);
	
	 @Value("${file.upload.directory}")
	 private String fileUploadDirectory;
	 
	 @Autowired
	 private FileMetaRepo fileMetaRepo;
	 
	 @Autowired
	 private FileHistoryRecordRepo fileHistoryRecordRepo;
	 
	 
	 @RequestMapping(value="/upload/{domain}/{domainId}", method = RequestMethod.POST)
	    public @ResponseBody List<FileMeta> upload(MultipartHttpServletRequest request,
	    		HttpServletResponse response,
	    		@PathVariable String domain,
	    		@PathVariable Long domainId) {
	 
		 	List<FileMeta> list = new LinkedList<>();
		 
	        //1. build an iterator
	         Iterator<String> itr =  request.getFileNames();
	         MultipartFile mpf = null;
	 
	         //2. get each file
	         while(itr.hasNext()){
	 
	             //2.1 get next MultipartFile
	             mpf = request.getFile(itr.next()); 
	             logger.debug(mpf.getOriginalFilename() +" uploaded! ");
	             logger.debug("saving to : " + fileUploadDirectory);
	             
	 
	             //2.3 create new fileMeta
	             FileMeta fileMeta = new FileMeta();
	             fileMeta.setFileName(mpf.getOriginalFilename());
	             fileMeta.setFileSize(mpf.getSize());
	             fileMeta.setFileType(mpf.getContentType());
	             fileMeta.setDomain(domain);
	             fileMeta.setDomainId(domainId);
	             fileMeta.setFileIndex(0);
	 
	             try {
	            	 
	            	 File base = new File(fileUploadDirectory+"/"+domain);
	            	 if(!base.exists()) {
	            		 base.mkdir();
	            	 }
	            	 
	            	 File subBase = new File(fileUploadDirectory+"/"+domain+"/"+ domainId);
	            	 if(!subBase.exists()) {
	            		 subBase.mkdir();
	            	 }
	            	 
	            	 
	                 // copy file to local disk (make sure the path "e.g. D:/temp/files" exists)            
	                 FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(fileUploadDirectory+"/"+domain + "/" + domainId+"/"+mpf.getOriginalFilename()));
	                 fileMetaRepo.save(fileMeta);
	                 list.add(fileMeta);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	             
	         }
	        // result will be like this
	        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
	        return list;
	    }
	
	 @RequestMapping(value="/list/{domain}/{domainId}", method=RequestMethod.GET)
	 public  @ResponseBody Iterable<FileMeta>  listFiles(
			 	@PathVariable String domain,
	    		@PathVariable Long domainId) {
		 QFileMeta q = QFileMeta.fileMeta;
		 BooleanExpression p = q.domain.eq(domain)
				 .and(q.domainId.eq(domainId));
		 Iterable<FileMeta> list = fileMetaRepo.findAll(p);
		 
		 return list;
	 }
	 
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
     public @ResponseBody String deleteFile(@PathVariable Long id){
		 
		 QFileMeta q = QFileMeta.fileMeta;
		 BooleanExpression p = q.id.eq(id);
		 try {  
			 FileMeta fileMeta = fileMetaRepo.findOne(p);
			 if(fileMeta == null) throw new FileNotFoundException();
			 fileMetaRepo.delete(fileMeta);
			 
			 File file = new File(fileUploadDirectory +"/"
					 + fileMeta.getDomain() + "/" 
					 + fileMeta.getDomainId()+"/"
					 + fileMeta.getFileName());
			 
			 logger.debug("deleting file : " + file.getAbsolutePath());
			 
			 if(file.delete()) {
				 return "success";
			 } else {
				 return "failed";
			 }
         }catch (IOException e) {
        	 e.printStackTrace();
         }
		 
		 return "failed";
     }
	 
	 @RequestMapping(value = "/get/{domain}/{domainId}/{filename:.+}", method = RequestMethod.GET)
     public void getFile(HttpServletResponse response,
    		 @PathVariable String filename,
    		 @PathVariable String domain,
    		 @PathVariable Long domainId,
    		 HttpServletRequest request){
		 File file = new File(fileUploadDirectory+"/"+domain + "/" + domainId+"/"+filename);
		 logger.debug("finding file @ : " + domain + " domainId:" + " filename: " + filename);
		 logger.debug("finding file : " + file.getAbsolutePath());
		 QFileMeta q = QFileMeta.fileMeta;
		 BooleanExpression p = q.domain.eq(domain)
				 .and(q.domainId.eq(domainId))
				 .and(q.fileName.eq(filename));
		 try {  
			 FileMeta fileMeta = fileMetaRepo.findOne(p);
			 if(fileMeta == null) throw new FileNotFoundException();
			 
        	 // now save usage record
        	 FileHistoryRecord record = new FileHistoryRecord(fileMeta, request);
        	 fileHistoryRecordRepo.save(record);
			 
        	 // and stream to response
			 String fileName = URLEncoder.encode(fileMeta.getFileName(),"UTF-8");
			 response.setCharacterEncoding("UTF-8");
             response.setContentType(fileMeta.getFileType() + ";charset=UTF-8");
        	 response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+fileName);
        	 
        	 FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());

         }catch (IOException e) {
        	 e.printStackTrace();
         }
		 
		 
     }
}

