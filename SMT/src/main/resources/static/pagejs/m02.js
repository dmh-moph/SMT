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
        "newJournal" : "newForm",
        "search" : "search",
        "Journal/:id" : "editForm",
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
		 this.journalTypes = options.journalTypes;

    	
    	
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
    	appRouter.navigate("newJournal", {trigger: true});
    	
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
    	this.searchModel = new smt.Model.Behavior();
    	this.render();
    }, 
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();
    	
    	json.journalTypes = this.journalTypes.toJSON();
    	__setSelect(json.journalTypes, this.searchModel.get('journalTypes'));
    	
    	console.log(json);
    	
    	this.$el.html(this.searchViewTemplate(json));
    	
    	return this;
	}	

});

var TableResultView = Backbone.View.extend({ 
	initialize: function(options){
		this.searchResults = new smt.Page.Journals();
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	events: {
		"click .editJournalBtn" : "onClickEditJournalBtn",
		"click .removeJournalBtn" : "onClickRemoveJournalBtn",
	},
	
	onClickRemoveJournalBtn: function(e) {
		var journalId = $(e.currentTarget).parents('tr').attr("data-id");
		var journal = smt.Model.Journal.findOrCreate({id: journalId});
		
		var r = confirm('คุณต้องการลบรายการ' + journal.get('nameTh'));
		if (r == true) {
			journal.destroy({
				success: function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					appRouter.search();
				}
			});
			
			
		} else {
		    return false;
		} 
		
		
	},
	
	onClickEditJournalBtn: function(e) {
		var journalId = $(e.currentTarget).parents('tr').attr("data-id");
		appRouter.navigate("Journal/"+journalId, {trigger: true});
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
				url: appUrl('Journal/search/page/' + this.pageNum),
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
		
		 // the three must have option!
		 this.journalTypes = options.journalTypes;
		 
		 this.organizationModalView = new OrganizationModalView({el : '#organizationModal', parentView: this});
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		 "click #newOrgBtn" : "onClickNewOrgBtn",
		 
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
		
		if(this.model.get('organization') == null) {
			alert ('กรุณาเลือกหน่วยงาน');
			return false;
		}
		
		
		if(!validated) {
			alert ('กรุณากรอกข้อมูลให้ครบถ้วน');
			return false;
		}
		
		// now set behaviorType
		this.model.set('type', behaviorType);
		
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
	onClickNewOrgBtn: function(e) {
		this.organizationModalView.render();
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
    	
    	 if(field == 'journalType') {
    		model = smt.Model.DV_JournalType.findOrCreate({id:id});
    	} else {
    		return;
    	}
    	this.model.set(field, model);
    	
    },
	
	newForm: function() {
		this.modelId = null;
		this.model = new smt.Model.Journal();
		
		this.render();
	},
	
	
	editForm: function(id) {
		this.model = smt.Model.Journal.findOrCreate({id: id});
		this.render();	
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		if(this.model.get('id') == null) {
			json.journalTypes=new Array();
			json.journalTypes.push({id:0,description: 'กรุณาเลือกประเภทผลงานทางวิชาการ'});
			$.merge(json.journalTypes, journalTypes.toJSON());
			
			
			
		} else {
			json.journalTypes=new Array();
			$.merge(json.journalTypes, journalTypes.toJSON());
			 __setSelect(json.journalTypes, this.model.get('journalTypes'));
			
		}
		
		console.log(json);
		this.$el.html(this.formViewTemplate(json));
		
		return this;
	}
});

var OrganizationModalView = Backbone.View.extend({
	initialize: function(options){
		if(options != null) {
			if(options.parentView != null) {
				this.parentView = options.parentView;
			}
			
		}
		this.searchResults = new smt.Page.OrganizationNetworks();
		this.searchModel = new smt.Model.OrganizationNetwork();
		this.organizationModalBodyTemplate = Handlebars.compile($("#organizationModalBodyTemplate").html());
		this.organizationTableResultTemplate = Handlebars.compile($("#organizationTableResultTemplate").html());
		this.pageNum = 1;
	},events : {
		"click #impactModalCloseBtn" : "onClickCloseBtn",
		"click #searchBtn" : "onClickSeachBtn",
		"click .chooseOrganizationNetworkBtn" : "onClickchooseOrganizationNetworkBtn"
	},
	onClickchooseOrganizationNetworkBtn: function(e) {
		var organizationNetworkId = $(e.currentTarget).parents('tr').attr("data-id");
		
		var orgNetwork = smt.Model.OrganizationNetwork.findOrCreate({id: organizationNetworkId});
		
		this.parentView.model.set('organization', orgNetwork);
		this.parentView.render();
		this.$el.modal('hide');
		return false;
	},
	onClickSeachBtn: function() {
		this.searchModel.set('name', this.$el.find('#orgNameTxt').val());
		
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
				this.$el.find('#organizationTableResultDiv').html(this.organizationTableResultTemplate(json));
    		}, this)
    	});
	},
	onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	render : function() {
		this.$el.find('.modal-header span').html("เพิ่มรายการข้อมูลผลกระทบ");
		this.$el.find('.modal-body').html(this.organizationModalBodyTemplate());
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	}
});