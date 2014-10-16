function appUrl(url) {
	return '/smt/REST/'+url;
}

(function(){

window.smt = {
  Model: {},
  Collection: {},
  Page: {},
  View: {}
};

Backbone.PageCollection = Backbone.Collection.extend({
	parse: function(response) {
		if(response.status == 'SUCCESS') {
			this.page = {};
			this.page.first = response.data.first;
	
			this.page.last = response.data.last;
			this.page.lastPage = response.data.lastPage;
			this.page.firstPage = response.data.firstPage;
			this.page.totalElements = parseInt(response.data.totalElements);
			this.page.totalPages = parseInt(response.data.totalPages);
			this.page.size = parseInt(response.data.size);
			this.page.number = parseInt(response.data.number);
			this.page.pageNumber = parseInt(response.data.number) + 1;
			this.page.numberOfElements = parseInt(response.data.numberOfElements);
			this.page.nextPage = this.page.pageNumber+1;
			this.page.prevPage = this.page.pageNumber-1;
			return response.data.content;
		}
		return null;
	}
});

smt.Model.DomainVariable = Backbone.RelationalModel.extend();

smt.Model.DV_NetworkType = Backbone.RelationalModel.extend({
});
smt.Model.DV_OrgType = Backbone.RelationalModel.extend({
});
smt.Model.DV_PersonType = Backbone.RelationalModel.extend({
});

smt.Model.HealthZone = Backbone.RelationalModel.extend();
 
smt.Model.Amphur = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'province',
		relatedModel: 'smt.Model.Province'
	}]
});

smt.Model.Province = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'zone',
		relatedModel: 'smt.Model.HealthZone'
	}],
	urlRoot: appUrl('Province')
});

smt.Model.OrganizationNetwork = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'zone',
		relatedModel: 'smt.Model.HealthZone'
	}, {
		type: Backbone.HasOne,
		key: 'province',
		relatedModel: 'smt.Model.Province'
	},{
		type: Backbone.HasOne,
		key: 'amphur',
		relatedModel: 'smt.Model.Amphur'
	}, {
		type: Backbone.HasOne,
		key: 'networkType',
		relatedModel: 'smt.Model.DV_NetworkType'
	}, {
		type: Backbone.HasOne,
		key: 'orgType',
		relatedModel: 'smt.Model.DV_OrgType'
	},{
		type: Backbone.HasMany,
		key: 'medicalStaffs',
		relatedModel: 'smt.Model.OrganizationPerson'
	}],
	urlRoot: appUrl('OrganizationNetwork')
});

smt.Model.OrganizationPerson = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'organizationNetwork',
		relatedModel: 'smt.Model.OrganizationNetwork'
	},{
		type: Backbone.HasOne,
		key: 'type',
		relatedModel: 'smt.Model.DV_PersonType'
	}]
});


smt.Page.OrganizationNetworks = Backbone.PageCollection.extend({
	model: smt.Model.OrganizationNetwork,
	url: appUrl('OrganizationNetwork/search')
});

smt.Collection.HealthZones = Backbone.Collection.extend({
	model: smt.Model.HealthZone,
	url: appUrl('Province/findAllZone')
});

smt.Collection.Provinces = Backbone.Collection.extend({
	model: smt.Model.Province
});

smt.Collection.Amphurs = Backbone.Collection.extend({
	model: smt.Model.Amphur
});

smt.Collection.NetworkTypes = Backbone.Collection.extend({
	model: smt.Model.DV_NetworkType,
	url: appUrl('DomainVariable/NETWORK_TYPE')
});

smt.Collection.OrgTypes = Backbone.Collection.extend({
	model: smt.Model.DV_OrgType,
	url: appUrl('DomainVariable/ORG_TYPE')
});
smt.Collection.PersonTypes = Backbone.Collection.extend({
	model: smt.Model.DV_PersonType,
	url: appUrl('DomainVariable/PERSON_TYPE')
});

})();