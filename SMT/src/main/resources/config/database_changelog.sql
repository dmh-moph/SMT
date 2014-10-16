

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


commit;