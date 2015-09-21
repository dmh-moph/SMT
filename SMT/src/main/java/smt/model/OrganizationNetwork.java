package smt.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import smt.auth.model.SecurityUser;
import smt.auth.model.User;
import smt.model.glb.Amphur;
import smt.model.glb.DomainVariable;
import smt.model.glb.HealthZone;
import smt.model.glb.JournalType;
import smt.model.glb.NetworkType;
import smt.model.glb.OrgType;
import smt.model.glb.Province;
import smt.model.glb.SchoolType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_ORGANIZATION_NETWORK")
@SequenceGenerator(name="SMT_ORGANIZATION_NETWORK_SEQ", sequenceName="SMT_ORGANIZATION_NETWORK_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class OrganizationNetwork implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7791733555935177163L;
	private static final String domainName = "ORGANIZATION_NETWORK";
	
	
	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_ORGANIZATION_NETWORK_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="DV_ORG_TYPE")
	private OrgType orgType;
	
	@ManyToOne
	@JoinColumn(name="DV_NETWORK_TYPE")
	private NetworkType networkType;
	
	@ManyToOne
	@JoinColumn(name="AMPHUR_ID")
	private Amphur amphur;
	
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private Province province;
	
	@ManyToOne
	@JoinColumn(name="ZONE_ID")
	private HealthZone zone;
	
	@Basic
	@Column(name="ORG_NAME")
	private String orgName;
	
	@Basic
	@Column(name="SCHOOL_NAME")
	private String schoolName;
	
	@ManyToOne
	@JoinColumn(name="DV_SCHOOL_TYPE")
	private SchoolType schoolType;
	
	@Basic
	@Column(name="ORG_CODE1")
	private String orgCode1;
	
	@Basic
	@Column(name="ORG_CODE2")
	private String orgCode2;
	
	@Basic
	@Column(name="ADDRESS")
	private String address;
	
	@Basic
	@Column(name="TEENFRIENDLY")
	private Boolean teenFriendly;

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
	
	@OneToMany
	@JoinColumn(name="domainId", referencedColumnName="Id")
	@Where(clause="domain='"+domainName+"'")
	private List<FileMeta> files;
	
	 @OneToMany(mappedBy="organizationNetwork", cascade=CascadeType.ALL)
	 @OrderColumn(name="organization_Network_index")
	 List<OrganizationPerson> medicalStaffs;
	
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

	public OrgType getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgType orgType) {
		this.orgType = orgType;
	}

	public NetworkType getNetworkType() {
		return networkType;
	}

	public void setNetworkType(NetworkType networkType) {
		this.networkType = networkType;
	}

	public Amphur getAmphur() {
		return amphur;
	}

	public void setAmphur(Amphur amphur) {
		this.amphur = amphur;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgCode1() {
		return orgCode1;
	}

	public void setOrgCode1(String orgCode1) {
		this.orgCode1 = orgCode1;
	}

	public String getOrgCode2() {
		return orgCode2;
	}

	public void setOrgCode2(String orgCode2) {
		this.orgCode2 = orgCode2;
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

	public List<OrganizationPerson> getMedicalStaffs() {
		return medicalStaffs;
	}

	public void setMedicalStaffs(List<OrganizationPerson> medicalStaffs) {
		if(this.medicalStaffs == null) {
			this.medicalStaffs = medicalStaffs;
		}
		
		this.medicalStaffs.removeAll(this.medicalStaffs);		
		if(medicalStaffs!=null) {
			this.medicalStaffs.addAll(medicalStaffs);
		}
	}

	public Boolean getTeenFriendly() {
		return teenFriendly;
	}

	public void setTeenFriendly(Boolean teenFriendly) {
		this.teenFriendly = teenFriendly;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public SchoolType getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}
	
	
	
}
