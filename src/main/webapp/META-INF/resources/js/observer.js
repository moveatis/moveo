
//
// TODO:
// - Send keep-alive signals to backend.
// - Is there need for Observer?
// - Rethink CategoryItem.
// - How and where from should we get categories?
// - Is there need for initial_time other than 0?
// - Error handling? Message dialog on ajax errors?
// - Remove/comment out console.log calls in release?
//


/*
 * Master clock that can be paused and resumed at will.
 * Individual categories get their time from the master clock.
 */
function Clock(initial_time) {
    this.total_time = initial_time;
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
    var m = Math.floor(t / 60);
    var s = Math.floor(t - m * 60);
    return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
}


function CategoryItem(name, index, initial_time) {
    this.li = $(document.createElement("li"));
    this.li.addClass("category-item");
    this.li.attr("id", "category-item_" + index);

    this.timer_div = $(document.createElement("div"));
    this.timer_div.addClass("category-timer");
    this.timer_div.append(document.createTextNode(timeToString(initial_time)));

    var name_div = $(document.createElement("div"));
    name_div.addClass("category-name");
    name_div.append(document.createTextNode(name));

    this.li.append(this.timer_div);
    this.li.append(name_div);
    
    this.time = 0;
    this.start_time = 0;
    this.down = false;
    
    this.click = function(master_time) {
        var recording;
        
        if (this.down) {
            this.li.removeClass("down");
            if (master_time > this.start_time) {
                this.time += master_time - this.start_time;
                recording = {category: name, startTime: this.start_time, endTime: master_time};
            }
            this.down = false;
        } else {
            this.li.addClass("down");
            this.start_time = master_time;
            this.down = true;
        }
        
        return recording;
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
function Observer(initial_time, category_data) {
    this.master_clock = new Clock(initial_time);
    this.categories = [];
    this.recordings = [];
    this.started = false;
    this.waiting = false;
    
    initialize(this, initial_time, category_data);
    
    function initialize(this_, initial_time, category_data) {
        $("#pause").hide();
        $("#stop").addClass("disabled");
        $("#total-time").append(document.createTextNode(timeToString(initial_time)));
        
        var category_list = $("#category-list");
        
        var index = 0;
        
        for (var i in category_data) {
            var set = category_data[i];
            if (set.categories.length > 0) {
                var category_set = $(document.createElement("ul"));
                category_set.attr("id", set.name);
                category_set.addClass("category-set");
                category_set.addClass("no-text-select");
                for (var j in set.categories) {
                    var data = set.categories[j];
                    var category = new CategoryItem(data.name, index, data.initial_time);
                    this_.categories.push(category);
                    category_set.append(category.li);
                    index += 1;
                }
                category_list.append(category_set);
            }
        }
    }

    this.addRecording = function(recording) {
        if (recording !== undefined) {
            this.recordings.push(recording);
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
                        console.log("Error: " + error);
                        this_.waiting = false;
                        // TODO: Error popup?
                    }
                });
            }
        }
    };
    
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
        
        for (var i in this.categories) {
            var category = this.categories[i];
            category.li.off("click");
            category.li.addClass("disabled");
            if (category.down) {
                this.addRecording(category.click(time));
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
                data: this.recordings
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
        this.addRecording(category.click(time));
    };
    
    this.tick = function() {
        var time = this.master_clock.getElapsedTime(Date.now());
        
        var time_str = timeToString(time);
        var total_time = $("#total-time");
        total_time.empty();
        total_time.append(document.createTextNode(time_str));
        
        for (var i in this.categories) {
            this.categories[i].updateTimer(time);
        }
    };
}


$(document).ready(function() {
    // TODO: Error message if initial_time or initial_category_data not defined?
    //       => No need for this default data!
    var time = initial_time || 0;
    var category_data = initial_category_data || [
        {name: "Opettajan toiminnot", categories: [
            {name: "Järjestelyt", initial_time: 0},
            {name: "Tehtävän selitys", initial_time: 0},
            {name: "Ohjaus", initial_time: 0},
            {name: "Palautteen anto", initial_time: 0},
            {name: "Tarkkailu", initial_time: 0},
            {name: "Muu toiminta", initial_time: 0}
        ]},
        {name: "Oppilaan toiminnot", categories: [
            {name: "Oppilas suorittaa tehtävää", initial_time: 0}
        ]}
    ];
    
    var observer = new Observer(time, category_data);
    
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
