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

//
// TODO:
// - How and where from should we get categories?
// - Remove/comment out console.log calls in release?
//

// NOTE: Functions in observer/index.xhtml.
var CategoryType = getCategoryTypes();
var msg = getMessages();


/*
 * Master clock that can be paused and resumed at will.
 * Individual categories get their time from the master clock.
 */
function Clock() {
    this.total_time = 0;
    this.resume_time = 0;
    this.running = false;
    
    this.resume = function(now) {
        if (!this.running) {
            this.resume_time = now;
            this.running = true;
        } else {
            console.log("Clock.resume(): Clock is already running!");
        }
    };
    
    this.pause = function(now) {
        if (this.running) {
            var delta_time = now - this.resume_time;
            this.total_time += delta_time;
            this.running = false;
        } else {
            console.log("Clock.pause(): Clock is already paused!");
        }
    };
    
    this.getElapsedTime = function(now) {
        if (this.running) {
            return this.total_time + (now - this.resume_time);
        } else {
            return this.total_time;
        }
    };
    
    this.isPaused = function() {
        return !this.running;
    };
}


/*
 * Converts milliseconds to a string representing time.
 * Time format is hh:mm:ss if hh > 0 and mm:ss otherwise.
 * @param {number} ms Time in milliseconds.
 * @returns {String} Time string.
 */
function timeToString(ms) {
    var t = Math.floor(ms / 1000);
    var s = t % 60;
    var m = Math.floor(t / 60) % 60;
    var h = Math.floor(t / 60 / 60) % 60;
    var str = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    if (h > 0) {
        str = (h < 10 ? "0" + h : h) + ":" + str;
    }
    return str;
}


/*
 * Handles one category button.
 * @param {String} name Name to be displayed on the button.
 * @param {number} type Type of the category (TIME or COUNTED).
 * @param {number} index
 * @returns {CategoryItem}
 */
function CategoryItem(name, type, index) {
    this.li = $(document.createElement("li"));
    this.li.addClass("category-item");
    this.li.attr("id", "category-item_" + index);
    this.value_div = $(document.createElement("div"));
    this.value_div.addClass("category-value");
    this.name_div = $(document.createElement("div"));
    this.name_div.addClass("category-name");
    this.name_div.append(document.createTextNode(name));
    this.li.append(this.value_div);
    this.li.append(this.name_div);
    
    this.type = type;
    
    if (this.type === CategoryType.COUNTED) {
        updateValueDiv(this, getCountString(this.count));
        this.count = 0;
    } else {
        updateValueDiv(this, timeToString(0));
        this.time = 0;
        this.start_time = 0;
        this.down = false;
    }
    
    this.click = function(master_time) {
        var record;
        
        if (this.down) {
            this.li.removeClass("down");
            if (master_time > this.start_time) {
                this.time += master_time - this.start_time;
                record = {category: name, startTime: this.start_time, endTime: master_time};
            }
            this.down = false;
        } else {
            this.li.addClass("down");
            this.start_time = master_time;
            this.down = true;
        }
        
        return record;
    };
    
    this.updateTimer = function(master_time) {
        var time = this.time;
        if (this.down) {
            time += master_time - this.start_time;
        }
        updateValueDiv(this, timeToString(time));
    };
    
    if (this.type === CategoryType.COUNTED) {
        this.click = function(master_time) {
            this.count += 1;
            updateValueDiv(this, getCountString(this.count));
            
            this.li.addClass("down");
            var item = this.li;
            setTimeout(function() { item.removeClass("down"); }, 50);
            
            return {category: name, startTime: master_time, endTime: master_time};
        };
        
        this.updateTimer = function() { };
    }
    
    function updateValueDiv(this_, text) {
        this_.value_div.empty();
        this_.value_div.append(document.createTextNode(text));
    }
    
    function getCountString(count) {
        return count + " " + msg.countAbbreviation;
    }
}


/*
 * Handles the actual observing.
 * @param {type} category_sets
 * @returns {Observer}
 */
function Observer(category_sets) {
    this.master_clock = new Clock();
    this.categories = [];
    this.records = [];
    this.started = false;
    this.waiting = false;
    
    initialize(this);
    
    /**
     * Private method that initializes various things.
     * @param {Observer} this_
     */
    function initialize(this_) {
        $("#pause").hide();
        $("#stop").addClass("disabled");
        $("#total-time").append(document.createTextNode(timeToString(0)));
        
        var category_list = $("#category-list");
        
        var index = 0;
        
        for (var i = 0; i < category_sets.length; i++) {
            
            var set = category_sets[i];
            
            if (set.categories.length > 0) {
                var category_set = $(document.createElement("ul"));
                category_set.attr("id", set.name);
                category_set.addClass("category-set");
                category_set.addClass("no-text-select");
                
                for (var j = 0; j < set.categories.length; j++) {
                    var name = set.categories[j].name;
                    var type = set.categories[j].type;
                    var category = new CategoryItem(name, type, index);
                    this_.categories.push(category);
                    category_set.append(category.li);
                    
                    index += 1;
                }
                
                category_list.append(category_set);
            }
        }
    }

    this.addRecord = function(record) {
        if (record !== undefined) {
            this.records.push(record);
        }
    };
    
    this.playClick = function() {
        if (this.master_clock.isPaused()) {
            if (this.started) {
                this.master_clock.resume(Date.now());
                $("#play").hide();
                $("#pause").show();
            } else {
                if (this.waiting) return;
                this.waiting = true;
                
                var this_ = this;
                
                $.ajax({
                    url: "../../webapi/records/startobservation",
                    type: "POST",
                    dataType: "text",
                    contentType: "text/plain",
                    cache: false,
                    data: "start observation",
                    success: function(data) {
                        console.log("Success: " + data);
                        this_.master_clock.resume(Date.now());
                        this_.started = true;
                        this_.waiting = false;
                        $("#play").hide();
                        $("#pause").show();
                        $("#stop").removeClass("disabled");
                    },
                    error: function(xhr, status, error) {
                        showError(msg.obs_errorCouldntSendStart + " " + error);
                        this_.waiting = false;
                    }
                });
            }
        }
    };
    
    /**
     * Private method that pauses observation.
     * @param {Observer} this_
     * @param {number} now Time in milliseconds.
     */
    function pause(this_, now) {
        if (!this_.master_clock.isPaused()) {
            this_.master_clock.pause(now);
            $("#pause").hide();
            $("#play").show();
        }
    }
    
    this.pauseClick = function() {
        pause(this, Date.now());
    };
    
    this.stopClick = function() {
        if (!this.started || this.waiting) return;
        this.waiting = true;
        
        var now = Date.now();
        
        pause(this, now);
        
        var play_button = $("#play");
        var pause_button = $("#pause");
        play_button.off("click");
        play_button.addClass("disabled");
        pause_button.off("click");
        pause_button.addClass("disabled");
        
        var time = this.master_clock.getElapsedTime(now);
        
        for (var i = 0; i < this.categories.length; i++) {
            var category = this.categories[i];
            category.li.off("click");
            category.li.addClass("disabled");
            if (category.down) {
                this.addRecord(category.click(time));
            }
        }
        
        console.log("Sending observation data to server...");
        
        var this_ = this;
        
        $.ajax({
            url: "../../webapi/records/addobservationdata",
            type: "POST",
            dataType: "text",
            contentType: "application/json",
            cache: false,
            // TODO: Is JSON.stringify necessary?
            data: JSON.stringify({
                duration: time,
                timeZoneOffsetInMs: getTimeZoneOffset(),
                daylightSavingInMs: getDaylightSaving(),
                data: this.records
            }),
            success: function(data) {
                console.log("Success: " + data);
                this_.waiting = false;
                // TODO: Redirect properly.
                window.location = "../summary/";
            },
            error: function(xhr, status, error) {
                showError(msg.obs_errorCouldntSendData + ": " + error);
                this_.waiting = false;
            }
        });
    };
    
    this.categoryClick = function(index) {
        var category = this.categories[index];
        var time = this.master_clock.getElapsedTime(Date.now());
        this.addRecord(category.click(time));
    };
    
    this.tick = function() {
        var time = this.master_clock.getElapsedTime(Date.now());
        
        var time_str = timeToString(time);
        var total_time = $("#total-time");
        total_time.empty();
        total_time.append(document.createTextNode(time_str));
        
        for (var i = 0; i < this.categories.length; i++) {
            this.categories[i].updateTimer(time);
        }
    };
}


/**
 * Sends ajax keep-alive signal to backend.
 */
function keepAlive() {
    $.ajax({
        url: "../../webapi/records/keepalive",
        type: "POST",
        dataType: "text",
        contentType: "text/plain",
        cache: false,
        data: "keep-alive",
        success: function(data) {
            console.log("Success: " + data);
        },
        error: function(xhr, status, error) {
            // TODO: Get translated error message from msg.
            showError("Sending keep-alive failed: " + error);
        }
    });
}


/**
 * Shows error message in a PrimeFaces growl.
 * @param {String} error_msg
 */
function showError(error_msg) {
    var growl = PF("growlWdgt");
    growl.removeAll();
    growl.renderMessage({
        summary: msg.dialogErrorTitle,
        detail: error_msg,
        severity: "error"
    });
}


$(document).ready(function() {
    var category_sets = getCategorySets(); // NOTE: Function in observer/index.xhtml.
    var observer = new Observer(category_sets);
    
    $("#play").click(function() { observer.playClick(); });
    $("#pause").click(function() { observer.pauseClick(); });
    $("#stop").click(function() { observer.stopClick(); });
    $(".category-item").click(function() {
        var id = $(this).attr("id");
        var index = parseInt(id.split("_")[1]);
        observer.categoryClick(index);
    });

    setInterval(function() { observer.tick(); }, 200);
    setInterval(keepAlive, 5*60000); // Send keep-alive every 5 minutes.
});

function getTimeZoneOffset(){
    return -1 * 60 * 1000 * new Date().getTimezoneOffset();
}

function getDaylightSaving() {
    var now = new Date();
    var jan = new Date(now.getFullYear(), 0, 1);
    return (jan.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000;
}
