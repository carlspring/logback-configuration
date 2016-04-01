var tailFilesApp = angular.module("tailFilesApp",[]);

tailFilesApp.controller("TailFilesController", function ($scope, $http) {
    $scope.filename = "log.log";
    $scope.newlog = "";

    // http://localhost:48080/logging/partial-log/test.log?offset=0

    $scope.contextBaseUrl = "http://localhost:48080";
    $scope.logfile = "test.log";
    $scope.offset = 0;

    //$scope.url = "/logging/partial-log/" + $scope.logfile + "?offset=" + offset;

    setInterval(function() {
        var url = $scope.contextBaseUrl + "/logging/partial-log/" + $scope.logfile + "?offset=" + $scope.offset;
        $http.get(url).then(function(response) {
            var data = response.data;
            alert(data);
        });
    }, 10000);

});