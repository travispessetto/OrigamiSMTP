$(document).ready(function()
{
	loadDownloads();
});

var loadDownloads = function()
{
	var downloads = $("#download-sources");
	console.log(downloads);
	$.get("https://api.github.com/repos/travispessetto/OrigamiGUI/releases",function(releases,status,jqXHR)
	{
		for(var release in releases)
		{
			var releaseRow = "<tr><th>"+release.tag_name+"</th></tr>";
			downloads.append(releaseRow);
			for(var file in releases.assets)
			{
				var link = '<tr><td><a href="'+file.browser_download_url+'">'+file.name+'</a>';
				link += ' (Download Count : ' + file.download_count + ')'</td></tr>;
				downloads.append(link);
			}
		}
	}).fail(function(xhr,status,error){alert("Failed to load downloads: " + error);});
}