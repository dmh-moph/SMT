

alter table glb_variable add(id number);
create SEQUENCE glb_variable_seq START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE;
update GLB_VARIABLE set id=glb_variable_seq.NEXTVAL;
alter table glb_variable add constraint pk primary key (id);

alter table SMT_ORGANIZATION_NETWORK add (
  DV_ORG_TYPE number,
  DV_NETWORK_TYPE number);
alter table SMT_ORGANIZATION_NETWORK 
  add CONSTRAINT dv_network_type_fk 
  foreign key (DV_NETWORK_TYPE)
  REFERENCES GLB_VARIABLE;
alter table SMT_ORGANIZATION_NETWORK 
  add CONSTRAINT dv_org_type_fk 
  foreign key (DV_ORG_TYPE)
  REFERENCES GLB_VARIABLE;
  
  
create table SEC_USER (
    id number(19,0) not null,
    username varchar2(255 char),
    password varchar2(255 char),
    primary key (id)
);
    
create sequence SEC_USER_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE;

alter table smt_organization_person add (dv_person_type number, organizationNetwork_index number);
alter table smt_organization_person 
  add CONSTRAINT dv_person_type_fk 
  foreign key (DV_PERSON_TYPE)
  REFERENCES GLB_VARIABLE;

alter table smt_behavior add (DV_EDUCATION_LEVEL number, DV_SITUATION_TYPE number);
alter table smt_behavior 
  add CONSTRAINT DV_EDUCATION_LEVEL_FK 
  foreign key (DV_EDUCATION_LEVEL)
  REFERENCES GLB_VARIABLE;
  
alter table smt_behavior 
  add CONSTRAINT DV_SITUATION_TYPE_FK 
  foreign key (DV_SITUATION_TYPE)
  REFERENCES GLB_VARIABLE;

  
alter table smt_behavior add (ZONE_ID number);
alter table smt_behavior
	add constraint ZONE_BEHAVIOR_FK
	foreign key (ZONE_ID)
	REFERENCES GLB_ZONE;

	
alter table smt_journal add (DV_JOURNAL_TYPE number);
alter table smt_journal
	add constraint DV_JOURNAL_TYPE_FK
	foreign key (DV_JOURNAL_TYPE)
	REFERENCES GLB_VARIABLE;
		
alter table smt_journal drop column JOURNAL_TYPE;

alter table smt_research add (DV_JOURNAL_TYPE number);
alter table smt_research
	add constraint RESEARCH_DV_JOURNAL_TYPE_FK
	foreign key (DV_JOURNAL_TYPE)
	REFERENCES GLB_VARIABLE;
		
alter table smt_research drop column RESEARCH_TYPE;
  
alter table SMT_ORGANIZATION_NETWORK add (
  TEENFRIENDLY NUMBER(1));
  
  
create table SMT_SITUATION (
    id number(19,0) not null,
    DV_SITUATION_TYPE number,
    code varchar2(255 char),
    name varchar2(255 char),
    primary key (id)
);

alter table SMT_SITUATION 
  add CONSTRAINT SITUATION_DV_SITUATION_TYPE_FK 
  foreign key (DV_SITUATION_TYPE)
  REFERENCES GLB_VARIABLE;
    
create sequence SMT_SITUATION_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE;
  

alter table smt_situation add (type varchar2(255 char));
alter table smt_behavior 
  add CONSTRAINT BEHAVIOR_SITUATION_FK
  FOREIGN KEY (SITUATION_ID) REFERENCES SMT_SITUATION;
alter table smt_behavior drop CONSTRAINT smt_behavior_situation_FK;



create table SMT_JOURNALSITUATION (
 	id number(19,0) not null,
 	journal_id number(19,0),
 	situation_id number(19,0),
 	primary key (id)
);
create sequence SMT_JOURNALSITUATION_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE; 
alter table SMT_JOURNALSITUATION 
  add CONSTRAINT JOURNALBEHAVIOR_SITUATION_FK
  FOREIGN KEY (SITUATION_ID) REFERENCES SMT_SITUATION;
 alter table SMT_JOURNALSITUATION 
  add CONSTRAINT JOURNALBEHAVIOR_JOURNAL_FK
  FOREIGN KEY (JOURNAL_ID) REFERENCES SMT_JOURNAL;
  
create table SMT_RESEARCHSITUATION (
 	id number(19,0) not null,
 	research_id number(19,0),
 	situation_id number(19,0),
 	primary key (id)
);
create sequence SMT_RESEARCHSITUATION_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE; 
alter table SMT_RESEARCHSITUATION 
  add CONSTRAINT RESEARCH_SITUATION_FK
  FOREIGN KEY (SITUATION_ID) REFERENCES SMT_SITUATION;
 alter table SMT_RESEARCHSITUATION 
  add CONSTRAINT RESEARCHSIT_RESEARCH_FK
  FOREIGN KEY (RESEARCH_ID) REFERENCES SMT_RESEARCH;


create table SMT_FILEMETA (
    id number(19,0) not null,
    domain varchar2(255 char),
    domainId number(19,0),
    fileIndex number(4,0),
    fileName varchar2(255 char),
    fileSize number(19,0),
    fileType varchar2(255 char),
    primary key (id)
);
create sequence SMT_FILEMETA_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE; 

commit;