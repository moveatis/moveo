/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF, links */

var TIMELINE_BEGIN = getLocalZeroDate();

$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var growl = PF("growlWdgt");

    //console.log(growl);
    //console.log(timeline);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-records").text(timeline.items.length);

    updateRecordsTable(timeline, timeline.getVisibleChartRange());

    // Timeline range selections
    $("#input-rangeStart").keyup(function () {
        updateTimelineRange(timeline, $(this));
    });
    $("#input-rangeEnd").keyup(function () {
        updateTimelineRange(timeline, $(this));
    });

    // Timeline Zoom buttons
    $("#button-zoom-in").click(function () {
        timeline.zoom(0.2, TIMELINE_BEGIN);
    });
    $("#button-zoom-out").click(function () {
        timeline.zoom(-0.2);
    });

    // On timeline event selection show growl message with record details
    links.events.addListener(timeline, "select", function () {
        showRecordDetails(timeline, growl);
    });
});

// Updates records table information for given time range
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
        var recordsPc = '<span class="percent"> (' + percentOf(records.length, totalCount) + " %)</span>";
        var durationPc = '<span class="percent"> (' + percentOf(duration, totalDuration) + " %)</span>";
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-2">' + records.length + "</div>");
        record.append('<div class="ui-grid-col-1">' + recordsPc + "</div>");
        record.append('<div class="ui-grid-col-2">' + convertMsToUnits(duration) + "</div>");
        record.append('<div class="ui-grid-col-1">' + durationPc + "</div>");
        table.append(record);
    });
    var summary = $('<div class="ui-grid-row summary-row">');
    summary.append('<div class="ui-grid-col-5">Yhteensä</div>');
    summary.append('<div class="ui-grid-col-2">' + totalCount + "</div>");
    summary.append('<div class="ui-grid-col-1"/>');
    summary.append('<div class="ui-grid-col-2">' + convertMsToUnits(totalDuration) + "</div>");
    summary.append('<div class="ui-grid-col-1"/>');
    table.append(summary);
}

// Update timeline range to match start and end range input values
function updateTimelineRange(timeline, input) {
    var inputStart = $("#input-rangeStart");
    var inputEnd = $("#input-rangeEnd");

    if (!inputStart.hasClass("ui-state-error") && !inputEnd.hasClass("ui-state-error")) {
        var msStart = convertStrToMs(inputStart.val());
        var msEnd = convertStrToMs(inputEnd.val());

        // check the validity of start and end values that start >= end
        if (msStart >= msEnd) {
            input.addClass("ui-state-error");
            return;
        }

        if (msStart) {
            timeline.options.min = new Date(TIMELINE_BEGIN.getTime() + msStart);
        } else {
            timeline.options.min = TIMELINE_BEGIN;
        }

        if (msEnd) {
            timeline.options.max = new Date(TIMELINE_BEGIN.getTime() + msEnd);
        }

        timeline.setVisibleChartRangeAuto();
        updateRecordsTable(timeline, timeline.getVisibleChartRange());
    }
}

// Show selected record details in growl message
function showRecordDetails(timeline, growl) {
    var selection = timeline.getSelection();
    if (selection.length) {
        if (selection[0].row !== undefined) {
            var record = timeline.getItem(selection[0].row);
            growl.removeAll();
            growl.renderMessage({
                summary: record.group,
                detail: getRecordDetails(record),
                severity: "info"
            });
        }
    }
}

// Get all records that are fully or partially in the given time range
function parseRecords(records, range) {
    var recordsIn = [];
    $.each(records, function (i, record) {
        if (record.start >= range.start && record.start < range.end) {
            recordsIn.push(record);
        } else if (record.end <= range.end && record.end > range.start) {
            recordsIn.push(record);
        } else if (record.start < range.start && record.end > range.end) {
            recordsIn.push(record);
        }
    });
    return recordsIn;
}

// Get record details
function getRecordDetails(record) {
    var details = "";
    var start = Math.abs(TIMELINE_BEGIN.getTime() - record.start.getTime());
    var end = Math.abs(TIMELINE_BEGIN.getTime() - record.end.getTime());
    details += "Aloitus: " + convertMsToStr(start);
    details += "<br/>";
    details += "Lopetus: " + convertMsToStr(end);
    details += "<br/>";
    details += "Kesto: " + convertMsToUnits(end - start);
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

// Convert milliseconds to time string hh:mm:ss
function convertMsToStr(ms) {
    var d = ms;
    d = Math.floor(d / 1000);
    var s = d % 60;
    d = Math.floor(d / 60);
    var m = d % 60;
    d = Math.floor(d / 60);
    var h = d % 60;
    return [lz(h), lz(m), lz(s)].join(':');
}

// Convert time string hh:mm:ss to milliseconds
// returns NaN for unparseable time string
function convertStrToMs(str) {
    var time = str.split(/:/);
    // insert missing values
    for (var i = 3 - time.length; i > 0; i--) {
        time.unshift("0");
    }
    var seconds = 0;
    for (var n in time) {
        seconds += parseInt(time[n], 10) * Math.pow(60, 2 - n);
    }
    return seconds * 1000;
}

// Convert milliseconds to string with units e.g. 1h 2m 0s
function convertMsToUnits(ms) {
    var time = convertMsToStr(ms).split(":");
    var units = "";

    var getTimeUnit = function (i, unit) {
        var n = parseInt(time[i], 10);
        if (n > 0) {
            units += n + unit;
        }
    };

    if (ms < 1000) {
        return ">1s";
    }
    if (time.length === 3) {
        getTimeUnit(0, "h");
        getTimeUnit(1, "m");
        getTimeUnit(2, "s");
    } else {
        units = "0s";
    }
    return units;
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
