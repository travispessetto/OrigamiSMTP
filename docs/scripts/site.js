// global stuff
var lastScrollTop = 0;
var scrollPage = 0;
var canScroll = true;

// angular stuff
var app = angular.module('origamiDownloads',[]);
app.controller('origamiController', function($scope,$http,$sce){
  $scope.method = "JSONP";
  $scope.url = "https://api.github.com/repos/travispessetto/OrigamiGUI/releases";
  $http({method: $scope.method, url: $sce.trustAsResourceUrl($scope.url)})
  	.then(function(response)
  	{
  		$scope.status = status;
  		$scope.versions = response.data.data;
  	},
  	function(response) {
       console.error(response);
  	});
});

// jquery stuff
$(document).ready(function()
{
	$("a").on('click',scrollToAnchor);
	centerContainers();
});

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