
//
// TODO:
// - Send keep-alive signals to backend.
// - Is there need for Observer?
// - Rethink CategoryItem.
// - How and where from should we get categories?
// - Error handling? Message dialog on ajax errors?
// - Remove/comment out console.log calls in release?
//


/*
 * Master clock that can be paused and resumed at will.
 * Individual categories get their time from the master clock.
 */
function Clock(initial_time) {
    this.total_time = initial_time; // TODO: No need for initial_time.
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


function CategoryItem(name, index) {
    this.li = $(document.createElement("li"));
    this.li.addClass("category-item");
    this.li.attr("id", "category-item_" + index);

    this.timer_div = $(document.createElement("div"));
    this.timer_div.addClass("category-timer");
    this.timer_div.append(document.createTextNode(timeToString(0)));

    var name_div = $(document.createElement("div"));
    name_div.addClass("category-name");
    name_div.append(document.createTextNode(name));

    this.li.append(this.timer_div);
    this.li.append(name_div);
    
    this.time = 0;
    this.start_time = 0;
    this.down = false;
    
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
        var time_str = timeToString(time);
        this.timer_div.empty();
        this.timer_div.append(document.createTextNode(time_str));
    };
}


/*
 * Handles the actual observing.
 */
function Observer(category_sets) {
    var initial_time = 0; // TODO: This should be removed entirely.
//    initial_time = 59*60*1000+50*1000; // TODO: Only for debuggin!
    this.master_clock = new Clock(initial_time);
    this.categories = [];
    this.records = [];
    this.started = false;
    this.waiting = false;
    
    initialize(this);
    
    function initialize(this_) {
        $("#pause").hide();
        $("#stop").addClass("disabled");
        $("#total-time").append(document.createTextNode(timeToString(initial_time)));
        
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
                    var category_name = set.categories[j];
                    var category = new CategoryItem(category_name, index);
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
                
                // TODO: Do we want to tell backend that observation started?
                // Or should we send start time when observation is stopped?
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
                        console.log("Error: " + error);
                        this_.waiting = false;
                        // TODO: Error popup?
                    }
                });
            }
        }
    };
    
    /**
     * Private method that pauses observation.
     * @param {type} this_
     * @param {type} now Time in milliseconds.
     * @returns {undefined}
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
            data: JSON.stringify({
                duration: time,
                categorySets: category_sets,
                timeZoneOffsetInMs: -1 * 60 * 1000 * new Date().getTimezoneOffset(), // Time zone offset in Java format
                data: this.records
            }),
            success: function(data) {
                console.log("Success: " + data);
                this_.waiting = false;
                window.location = "../summary/";
            },
            error: function(xhr, status, error) {
                console.log("Error: " + error);
                this_.waiting = false;
                // TODO: Error popup?
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


$(document).ready(function() {
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
});
