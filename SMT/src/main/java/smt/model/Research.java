package smt.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import smt.auth.model.SecurityUser;
import smt.model.glb.JournalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_RESEARCH")
@SequenceGenerator(name="SMT_RESEARCH_SEQ", sequenceName="SMT_RESEARCH_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Research.class)
public class Research implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2327790821232340317L;
	private static final String domainName = "RESEARCH";
	
	

	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_RESEARCH_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="DV_JOURNAL_TYPE")
	private JournalType journalType;
	
	@Basic
	@Column(name="RESEARCH_NAME")
	private String nameTh;
	
	@Basic
	@Column(name="RESEARCH_ENAME")
	private String nameEn;
	
	@ManyToOne
	@JoinColumn(name="ORGANIZATION_ID")
	private OrganizationNetwork organization;
	
	@Basic
	@Column(name="RESEARCHER")
	private String researcher;
	
	@Basic
	@Column(name="RESEARCH_OUTPUT")
	private String researchOutput;
	
	@Basic
	@Column(name="UNIT")
	private String unit;
	
	@Basic
	@Column(name="OUTPUT_NAME")
	private String outputName;
	
	@Basic
	@Column(name="OWNER")
	private String owner;
	
	@Basic
	@Column(name="YEAR")
	private Integer year;
	
	@Basic
	@Column(name="COPY_NO")
	private Integer copyNo;
	
	@Basic
	@Column(name="PRINTED_YEAR")
	private Integer printedYear;
	
	@Basic
	@Column(name="PUBLISHED_YEAR")
	private Integer publishYear;
	
	@Basic
	@Column(name="OBJECTIVE")
	private String objective;
	
	@Basic
	@Column(name="ABSTRACT_THAI")
	private String abstractTh;
	
	@Basic
	@Column(name="ABSTRACT_ENG")
	private String abstractEn;
	
	
	@Basic
	@Column(name="KEYWORD")
	private String keyword;

	@Basic
	@Column(name="ADDRESS")
	private String address;

	@Basic
	@Column(name="EMAIL")
	private String email;
	
	@Basic
	@Column(name="REFERENCE")
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
	
	@OneToMany
	@JoinColumn(name="domainId", referencedColumnName="Id")
	@Where(clause="domain='"+domainName+"'")
	private List<FileMeta> files;

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

		public OrganizationNetwork getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationNetwork organization) {
		this.organization = organization;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

		public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getResearcher() {
		return researcher;
	}

	public void setResearcher(String researcher) {
		this.researcher = researcher;
	}

	public String getResearchOutput() {
		return researchOutput;
	}

	public void setResearchOutput(String researchOutput) {
		this.researchOutput = researchOutput;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getCopyNo() {
		return copyNo;
	}

	public void setCopyNo(Integer copyNo) {
		this.copyNo = copyNo;
	}

	public Integer getPrintedYear() {
		return printedYear;
	}

	public void setPrintedYear(Integer printedYear) {
		this.printedYear = printedYear;
	}

	public Integer getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(Integer publishYear) {
		this.publishYear = publishYear;
	}

	public String getAbstractTh() {
		return abstractTh;
	}

	public void setAbstractTh(String abstractTh) {
		this.abstractTh = abstractTh;
	}

	public String getAbstractEn() {
		return abstractEn;
	}

	public void setAbstractEn(String abstractEn) {
		this.abstractEn = abstractEn;
	}

	public List<FileMeta> getFiles() {
		return files;
	}

	public void setFiles(List<FileMeta> files) {
		this.files = files;
	}

	public String getDomainName() {
		return domainName;
	}
	
}
