function sendToServer() {
    console.log("Sending data to server");
    
    
    $.ajax({
        url: "webapi/records/addrecord",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        cache: false,
        data: '{"firstName": "Michael", "lastName":"Jordan"}'        
    });
    
    
}

function serverResponded(data) {
    console.log(data);
}

