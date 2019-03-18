/**
 * @fileOverview JavaScript logic for feedback analysis view.
 * @module analyzer
 * @author Tuomas Moisio <tuomas.s.moisio at student.jyu.fi>
 */

//var msg = getMessages();

$(document).ready(function() {
	var category_sets = getCategorySets();
	var analyzer = new Analyzer(category_sets);
});

function Analyzer(category_sets){
	//this.categories = [];
	
	initialize(this);
	
	function initialize(this_){
		var div = $(document.getElementById("textArea"));
		var input = $(document.createElement("textarea"));
		var button = $(document.createElement("button"));
		input.name = "post";
		input.maxLength = "5000";
		input.cols = "580";
		input.rows = "40";
		button.setAttribute = ('value', 'dumbledore');
		div.append(input);
		div.append(button);
		var categories = document.createElement("input");
		categories.type = "text";
		categories.value = category_sets;
		console.log(categories);
		div.append(categories);
				
		var category_list = $(document.getElementById("category-list"));  

		for (var i = 0; i < category_sets.length; i++) {
        	
            var set = category_sets[i];
            
            if (set.categories.length > 0) {
                
            	var category_set = $(document.createElement("ul"));
            	category_set.attr("id", set.name);
            	category_set.addClass("category-set");
                //category_set.append(set.name);
                //category_list.append(category);
                //category_list.append(category_set);
                
                for (var j = 0; j < set.categories.length; j++) {
                    var cat = set.categories[j];
                    var catElement = $(document.createElement("li"));
                    catElement.append(document.createTextNode(cat.name));
                    
                }
                
                
            }
        }
        
//        $(".category-item").addClass("disabled");
    }
    
	
}