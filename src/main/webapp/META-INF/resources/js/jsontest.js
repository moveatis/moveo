function sendToServer() {
    console.log("Sending data to server");
    
    var category = "Kategoria";
    var startTime = 10;
    var endTime = 100;
    
    var dataToSend = JSON.stringify({"category": category, "startTime" : startTime, "endTime" : endTime});
    
    
    $.ajax({
        url: "webapi/records/addrecord",
        type: "POST",
        dataType: "text",
        contentType: "application/json",
        cache: false,
        data: dataToSend,
        success: serverResponded
    });
    
    
}

function serverResponded(data) {
    console.log(data);
}

