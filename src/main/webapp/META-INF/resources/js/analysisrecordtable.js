var URI;

function save() {
	var checkBoxCsv = document.getElementById('saveForm:basic:1');
	var checkBoxImage = document.getElementById('saveForm:basic:0');
	var filename = document.getElementById('saveForm:input-name').value;
	if (filename == "") {
		return;
	}

	if (checkBoxImage.checked) {
		saveAsImage();
	}
	if (checkBoxCsv.checked) {
		setTimeout(function() {
			saveAsCsv();
		}, 100);
	}

}


$(document).ready(function(){sendImageAndCSV();});

function sendImageAndCSV() {
	var tmpclass=$('#entries').attr('class');
	$('#entries').attr('class',"");
	$('#entries table').each(function() {
		var $table = $(this);
		var csv = $table.table2CSV({
			delivery : 'value'
		});
		sendData(csv,"csv")
	});
	$('#entries').attr('class',tmpclass);
	html2canvas(document.getElementById('dataTableImage')).then(
			function(canvas) {
				URI = canvas.toDataURL();
				sendData("reporttable,"+URI, "image")
				}
			)	
}

/**
 * Sends the given data to the given page through ajax
 * 
 * @param URI the data to be sent 
 * @param page the page to which the data should be sent, currently either csv or image
 */
function sendData(URI,page) {
	$.ajax({
		url : "../../webapi/summary/"+page,
		type : "POST",
		dataType : "text",
		contentType : "text/plain",
		cache : false,
		data : URI,
		success : function(data) {

		},
		error : function(xhr, status, error) {
			showError(msg.obs_errorCouldntSendData + ": " + error);
			this_.waiting = false;
		}
	});
}

function saveAsCsv() {
	document.getElementById('saveForm:csvButton').click();
}

function saveAsImage() {
	html2canvas(document.getElementById('dataTableImage')).then(
			function(canvas) {
				URI = canvas.toDataURL();
				var filename;
				var filenameRaw;

				try {
					filenameRaw = document
							.getElementById('saveForm:input-name').value;
					if (filenameRaw == "") {
						return;
					}
					filename = filenameRaw.replace(/\./g, '-');
				} catch (err) {
					filename = 'summary.png';
				}
				var link = document.createElement('a');
				if (typeof link.download === 'string') {
					link.href = URI;
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
			});
}