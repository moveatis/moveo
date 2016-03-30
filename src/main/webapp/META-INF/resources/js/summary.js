/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF, links */

$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var zeroDate = getLocalZeroDate();
    var growl = PF("growlWdgt");

    //console.log(growl);
    //console.log(timeline);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-records").text(timeline.items.length);

    updateRecordsTable(timeline, timeline.getVisibleChartRange());

    // Timeline range selection
    $("#input-rangeStart").keyup(function () {
        var input = $(this);
        if (!input.hasClass("ui-state-error")) {
            var startMs = convertStrToMs(input.val());
            var inputEnd = $("#input-rangeEnd");
            if (inputEnd.val() && !inputEnd.hasClass("ui-state-error") && startMs > convertStrToMs(inputEnd.val())) {
                input.addClass("ui-state-error");
                return;
            }
            timeline.options.min = new Date(zeroDate.getTime() + startMs);
            timeline.setVisibleChartRangeAuto();
            var range = timeline.getVisibleChartRange();
            updateRecordsTable(timeline, range);
        }
    });
    $("#input-rangeEnd").keyup(function () {
        var input = $(this);
        if (!input.hasClass("ui-state-error")) {
            var endMs = convertStrToMs(input.val());
            var inputStart = $("#input-rangeStart");
            if (inputStart.val() && !inputStart.hasClass("ui-state-error") && endMs < convertStrToMs(inputStart.val())) {
                input.addClass("ui-state-error");
                return;
            }
            timeline.options.max = new Date(zeroDate.getTime() + endMs);
            timeline.setVisibleChartRangeAuto();
            var range = timeline.getVisibleChartRange();
            updateRecordsTable(timeline, range);
        }
    });

    // Timeline Zoom buttons
    $("#button-zoom-in").click(function () {
        timeline.zoom(0.2, zeroDate);
    });
    $("#button-zoom-out").click(function () {
        timeline.zoom(-0.2);
    });

    // On timeline event selection show growl with record details
    links.events.addListener(timeline, "select", function () {
        var selection = timeline.getSelection();
        if (selection.length) {
            if (selection[0].row !== undefined) {
                var record = timeline.getItem(selection[0].row);
                growl.removeAll();
                growl.renderMessage({summary: 'Kirjaus: ' + record.group,
                    detail: getRecordDetails(record),
                    severity: "info"});
            }
        }

    });
});

function updateRecordsTable(timeline, range) {
    var table = $("#records");
    var categories = timeline.getItemsByGroup(timeline.items);
    var totalDuration = getTotalDuration(categories, range);
    var totalCount = parseRecords(timeline.items, range).length;
    table.empty();
    $.each(categories, function (category, records) {
        var record = $('<div class="ui-grid-row">');
        var records = parseRecords(records, range);
        var duration = recordsDuration(records, range);
        var recordsStr = records.length + '<span class="percent"> (' + percentOf(records.length, totalCount) + " %)</span>";
        var durationStr = convertMsToStr(duration) + '<span class="percent"> (' + percentOf(duration, totalDuration) + " %)</span>";
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-3">' + recordsStr + "</div>");
        record.append('<div class="ui-grid-col-3">' + durationStr + "</div>");
        table.append(record);
    });
    var summary = $('<div class="ui-grid-row">');
    summary.append('<div class="ui-grid-col-5">Yhteensä</div>');
    summary.append('<div class="ui-grid-col-3">' + totalCount + "</div>");
    summary.append('<div class="ui-grid-col-3">' + convertMsToStr(totalDuration) + "</div>");
    table.append(summary);
}

// Get all items which start or end time is in given range
function parseRecords(records, range) {
    var recordsIn = [];
    $.each(records, function (i, record) {
        if (record.start >= range.start && record.start < range.end) {
            recordsIn.push(record);
        } else if (record.end <= range.end && record.end > range.start) {
            recordsIn.push(record);
        }
    });
    return recordsIn;
}

// Get record details
function getRecordDetails(record) {
    var details = "";
    var begin = getLocalZeroDate().getTime();
    var start = Math.abs(begin - record.start.getTime());
    var end = Math.abs(begin - record.end.getTime());
    details += "Aloitus: " + convertMsToStr(start);
    details += "<br/>";
    details += "Lopetus: " + convertMsToStr(end);
    details += "<br/>";
    details += "Kesto: " + convertMsToStr(end - start);
    return details;
}

// calculate total duration of categories
function getTotalDuration(categories, range) {
    var duration = 0;
    $.each(categories, function (category, records) {
        duration += recordsDuration(records, range);
    });
    return duration;
}

// Calculate duration for records in given range
function recordsDuration(records, range) {
    var duration = 0;
    $.each(records, function () {
        var start = this.start;
        var end = this.end;
        if (start < range.start) {
            start = range.start;
        }
        if (end > range.end) {
            end = range.end;
        }
        if (end > start) {
            duration += end - start;
        }
    });
    return duration;
}

// Converts milliseconds to time string hh:mm:ss
function convertMsToStr(ms) {
    var d = ms;
    var ms = d % 1000;
    d = Math.floor(d / 1000);
    var s = d % 60;
    d = Math.floor(d / 60);
    var m = d % 60;
    var h = Math.floor(d / 60);
    return [lz(h), lz(m), lz(s)].join(':');
}

// Converts time string hh:mm:ss to milliseconds
function convertStrToMs(str) {
    var time = str.split(/:/);
    for (var i = 3 - time.length; i > 0; i--) {
        time.unshift("0");
    }
    var seconds = 0;
    for (var n in time) {
        seconds += parseInt(time[n], 10) * Math.pow(60, 2 - n);
    }
    return seconds * 1000;
}

// Append leading zero to single digit numbers
function lz(n) {
    return (n < 10 ? "0" + n : n);
}

// Calculate percentage
function percentOf(a, b) {
    if (a === 0 || b === 0) {
        return 0;
    }
    return Math.round((a / b) * 100);
}

// Get "zero" date with timezone offset
function getLocalZeroDate() {
    var localDate = new Date(0);
    var zeroDate = new Date(localDate.getTimezoneOffset() * 60 * 1000);
    return zeroDate;
}
