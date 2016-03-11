
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
                recording = {category: name, start: this.start_time, end: master_time};
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


function Observer(initial_time, category_data) {
    var master_clock = new Clock(initial_time);
    var categories = [];
    var recordings = [];
    
    var play_button = $("#play");
    var pause_button = $("#pause");
    var stop_button = $("#stop");
    var total_time = $("#total-time");
    var category_list = $("#category-list");
    
    pause_button.hide();
    stop_button.addClass("disabled");
    total_time.append(document.createTextNode(timeToString(initial_time)));
    
    for (var i in category_data) {
        var data = category_data[i];
        var category = new CategoryItem(data.name, i, data.initial_time);
        categories.push(category);
        category_list.append(category.li);
    }
    
    // The whole recordings-adding-nonsense was inspired by this:
    // http://www.mkyong.com/jsf2/how-to-pass-new-hidden-value-to-backing-bean-in-jsf/
    this.addRecording = function(recording) {
        if (recording !== undefined) {
            recordings.push(recording);
            $("#rec-category").val(recording.category);
            $("#rec-start-time").val(recording.start);
            $("#rec-end-time").val(recording.end);
            // This ajax request is described here: http://stackoverflow.com/a/15571052
            // If ajax error handling is needed, look here: http://stackoverflow.com/a/28540357
            if ("jsf" in window) {
                jsf.ajax.request("recording-form:add-recording", null, {
                    "javax.faces.behavior.event": "action",
                    "execute": "@form"
                });
            }
        }
    };
    
    this.playClick = function() {
        if (master_clock.isPaused()) {
            master_clock.resume(Date.now());
            play_button.hide();
            pause_button.show();
            stop_button.removeClass("disabled");
        }
    };
    
    function pause(now) {
        if (!master_clock.isPaused()) {
            master_clock.pause(now);
            pause_button.hide();
            play_button.show();
        }
    }
    
    this.pauseClick = function() {
        pause(Date.now());
    };
    
    this.stopClick = function() {
        if (stop_button.hasClass("disabled")) {
            return;
        }
        
        var now = Date.now();
        
        pause(now);
        
        play_button.off("click");
        play_button.addClass("disabled");
        pause_button.off("click");
        pause_button.addClass("disabled");
//        stop_button.off("click");
//        stop_button.addClass("down");
        
        var time = master_clock.getElapsedTime(now);
        
        for (var i in categories) {
            var category = categories[i];
            category.li.off("click");
            category.li.addClass("disabled");
            if (category.down) {
                this.addRecording(category.click(time));
            }
        }
        
        console.log(recordings);
        
        // TODO: IMPORTANT: We should ensure that all recordings have been received by backend before redirecting!
        // TODO: We propably shouldn't redirect when stop is clicked --> We need a dedicated button that takes to summary page(?)
        setTimeout(function() {
            window.location = "../summary/";
        }, 1000);
    };
    
    this.categoryClick = function(index) {
        var category = categories[index];
        var time = master_clock.getElapsedTime(Date.now());
        this.addRecording(category.click(time));
    };
    
    this.tick = function() {
        var time = master_clock.getElapsedTime(Date.now());
        var time_str = timeToString(time);
        total_time.empty();
        total_time.append(document.createTextNode(time_str));
        for (var i in categories) {
            categories[i].updateTimer(time);
        }
    };
}


$(document).ready(function() {
    var initial_time = initial_time || 0;
    var category_data = category_data || [
        {name: "Järjestelyt", initial_time: 0},
        {name: "Tehtävän selitys", initial_time: 0},
        {name: "Ohjaus", initial_time: 0},
        {name: "Palautteen anto", initial_time: 0},
        {name: "Tarkkailu", initial_time: 0},
        {name: "Muu toiminta", initial_time: 0},
        {name: "Oppilas suorittaa tehtävää", initial_time: 0}
    ];
    
    var observer = new Observer(initial_time, category_data);
    
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
