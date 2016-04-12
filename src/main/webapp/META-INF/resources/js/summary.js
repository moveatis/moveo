/*
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* global PF, links, SummaryIndex */

/**
 * Javascript methods for summary page.
 *  Calculates recordings summary details.
 *  Updates the details on time frame change.
 *  Adds zoom button click events for timeline zooming.
 *  Shows growl message on timeline event selection.
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
var TIMELINE_BEGIN = getLocalZeroDate();
var OBSERVATION_DURATION = SummaryIndex.getObservationDuration(); // function in summary/index.xhtml
var msg = SummaryIndex.getMessages(); // function in summary/index.xhtml

$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var growl = PF("growlWdgt");
    var range = timeline.getVisibleChartRange();

    //console.log(growl);
    //console.log(timeline);
    //console.log(OBSERVATION_DURATION);

    // NOTE: setting showCurrentTime did not work from JSF
    timeline.options.showCurrentTime = false;

    $("#total-records").text(parseRecords(timeline.items, range).length);
    $("#total-duration").text(convertMsToUnits(OBSERVATION_DURATION));
    $("#input-rangeEnd").val(convertMsToStr(OBSERVATION_DURATION));
    updateRecordsTable(timeline, range);

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
    var rangeDuration = getRangeDuration(range);
    var recordsCount = parseRecords(timeline.items, range).length;
    table.empty();
    $.each(categories, function (category, records) {
        var record = $('<div class="ui-grid-row">');
        var records = parseRecords(records, range);
        var duration = recordsDuration(records, range);
        var recordsPc = '<span class="percent"> (' + percentOf(records.length, recordsCount) + " %)</span>";
        var durationPc = '<span class="percent"> (' + percentOf(duration, rangeDuration) + " %)</span>";
        record.append('<div class="ui-grid-col-5">' + category + "</div>");
        record.append('<div class="ui-grid-col-1">' + records.length + "</div>");
        record.append('<div class="ui-grid-col-1">' + recordsPc + "</div>");
        record.append('<div class="ui-grid-col-2">' + convertMsToUnits(duration) + "</div>");
        record.append('<div class="ui-grid-col-1">' + durationPc + "</div>");
        table.append(record);
    });
    var summary = $('<div class="ui-grid-row summary-row">');
    summary.append('<div class="ui-grid-col-5">' + msg.sum_total + '</div>');
    summary.append('<div class="ui-grid-col-1">' + recordsCount + "</div>");
    summary.append('<div class="ui-grid-col-1"/>');
    summary.append('<div class="ui-grid-col-2">' + convertMsToUnits(rangeDuration) + "</div>");
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

        // check validity of time range
        if (msStart >= msEnd || convertStrToMs(input.val()) > OBSERVATION_DURATION) {
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
        } else {
            timeline.options.max = new Date(TIMELINE_BEGIN.getTime() + OBSERVATION_DURATION * 1.1);
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
        if (record.className === "dummyRecord") {
            return true;
        } else if (record.start >= range.start && record.start < range.end) {
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
    var start = toTimelineTime(record.start);
    var end = toTimelineTime(record.end);
    details += msg.sum_begin + ": " + convertMsToStr(start);
    details += "<br/>";
    details += msg.sum_end + ": " + convertMsToStr(end);
    details += "<br/>";
    details += msg.sum_duration + ": " + convertMsToUnits(end - start);
    return details;
}

// Get total duration of records in given range
function getTotalRecordsDuration(categories, range) {
    var duration = 0;
    $.each(categories, function (category, records) {
        duration += recordsDuration(records, range);
    });
    return duration;
}

// Get duration of observation or if given is shorter get duration of range
function getRangeDuration(range) {
    var rStartMs = toTimelineTime(range.start);
    var rEndMs = toTimelineTime(range.end);
    var start = (rStartMs > 0) ? rStartMs : 0;
    var end = (rEndMs < OBSERVATION_DURATION) ? rEndMs : OBSERVATION_DURATION;
    return end - start;
}

// Calculate duration for records in given range
function recordsDuration(records, range) {
    var duration = 0;
    $.each(records, function () {
        var start = this.start;
        var end = this.end;
        if (this.className === "dummyRecord") {
            return true;
        }
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

    if (ms <= 0) {
        return "0s";
    }
    if (ms < 1000) {
        return ">1s";
    }
    if (time.length === 3) {
        getTimeUnit(0, "h");
        getTimeUnit(1, "m");
        getTimeUnit(2, "s");
    } else {
        return "0s";
    }
    return units.replace(/([h,m,s])(\d)/g, "$1 $2");
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

function toTimelineTime(date) {
    return Math.abs(TIMELINE_BEGIN.getTime() - date.getTime());
}