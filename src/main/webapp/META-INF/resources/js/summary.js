/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF */

$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var zeroDate = getLocalZeroTime();

    console.log(timeline);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-recordings").text(timeline.items.length);

    updateRecordingsInfo(timeline);

    // Zoom buttons
    $("#button-zoom-in").click(function () {
        timeline.zoom(0.2, zeroDate);
    });
    $("#button-zoom-out").click(function () {
        timeline.zoom(-0.2);
    });
});

function updateRecordingsInfo(timeline) {
    var grid = $("#recordings");
    var categories = timeline.getItemsByGroup(timeline.items);
    var totalDuration = getTotalDuration(categories);
    grid.empty();
    var keys = Object.keys(categories);
    for (var i = keys.length - 1; i >= 0; i--) {
        var category = keys[i];
        var recordings = categories[category];
        var record = $('<div class="ui-grid-row">');
        var duration = recordingsDuration(recordings);
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-2">' + recordings.length + "</div>");
        record.append('<div class="ui-grid-col-2">' + convertMsToStr(duration) + "</div>");
        record.append('<div class="ui-grid-col-2">' + percentOf(duration, totalDuration) + " %</div>");
        grid.append(record);
    }
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
