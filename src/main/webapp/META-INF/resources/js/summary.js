/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF, links */

$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var zeroDate = getLocalZeroTime();

    //console.log(timeline);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-recordings").text(timeline.items.length);

    updateRecordingsInfo(timeline);

    // Add items for time range selection
//    timeline.addItem({
//        "start": zeroDate,
//        "end": new Date(0),
//        "content": "",
//        "className": "range-selection",
//        "editable": true,
//        "group": "range-selection"
//    });

    // Zoom buttons
    $("#button-zoom-in").click(function () {
        timeline.zoom(0.2, zeroDate);
    });
    $("#button-zoom-out").click(function () {
        timeline.zoom(-0.2);
    });

    // Calculation source change event
    $("#select-calc-source").change(function () {
        var selected = $(this).find(":selected").val();
        if (selected === "duration") {
            updateRecordingsInfo(timeline);
        } else {
            updateRecordingsInfo(timeline, {"counts": true});
        }
    });
});

function updateRecordingsInfo(timeline, options) {
    var grid = $("#recordings");
    var categories = timeline.getItemsByGroup(timeline.items);
    var totalDuration = getTotalDuration(categories);
    if (!options) {
        options = {};
    }
    grid.empty();
    $.each(categories, function (category, recordings) {
        var record = $('<div class="ui-grid-row">');
        var duration = recordingsDuration(recordings);
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-2">' + recordings.length + "</div>");
        record.append('<div class="ui-grid-col-2">' + convertMsToStr(duration) + "</div>");
        if (options["counts"]) {
            record.append('<div class="ui-grid-col-2">' + percentOf(recordings.length, timeline.items.length) + " %</div>");
        } else {
            record.append('<div class="ui-grid-col-2">' + percentOf(duration, totalDuration) + " %</div>");
        }
        grid.append(record);
    });
}

// calculate total duration of categories
function getTotalDuration(categories) {
    var duration = 0;
    $.each(categories, function (category, recordings) {
        duration += recordingsDuration(recordings);
    });
    return duration;
}

// calculates duration for recordings
function recordingsDuration(recordings) {
    var duration = 0;
    $.each(recordings, function () {
        duration += this.end - this.start;
    });
    return duration;
}

// converts ms number to time string
function convertMsToStr(ms) {
    var d = ms;
    var ms = d % 1000;
    d = Math.floor(d / 1000);
    var s = d % 60;
    d = Math.floor(d / 60);
    var m = d % 60;
    var h = Math.floor(d / 60);
    if (h > 0)
        return [lz(h), lz(m), lz(s)].join(':');
    return [lz(m), lz(s)].join(':');
}

// converts time string hh:mm:ss to milliseconds
function convertStrToMs(str) {
    var time = str.split(/:/);
    return (time[0] * 3600 + time[1] * 60 + time[2]) * 1000;
}

// append leading zero to number if smaller than 10
function lz(n) {
    return (n < 10 ? "0" + n : n);
}

// calculate percentage
function percentOf(a, b) {
    return Math.round((a / b) * 100);
}

// get "zero" date with timezone offset
function getLocalZeroTime() {
    var localDate = new Date(0);
    var zeroDate = new Date(localDate.getTimezoneOffset() * 60 * 1000);
    return zeroDate;
}
