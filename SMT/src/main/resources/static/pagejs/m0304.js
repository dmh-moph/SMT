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
		 this.situations = options.situations;
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
    	this.searchModel = new smt.Model.Behavior();
    	this.render();
    }, 
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();
    	
    	json.situations = this.situations.toJSON();
    	__setSelect(json.situations, this.searchModel.get('situationType'));
    	
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

    	"click .btnPageNav" : "onClickBtnPageNav",
    	"change #pageNavTxt" : "onChangePageNavTxt"
	},
	onChangePageNavTxt : function(e) {

		var newValue = $(e.currentTarget).val();
		var oldValue = $(e.currentTarget).attr('data-value');
		
		if(parseInt(newValue) > 0 && parseInt(newValue) <= this.searchResults.page.totalPages ) {
			
			$(e.currentTarget).attr('data-value', newValue);
			this.renderWithPage(parseInt(newValue));
			
		} else {
			alert('กรุณาระบุหน้าระหว่างเลข 1 ถึง '+ this.searchResults.page.totalPages);
			$(e.currentTarget).val(oldValue);
		}
		
		return false;
	},
    onClickBtnPageNav: function(e) {
    	var pageNum = $(e.currentTarget).attr('data-targetPage');
    	
    	if(pageNum > 0) {
    		this.renderWithPage(pageNum);
    	}
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
		if(this.searchModel != null) {
		
			this.searchModel.set('type', behaviorType);
			
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
		 this.amphurSltTemplate = Handlebars.compile($("#amphurSltTemplate").html());
		 this.impactsTbodyTemplate = Handlebars.compile($("#impactsTbodyTemplate").html());
		 
		
		 // the three must have option!
		 this.situations = options.situations;
		 this.educationLevels = options.educationLevels;
		 this.heathZones = options.healthZones;
		
		 
		 this.provinces = new smt.Collection.Provinces();
		 this.amphurs = new smt.Collection.Amphurs();
		 
		 this.impactModalView = new ImpactModalView({el : '#impactModal', parentView: this});
		 
		 this.trFilesTemplate = Handlebars.compile($("#trFilesTemplate").html());
		 
		 Handlebars.registerPartial("trFilesTemplate", $("#trFilesTemplate").html());
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		 "click #newImpactBtn" : "onClickNewImpactBtn",
		 "click .removeImpactBtn" : "onClickRemoveImpactBtn",
		 "click .editImpactBtn" : "onClickEditImpactBtn",
		 
		"click #saveFormBtn" : "onClickSaveFormBtn",
		"click #backBtn" : "onClickBackBtn",
		
		"click .fileDeleteLnk" : "onClickFileDeleteLnk"
			 
	},
	onClickFileDeleteLnk: function(e){
		var fileId = $(e.currentTarget).attr('data-id');
		var file = smt.Model.FileMeta.findOrCreate(fileId);
		
		var r = confirm("คุณต้องการลบไฟล์ " + file.get('fileName'));
		if (r == true) {
		    
			file.destroy({
				success: _.bind(function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					
					this.model.get('files').remove(file);
					
					var json= {};
					json.model = this.model.toJSON();
					
					$('#filesTbl tbody').empty();
					$('#filesTbl tbody').html(this.trFilesTemplate(json));
					
				},this)
			});
			
		} 
		
		return false;
	},	
	onClickSaveFormBtn: function(e) {
		var validated = true;
		
		// we'll validate 
		this.$el.find('.formTxt').each(function(index, el){
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
		
		// now set behaviorType
		this.model.set('type', behaviorType);
		
		this.model.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.model.set('id', response.data);
				this.model.set('domainName', 'BEHAVIOR');
				alert("บันทึกข้อมูลแล้ว");
				this.render();
		},this)});
	},
	onClickBackBtn: function(e) {
		appRouter.navigate("search", {trigger: true});
	},
	onClickEditImpactBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
    	var impact = this.model.get('impacts').at(index);
    	
    	this.impactModalView.setCurrentBehavior(this.model);
    	this.impactModalView.setCurrentImpactAndRender(impact);
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
    		
    	} else if(field == 'situation') {
    		model = smt.Model.Situation.findOrCreate({id:id});
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
		this.model.fetch({
			success: _.bind(function() {
				var zoneId=this.model.get('zone').get('id');
				var provinceId = this.model.get('province').get('id');
				

				$.when(this.model.fetch,
						this.provinces.fetch({url: appUrl('Province/findAllByZone/'+zoneId)}),
						this.amphurs.fetch({url: appUrl('Province/'+provinceId +'/Amphur')}))
						.done(_.bind(function(x) {
					
							this.render();		
						},this));
				
			},this)
		})
		
		
		
		
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
			json.situations=new Array();
			json.situations.push({id:0,name: 'กรุณาเลือกประเภทสถานการณ์'});
			$.merge(json.situations, situations.toJSON());
			
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
			json.situations=new Array();
			$.merge(json.situations, situations.toJSON());
			 __setSelect(json.situations, this.model.get('situation'));
			
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
		
		if(behaviorType == 'B') {
			json.nameTxt = "ชื่อพฤติกรรมปัญหา"; 
			json.causeTxt = "สาเหตุของปัญหา";
			json.descTxt = "รายละเอียดของปัญหา";
		} else if(behaviorType == 'R') {
			json.nameTxt = "ชื่อพฤติกรรมเสี่ยง";
			json.causeTxt = "สาเหตุของพฤติกรรมเสี่ยง";
			json.descTxt = "รายละเอียดของพฤติกรรมเสี่ยง";
			json.symptomTxt = "ลักษณะอาการที่เกิดพฤติกรรมเสี่ยง"
		}
		
		
		this.$el.html(this.formViewTemplate(json));
		
		this.renderImpactTbl();
		
		$('#fileupload').fileupload({
	        dataType: 'json',
	 
	        done: _.bind(function (e, data) {
	            $.each(data.result, _.bind(function (index, file) {
	            	var file = new smt.Model.FileMeta(file);
	            	this.model.get('files').add(file);
	            	var json = {};
	            	json.model = this.model.toJSON();
	            	$('#filesTbl tbody').empty();
	            	$('#filesTbl tbody').html(this.trFilesTemplate(json));
	            },this)); 
	        },this),	 
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#progress .bar').css(
	                'width',
	                progress + '%'
	            );
	        }
		});
		
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
