function sendToServer() {
    console.log("Sending data to server");
    
    var category = makeCategory();
    var startTime = makeStartTime();
    var endTime = makeEndTime(startTime);
    
    var dataToSend = JSON.stringify(
            {"category": category, "startTime" : startTime, "endTime" : endTime}
    );
    
    $("#generatedJson").html("Sent -> " + dataToSend);
    
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

//http://stackoverflow.com/a/1349426
function makeCategory()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

function makeStartTime() {
    return Math.floor(Math.random() * 20);
}

function makeEndTime(startTime) {
    return Math.floor(Math.random() * 20) + startTime;
}
