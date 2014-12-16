package smt.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import smt.model.glb.HealthZone;
import smt.model.glb.JournalType;
import smt.model.glb.Province;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_JOURNAL")
@SequenceGenerator(name="SMT_JOURNAL_SEQ", sequenceName="SMT_JOURNAL_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Journal.class)
public class Journal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1152936322423117290L;

	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_JOURNAL_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="DV_JOURNAL_TYPE")
	private JournalType journalType;
	
	@Basic
	@Column(name="JOURNAL_NAME")
	private String nameTh;
	
	@Basic
	@Column(name="JOURNAL_ENAME")
	private String nameEn;
	
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private Province province;
	
	@ManyToOne
	@JoinColumn(name="ZONE_ID")
	private HealthZone zone;
	
	@Basic
	@Column(name="AUTHOR")
	private String author;
	
	@Basic
	@Column(name="OBJECTIVE")
	private String objective;
	
	@Basic
	@Column(name="SUMMARY_CONTENT")
	private String summaryContent;
	
	@Basic
	@Column(name="KEYWORD")
	private String keyword;

	@Temporal(TemporalType.DATE)
	@Column(name="PUBLISED_DATE")
	private Date publishDate;
	
	@Basic
	@Column(name="ADDRESS")
	private String address;

	@Basic
	@Column(name="TELEPHONE")
	private String telephone;
	
	@Basic
	@Column(name="CONTACT_PERSON")
	private String contactPerson;
	
	@Basic
	@Column(name="EMAIL")
	private String email;
	
	@Basic
	@Column(name="WEBSITE")
	private String website;
	
	@Basic
	@Column(name="REFERNECE")
	private String reference;
	
	@ManyToOne
	@JoinColumn(name="CREATE_BY")
	private SecurityUser createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	@ManyToOne
	@JoinColumn(name="LAST_UPDATE_BY")
	private SecurityUser lastUpdateBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JournalType getJournalType() {
		return journalType;
	}

	public void setJournalType(JournalType journalType) {
		this.journalType = journalType;
	}

	public String getNameTh() {
		return nameTh;
	}

	public void setNameTh(String nameTh) {
		this.nameTh = nameTh;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public HealthZone getZone() {
		return zone;
	}

	public void setZone(HealthZone zone) {
		this.zone = zone;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getSummaryContent() {
		return summaryContent;
	}

	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public SecurityUser getCreateBy() {
		return createBy;
	}

	public void setCreateBy(SecurityUser createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SecurityUser getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(SecurityUser lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	
	
}
