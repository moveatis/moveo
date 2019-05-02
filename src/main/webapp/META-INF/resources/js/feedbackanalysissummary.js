var URI;
var arr = [];
function save() {
	let checkBoxImage = document.getElementById('saveForm:basic:1');
	let checkBoxImage2 = document.getElementById('saveForm:anonymityUserBoxes:1');

	let filename = document.getElementById('saveForm:input-name').value;
	if (filename == "") {
		return;
	}
	if (checkBoxImage != null) {
		if (checkBoxImage.checked) {
			for(let i = 0; i < arr.length; i++)
				{
					saveAsImage(arr[i]);
				}
		}
	}
	if (checkBoxImage2 != null) {
		if (checkBoxImage2.checked) {
			for(let j = 0; j < arr.length; j++)
			{
				saveAsImage(arr[j]);
			}
		}
	}
}
function createImage(){
	exportChart2();
html2canvas(document.getElementById('tableImage')).then(function(canvas) {
	let array = [];
	arr = array;
	arr.push(canvas.toDataURL());
});

if(document.getElementById('charts:barChart_input').checked){
	html2canvas(document.getElementById('barimages')).then(function(canvas) {
		arr.push(canvas.toDataURL());
		document.getElementById('barimages').innerHTML = "";
});
}
if(document.getElementById('charts:pieChart_input').checked){
	html2canvas(document.getElementById('pieimages')).then(function(canvas) {
	arr.push(canvas.toDataURL());
	document.getElementById('pieimages').innerHTML = "";
});
}


	//exportChart3();
}


function saveAsImage(dataURL) {
	var filename;
	var filenameRaw;
	try {
		filenameRaw = document.getElementById('saveForm:input-name').value;
		if (filenameRaw == "") {
			return;
		}
		filename = filenameRaw.replace(/\./g, '-');
	} catch (err) {
		filename = 'summary.png';
	}
	var link = document.createElement('a');
	if (typeof link.download === 'string') {
		link.href = dataURL;
		link.download = filename;

		// Firefox requires the link to be in the body
		document.body.appendChild(link);

		// simulate click
		link.click();

		// remove the link when done
		document.body.removeChild(link);
	} else {
		window.open(URI);
	}
}


function exportChart2() {
    let count = document.getElementById('chartCount').innerHTML;
	for(let index = 0; index < count; index++)
    	{
    		let b = 'piechart' + index;
    		let a = 'barchart' + index;
    		let linebreak = document.createElement('br');
    		if(document.getElementById('charts:pieChart_input').checked){
    			document.getElementById('pieimages').append(PF(b).exportAsImage());
    			document.getElementById('pieimages').appendChild(linebreak);
    			}
    		
    		if(document.getElementById('charts:barChart_input').checked){
    			document.getElementById('barimages').append(PF(a).exportAsImage());
    			document.getElementById('barimages').appendChild(linebreak);
    			}
    	}
}


function exportChart3(){
	let count = document.getElementById('chartCount').innerHTML;
	for(let i = 0; i < count; i++){
		let b = 'piechart' + i;
		let a = 'barchart' + i;
		if(document.getElementById('charts:pieChart_input').checked){
		html2canvas(document.getElementById(b)).then(function(canvas) {
			document.getElementById('pieimages').append(canvas);
			arr.push(canvas.toDataURL());
		});
		}
		if(document.getElementById('charts:barChart_input').checked){
		html2canvas(document.getElementById(a)).then(function(canvas) {
			document.getElementById('barimages').append(canvas);
			arr.push(canvas.toDataURL());
		});
		}
	}
}
