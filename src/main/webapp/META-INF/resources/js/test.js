
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
    }
    
    this.pause = function(now) {
        if (this.running) {
            var delta_time = now - this.resume_time;
            this.total_time += delta_time;
            this.running = false;
        } else {
            console.log("Clock.pause(): Clock is already paused!");
        }
    }
    
    this.getElapsedTime = function(now) {
        if (this.running) {
            return this.total_time + (now - this.resume_time);
        } else {
            return this.total_time;
        }
    }
    
    this.isPaused = function() {
        return !this.running;
    }
}


function timeToString(ms) {
    var t = Math.floor(ms / 1000);
    var m = Math.floor(t / 60);
    var s = Math.floor(t - m * 60);
    return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
}


function CategoryItem(name, index) {
    this.li = $(document.createElement("li"));
    this.li.addClass("category-item");
    this.li.attr("id", "category-item_" + index);

    this.timer_div = $(document.createElement("div"));
    this.timer_div.addClass("category-timer");
    this.timer_div.append(document.createTextNode("00:00"));

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
                recording = {start: this.start_time, end: master_time, category: name};
            }
            this.down = false;
        } else {
            this.li.addClass("down");
            this.start_time = master_time;
            this.down = true;
        }
        
        return recording;
    }
    
    this.updateTimer = function(master_time) {
        var time = this.time;
        if (this.down) {
            time += master_time - this.start_time;
        }
        var time_str = timeToString(time);
        this.timer_div.empty();
        this.timer_div.append(document.createTextNode(time_str));
    }
}


$(document).ready(function() {
    var master_clock = new Clock();
    var categories = [];
    var recordings = [];
    
    var category_names = [
        "Järjestelyt",
        "Tehtävän selitys",
        "Ohjaus",
        "Palautteen anto",
        "Tarkkailu",
        "Muu toiminta",
        "Oppilas suorittaa tehtävää"
    ];

    for (var i in category_names) {
        var category = new CategoryItem(category_names[i], i);
        categories.push(category);
        $("#category-list").append(category.li);
    }
    
    //
    //
    //
    
    function playClick() {
        if (master_clock.isPaused()) {
            master_clock.resume(Date.now());
            $("#play").hide();
            $("#pause").show();
            $("#stop").removeClass("disabled");
        }
    }
    
    function pauseClick() {
        if (!master_clock.isPaused()) {
            master_clock.pause(Date.now());
            $("#pause").hide();
            $("#play").show();
        }
    }
    
    // The whole recordings-adding-nonsense was inspired by this:
    // http://www.mkyong.com/jsf2/how-to-pass-new-hidden-value-to-backing-bean-in-jsf/
    function addRecording(recording) {
        if (recording !== undefined) {
            recordings.push(recording);
            $("#start-time").val(recording.start);
            $("#end-time").val(recording.end);
            // This ajax request is described here: http://stackoverflow.com/a/15571052 
            jsf.ajax.request("rec:add", null, {"javax.faces.behavior.event": "action", "execute": "@form", "render": "recording"});
        }
    }
    
    function stopClick() {
        var now = Date.now();
        
        if (!master_clock.isPaused()) {
            master_clock.pause(now);
            $("#pause").hide();
            $("#play").show();
        }
        
        $("#play").off("click");
        $("#pause").off("click");
        $("#stop").off("click");
        $(".category-item").off("click");
        
        $("#play").addClass("disabled");
        $("#pause").addClass("disabled");
        $(".category-item").addClass("disabled");
        
        var time = master_clock.getElapsedTime(now);
        
        for (var i in categories) {
            var category = categories[i];
            if (category.down) {
                addRecording(category.click(time));
            }
        }
        
        console.log(recordings);
    }
    
    $("#pause").hide();
    $("#stop").addClass("disabled");
    $("#play").click(playClick);
    $("#pause").click(pauseClick);
    $("#stop").click(stopClick);
    $("#total-time").append(document.createTextNode("00:00"));

    function categoryClick() {
        var id = $(this).attr("id");
        var index = parseInt(id.split("_")[1]);
        var category = categories[index];
        var time = master_clock.getElapsedTime(Date.now());
        addRecording(category.click(time));
    }

    $(".category-item").click(categoryClick);
    
    function tick() {
        var time = master_clock.getElapsedTime(Date.now());
        var time_str = timeToString(time);
        $("#total-time").empty();
        $("#total-time").append(document.createTextNode(time_str));
        for (var i in categories) {
            categories[i].updateTimer(time);
        }
    }

    setInterval(tick, 200);
});
