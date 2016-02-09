$(function(){
    $("#loadSideBar").load("sidebar.html", function() {
        $("#constantsExpand").hide();
        $("#functionsExpand").hide();
        $("#gsExpand").hide();
        $("#lbeExpand").hide();

	// get the page we're on
	var url = document.URL;
	url = url.substring(url.lastIndexOf("/")+1);
	if (url.contains("#"))
		url = url.substring(0, url.indexOf("#"));

	// expand the right one based on the page
	if (url == "constants.html")
		$("#constantsExpand").slideToggle(1000);
	else if (url == "functions.html")
		$("#functionsExpand").slideToggle(1900);
	else if (url == "getting-started.html")
		$("#gsExpand").slideToggle(1000);
	else if (url == "learn-by-example.html")
		$("#lbeExpand").slideToggle(1000);
    }); 
});
