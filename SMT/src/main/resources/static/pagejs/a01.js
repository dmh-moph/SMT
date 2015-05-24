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
        "newUser" : "newForm",
        "search" : "search",
        "User/:id" : "editForm",
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

    	this.searchModel = new smt.Model.SecurityUser();
    	
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
    	this.searchModel = new smt.Model.SecurityUser();
    	
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
    	appRouter.navigate("newUser", {trigger: true});
    	
    },
    resetForm: function() {
    	this.searchModel = new smt.Model.SecurityUser();
    	this.render();
    }, 
    
    render: function() {
    	var json = {};
    	json.searchModel = this.searchModel.toJSON();

    	this.$el.html(this.searchViewTemplate(json));
    	
    	return this;
	}	

});

var TableResultView = Backbone.View.extend({ 
	initialize: function(options){
		this.searchResults = new smt.Page.SecurityUsers();
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	events: {
		"click .editBtn" : "onClickEditBtn",
		"click .removeBtn" : "onClickRemoveBtn",

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
	
	onClickRemoveBtn: function(e) {
		var userId = $(e.currentTarget).parents('tr').attr("data-id");
		var user = smt.Model.SecurityUser.findOrCreate({id: userId});
		
		var r = confirm('คุณต้องการลบรายการ' + user.get('login'));
		if (r == true) {
			user.destroy({
				success: function(model, response) {
					alert("ลบข้อมูลเรียบร้อยแล้ว")
					appRouter.search();
				}
			});
			
			
		} else {
		    return false;
		} 
		
		
	},
	
	onClickEditBtn: function(e) {
		var userId = $(e.currentTarget).parents('tr').attr("data-id");
		appRouter.navigate("User/"+userId, {trigger: true});
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
				url: appUrl('SecurityUser/search/page/' + this.pageNum),
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
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 
		"click #saveFormBtn" : "onClickSaveFormBtn",
		"click #backBtn" : "onClickBackBtn",
		
			 
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
	
	
	newForm: function() {
		this.modelId = null;
		this.model = new smt.Model.SecurityUser();
		
		this.render();
	},
	
	
	editForm: function(id) {
		this.model = smt.Model.SecurityUser.findOrCreate({id: id});
		this.model.fetch({
			success: _.bind(function() {
				this.render();		
			},this)
		});
			
		
	},
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		
		this.$el.html(this.formViewTemplate(json));
		
		
		return this;
	}
});
