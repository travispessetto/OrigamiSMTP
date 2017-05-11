$(document).ready(function()
{
	loadDownloads();
});

var loadDownloads = function()
{
	var downloads = $("#download-sources");
	console.log(downloads);
	$.get("downloads.json",function(data,status,jqXHR)
	{
		var versions = data.versions;
		for(var version in versions)
		{
			downloads.append("<tr><th>"+version+"</th></tr>");
			var downloadLinks = "<tr><td>";
			var first = true;
			for(var index in versions[version])
			{
				if(!first)
				{
					downloadLinks += " | ";
				}
				for(var type in versions[version][index])
				{
					downloadLinks += '<a href="'+versions[version][index][type]+'">'+type+'</a>';
				}
				first = false;
			}
			downloadLinks += "</td></tr>";
			downloads.append(downloadLinks);
		}
	}).fail(function(xhr,status,error){alert("Failed to load downloads: " + error);});
}