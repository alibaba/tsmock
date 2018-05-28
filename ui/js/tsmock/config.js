function loadConfig() {
	var url = "/ui/ws/config/http";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("GET", url, false);
    xmlHttpRequest.onreadystatechange = function ()
    {
        if(xmlHttpRequest.readyState === 4)
        {
            if(xmlHttpRequest.status === 200 || xmlHttpRequest.status == 0)
            {
                var allText = xmlHttpRequest.responseText;
				JsonObj = JSON.parse(allText);
                document.getElementById("id_config_content").innerHTML = JSON.stringify(JsonObj,undefined,2);
            }
        }
    }
    xmlHttpRequest.send(null);
}


function saveConfig() {
	var url = "/ui/ws/config/http";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("POST", url, false);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/json");
    xmlHttpRequest.send(document.getElementById("id_config_content").innerHTML);
}

function startTSMock() {
	var url = "/ui/ws/start/http";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("PUT", url, false);
    xmlHttpRequest.send();
}

function stopTSMock() {
	
}