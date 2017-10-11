var app = angular.module('origamiDownloads',[]);
app.controller('origamiController', function($scope,$http,$sce){
  $scope.method = "JSONP";
  $scope.url = "https://api.github.com/repos/travispessetto/OrigamiGUI/releases";
  $http({method: $scope.method, url: $sce.trustAsResourceUrl($scope.url)})
  	.then(function(response)
  	{
  		$scope.status = status;
  		console.log(status);
  		$scope.versions = response.data.data;
  	},
  	function(response) {
       console.error(response);
  	});
});