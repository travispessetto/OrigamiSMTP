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
		for(var index in releases)
		{
			var release = releases[index];
			var releaseRow = "<tr><th colspan=\"2\">"+release.tag_name+"</th></tr>";
			downloads.append(releaseRow);
			for(var fileIndex in release.assets)
			{
				var file = release.assets[fileIndex];
				var link = '<tr><td><a href="'+file.browser_download_url+'">'+file.name+'</a>';
				link += '</td><td class="text-right">'+file.download_count + ' Downloads</td></tr>';
				downloads.append(link);
			}
		}
	}).fail(function(xhr,status,error){alert("Failed to load downloads: " + error);});
}