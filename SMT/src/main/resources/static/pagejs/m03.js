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
        "newBehavior" : "newForm",
        "search" : "search",
        "Behavior/:id" : "editForm",
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
    	this.heathZones = options.healthZones;
		 this.situationTypes = options.situationTypes;
		 this.educationLevels = options.educationLevels;

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
    	appRouter.navigate("newBehavior", {trigger: true});
    	
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
    	
    	json.situationTypes = this.situationTypes.toJSON();
    	__setSelect(json.situationTypes, this.searchModel.get('situationType'));
    	
    	json.educationLevels = this.educationLevels.toJSON();
    	__setSelect(json.educationLevels, this.searchModel.get('educationLevel'));
    	
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
		this.searchResults = new smt.Page.Behaviors();
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	events: {
		"click .editBehaviorBtn" : "onClickEditBehaviorBtn",
		"click .removeBehaviorBtn" : "onClickRemoveBehaviorBtn",
	},
	
	onClickRemoveBehaviorBtn: function(e) {
		var behaviorId = $(e.currentTarget).parents('tr').attr("data-id");
		var behavior = smt.Model.Behavior.findOrCreate({id: behaviorId});
		
		var r = confirm('คุณต้องการลบรายการ' + behavior.get('name'));
		if (r == true) {
			behavior.destroy({
				success: function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					appRouter.search();
				}
			});
			
			
		} else {
		    return false;
		} 
		
		
	},
	
	onClickEditBehaviorBtn: function(e) {
		var behaviorId = $(e.currentTarget).parents('tr').attr("data-id");
		appRouter.navigate("Behavior/"+behaviorId, {trigger: true});
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
		this.searchResults.fetch({
			url: appUrl('Behavior/search/page/' + this.pageNum),
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
		return this;
	}
});

var FormView = Backbone.View.extend({
	 initialize: function(options){
		 this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
		 this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
		 this.amphurSltTemplate = Handlebars.compile($("#amphurSltTemplate").html());
		 this.impactsTbodyTemplate = Handlebars.compile($("#impactsTbodyTemplate").html());
		 
		
		 // the three must have option!
		 this.situationTypes = options.situationTypes;
		 this.educationLevels = options.educationLevels;
		 this.heathZones = options.healthZones;
		
		 
		 this.provinces = new smt.Collection.Provinces();
		 this.amphurs = new smt.Collection.Amphurs();
		 
		 this.impactModalView = new ImpactModalView({el : '#impactModal', parentView: this});
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		 "click #newImpactBtn" : "onClickNewImpactBtn",
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
    	var impact = this.model.get('impact').at(index);
    	
    	this.impactModalView.setCurrentBehavior(this.model);
    	this.impactModalView.setCurrentImpactAndRender(person);
   	},
	onClickRemoveImpactBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.model.get('impacts').at(index);
		
		var r = confirm('คุณต้องการลบรายการผลกระทบ ' + item.get('description'));
		if (r == true) {
			this.model.get('impacts').remove(item);
			this.renderImpactTbl();
		} else {
		    return false;
		} 
		
		return false;
	},
	onClickNewImpactBtn: function(e) {
		this.impactModalView.setCurrentBehavior(this.model);
		this.impactModalView.newImpactAndRender();
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
    		
    	} else if(field == 'situationType') {
    		model = smt.Model.DV_SituationType.findOrCreate({id:id});
    	} else if(field == 'targetEducationLevel') {
    		model = smt.Model.DV_EducationLevel.findOrCreate({id:id});
    	} else if(field == 'amphur'){
    		model = smt.Model.Amphur.findOrCreate({id: id});
    	} else if(field == 'sex') {
    		model = id;
    	} else {
    		return;
    	}
    	this.model.set(field, model);
    	
    },
	
	newForm: function() {
		this.modelId = null;
		this.model = new smt.Model.Behavior();
		
		this.render();
	},
	
	
	editForm: function(id) {
		this.model = smt.Model.Behavior.findOrCreate({id: id});
		var zoneId=this.model.get('zone').get('id');
		var provinceId = this.model.get('province').get('id');
		$.when(this.provinces.fetch({url: appUrl('Province/findAllByZone/'+zoneId)}),
				this.amphurs.fetch({url: appUrl('Province/'+provinceId +'/Amphur')}))
				.done(_.bind(function(x) {
			this.render();	
		},this));
		
	},
	renderImpactTbl: function() {
		var json;
		if(this.model.get('impacts').length >0) {
			json = this.model.get('impacts').toJSON();
		}
		var html = this.impactsTbodyTemplate(json);
		
		this.$el.find('#impactsTbody').html(html);
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		if(this.model.get('id') == null) {
			json.situationTypes=new Array();
			json.situationTypes.push({id:0,description: 'กรุณาเลือกประเภทสถานการณ์'});
			$.merge(json.situationTypes, situationTypes.toJSON());
			
			json.educationLevels=new Array();
			json.educationLevels.push({id:0,description: 'กรุณาเลือกระดับการศึกษา'});
			$.merge(json.educationLevels, educationLevels.toJSON());
			
			json.healthZones=new Array();
			json.healthZones.push({id:0,name: 'กรุณาเลือกเขตบริการสุขภาพ'});
			$.merge(json.healthZones, healthZones.toJSON());
			
			json.provinces=new Array();
			json.provinces.push({id:0,name: 'กรุณาเลือกจังหวัด'});
			
			json.amphurs=new Array();
			json.amphurs.push({id:0,name: 'กรุณาเลือกอำเภอ'});
			
		} else {
			json.situationTypes=new Array();
			$.merge(json.situationTypes, situationTypes.toJSON());
			 __setSelect(json.situationTypes, this.model.get('situationTypes'));
			
			json.educationLevels=new Array();
			$.merge(json.educationLevels, educationLevels.toJSON());
			__setSelect(json.educationLevels, this.model.get('educationLevels'));
			
			json.healthZones=new Array();
			$.merge(json.healthZones, healthZones.toJSON());
			__setSelect(json.healthZones, this.model.get('zone'));
			
			json.provinces=new Array();
			$.merge(json.provinces, this.provinces.toJSON());
			__setSelect(json.provinces, this.model.get('province'));
			
			json.amphurs=new Array();
			$.merge(json.amphurs, this.amphurs.toJSON());
			__setSelect(json.amphurs, this.model.get('amphur'));
			
			
			
			if(json.model.sex != null && json.model.sex == 'M') {
				json.model.isMale = true;
			} else {
				json.model.isMale = false;
			}
		}
		
		console.log(json);
		this.$el.html(this.formViewTemplate(json));
		
		this.renderImpactTbl();
		return this;
	}
});

var ImpactModalView = Backbone.View.extend({
	initialize: function(options){
		if(options != null) {
			if(options.parentView != null) {
				this.parentView = options.parentView;
			}
			
		}
		this.currentBehavior = null;
		this.currentImpact = null;
		
		this.impactModalBodyTemplate = Handlebars.compile($("#impactModalBodyTemplate").html());
	},events : {
		"click #impactModalCloseBtn" : "onClickCloseBtn",
		 "click #impactModalSaveBtn" : "onClickSaveBtn",
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
		
		if(!validated) {
			alert ('กรุณากรอกข้อมูลให้ครบถ้วน');
			return false;
		}

		 // do save
		 this.currentBehavior.get('impacts').push(this.currentImpact);
		 
		 this.parentView.renderImpactTbl();
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
		
		this.currentImpact.set(field, value);
	},
	render : function() {
		var json = {};
		json.model=this.currentImpact.toJSON();
		
		this.$el.find('.modal-body').html(this.impactModalBodyTemplate(json));
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	},
	setCurrentBehavior: function(behavior) {
		this.currentBehavior = behavior;
	},
	setCurrentImpactAndRender: function(impact){
		this.currentImpact = impact;
		this.$el.find('.modal-header span').html("แก้ไขรายการข้อมูลผลกระทบ");
		this.render();
		
		return this;
	},
	newImpactAndRender : function() {
		this.currentImpact = new smt.Model.BehaviorImpact();
		this.$el.find('.modal-header span').html("เพิ่มรายการข้อมูลผลกระทบ");
		this.render();
		
		return this;
	}
});
