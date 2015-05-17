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
        "newReport" : "newForm",
        "search" : "search",
        "PsychoSocialReport/:id" : "editForm",
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
	/**
	 * @memberOf Searchview
	 */
    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
    	this.searchModel = new smt.Model.PsychoSocialReport();
    	this.healthZoneModel = new smt.Model.HealthZone();
    	this.provinceModel = new smt.Model.Province();
    	this.organizationModel = new smt.Model.OrganizationNetwork();
    	this.dummyOrg = new smt.Model.OrganizationNetwork({id: 0});
    	
    	this.organizationSltTemplate = Handlebars.compile($("#organizationSltTemplate").html());
     	
    	this.heathZones = options.healthZones;
    	this.provinces = new smt.Collection.Provinces();
    	this.organizations = new smt.Collection.OrganizationNetworks();
    	
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
    	console.log(JSON.stringify(this.searchModel.toJSON()));
    	this.setupSearchModel();
    	
    	$.fileDownload('/smt/Report/m08Report',
    	{
    		data: {
    			beginDate: this.searchModel.get('beginReportDate'),
    			endDate: this.searchModel.get('endReportDate'),
    			orgId: this.searchModel.get('organization').get('id'),
    			provinceId :  this.searchModel.get('organization').get('province').get('id'),
    			zoneId:  this.searchModel.get('organization').get('zone').get('id')
    		},
    	
    		successCallback: function (url) {
    			 
    	        alert('You just got a file download dialog or ribbon for this URL :' + url);
    	    },
    	    failCallback: function (html, url) {
    	 
    	        alert('Your file download just failed for this URL:' + url + '\r\n' +
    	                'Here was the resulting error HTML: \r\n' + html
    	                );
    	    },
    		httpMethod: 'POST'
    	});
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
    setupSearchModel : function() {
    	if(this.organizationModel.get('id') != null && this.organizationModel.get('id') != 0) {
    		this.organizationModel.set('lastUpdateBy', null);
    		this.organizationModel.set('createBy', null);
    		this.searchModel.set('organization', this.organizationModel);
    	} else if(this.provinceModel.get('id') != null && this.provinceModel.get('id') != 0) {
    		this.searchModel.set('organization', this.dummyOrg);
    		this.searchModel.get('organization').set('province', this.provinceModel);
    	} else if(this.healthZoneModel.get('id') != null && this.healthZoneModel.get('id') != 0) {
     		this.searchModel.set('organization', this.dummyOrg);
     		this.searchModel.get('organization').set('zone', this.healthZoneModel);
     	}    
    },
    
    onClickSearchBtn:function(e) {
    	this.setupSearchModel();
    	appRouter.searchWithModelAndPage(this.searchModel, 1);
    },
    onClicknewFormBtn : function(e) {
    	// we can simply navigate to newForm
    	appRouter.navigate("newReport", {trigger: true});
    	
    },
    onChangeFormSlt: function(e) {
    	//get healthZone val
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 
    	
    	var model;
    	
    	if(field == 'zone') {
    		model = smt.Model.HealthZone.findOrCreate({id:id});
    		this.healthZoneModel = model;
    		
    		//update provinceSlt
        	this.provinces.fetch({
        		url: appUrl('Province/findAllByZone/'+id),
        		success: _.bind(function(collection, response, options) {
        			var json = this.provinces.toJSON();
        			
        			this.$el.find('#provinceSlt').html(this.provinceSltTemplate(json));
        			
        			//clear 
        			this.searchModel.set('organization', null);
        			
        		}, this)
        	})
    		
    	} else if(field == 'province') {
    		model = smt.Model.Province.findOrCreate({id:id});
    		this.provinceModel = model;
    		
    		//update amphurSlt
        	this.organizations.fetch({
        		url: appUrl('Province/'+id +'/Organization'),
        		success: _.bind(function(collection, response, options) {
        			
        			var json = this.organizations.toJSON();
        			console.log(json);
        			
        			this.$el.find('#organizationSlt').html(this.organizationSltTemplate(json));
        			
        			//clear 
        			this.searchModel.set('organization', null);
        			
        		}, this)
        	})
    		
    	} else if (field == 'organization') {
    		model = smt.Model.OrganizationNetwork.findOrCreate({id:id});
    		this.organizationModel=model;
    		
    		
    	}
    		
    	
    },
    resetForm: function() {
    	this.searchModel = new smt.Model.PsychoSocialReport();
    	this.healthZoneModel = new smt.Model.HealthZone();
    	this.provinceModel = new smt.Model.Province();
    	this.organizationModel = new smt.Model.OrganizationNetwork();
    	
    	this.render();
    }, 
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();
    	
    	json.healthZones = this.heathZones.toJSON();
    	__setSelect(json.healthZones, this.searchModel.get('zone'));
    	
    	json.provinces = this.provinces.toJSON();
    	__setSelect(json.provinces, this.searchModel.get('province'));
    	
    	
    	json.organizations = this.organizations.toJSON();
    	__setSelect(json.organizations, this.searchModel.get('organization'));
    	
    	console.log(json);
    	
    	this.$el.html(this.searchViewTemplate(json));
    	$('#beginReportDateTxt').datepicker({
			format: 'dd/mm/yyyy',
			todayBtn: 'linked',
			autoclose : true,
			language: "th",
			orientation: "top left"
		});
		$('#endReportDateTxt').datepicker({
			format: 'dd/mm/yyyy',
			todayBtn: 'linked',
			autoclose : true,
			language: "th",
			orientation: "top left"
		});
    	
    	return this;
	}	

});

var TableResultView = Backbone.View.extend({ 
	/**
	 * @memberOf TableResultView
	 */
	initialize: function(options){
		this.searchResults = new smt.Page.PsychoSocialReports();
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	events: {
		"click .editPsychoSocialReportBtn" : "onClickEditPsychoSocialReportBtn",
		"click .removePsychoSocialReportBtn" : "onClickremovePsychoSocialReportBtn",
	},
	
	onClickremovePsychoSocialReportBtn: function(e) {
		var reportId = $(e.currentTarget).parents('tr').attr("data-id");
		
		var psychoSocialReport = smt.Model.PsychoSocialReport.findOrCreate({id: reportId});
		
		//var r = confirm('คุณต้องการลบรายการ: ' + psychoSocialReport.get('organization').get('orgName'));
		var r= confirm('Do you want to delete');
		if (r == true) {
			psychoSocialReport.destroy({
				success: function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					appRouter.search();
				}
			});
			
			
		} else {
		    return false;
		} 
		
		
	},
	
	onClickEditPsychoSocialReportBtn: function(e) {
		var reportId = $(e.currentTarget).parents('tr').attr("data-id");
		appRouter.navigate("PsychoSocialReport/"+reportId, {trigger: true});
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
				url: appUrl('PsychoSocialReport/search/page/' + this.pageNum),
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
	/**
	 * @memberOf FormView
	 */
	initialize: function(options){
		 this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
		 this.provinceSltTemplate = Handlebars.compile($("#provinceSltTemplate").html());
		 this.organizationSltTemplate = Handlebars.compile($("#organizationSltTemplate").html());
		 
		
		 // the three must have option!
		 this.heathZones = options.healthZones;
		 
		 this.provinces = new smt.Collection.Provinces();
		 this.organizations = new smt.Collection.OrganizationNetworks();
		 
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		"click #saveFormBtn" : "onClickSaveFormBtn",
		"click #backBtn" : "onClickBackBtn"
			 
	},
	onClickSaveFormBtn: function(e) {
		var validated = true;
		
		// we'll validate 
		this.$el.find('.formTxt').each(function(index, el){
			var id = $(el)
			
			if( ($(el).val() == null ||  $(el).val().length == 0) && 
					$(el).attr('required') != null)	{
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
        	this.organizations.fetch({
        		url: appUrl('Province/'+id +'/Organization'),
        		success: _.bind(function(collection, response, options) {
        			
        			var json = this.organizations.toJSON();
        			console.log(json);
        			
        			this.$el.find('#organizationSlt').html(this.organizationSltTemplate(json));
        			
        			//clear 
        			this.model.set('organization', null);
        			
        		}, this)
        	})
    		
    	} else if(field == 'organization' && id > 0) {
    		model = smt.Model.OrganizationNetwork.findOrCreate({id:id});
    		this.model.set(field, model);
    	}
    	
    	return false;
    },
	
	newForm: function() {
		this.modelId = null;
		this.model = new smt.Model.PsychoSocialReport();
		
		this.render();
	},
	
	
	editForm: function(id) {
		this.model = smt.Model.PsychoSocialReport.findOrCreate({id: id});
		this.model.fetch({
			success: _.bind(function() {
				this.render();
			},this)
		});
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		if(this.model.get('id') == null) {
			json.healthZones=new Array();
			json.healthZones.push({id:0,name: 'กรุณาเลือกเขตบริการสุขภาพ'});
			$.merge(json.healthZones, healthZones.toJSON());
			
			json.provinces=new Array();
			json.provinces.push({id:0,name: 'กรุณาเลือกจังหวัด'});
			
			json.organizations=new Array();
			json.organizations.push({id:0,name: 'กรุณาเลือกหน่วยงาน'});
			json.newForm = true;
		} else {
			json.newForm = false;
		}
		
		console.log(json);
		
		this.$el.html(this.formViewTemplate(json));
		$('#beginReportDateTxt').datepicker({
			format: 'dd/mm/yyyy',
			todayBtn: 'linked',
			autoclose : true,
			language: "th",
			orientation: "top left"
		});
		$('#endReportDateTxt').datepicker({
			format: 'dd/mm/yyyy',
			todayBtn: 'linked',
			autoclose : true,
			language: "th",
			orientation: "top left"
		});
		
		return this;
	}
});

