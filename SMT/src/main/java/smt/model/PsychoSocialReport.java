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

import smt.auth.model.SecurityUser;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_PSYCHO_REPORT")
@SequenceGenerator(name="SMT_PSYCHO_REPORT_SEQ", sequenceName="SMT_PSYCHO_REPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=PsychoSocialReport.class)
public class PsychoSocialReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6874933582068802187L;
	

	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_PSYCHO_REPORT_SEQ")
	@Column(name="ID")
	private Long id;
	

	@ManyToOne
	@JoinColumn(name="ORG_ID")
	private OrganizationNetwork organization;
	
	@Column(name="month")
	private Integer month;
	
	@Column(name="year")
	private Integer year;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="begin_report_date")
	private Date beginReportDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_report_date")
	private Date endReportDate;
	
	@Column(name="target_Age")
	private String targetAge;
	
	@Column(name="pragnant_count")
	private Integer pragnantCount;
	
	@Column(name="pragnant_service")
	private Integer pragnantService;
	
	
	@Column(name="alcohol_count")
	private Integer alcoholCount;
	@Column(name="alcohol_service")
	private Integer alcoholService;
	
	@Column(name="violence_count")
	private Integer violenceCount;
	
	@Column(name="violence_service")
	private Integer violenceService;
	
	@Column(name="gambling_count")
	private Integer gamblingCount;
	
	@Column(name="gambling_service")
	private Integer gamblingService;
	
	@Column(name="drug_count")
	private Integer drugCount;
	
	@Column(name="drug_service")
	private Integer drugService;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="report_date")
	private Date reportDate;
	
	@ManyToOne
	@JoinColumn(name="reprot_user_id")
	private SecurityUser reportUser;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrganizationNetwork getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationNetwork organization) {
		this.organization = organization;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Date getBeginReportDate() {
		return beginReportDate;
	}

	public void setBeginReportDate(Date beginReportDate) {
		this.beginReportDate = beginReportDate;
	}

	public Date getEndReportDate() {
		return endReportDate;
	}

	public void setEndReportDate(Date endReportDate) {
		this.endReportDate = endReportDate;
	}

	public String getTargetAge() {
		return targetAge;
	}

	public void setTargetAge(String targetAge) {
		this.targetAge = targetAge;
	}

	public Integer getPragnantCount() {
		return pragnantCount;
	}

	public void setPragnantCount(Integer pragnantCount) {
		this.pragnantCount = pragnantCount;
	}

	public Integer getPragnantService() {
		return pragnantService;
	}

	public void setPragnantService(Integer pragnantService) {
		this.pragnantService = pragnantService;
	}

	public Integer getAlcoholCount() {
		return alcoholCount;
	}

	public void setAlcoholCount(Integer alcoholCount) {
		this.alcoholCount = alcoholCount;
	}

	public Integer getAlcoholService() {
		return alcoholService;
	}

	public void setAlcoholService(Integer alcoholService) {
		this.alcoholService = alcoholService;
	}

	public Integer getViolenceCount() {
		return violenceCount;
	}

	public void setViolenceCount(Integer violenceCount) {
		this.violenceCount = violenceCount;
	}

	public Integer getViolenceService() {
		return violenceService;
	}

	public void setViolenceService(Integer violenceService) {
		this.violenceService = violenceService;
	}

	public Integer getGamblingCount() {
		return gamblingCount;
	}

	public void setGamblingCount(Integer gamblingCount) {
		this.gamblingCount = gamblingCount;
	}

	public Integer getGamblingService() {
		return gamblingService;
	}

	public void setGamblingService(Integer gamblineService) {
		this.gamblingService = gamblineService;
	}

	public Integer getDrugCount() {
		return drugCount;
	}

	public void setDrugCount(Integer drugCount) {
		this.drugCount = drugCount;
	}

	public Integer getDrugService() {
		return drugService;
	}

	public void setDrugService(Integer drugService) {
		this.drugService = drugService;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public SecurityUser getReportUser() {
		return reportUser;
	}

	public void setReportUser(SecurityUser reportUser) {
		this.reportUser = reportUser;
	}
	
	
	
	
}
