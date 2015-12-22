package smt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_FILEHISTORYRECORD")
@SequenceGenerator(name="SMT_FILEHISTORYRECORD_SEQ", sequenceName="SMT_FILEHISTORYRECORD_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=FileHistoryRecord.class)
public class FileHistoryRecord implements Serializable{

	private static final long serialVersionUID = 3664601474148756173L;

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_FILEHISTORYRECORD_SEQ")
	@Column(name="ID")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="fileMeta_id")
	private FileMeta file;
	
	@Column(name="ip_address")
	private String ipAddress;
	
	@Column(name="user_agent")
	private String userAgent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="search_time")
	private Date searchTime;
	
	@Column(name="referrer")
	private String referrer;

	public FileHistoryRecord() {
		
	}
	
	public FileHistoryRecord(FileMeta file, HttpServletRequest request) {
		this.file = file;
		this.ipAddress = request.getRemoteAddr();
		this.userAgent = request.getHeader("User-Agent");
		this.searchTime = new Date();
		this.referrer = request.getHeader("referer");
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FileMeta getFile() {
		return file;
	}

	public void setFile(FileMeta file) {
		this.file = file;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Date getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	
}
