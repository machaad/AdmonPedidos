(function () {
    var app = angular.module('app.settings', []);
    app.factory("SettingsService", [
        '$resource', '$http',
        function ($resource, $http) {
            var res = $resource('api/v1/crud/settings', {}, {
                update: {
                    method: 'PUT'
                }
            });
            res.testEmailSettings = function (settings) {
                return $http.post("api/v1/settings/testEmail", settings, {defaultErrorHandler: false})
            };
            return res;
        }]);
    app.controller("SettingsCtrl", [
        '$scope', 'SettingsService', '$mdToast',
        function ($scope, SettingsService, $mdToast) {

            $scope.entry = {};
            SettingsService.get({}, function (data) {
                $scope.entry = data;
            });
            $scope.submit = function () {
                SettingsService.update($scope.entry, function () {
                    alert("Ok");
                });
            };
            $scope.testEmail = function () {
                $scope.emailBusy = true;
                SettingsService
                    .testEmailSettings($scope.entry.email)
                    .then(
                        function (data) {
                            $scope.emailBusy = false;
                        }
                        , function (response) {
                            $scope.emailBusy = false;
                            if (response.data && response.data.errors) {
                                var error = response.data.errors[0];
                                var toast = $mdToast.simple();
                                toast.textContent(error.message);
                                $mdToast.show(toast);
                            }
                        });
            }
        }]);
}());