package smt.auth.model;

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

import smt.model.FileMeta;

@Entity
@Table(name="SEC_USER_LOGINHISOTRY")
@SequenceGenerator(name="SEC_USER_LOGINHISOTRY_SEQ", sequenceName="SEC_USER_LOGINHISOTRY_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SecurityUserLoginHistory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -455309058249250860L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SEC_USER_LOGINHISOTRY_SEQ")
	@Column(name="ID")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private SecurityUser user;
	
	@Column(name="ip_address")
	private String ipAddress;
	
	@Column(name="user_agent")
	private String userAgent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="search_time")
	private Date searchTime;
	
	@Column(name="referrer")
	private String referrer;

public SecurityUserLoginHistory() {
		
	}
	
	public SecurityUserLoginHistory(SecurityUser user, HttpServletRequest request) {
		this.user = user;
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

	public SecurityUser getUser() {
		return user;
	}

	public void setUser(SecurityUser user) {
		this.user = user;
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
