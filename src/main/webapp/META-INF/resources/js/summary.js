/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF */

$(function () {
    var timeline = PF("timelineWdgt").getInstance();

    console.log(timeline);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-recordings").text(timeline.items.length);

    updateRecordingsInfo(timeline);
});

function updateRecordingsInfo(timeline) {
    var grid = $("#recordings-grid");
    var categories = timeline.getItemsByGroup(timeline.items);
    var totalDuration = getTotalDuration(categories);
    grid.empty();
    $.each(categories, function (category, recordings) {
        var record = $('<div class="ui-grid-row">');
        var duration = recordingsDuration(recordings);
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-2">' + convertMsToStr(duration) + "</div>");
        record.append('<div class="ui-grid-col-2">' + recordings.length + "</div>");
        record.append('<div class="ui-grid-col-2">' + percentOf(duration, totalDuration) + " %</div>");
        grid.append(record);
    });
}

function getTotalDuration(categories) {
    var duration = 0;
    $.each(categories, function (category, recordings) {
        duration += recordingsDuration(recordings);
    });
    return duration;
}

function recordingsDuration(recordings) {
    var duration = 0;
    $.each(recordings, function () {
        duration += this.end - this.start;
    });
    return duration;
}

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

function lz(n) {
    return (n < 10 ? "0" + n : n);
}

function percentOf(a, b) {
    return Math.round((a / b) * 100);
}
