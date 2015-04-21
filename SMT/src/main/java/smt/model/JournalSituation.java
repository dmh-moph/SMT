package smt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_JOURNALSITUATION")
@SequenceGenerator(name="SMT_JOURNALSITUATION_SEQ", sequenceName="SMT_JOURNALSITUATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=JournalSituation.class)
public class JournalSituation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7916215942202498185L;
	
	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_JOURNALSITUATION_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="JOURNAL_ID")
	private Journal journal;
	
	@ManyToOne
	@JoinColumn(name="SITUATION_ID")
	private Situation situation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Situation getSituation() {
		return situation;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

}
