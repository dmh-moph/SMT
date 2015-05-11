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
import smt.auth.model.User;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_ORG_REPORT")
@SequenceGenerator(name="SMT_ORG_REPORT_SEQ", sequenceName="SMT_ORG_REPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=OrganizationNetworkReport.class)
public class OrganizationNetworkReport implements Serializable{

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
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_ORG_REPORT_SEQ")
	@Column(name="ID")
	private Long id;
	

	@ManyToOne
	@JoinColumn(name="ORG_ID")
	private OrganizationNetwork org;
	
	@Column(name="month")
	private Integer month;
	
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
	private Integer alcoholCoun;
	@Column(name="alcohol_service")
	private Integer alcoholService;
	
	@Column(name="violence_count")
	private Integer violenceCount;
	
	@Column(name="violence_service")
	private Integer violenceService;
	
	@Column(name="gambling_count")
	private Integer gamblingCount;
	
	@Column(name="gambling_service")
	private Integer gamblineService;
	
	@Column(name="drug_count")
	private Integer drungCount;
	
	@Column(name="drug_service")
	private Integer drugService;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="report_date")
	private Date reportDate;
	
	@ManyToOne
	@JoinColumn(name="reprot_user_id")
	private SecurityUser reportUser;
	
	
	
	
}
