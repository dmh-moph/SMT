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

smt.Model.DV_EducationLevel = Backbone.RelationalModel.extend({
});
smt.Model.DV_JournalType = Backbone.RelationalModel.extend({
});
smt.Model.DV_NetworkType = Backbone.RelationalModel.extend({
});
smt.Model.DV_OrgType = Backbone.RelationalModel.extend({
});
smt.Model.DV_PersonType = Backbone.RelationalModel.extend({
});
smt.Model.DV_SituationType = Backbone.RelationalModel.extend({
});
smt.Model.DV_EducationLevel = Backbone.RelationalModel.extend({
});

smt.Model.HealthZone = Backbone.RelationalModel.extend();
 
smt.Model.FileMeta = Backbone.RelationalModel.extend({
	urlRoot: '/smt/FILES'
});

smt.Model.Amphur = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'province',
		relatedModel: 'smt.Model.Province',
		includeInJSON: 'id'
	}]
});

smt.Model.Province = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'zone',
		relatedModel: 'smt.Model.HealthZone',
		includeInJSON: Backbone.Model.prototype.idAttribute
			
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

smt.Model.Journal = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'journalType',
		relatedModel: 'smt.Model.DV_JournalType'
	},{
		type: Backbone.HasOne,
		key: 'organization',
		relatedModel: 'smt.Model.OrganizationNetwork'
	},{
		type: Backbone.HasMany,
		key: 'files',
		relatedModel: 'smt.Model.FileMeta'
	}],
	urlRoot: appUrl('Journal')
});
smt.Page.Journals = Backbone.PageCollection.extend({
	model: smt.Model.Journal,
	url: appUrl('Journal/search')
});
smt.Model.JournalSituation = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'situation',
		relatedModel: 'smt.Model.Situation'
	},{
		type: Backbone.HasOne,
		key: 'journal',
		relatedModel: 'smt.Model.Journal'
	}],
	urlRoot: appUrl('JournalSituation')
});
smt.Page.JournalSituations = Backbone.PageCollection.extend({
	model: smt.Model.JournalSituation,
	url: appUrl('JournalSituation/search')
});

smt.Model.Research = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'journalType',
		relatedModel: 'smt.Model.DV_JournalType'
	},{
		type: Backbone.HasOne,
		key: 'organization',
		relatedModel: 'smt.Model.OrganizationNetwork'
	},{
		type: Backbone.HasMany,
		key: 'files',
		relatedModel: 'smt.Model.FileMeta'
	}],
	urlRoot: appUrl('Research')
});

smt.Page.Researches = Backbone.PageCollection.extend({
	model: smt.Model.Research,
	url: appUrl('Research/search')
});

smt.Model.ResearchSituation = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'situation',
		relatedModel: 'smt.Model.Situation'
	},{
		type: Backbone.HasOne,
		key: 'research',
		relatedModel: 'smt.Model.Research'
	}],
	urlRoot: appUrl('ResearchSituation')
});
smt.Page.ResearchSituations = Backbone.PageCollection.extend({
	model: smt.Model.ResearchSituation,
	url: appUrl('ResearchSituation/search')
});

smt.Model.Situation = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'situationType',
		relatedModel: 'smt.Model.DV_SituationType'
	}],
	urlRoot: appUrl('Situation')
});
smt.Collection.Situations = Backbone.Collection.extend({
	model: smt.Model.Situation,
	url: appUrl('Situation/findAllSituation')
});
smt.Page.Situations = Backbone.PageCollection.extend({
	model: smt.Model.Situation,
	url: appUrl('Situation/search')
});

smt.Model.Behavior = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'zone',
		relatedModel: 'smt.Model.HealthZone'
	},{
		type: Backbone.HasOne,
		key: 'province',
		relatedModel: 'smt.Model.Province'
	}, {
		type: Backbone.HasOne,
		key: 'situationType',
		relatedModel: 'smt.Model.DV_SituationType'
	}, {
		type: Backbone.HasOne,
		key: 'situation',
		relatedModel: 'smt.Model.Situation'
	}, {
		type: Backbone.HasOne,
		key: 'targetEducationLevel',
		relatedModel: 'smt.Model.DV_EducationLevel'
	},{
		type: Backbone.HasMany,
		key: 'impacts',
		collectionType: 'smt.Collection.BehaviorImpacts',
		relatedModel: 'smt.Model.BehaviorImpact'
	},{
		type: Backbone.HasMany,
		key: 'files',
		relatedModel: 'smt.Model.FileMeta'
	}],
	urlRoot: appUrl('Behavior')
});
smt.Page.Behaviors = Backbone.PageCollection.extend({
	model: smt.Model.Behavior,
	url: appUrl('Behavior/search')
});

smt.Model.BehaviorImpact = Backbone.RelationalModel.extend({
});
smt.Collection.BehaviorImpacts = Backbone.Collection.extend({
	model: smt.Model.BehaviorImpact
});

smt.Model.PsychoSocialReport = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'organizationNetwork',
		relatedModel: 'smt.Model.OrganizationNetwork'
	}],
	urlRoot: appUrl('PsychoSocialReport')
});

smt.Page.PsychoSocialReports = Backbone.PageCollection.extend({
	model: smt.Model.PsychoSocialReport,
	url: appUrl('PsychoSocialReport/search')
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

smt.Collection.OrganizationNetworks = Backbone.Collection.extend({
	model: smt.Model.OrganizationNetwork
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
smt.Collection.JournalTypes = Backbone.Collection.extend({
	model: smt.Model.DV_JournalType,
	url: appUrl('DomainVariable/JOURNAL_TYPE')
});

smt.Collection.OrgTypes = Backbone.Collection.extend({
	model: smt.Model.DV_OrgType,
	url: appUrl('DomainVariable/ORG_TYPE')
});
smt.Collection.PersonTypes = Backbone.Collection.extend({
	model: smt.Model.DV_PersonType,
	url: appUrl('DomainVariable/PERSON_TYPE')
});
smt.Collection.EducationLevels = Backbone.Collection.extend({
	model: smt.Model.DV_EducationLevel,
	url: appUrl('DomainVariable/EDUCATION')
});
smt.Collection.SituationTypes = Backbone.Collection.extend({
	model: smt.Model.DV_SituationType,
	url: appUrl('DomainVariable/SITUATION_TYPE')
});

})();