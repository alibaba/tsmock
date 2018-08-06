function loadConfig() {
	var url = "/ui/ws/config/http";
    $.get(url,function(data,status){
        var jsonStr = JSON.stringify(data,undefined,2);
        //JsonObj = JSON.parse(data);
        $("#id_config_content").val(jsonStr);
    });
}


function saveConfig() {
	var url = "/ui/ws/config/http";
    var configData = $("#id_config_content").val();
    var promise = $.ajax({
        url: url,
        type: 'POST',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: configData
    });


    //$.post(url, data);
    /*
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("POST", url, false);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/json");
    xmlHttpRequest.send(document.getElementById("id_config_content").innerHTML);
    */
}

function restartTSMock() {
    var url = "/ui/ws/restart/http";
    var promise = $.ajax({
        url: url,
        type: 'POST'
    });
}




$('#id_ul_tab a').click(function (e) {
    e.preventDefault()
    $(this).tab('show')
})