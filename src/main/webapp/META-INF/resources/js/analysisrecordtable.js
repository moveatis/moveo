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
	$('#entries table').each(function() {
		var $table = $(this);
		var csv = $table.table2CSV({
			delivery : 'value'
		});
		sendData(csv,"csv")
	});
	html2canvas(document.getElementById('dataTableImage')).then(
			function(canvas) {
				URI = canvas.toDataURL();
				sendData("reporttable,"+URI, "image")
				}
			)	
}

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