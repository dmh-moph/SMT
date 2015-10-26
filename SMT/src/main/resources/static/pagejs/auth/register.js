/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		
		if(options.formView != null) {
			this.formView = options.formView;	
		} 
		
	},
	routes: {
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	
    	// no form
    	this.formView.render();
    }
    
});



var FormView = Backbone.View.extend({
	/**
	 * @memberOf FormView
	 */
	 initialize: function(options){
		 this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
		 this.positionRowTemplate = Handlebars.compile($("#positionRowTemplate").html());
		 this.successAlertTemplate = Handlebars.compile($("#successAlertTemplate").html());
		 
		 
		 this.model = new smt.Model.Auth.User();
		 this.model.url = rootUrl('Register');
		 
		 this.model.set('info', new smt.Model.Auth.UserInfo());
		 
		 
		 
		 this.objectives = options.objectives;
		 this.occupations = options.occupations;
		 this.positions = options.positions;
		 
		 this.model.get('info').set('position', this.positions.at(0));
		 this.model.get('info').set('objective', this.objectives.at(0));
		 this.model.get('info').set('occupation', this.occupations.at(0));
		 
	 },
	 events: {
		 "change .formSlt": "onChangeFormSlt",
		 "change .formTxt" : "onChangeTxtSlt",
		 "change .formRdo" : "onChangeRdo",
		 
		"click #saveFormBtn" : "onClickSaveFormBtn",
		"click #backBtn" : "onClickBackBtn",
		
	},
	
	
	
	onClickSaveFormBtn: function(e) {
		var validated = true;
		
		// we'll validate 
		
		this.$el.find('.formSlt').each(function(index, el){
			if($(el).val() == 0) {
				$(el).parents('.form-group').addClass('has-error');
				
				validated = false;
				
			}
		});
		
		if(this.$el.find('#usernameTxt').val().trim().length == 0) {
			this.$el.find('#usernameTxt').parents('.form-group').addClass('has-error');
			validated = false;
		}
		
		if(this.$el.find('#info\\.departmentTxt').val().trim().length == 0) {
			this.$el.find('#info\\.departmentTxt').parents('.form-group').addClass('has-error');
			validated = false;
		}
		
		if(this.$el.find('#info\\.emailTxt').val().trim().length == 0) {
			this.$el.find('#info\\.emailTxt').parents('.form-group').addClass('has-error');
			validated = false;
		}
		
		if(this.$el.find('#check\\.password1Txt').val().trim().length == 0) {
			this.$el.find('#check\\.password1Txt').parents('.form-group').addClass('has-error');
			validated = false;
		}
		
		if(this.$el.find('#check\\.password2Txt').val().trim().length == 0) {
			this.$el.find('#check\\.password2Txt').parents('.form-group').addClass('has-error');
			validated = false;
		}
		
		if(this.$el.find('input[name=sex]:checked').val() == null) {
			this.$el.find('#sexFormGroup').addClass('has-error');
		}
		
		if (this.$el.find('#occupationOtherDiv').css('display') != 'none') {
			if(this.$el.find('#info\\.occupationOtherTxt').val().trim().length == 0) {
				this.$el.find('#info\\.occupationOtherTxt').parents('.form-group').addClass('has-error');
				validated = false;
			}
		} else {
			this.model.get('info').set('occupationOther', null);
		}
		
		if (this.$el.find('#positionOtherDiv').css('display') != 'none') {
			if(this.$el.find('#info\\.positionOtherTxt').val().trim().length == 0) {
				this.$el.find('#info\\.positionOtherTxt').parents('.form-group').addClass('has-error');
				validated = false;
			}
		} else {
			this.model.get('info').set('positionOther', null);
		}
		
		if (this.$el.find('#objectiveOtherDiv').css('display') != 'none') {
			if(this.$el.find('#info\\.objectiveOtherTxt').val().trim().length == 0) {
				this.$el.find('#info\\.objectiveOtherTxt').parents('.form-group').addClass('has-error');
				validated = false;
			}
		} else {
			this.model.get('info').set('objectiveOther', null);
		}
		
		
		var passwd1 =this.$el.find('#check\\.password1Txt').val();
		var passwd2 =this.$el.find('#check\\.password2Txt').val();
		
		if(passwd1 != passwd2 ) {
			alert("password is not matched");
			this.$el.find('#check\\.password1Txt').parents('.form-group').addClass('has-error');
			this.$el.find('#check\\.password2Txt').parents('.form-group').addClass('has-error');	
			validated = false;
			return;
		} else {
			this.model.set('password', passwd1);
		}
		
		
		if(!validated) {
			alert ('กรุณากรอกข้อมูลให้ครบถ้วน');
			return false;
		}
		
		
		this.model.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
					var d = new Date();
					$('#captchaImg').attr("src", "/smt/captcha?d="+d.getTime());
					$('#captchaTxt').val(null);
					
					if(response.data == -2) {
						$('#usernameTxt').parents('.form-group').addClass('has-error');
					}
					
				} else {
					this.model.set('id', response.data);
					
					this.$el.find('#alert').html(this.successAlertTemplate());
					$('.panel-footer').empty();
				}
		},this)});
	},
	onClickBackBtn: function(e) {
		appRouter.navigate("search", {trigger: true});
	},
	onChangeRdo:function(e) {
		var value = $(e.currentTarget).val();
		var field=$(e.currentTarget).attr('data-field');
		
		if(field == "info.sex") {
			this.model.get('info').set('sex', value);
			this.$el.find('#sexFormGroup').removeClass('has-error');
		}
	},
	
	onChangeTxtSlt : function(e) {
		var value = $(e.currentTarget).val();
		
		if(value != null && value.length > 0) {
			// reset error
	    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
	    	$(e.currentTarget).parents('.form-group').find('.form-control-feedback').remove()
		}
		
		var field=$(e.currentTarget).attr('data-field');
		if(field.indexOf('info.') == 0) {
			field = field.slice(5);
			this.model.get('info').set(field,value);
			
		} else if(field.indexOf('check.') == 0) {
			
		} else {
			this.model.set(field, value);
		}
		
		
	},
	
	onChangeFormSlt: function(e) {
    	//get healthZone val
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field');
    	
    	// reset error
    	$(e.currentTarget).parents('.form-group').removeClass('has-error');
    	
    	var model;
    	
    	 if(field == 'info.occupation') {
    		model = smt.Model.DV_Occupation.findOrCreate({id:id});
    		this.model.get('info').set('occupation', model);
    		
    		if(model.get('description') == 'อิ่นๆ') {
    			$('#occupationOtherDiv').show();
    		} else {
    			this.$el.find('#info\\.occupationOtherTxt').val(null);
    			$('#occupationOtherDiv').hide();
    		}
    		
    		// now update position;
    		positions.url = appUrl('DomainVariable/OCCUPATION/'+model.get('id')+'/POSITION');
    		
    		
    		$.when(positions.fetch()).done(_.bind(function(y) {
    			this.renderPositionRow(positions);
    			this.model.get('info').set('position', positions.at(0));
    		}, this));
    		
    	} else  if(field == 'info.objective') {
    		model = smt.Model.DV_Objective.findOrCreate({id:id});
    		this.model.get('info').set('objective', model);
    		
    		if(model.get('description') == 'อิ่นๆ') {
    			$('#objectiveOtherDiv').show();
    		} else {
    			this.$el.find('#info\\.objectiveOtherTxt').val(null);
    			$('#objectiveOtherDiv').hide();
    		}
    		
    	} else  if(field == 'info.position') {
    		model = smt.Model.DV_Position.findOrCreate({id:id});
    		this.model.get('info').set('position', model);
    		
    		if(model.get('description') == 'อิ่นๆ') {
    			$('#positionOtherDiv').show();
    		} else {
    			this.$el.find('#info\\.positionOtherTxt').val(null);
    			$('#positionOtherDiv').hide();
    		}
    		
    	} else {
    		return;
    	}
    	
    	
    },
    renderPositionRow: function(positions) {
    	var json = {};
    	json.positions=new Array();
		$.merge(json.positions, this.positions.toJSON());
		
		if(this.positions.at(0).get('description') == 'อิ่นๆ') {
			json.otherHidden = false;
		} else {
			json.otherHidden = true;
		}
    	
		this.$el.find('#positionRow').html(this.positionRowTemplate(json));
		return this;
    },
    
	render: function() {
		var json={};
		json.model = this.model.toJSON();
		
		json.occupations=new Array();
		$.merge(json.occupations, this.occupations.toJSON());
		 
		json.objectives=new Array();
		$.merge(json.objectives, this.objectives.toJSON());
	
		this.$el.html(this.formViewTemplate(json));
		
		this.renderPositionRow(positions);
		
		return this;
	}
});
