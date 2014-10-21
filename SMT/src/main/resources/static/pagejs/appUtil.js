/**
 * 
 */
//Globally register console
if (typeof console === 'undefined') {
    console = { log: function() {} };
}

Handlebars.registerHelper('formatNumber', function(number) {
	return addCommas(number);
});

Handlebars.registerHelper('txtInput', function(size, label, field, defaultValue) {
	
	var aValue = "";
	
	if(defaultValue != null) {
		aValue = defaultValue;
	}
	
	var mdLabel = 2;
	var mdTxt = 10;
	if(size == "half") {
		mdLabel = 4;
		mdTxt = 8;
	} 
	
	var s = "" +
			"<div class='form-group'> \n" +
			"	<label for='"+ field+"Txt' class='col-md-"+mdLabel+" control-label'>"+label+"</label> \n" +
			"	<div class='col-md-"+mdTxt+"'> \n" +
			"		<input type='text' class='form-control formTxt' id='"+ field+"Txt' data-field='"+field+"' value='"+aValue+"'></input> \n" +
			"	</div> \n" +
			"</div>"; 
	
	return new Handlebars.SafeString(s);
});

function __addCommas(nStr)
{
	if(nStr == null || isNaN(nStr)) {
		return '-';
	}
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function __loaderHtml() {
	return "<div class='loader-center'><div class='loader'></div>";
}

function __loaderInEl($el) {
	$el.html(__loaderHtml());
	$el.find('.loader').loader();
}

function __setSelect(array, model) {
	if(model == null) return;
	
	for(var i=0; i< array.length; i++ ) {
		if(array[i].id == model.get('id')) {
			array[i].selected = true;
			return;
		}
	}
}