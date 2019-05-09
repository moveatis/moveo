/**
 * @fileOverview JavaScript logic for feedback analysis view.
 * @module analyzer
 * @author Tuomas Moisio <tuomas.s.moisio at student.jyu.fi>
 */

//var msg = getMessages();
var id = 1;
var selectedCategories = [];
$(document).ready(function() {
	//var category_sets = getCategorySets();
	//var analyzer = new Analyzer(category_sets);
	$("#buttons").children().each(function(){
		if(this.value = true){selectedCategories.push(this.onLabel);}
		console.log(this);
		
	});
	console.log(selectedCategories);
});
	

function changeValue(){
	this.value = true;
}
	

function newAnalyzeEvent(){
	setID();
	empty();
}

function setID(){
	id++;
	document.getElementById("currentAnalyze").innerText = id;
}

function empty(){
	$("#buttons").children().each(function(){
		if(this.hasClass("ui-state-active")){
			
		}
	});
}