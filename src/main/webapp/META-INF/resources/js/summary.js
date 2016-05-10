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
 * Javascript methods for the summary page.
 * @author Juha Moisio <juha.pa.moisio at student.jyu.fi>
 */
var TIMELINE_BEGIN = getLocalZeroDate();
var OBSERVATION_DURATION = SummaryIndex.getObservationDuration(); // function in summary/index.xhtml
var msg = SummaryIndex.getMessages(); // function in summary/index.xhtml
var ESCAPE_KEY = 27;

/*
 * On document ready:
 *  - Calculate recordings summary details.
 *  - Update the details on time frame change.
 *  - Add zoom button click events for timeline zooming.
 *  - Show growl message on timeline event selection.
 */
$(function () {
    var timeline = PF("timelineWdgt").getInstance();
    var growl = PF("growlWdgt");
    var startTimeWdgt = PF("startTimeWdgt");
    var endTimeWdgt = PF("endTimeWdgt");
    var timeframe = timeline.getVisibleChartRange();
    var startTimePicker = $("#startTime_input");
    var endTimePicker = $("#endTime_input");

    timeline.options.showCurrentTime = false; // NOTE: setting this did not work from JSF

    $("#total-records").text(getRecordsInTimeframe(timeline.items, timeframe).length);
    $("#total-duration").text(convertMsToUnits(OBSERVATION_DURATION));
    updateRecordsTable(timeline, timeframe);

    // Set time select listeners and restore original dates that get reseted on event bind.
    var startDate = startTimeWdgt.getDate();
    var endDate = endTimeWdgt.getDate();
    startTimePicker.timepicker("option", "onSelect", function (startTime) {
        var error = updateTimelineTimeframe(timeline, startTime, endTimePicker.val());
        startTimePicker.toggleClass("ui-state-error", error);
        if (error && convertStrToMs(startTime) > OBSERVATION_DURATION) {
            startTimeWdgt.setDate(endDate);
        }
    });
    endTimePicker.timepicker("option", "onSelect", function (endTime) {
        var error = updateTimelineTimeframe(timeline, startTimePicker.val(), endTime);
        endTimePicker.toggleClass("ui-state-error", error);
        if (error && convertStrToMs(endTime) > OBSERVATION_DURATION) {
            endTimeWdgt.setDate(endDate);
        }
    });
    startTimeWdgt.setDate(startDate);
    endTimeWdgt.setDate(endDate);

    $("#button-zoom-in").click(function () {
        timeline.zoom(0.2, TIMELINE_BEGIN);
    });
    $("#button-zoom-out").click(function () {
        timeline.zoom(-0.2);
    });

    links.events.addListener(timeline, "select", function () {
        showRecordDetails(timeline, growl);
    });

    $(document).click(function (e) {
        if (!$(e.target).hasClass("timeline-event-content")) {
            hideMessages(timeline, growl);
        }
    });

    $(document).keyup(function (e) {
        if (e.keyCode === ESCAPE_KEY) {
            hideMessages(timeline, growl);
        }
    });

    $(window).on('scroll resize', function () {
        $("#timelineControls").toggleClass("bottom",
                isBottomOfDocument($("#Footer").height()));
    });
    $("#timelineControls").toggleClass("bottom",
            isBottomOfDocument($("#Footer").height()));

    /* Ask confirmation before leaving unsaved observation data */
    /*
     window.onbeforeunload = function () {
     return msg.dlg_confirmLeave;
     };
     */
});

/**
 * Updates records table information for given time frame.
 * @param {object} timeline - The timeline component.
 * @param {object} timeframe - The selected start and end time.
 */
function updateRecordsTable(timeline, timeframe) {
    var recordsTable = $("#records");
    var categories = timeline.getItemsByGroup(timeline.items);
    var timeframeDuration = getTimeframeDuration(timeframe);
    var recordsTotalCount = getRecordsInTimeframe(timeline.items, timeframe).length;
    recordsTable.empty();

    var oldCategorySet;
    $.each(categories, function (category, categoryRecords) {
        var records = getRecordsInTimeframe(categoryRecords, timeframe);
        var duration = getDurationOfRecords(records, timeframe);
        var newCategorySet = category.match("<span class=categorySet>(.*)</span>")[1];
        var recordRow = createRecordRow({
            name: category,
            count: records.length,
            duration: duration,
            addGap: oldCategorySet !== newCategorySet,
            countPercent: spanPercentOf(records.length, recordsTotalCount),
            durationPercent: spanPercentOf(duration, timeframeDuration)
        });
        recordsTable.append(recordRow);
        oldCategorySet = newCategorySet;
    });
    var summaryRow = createRecordRow({
        name: msg.sum_total,
        count: recordsTotalCount,
        duration: timeframeDuration,
        countPercent: "",
        durationPercent: ""
    });
    summaryRow.addClass("summary-row");
    recordsTable.append(summaryRow);
}

/*
 * Create html element containing the data of a record.
 * @param {object} record - object containing record data
 *  Data form: {name, count, countPercentage, duration, durationPercentage}
 * @returns {object} - jquery object containing the record row element.
 */
function createRecordRow(record) {
    // TODO: escape XSS; Is it required? Values are from backing bean and are
    //       already escaped and user cannot change them later.
    // TODO: set information of category group
    var row = $('<div class="ui-grid-row">');
    var count = $('<div class="ui-grid-col-3">');
    var duration = $('<div class="ui-grid-col-3">');
    count.append('<span>' + record.count + " " + msg.sum_countAbr + "</span>");
    count.append('<span>' + record.countPercent + "</span>");
    duration.append('<span>' + convertMsToUnits(record.duration) + "</span>");
    duration.append('<span>' + record.durationPercent + "</span>");
    row.append('<div class="ui-grid-col-5">' + record.name + "</div>");
    row.append(count);
    row.append(duration);
    if (record.addGap) {
        row.addClass("gapBefore");
    }
    return row;
}

/*
 * Updates timeline's time frame to the given start and end times.
 * @param {object} timeline - The timeline component.
 * @param {string} strStart - time frame start in hh:mm:ss format
 * @param {string} strEnd - time frame end in hh:mm:ss format
 * @returns {boolean} - returns true on errors, false if updated successfully.
 */
function updateTimelineTimeframe(timeline, strStart, strEnd) {
    var msStart = convertStrToMs(strStart);
    var msEnd = convertStrToMs(strEnd);

    // check the validity of time frame
    if (msStart >= msEnd
            || msStart > OBSERVATION_DURATION
            || msEnd > OBSERVATION_DURATION) {
        return true;
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
    return false;
}

/*
 * Show a growl message of the selected record.
 * @param {object} timeline - The timeline component.
 * @param {object} growl - The growl component.
 */
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

/*
 * Hide all growl messages and remove timeline selection.
 * @param {object} timeline - The timeline component.
 * @param {object} growl - The growl component.
 */
function hideMessages(timeline, growl) {
    growl.removeAll();
    timeline.setSelection(null);
}

/*
 * Get all records that are fully or partially in the given time frame.
 * @param {object} records - object containing the records.
 * @param {object} timeframe - The selected start and end time.
 * @returns {object} - returns a list of matched records.
 */
function getRecordsInTimeframe(records, timeframe) {
    var recordsIn = [];
    $.each(records, function (i, record) {
        if (record.className === "dummyRecord") {
            return true;
        } else if (record.start >= timeframe.start && record.start < timeframe.end) {
            recordsIn.push(record);
        } else if (record.end <= timeframe.end && record.end > timeframe.start) {
            recordsIn.push(record);
        } else if (record.start < timeframe.start && record.end > timeframe.end) {
            recordsIn.push(record);
        }
    });
    return recordsIn;
}

/*
 * Get record details.
 * @param {object} record - a record object from the timeline component.
 * @returns {string} - details as a string value.
 */
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

/*
 * Get total duration of records of all categories in given time frame.
 * @param {object} records - object containing the records.
 * @param {object} timeframe - The selected start and end time.
 * @returns {number} - duration of the records.
 */
function getDurationOfCategories(categories, timeframe) {
    var duration = 0;
    $.each(categories, function (category, records) {
        duration += getDurationOfRecords(records, timeframe);
    });
    return duration;
}

/*
 * Get duration of observation's time frame.
 * @param {object} timeframe - The selected start and end time.
 * @returns {number} - duration of the observation's time frame.
 */
function getTimeframeDuration(timeframe) {
    var rStartMs = toTimelineTime(timeframe.start);
    var rEndMs = toTimelineTime(timeframe.end);
    var start = (rStartMs > 0) ? rStartMs : 0;
    var end = (rEndMs < OBSERVATION_DURATION) ? rEndMs : OBSERVATION_DURATION;
    return end - start;
}

/* Get total duration of records in given time frame.
 * @param {object} records - object containing the records.
 * @returns {number} - duration of the records.
 */
function getDurationOfRecords(records, timeframe) {
    var duration = 0;
    $.each(records, function () {
        var start = this.start;
        var end = this.end;
        if (this.className === "dummyRecord") {
            return true;
        }
        if (start < timeframe.start) {
            start = timeframe.start;
        }
        if (end > timeframe.end) {
            end = timeframe.end;
        }
        if (end > start) {
            duration += end - start;
        }
    });
    return duration;
}

/*
 * Convert milliseconds to time string hh:mm:ss. 
 * @param {number} ms - time in milliseconds.
 * @returns {string} - time in string as hh:mm:ss.
 */
function convertMsToStr(ms) {
    var d = ms;
    d = Math.floor(d / 1000);
    var s = d % 60;
    d = Math.floor(d / 60);
    var m = d % 60;
    d = Math.floor(d / 60);
    var h = d % 60;
    return [leadingZero(h), leadingZero(m), leadingZero(s)].join(':');
}

/*
 * Convert time string hh:mm:ss to milliseconds
 * @param {string} str - time in string as hh:mm:ss.
 * @returns {number} - time in milliseconds or NaN for unparseable time string.
 */
function convertStrToMs(str) {
    var time = str.split(/:/);
    // insert missing values
    for (var i = 3 - time.length; i > 0; i--) {
        time.unshift("0");
    }
    var seconds = 0;
    for (var i = 0; i < time.length; i++) {
        seconds += parseInt(time[i], 10) * Math.pow(60, 2 - i);
    }
    return seconds * 1000;
}

/*
 * Convert time in milliseconds to string with time units e.g. 1h 2m 0s. 
 * @param {number} ms - time in milliseconds.
 * @returns {string} - time in string with units e.g. 1h 2m 0s.
 */
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
        return "0 s";
    }
    if (ms < 1000) {
        return "~1 s";
    }
    if (time.length === 3) {
        getTimeUnit(0, " h");
        getTimeUnit(1, " m");
        getTimeUnit(2, " s");
    } else {
        return "0 s";
    }
    return units.replace(/([h,m,s])(\d)/g, "$1 $2");
}

/*
 * Append leading zero to single digit numbers. 
 * @param {number} n - number.
 * @returns {string} - number with possible leading zero.
 */
function leadingZero(n) {
    return (n < 10 ? "0" + n : n.toString());
}

/*
 * Calculate percent of two values.
 * @param {number} a - the number of share.
 * @param {number} b - the number of total quantity.
 * @returns {number} - percentage ratio.
 */
function percentOf(a, b) {
    if (a === 0 || b === 0) {
        return 0;
    }
    return Math.round((a / b) * 100);
}

/*
 * Get percent of two values as span element string.
 * @param {number} a - the number of share.
 * @param {number} b - the number of total quantity.
 * @returns {string} - percent as span element string.
 */
function spanPercentOf(a, b) {
    return '<span class="percent"> (' + percentOf(a, b) + " %)</span>";
}

/*
 * Get "zero" date with time zone offset.
 * @returns {date} - zero date with time zone offset.
 */
function getLocalZeroDate() {
    var localDate = new Date(0);
    var zeroDate = new Date(localDate.getTimezoneOffset() * 60 * 1000);
    return zeroDate;
}

/*
 * Convert date object to the timeline component time.
 * @param {date} date - the date object of the time to convert.
 * @returns {number} - converted time in milliseconds.
 */
function toTimelineTime(date) {
    return Math.abs(TIMELINE_BEGIN.getTime() - date.getTime());
}

/*
 * Encode main html markup characters to html entities.
 * @param {string} str - the string to encode.
 * @returns {str} - the encoded string.
 */
function encodeHTML(str) {
    return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
}

/*
 * Check if scrolled to the bottom of the page.
 * @param {number} padding - extra padding from bottom to check.
 * @return {boolean} - true if at bottom otherwise false.
 */
function isBottomOfDocument(padding) {
    return $(window).scrollTop() >= $(document).height() - padding - $(window).height();
}

function getTimeZoneOffset() {
    return -1 * 60 * 1000 * new Date().getTimezoneOffset();
}