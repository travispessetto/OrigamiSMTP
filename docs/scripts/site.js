// global stuff
var lastScrollTop = 0;
var scrollPage = 0;
var canScroll = true;

// jquery stuff
$(document).ready(function()
{
	$("a").on('click',scrollToAnchor);
	centerContainers();
	loadDownloads();
});

var loadDownloads = function()
{
	var url = "https://api.github.com/repos/travispessetto/OrigamiGUI/releases";
	var isPrerelease = false;
	$.get(url,function(data)
	{
		var nonPreReleaseIndex = 0;
		while(nonPreReleaseIndex < data.length)
		{
			if(data[nonPreReleaseIndex].prerelease)
			{
				++nonPreReleaseIndex;
			}
			else
			{
				break;
			}
		}
		var version = data[nonPreReleaseIndex];
		$("body :not(script)").contents().filter(function()
		{
			return this.nodeType == 3;
		}).replaceWith(function()
		{
			return this.nodeValue.replace("{{version.tag_name}}",version.tag_name);
		});
		loadAssets(version);
	});
}

var loadAssets = function(version)
{
	var assets = version.assets;
	for(var i = 0; i < assets.length; ++i)
	{
		var asset = assets[i];
		console.log(asset.content_type);
		if(asset.content_type.includes("msdos"))
		{
			var link = $('a[href="{{asset.windows}}"]');
			link.prop("href",asset.browser_download_url);
		}
		else if(asset.content_type.includes("debian"))
		{
			$('a[href="{{asset.debian}}"]').prop("href",asset.browser_download_url);
		}
		else if(asset.content_type.includes("java"))
		{
			$('a[href="{{asset.java}}"]').prop("href",asset.browser_download_url);
		}

	}	
}

var scrollToAnchor = function(event)
{
	if(canScroll && this.hash != "")
	{
		event.preventDefault();
		var hash = this.hash;
		var pages = $(".fullpage");
		var index = 0;
		pages.each(function()
		{
			if("#"+$(this).attr('id') == hash)
			{
				scrollToLocation($(this).offset().top);
				scrollPage = index;
			}
			++index;
		});
	}
}

var scrollToNextPage = function()
{
	if(canScroll)
	{
		var st = $(document).scrollTop();
		var down = st > lastScrollTop;
		var up = st < lastScrollTop;
		var pages = $(document).find('.fullpage');
		if(down)
		{
			++scrollPage;
		}
		if(up)
		{
			--scrollPage;
		}
		if(scrollPage < 0)
		{
			scrollPage = 0;
		}
		if(scrollPage >= pages.length)
		{
			scrollPage = pages.length - 1;
		}
		var page = pages[scrollPage];
		console.log("scroll page is : " + scrollPage);
		var locationTop = $(page).offset().top;
		scrollToLocation(locationTop);
	}
}


var scrollToLocation = function(locationTop)
{
	if(canScroll)
	{
		console.log("perform scroll event");
		canScroll = false;
		$('html, body').animate({scrollTop: locationTop}).promise().done(function()
		{
			lastScrollTop = $(document).scrollTop();
			canScroll = true;
			$('html, body').css('overflow','visible');
			console.log("scroll event complete");
		});
	}
}

var centerContainers = function()
{
	$('.center-container').each(function()
	{
		var page = $(this).closest('.fullpage');
		var pageHeight = page.height();
		var contentHeight = $(this).height();
		var newPadding = (pageHeight - contentHeight) / 2;
		$(this).css('padding-top',newPadding+"px");
	});
}