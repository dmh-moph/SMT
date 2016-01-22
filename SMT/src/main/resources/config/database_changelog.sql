

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


/** 11 May 2015
 * 
 */
alter table smt_journal add (temp clob);
update smt_journal set temp=KEYWORD;
alter table smt_journal drop column keyword;
alter table smt_journal rename column temp to keyword;

alter table smt_journal add (temp clob);
update smt_journal set temp=SUMMARY_CONTENT;
alter table smt_journal drop column SUMMARY_CONTENT;
alter table smt_journal rename column temp to SUMMARY_CONTENT;

alter table smt_journal add (temp clob);
update smt_journal set temp=OBJECTIVE;
alter table smt_journal drop column OBJECTIVE;
alter table smt_journal rename column temp to OBJECTIVE;

create table SMT_PSYCHO_REPORT (
    id number(19,0) not null,
    month number(4,0),
    year number(4,0),
    begin_Report_Date date,
    end_Report_Date date,
    target_Age varchar2(20),
    pragnant_Count number(19,0),
    pragnant_Service number(19,0),
    alcohol_Count number(19,0),
    alcohol_Service number(19,0),
    violence_count number(19,0),
    violence_service number(19,0),
    gambling_count number(19,0),
    gambling_service number(19,0),
    drug_count number(19,0),
    drug_service  number(19,0),
    report_date timestamp,
    reprot_user_id number(19,0),
    ORG_ID number(19,0),
    primary key (id)
);
create sequence SMT_PSYCHO_REPORT_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE; 
alter table SMT_PSYCHO_REPORT 
  add CONSTRAINT SMT_PSYCHO_REPORT_ORG_FK
  FOREIGN KEY (ORG_ID) REFERENCES SMT_ORGANIZATION_NETWORK;
  
  
/** 18 May 2015
 * 
 */
  
alter table smt_journal add (temp varchar2(100));
update smt_journal set temp=to_char(published_date, 'dd month yyyy');
alter table smt_journal drop column published_date;
alter table smt_journal rename column temp to published_date;


/**
 * 20 May 2015
 * 
 */

alter table smt_journal add (temp clob);
update smt_journal set temp=KEYWORD;
alter table smt_journal drop column reference;
alter table smt_journal rename column temp to reference;

/**
 * 22 May 2015
 * 
 */
alter table smt_research add (temp clob);
update smt_research set temp=ABSTRACT_THAI;
alter table smt_research drop column ABSTRACT_THAI;
alter table smt_research rename column temp to ABSTRACT_THAI;

alter table smt_research add (temp clob);
update smt_research set temp=ABSTRACT_ENG;
alter table smt_research drop column ABSTRACT_ENG;
alter table smt_research rename column temp to ABSTRACT_ENG;

alter table smt_research add (temp clob);
update smt_research set temp=OUTPUT_NAME;
alter table smt_research drop column OUTPUT_NAME;
alter table smt_research rename column temp to OUTPUT_NAME;

alter table smt_research add (temp clob);
update smt_research set temp=reference;
alter table smt_research drop column reference;
alter table smt_research rename column temp to reference;

alter table smt_research add (temp clob);
update smt_research set temp=OBJECTIVE;
alter table smt_research drop column OBJECTIVE;
alter table smt_research rename column temp to OBJECTIVE;


create table SEC_ROLE (
    id number(19,0) not null,
    name varchar2(255 char),
    primary key (id)
);
    
create sequence SEC_ROLE_SEQ START WITH 1 INCREMENT by 1 MAXVALUE 9999999999 MINVALUE 1 NOCYCLE;

create table sec_user_role (
  user_id number(19,0) not null,
  role_id number(19,0) not null,
  primary key (user_id, role_id)
);

alter table sec_user_role add constraint sec_user_role_user_id foreign key (user_id) REFERENCES sec_user;
alter table sec_user_role add constraint sec_user_role_role_id foreign key (role_id) REFERENCES sec_role;
insert INTO sec_role VALUES (sec_role_seq.nextval, 'ADMIN');
insert INTO sec_role VALUES (sec_role_seq.nextval, 'USER');


alter table SMT_ORGANIZATION_NETWORK add (school_name varchar2(255 char));


alter table SMT_ORGANIZATION_NETWORK add (DV_SCHOOL_TYPE number);
alter table SMT_ORGANIZATION_NETWORK
	add constraint DV_SCHOOL_TYPE_FK
	foreign key (DV_SCHOOL_TYPE)
	REFERENCES GLB_VARIABLE;
	
	
create table SMT_USERINFO (
  id number(19,0) not null,
  EMAIL varchar2(255 char),
  SEX VARCHAR2(1), 
  DEPARTMENT varchar2(255 char),
  
  OCCUPATION number,
  OCCUPATION_OTHER varchar2(255 char),
  
  POSITION number,
  POSITION_OTHER varchar2(255 char),
  
  OBJECTIVE number,
  OBJECTIVE_OTHER varchar2(255 char),
  primary key (id)
);

alter table SMT_USERINFO
	add constraint DV_OCCUPATION_FK
	foreign key (OCCUPATION)
	REFERENCES GLB_VARIABLE;
	
alter table SMT_USERINFO
	add constraint DV_POSITION_FK
	foreign key (POSITION)
	REFERENCES GLB_VARIABLE;
	
alter table SMT_USERINFO
	add constraint DV_OBJECTIVE_FK
	foreign key (OBJECTIVE)
	REFERENCES GLB_VARIABLE;

alter table sec_user add (info number);

-- init for Global variable
insert into glb_variable values('OCCUPATION', 1, '1', 'บุคลากรสาธารณสุข', 'บุคลากรสาธารณสุข', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('OCCUPATION', 2, '2', 'บุคลากรศึกษาธิการ', 'บุคลากรศึกษาธิการ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('OCCUPATION', 3, '3', 'ประชาชนทั่วไป', 'ประชาชนทั่วไป', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('OCCUPATION', 4, '4', 'วัยรุ่น/นักเรียน/นักศึกษา', 'วัยรุ่น/นักเรียน/นักศึกษา', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('OCCUPATION', 5, '5', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);

insert into glb_variable values('POSITION', 1, '11', 'จิตแพทย์', 'จิตแพทย์', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 2, '12', 'แพทย์', 'แพทย์', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 3, '13', 'นักจิตวิทยา', 'นักจิตวิทยา', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 4, '14', 'พยาบาล', 'พยาบาล', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 5, '15', 'นักสังคมสงเคราะห์', 'นักสังคมสงเคราะห์', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 6, '16', 'นักวิชาการสาธารณสุข', 'นักวิชาการสาธารณสุข', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 7, '17', 'นักวิจัย', 'นักวิจัย', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 8, '18', 'บุคลากรสาธารณสุข', 'บุคลากรสาธารณสุข', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 9, '19', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);

insert into glb_variable values('POSITION', 1, '21', 'อาจารย์/ผู้บรรยาย', 'อาจารย์/ผู้บรรยาย', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 2, '22', 'นักวิจัย', 'นักวิจัย', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 3, '23', 'บุคลากรทางการศึกษา', 'บุคลากรทางการศึกษา', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('POSITION', 4, '24', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);

insert into glb_variable values('POSITION', 1, '31', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);

insert into glb_variable values('POSITION', 1, '41', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);

insert into glb_variable values('POSITION', 1, '51', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);


insert into glb_variable values('USER_INFO_OBJECTIVE', 1, '1', 'สอน/บรรยาย', 'สอน/บรรยาย', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('USER_INFO_OBJECTIVE', 2, '2', 'วิจัย', 'วิจัย', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('USER_INFO_OBJECTIVE', 3, '3', 'ศึกษาค้นคว้าเพื่อการศึกษา', 'ศึกษาค้นคว้าเพื่อการศึกษา', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('USER_INFO_OBJECTIVE', 4, '4', 'เพิ่มพูนความรู้', 'เพิ่มพูนความรู้', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);
insert into glb_variable values('USER_INFO_OBJECTIVE', 5, '5', 'อิ่นๆ', 'อิ่นๆ', 0, sysdate, 0, sysdate, glb_variable_seq.nextval);



create table SMT_FILEHISTORYRECORD (
  id number(19,0) not null,
  fileMeta_id number(19,0),
  ip_address varchar2(40),
  user_agent varchar2(100),
  search_time date,
  referrer varchar2(255),
    primary key (id)
);

alter table SMT_FILEHISTORYRECORD
	add constraint FILEHISTORY_FILE_FK
	foreign key (fileMeta_id)
	REFERENCES SMT_FILEMETA;

create sequence SMT_FILEHISTORYRECORD_seq;


create table SEC_USER_LOGINHISOTRY (
  id number(19,0) not null,
  user_id number(19,0),
  ip_address varchar2(40),
  user_agent varchar2(100),
  search_time date,
  referrer varchar2(255),
    primary key (id)
);

alter table SEC_USER_LOGINHISOTRY
	add constraint SEC_USER_LOGIN_USERID
	foreign key (user_id)
	REFERENCES SEC_USER;

create sequence SEC_USER_LOGINHISOTRY_SEQ;

commit;