/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newFormBreadCrumb = Handlebars.compile($("#newFormBreadCrumbTemplate").html());
		this.editFormBreadCrumb = Handlebars.compile($("#editFormBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
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
        "newOrgnazationNetwork" : "newForm",
        "search" : "search",
        "OrganizationNetwork/:id" : "editForm",
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
    searchWithModelAndPage: function(model, pageNum) {
    	// now table result
    	this.tableResultView.renderWithSearchModel(model, pageNum);
    },
    search: function() {
    	// set breadcrumb
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());

    	// show search
    	this.searchView.render();
    	// no form
    	this.formView.$el.empty();
    	// no table result
    	this.tableResultView.render();
    	
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
    	"change .formTxt" : "onChangeFormTxt",
    	
    	"click #newFormBtn" : "onClicknewFormBtn",
    	"click #searchBtn" : "onClickSearchBtn",
    	"click #clearFormBtn" : "onClickClearFormBtn",
    	"submit #searchForm" : "onSubmitSearchForm"
    		
    },
    onClickClearFormBtn: function(e) {
    	this.searchModel = new smt.Model.OrganizationNetwork();
    	
    	appRouter.search();
    },
    onSubmitSearchForm: function(e) {
    	this.onClickSearchBtn(e);
    	return false;
    },
    onChangeFormTxt: function(e) {
    	var value = $(e.currentTarget).val();
		var field=$(e.currentTarget).attr('data-field'); 
		this.searchModel.set(field, value);
    },
    onClickSearchBtn:function(e) {
    	appRouter.searchWithModelAndPage(this.searchModel, 1);
    },
    onClicknewFormBtn : function(e) {
    	// we can simply navigate to newForm
    	appRouter.navigate("newOrgnazationNetwork", {trigger: true});
    	
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
    	if(id == 0) {
    		this.searchModel.set(field, null);
    	} else {
    		this.searchModel.set(field, model);
    	}
    	
    },
    resetForm: function() {
    	this.searchModel = new smt.Model.OrganizationNewtork();
    	this.render();
    }, 
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();
    	
    	json.networkTypes = this.networkTypes.toJSON();
    	__setSelect(json.networkTypes, this.searchModel.get('networkType'));
    	
    	json.orgTypes = this.orgTypes.toJSON();
    	__setSelect(json.orgTypes, this.searchModel.get('orgType'));
    	
    	json.healthZones = this.heathZones.toJSON();
    	__setSelect(json.healthZones, this.searchModel.get('zone'));
    	
    	json.provinces = this.provinces.toJSON();
    	__setSelect(json.provinces, this.searchModel.get('province'));
    	
    	console.log(json);
    	
    	this.$el.html(this.searchViewTemplate(json));
    	
    	return this;
	}	

});

var TableResultView = Backbone.View.extend({ 
	initialize: function(options){
		this.searchResults = new smt.Page.OrganizationNetworks();
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	events: {
		"click .editOrganizationNetworkBtn" : "onClickEditOrganizationNetworkBtn",
		"click .removeOrganizationNetworkBtn" : "onClickremoveOrganizationNetworkBtn",
	},
	
	onClickremoveOrganizationNetworkBtn: function(e) {
		var organizationNetworkId = $(e.currentTarget).parents('tr').attr("data-id");
		
		var orgNetwork = smt.Model.OrganizationNetwork.findOrCreate({id: organizationNetworkId});
		
		var r = confirm('คุณต้องการลบรายการ' + orgNetwork.get('orgName'));
		if (r == true) {
			orgNetwork.destroy({
				success: function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					appRouter.search();
				}
			});
			
			
		} else {
		    return false;
		} 
		
		
	},
	
	onClickEditOrganizationNetworkBtn: function(e) {
		var organizationNetworkId = $(e.currentTarget).parents('tr').attr("data-id");
		appRouter.navigate("OrganizationNetwork/"+organizationNetworkId, {trigger: true});
	},
	
	renderWithSearchModel: function(searchModel, pageNum) {
		this.searchModel = searchModel;
		this.pageNum = pageNum;
		this.render();
	},
	
	renderWithPage: function(pageNum) {
		this.pageNum = pageNum;
		this.render();
	},
	
	render: function() {
		if(this.searchModel != null) {
			this.searchResults.fetch({
				url: appUrl('OrganizationNetwork/search/page/' + this.pageNum),
	    		type: 'POST',
	    		data: JSON.stringify(this.searchModel.toJSON()),
	    		dataType: 'json',
	    		contentType: 'application/json',
	    		success: _.bind(function(collection, response, options) {
	    			
					var json = {};
					json.page = this.searchResults.page;
					json.content = this.searchResults.toJSON();
					this.$el.html(this.tableResultViewTemplate(json));
	    		}, this)
	    	});
		}
		return this;
	}
});

var FormView = Backbone.View.extend({
	 initialize: function(options){
		 this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
		 this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
		 this.amphurSltTemplate = Handlebars.compile($("#amphurSltTemplate").html());
		 this.medicalStaffsTbodyTemplate = Handlebars.compile($("#medicalStaffsTbodyTemplate").html());
		 
		
		 // the three must have option!
		 this.networkTypes = options.networkTypes;
		 this.orgTypes = options.orgTypes;
		 this.heathZones = options.healthZones;
		 this.personTypes = options.personTypes;
		 
		 this.provinces = new smt.Collection.Provinces();
		 this.amphurs = new smt.Collection.Amphurs();
		 
		 this.personModalView = new PersonModalView({el : '#personModal', parentView: this, personTypes : this.personTypes});
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
		var validated = true;
		
		// we'll validate 
		this.$el.find('.formTxt').each(function(index, el){
			if($(el).val() == null ||  $(el).val().length == 0) {
				$(el).parents('.form-group').addClass('has-error has-feedback');
				$(el).parent().after('<span class="fa fa-question-circle form-control-feedback"></span>');
				
				validated = false;
				
			}
		});
		
		this.$el.find('.formSlt').each(function(index, el){
			if($(el).val() == 0) {
				$(el).parents('.form-group').addClass('has-error');
				
				validated = false;
				
			}
		});
		
		
		if(!validated) {
			alert ('กรุณากรอกข้อมูลให้ครบถ้วน');
			return false;
		}
		
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
		appRouter.navigate("search", {trigger: true});
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
		
		if(value != null && value.length > 0) {
			// reset error
	    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
	    	$(e.currentTarget).parents('.form-group').find('.form-control-feedback').remove()
		}
		
		var field=$(e.currentTarget).attr('data-field'); 
		this.model.set(field, value);
	},
	
	onChangeFormSlt: function(e) {
    	//get healthZone val
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field');
    	
    	// reset error
    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
    	
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
		this.model = smt.Model.OrganizationNetwork.findOrCreate({id: id});
		var zoneId=this.model.get('zone').get('id');
		var provinceId = this.model.get('province').get('id');
		$.when(this.provinces.fetch({url: appUrl('Province/findAllByZone/'+zoneId)}),
				this.amphurs.fetch({url: appUrl('Province/'+provinceId +'/Amphur')}))
				.done(_.bind(function(x) {
			this.render();	
		},this));
		
	},
	renderPersonTbl: function() {
		var json = this.model.get('medicalStaffs').toJSON();
		var html = this.medicalStaffsTbodyTemplate(json);
		
		this.$el.find('#medicalStaffsTbody').html(html);
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		if(this.model.get('id') == null) {
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
		} else {
			json.networkTypes=new Array();
			$.merge(json.networkTypes, networkTypes.toJSON());
			 __setSelect(json.networkTypes, this.model.get('networkType'));
			
			json.orgTypes=new Array();
			$.merge(json.orgTypes, orgTypes.toJSON());
			__setSelect(json.orgTypes, this.model.get('orgType'));
			
			json.healthZones=new Array();
			$.merge(json.healthZones, healthZones.toJSON());
			__setSelect(json.healthZones, this.model.get('zone'));
			
			json.provinces=new Array();
			$.merge(json.provinces, this.provinces.toJSON());
			__setSelect(json.provinces, this.model.get('province'));
			
			json.amphurs=new Array();
			$.merge(json.amphurs, this.amphurs.toJSON());
			__setSelect(json.amphurs, this.model.get('amphur'));
		}
		
		console.log(json);
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
			
			if(options.personTypes != null) {
				
				this.personTypes = options.personTypes;
				
			}
		}
		this.currentOrganizationNetwork = null;
		this.currentPerson = null;
		
		this.personModalBodyTemplate = Handlebars.compile($("#personModalBodyTemplate").html());
	},events : {
		"click #personModalCloseBtn" : "onClickCloseBtn",
		 "click #personModalSaveBtn" : "onClickSaveBtn",
		"change .formTxt" : "onChangeFormTxt",
		"change .formSlt" : "onChangeFormSlt"
	},
	
	onChangeFormSlt: function(e) {
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 
    	
    	// reset error
    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
    	
    	var model;
    	
    	if(field == 'type') {
    		model = smt.Model.DV_PersonType.findOrCreate({id:id});
    	}
    	this.currentPerson.set(field,model);
    	
	},
	onClickSaveBtn: function(e) {
		// validate input here...
		var validated = true;
		
		// we'll validate 
		this.$el.find('.formTxt').each(function(index, el){
			if($(el).val() == null ||  $(el).val().length == 0) {
				$(el).parents('.form-group').addClass('has-error has-feedback');
				$(el).after('<span class="fa fa-question-circle form-control-feedback"></span>');
				
				validated = false;
				
			}
		});
		
		this.$el.find('.formSlt').each(function(index, el){
			if($(el).val() == 0) {
				$(el).parents('.form-group').addClass('has-error');
				
				validated = false;
				
			}
		});
		
		
		if(!validated) {
			alert ('กรุณากรอกข้อมูลให้ครบถ้วน');
			return false;
		}
		
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
	 onChangeFormTxt: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		if(value != null && value.length > 0) {
			// reset error
	    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
	    	$(e.currentTarget).parents('.form-group').find('.form-control-feedback').remove()
		}
		
		this.currentPerson.set(field, value);
	},
	render : function() {
		var json = {};
		json.model=this.currentPerson.toJSON();
		json.personTypes = new Array();
		if(this.currentPerson.get('id')==null) {
			json.personTypes.push({id:0, description: 'กรุณาเลือกประเภทบุคคลากรทางการแพทย์'});
		}
		$.merge(json.personTypes, this.personTypes.toJSON());
		__setSelect(json.personTypes, this.currentPerson.get('type'));
		
		
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
