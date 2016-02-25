
$(document).ready(function() {
    var time_running = false;
    var total_time = 0;
    
    function timeString(t) {
        var min = Math.floor(t / 60);
        var sec = t - min * 60;
        return  (min < 10 ? "0" + min : min) + ":" +
                (sec < 10 ? "0" + sec : sec);
    }
    
    function updateTime() {
        if (time_running) {
            total_time++;
        }
        var str = timeString(total_time);
        $("#total-time").empty();
        $("#total-time").append(document.createTextNode(str));
    }
    
    updateTime();
    
    $("#pause").hide();
    $("#play").click(function() {
        $("#play").hide();
        $("#pause").show();
        time_running = true;
    });

    $("#pause").click(function() {
        $("#pause").hide();
        $("#play").show();
        time_running = false;
    });
    
    var category_names = [
        "Järjestelyt",
        "Tehtävän selitys",
        "Ohjaus",
        "Palautteen anto",
        "Tarkkailu",
        "Muu toiminta",
        "Oppilas suorittaa tehtävää"
//        "..."
//        "Dummy data1",
//        "Dummy data2",
//        "Dummy data3",
//        "Dummy data4"
    ];

    var categories = [];

    function updateTimer(index) {
        var div = categories[index].timer_div;
        var str = timeString(categories[index].timer);
        div.empty();
        div.append(document.createTextNode(str));
    }

    function createCategoryItem(index) {
        var category = category_names[index];
        
        var li = $(document.createElement("li"));
        li.addClass("category-item");
        li.attr("id", "category-item_" + index);
        
        var timer_div = $(document.createElement("div"));
        timer_div.addClass("category-timer");
        
        var name_div = $(document.createElement("div"));
        name_div.addClass("category-name");
        name_div.append(document.createTextNode(category));
        
        li.append(timer_div);
        li.append(name_div);
        
        categories.push({
            down: false,
            timer: 0,
            timer_div: timer_div
        });
        updateTimer(index);
        
        return li;
    }

    for (var i in category_names) {
        $("#category-list").append(createCategoryItem(i));
    }

    function categoryClick() {
        var elem = $(this);
        var id = elem.attr("id").split("_");
        var index = parseInt(id[1]);
        var category = categories[index];

        if (category.down) {
            elem.removeClass("down");
            category.down = false;
        } else {
            elem.addClass("down");
            category.down = true;
        }
    }

    $(".category-item").click(categoryClick);
    
    function tick() {
        updateTime();
        for (var i in categories) {
            var category = categories[i];
            if (time_running && category.down) {
                category.timer++;
                updateTimer(i);
            }
        }
    }

    setInterval(tick, 1000);
});
