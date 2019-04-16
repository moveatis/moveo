$(function () {
        updateRecordsTable();
    });

function updateRecordsTable(){
    for (var i = 1; i < 10; i++){
        var datatable = document.getElementById("datatable");
        var column = $('<p:column></p:column>')
        var outputText = $('<h:outputText />')
        column.headerText = "ID" + i;
        outputText.value = "kalkaros" + i;
        datatable.append(column);
        column.append(outputText);
    }
}