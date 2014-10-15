/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newFormBreadCrumb = Handlebars.compile($("#newFormBreadCrumbTemplate").html());
		this.editFormBreadCrumb = Handlebars.compile($("#editFormBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
		console.log(options);
		
		if(options.formView != null) {
			this.formView = options.formView;	
		} 
		if(options.searchView != null) {
			this.searchView = options.searchView;
		}
		if(options.tableResultView != null) {
			this.tableResultView = options.tableResultView;
		}
		
	},
	routes: {
        "newNetworkOrgnazation" : "newForm",
        "search" : "search",
        "NetworkOrganization/:id" : "editForm",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	// set breadcrumb
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	// no table result
    	this.tableResultView.$el.empty();
    	
    	// no form
    	this.formView.$el.empty();

    	// show search
    	this.searchView.render();  	
    },
    
    search: function() {
    	// set breadcrumb
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());

    	// show search
    	this.searchView.render();

    	
    	// no table result
    	this.tableResultView.render();
    	
    	// no form
    	this.formView.$el.empty();
    },
    
    newForm: function() {
    	this.tableResultView.$el.empty();
    	this.searchView.$el.empty();
    	this.$breadcrubmEl.html(this.newFormBreadCrumb());
    	
    	this.formView.newForm();
    },
    
    editForm: function(id){
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	var json={};
    	json.companyId=id;
    	this.$breadcrubmEl.html(this.editFormBreadCrumb(json));
    	
    	this.formView.editForm(id);
    }
    
    
});


var SearchView = Backbone.View.extend({
    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
    	this.searchModel = new smt.Model.OrganizationNetwork();
    	
    	// the three must have option!
    	this.networkTypes = options.networkTypes;
    	this.orgTypes = options.orgTypes;
    	this.heathZones = options.healthZones;
    	

    	this.provinces = new smt.Collection.Provinces();
    },
    
    events: {
    	"change .formSlt": "onChangeFormSlt",
    	
    	"click #newFormBtn" : "onClicknewFormBtn"
    		
    },
    
    onClicknewFormBtn : function(e) {
    	// we can simply navigate to newForm
    	appRouter.navigate("newNetworkOrgnazation", {trigger: true});
    	
    },
    onChangeFormSlt: function(e) {
    	//get healthZone val
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 
    	
    	var model;
    	
    	if(field == 'zone') {
    		model = smt.Model.HealthZone.findOrCreate({id:id});
    		
    		//update provinceSlt
        	this.provinces.fetch({
        		url: appUrl('Province/findAllByZone/'+id),
        		success: _.bind(function(collection, response, options) {
        			var json = this.provinces.toJSON();
        			
        			this.$el.find('#provinceSlt').html(this.provinceSltTemplate(json));
        			
        			//clear 
        			this.searchModel.set('province', null);
        			
        		}, this)
        	})
    		
    	} else if(field == 'province') {
    		model = smt.Model.Province.findOrCreate({id:id});
    	} else if(field == 'networkType') {
    		model = smt.Model.DV_NetworkType.findOrCreate({id:id});
    	} else if(field == 'orgType') {
    		model = smt.Model.DV_OrgType.findOrCreate({id:id});
    	} else {
    		model = null;
    		return;
    	}
    	this.searchModel.set(field, model);
    	
    },
    
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();
    	
    	json.networkTypes = this.networkTypes.toJSON();
    	json.orgTypes = this.orgTypes.toJSON();
    	json.healthZones = this.heathZones.toJSON();
    	
    	this.$el.html(this.searchViewTemplate(json));
    	
    	return this;
	}	

});

var TableResultView = Backbone.View.extend({
	render: function() {
		return this;
	}
});

var FormView = Backbone.View.extend({
	 initialize: function(options){
		 this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
		 this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
		 this.amphurSltTemplate = Handlebars.compile($("#amphurSltTemplate").html());
		 this.medicalStaffsTbodyTemplate = Handlebars.compile($("#medicalStaffsTbodyTemplate").html());
		 
		 
		 this.personModalView = new PersonModalView({el : '#personModal', parentView: this});
			
		 // the three must have option!
		 this.networkTypes = options.networkTypes;
		 this.orgTypes = options.orgTypes;
		 this.heathZones = options.healthZones;
		 
		 this.provinces = new smt.Collection.Provinces();
		 this.amphurs = new smt.Collection.Amphurs();
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		 "click #newPersonBtn" : "onClickNewPersonBtn",
		 "click .removePersonBtn" : "onClickRemovePersonBtn",
		 "click .editPersonBtn" : "onClickEditPersonBtn",
		 
			"click #saveFormBtn" : "onClickSaveFormBtn",
			"click #backBtn" : "onClickBackBtn"
			 
	},
	onClickSaveFormBtn: function(e) {
		this.model.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.model.set('id', response.data);
				alert("บันทึกข้อมูลแล้ว");
		},this)});
	},
	onClickBackBtn: function(e) {
		appRouter.navigate("", {trigger: true});
	},
	onClickEditPersonBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
    	var person = this.model.get('medicalStaffs').at(index);
    	
    	this.personModalView.setCurrentOrganizationNetwork(this.model);
    	this.personModalView.setCurrentPersonAndRender(person);
   	},
	onClickRemovePersonBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.model.get('medicalStaffs').at(index);
		
		var r = confirm('คุณต้องการลบรายการบุคลากรทางการแพทย์ ' + item.get('name'));
		if (r == true) {
			this.model.get('medicalStaffs').remove(item);
			this.renderPersonTbl();
		} else {
		    return false;
		} 
		
		return false;
	},
	onClickNewPersonBtn: function(e) {
		this.personModalView.setCurrentOrganizationNetwork(this.model);
		this.personModalView.newPersonAndRender();
	},
	onChangeTxtSlt : function(e) {
		var value = $(e.currentTarget).val();
		var field=$(e.currentTarget).attr('data-field'); 
		this.model.set(field, value);
	},
	
	onChangeFormSlt: function(e) {
    	//get healthZone val
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 
    	
    	var model;
    	
    	if(field == 'zone') {
    		model = smt.Model.HealthZone.findOrCreate({id:id});
    		
    		//update provinceSlt
        	this.provinces.fetch({
        		url: appUrl('Province/findAllByZone/'+id),
        		success: _.bind(function(collection, response, options) {
        			var json = this.provinces.toJSON();
        			
        			this.$el.find('#provinceSlt').html(this.provinceSltTemplate(json));
        			
        			//clear 
        			this.model.set('province', null);
        			
        		}, this)
        	})
    		
    	} else if(field == 'province') {
    		model = smt.Model.Province.findOrCreate({id:id});
    		
    		//update amphurSlt
        	this.amphurs.fetch({
        		url: appUrl('Province/'+id +'/Amphur'),
        		success: _.bind(function(collection, response, options) {
        			var json = this.amphurs.toJSON();
        			
        			this.$el.find('#amphurSlt').html(this.amphurSltTemplate(json));
        			
        			//clear 
        			this.model.set('amphur', null);
        			
        		}, this)
        	})
    		
    	} else if(field == 'networkType') {
    		model = smt.Model.DV_NetworkType.findOrCreate({id:id});
    	} else if(field == 'orgType') {
    		model = smt.Model.DV_OrgType.findOrCreate({id:id});
    	} else if(field == 'amphur'){
    		model = smt.Model.Amphur.findOrCreate({id: id});
    	} else {
    		return;
    	}
    	this.model.set(field, model);
    	
    },
	
	newForm: function() {
		this.modelId = null;
		this.model = new smt.Model.OrganizationNetwork();
		
		this.render();
	},
	
	
	editForm: function(id) {
		this.modelId=id;
	},
	renderPersonTbl: function() {
		var json = this.model.get('medicalStaffs').toJSON();
		var html = this.medicalStaffsTbodyTemplate(json);
		
		this.$el.find('#medicalStaffsTbody').html(html);
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		if(this.modelId == null) {
			json.networkTypes=new Array();
			json.networkTypes.push({id:0,description: 'กรุณาเลือกประเภทเครือข่าย'});
			$.merge(json.networkTypes, networkTypes.toJSON());
			
			json.orgTypes=new Array();
			json.orgTypes.push({id:0,description: 'กรุณาเลือกประเภทหน่วยงาน'});
			$.merge(json.orgTypes, orgTypes.toJSON());
			
			json.healthZones=new Array();
			json.healthZones.push({id:0,name: 'กรุณาเลือกเขตบริการสุขภาพ'});
			$.merge(json.healthZones, healthZones.toJSON());
			
			json.provinces=new Array();
			json.provinces.push({id:0,name: 'กรุณาเลือกจังหวัด'});
			
			json.amphurs=new Array();
			json.amphurs.push({id:0,name: 'กรุณาเลือกอำเภอ'});
		}
		
		
		this.$el.html(this.formViewTemplate(json));
		
		this.renderPersonTbl();
		return this;
	}
});

var PersonModalView = Backbone.View.extend({
	initialize: function(options){
		if(options != null) {
			if(options.parentView != null) {
				this.parentView = options.parentView;
			}
		}
		this.currentOrganizationNetwork = null;
		this.currentPerson = null;
		
		this.personModalBodyTemplate = Handlebars.compile($("#personModalBodyTemplate").html());
	},events : {
		"click #personModalCloseBtn" : "onClickCloseBtn",
		 "click #personModalSaveBtn" : "onClickSaveBtn",
		"change .txtForm" : "onChangeTxtForm"
	},
	onClickSaveBtn: function(e) {
		// validate input here...
		
		 // do save
		 this.currentOrganizationNetwork.get('medicalStaffs').add(this.currentPerson);
		 this.currentPerson.set('organizationNetwork', this.currentOrganizationNetwork);
		 
		 this.parentView.renderPersonTbl();
		 this.$el.modal('hide');
	 },
	onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	 onChangeTxtForm: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		this.currentPerson.set(field, value);
	},
	render : function() {
		var json=this.currentPerson.toJSON(); 
		
		this.$el.find('.modal-body').html(this.personModalBodyTemplate(json));
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	},
	setCurrentOrganizationNetwork: function(org) {
		this.currentOrganizationNetwork = org;
	},
	setCurrentPersonAndRender: function(person){
		this.currentPerson = person;
		this.$el.find('.modal-header span').html("แก้ไขรายการข้อมูลบุคลากรทางแพทย์");
		this.render();
		
		return this;
	},
	newPersonAndRender : function() {
		this.currentPerson = new smt.Model.OrganizationPerson();
		this.$el.find('.modal-header span').html("เพิ่มรายการข้อมูลบุคลากรทางแพทย์");
		this.render();
		
		return this;
	}
});
