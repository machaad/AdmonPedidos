(function () {
    var app = angular.module('app.clients', ['ngResource']);

    app.factory("ClientsService", ['$resource',
        function ($resource) {
            return $resource('api/v1/crud/clients/:id', {id: '@_id'}, {
                update: {
                    method: 'PUT'
                }
            });
        }]);
    app.controller("ClientsListCtrl", [
        '$scope', 'ClientsService', 'TableTemplateService', '$mdDialog', '$localStorage', '$rootScope',
        function ($scope, ClientsService, TableTemplateService, $mdDialog, $localStorage, $rootScope) {
            var filterSearch = ["name"];
            var table = TableTemplateService.setup($scope, ClientsService, filterSearch);
            $scope.clientsData = $localStorage.user;
            $scope.showForm = function ($event, target) {
                $mdDialog.show({
                    controller: 'ClientsFormCtrl',
                    templateUrl: 'app/clients/clients.form.html',
                    parent: angular.element(document.body),
                    targetEvent: $event,
                    clickOutsideToClose: false,
                    locals: {
                        target: target
                    },
                    fullscreen: true
                })
                    .then(function (answer) {
                        table.refresh(true);
                    });
            };
            $scope.onEdit = function ($event, target) {
                $scope.showForm($event, target)
            };
            $scope.onDelete = function ($event, target) {
            	ClientsService.delete({id: target.id}, function () {
                    table.refresh(true)
                })
            };
        }]);

    app.controller("ClientsFormCtrl", [
        '$scope', '$mdDialog', 'ArraySimpleFilter', 'ClientsService', 'target',
        function ($scope, $mdDialog, ArraySimpleFilter, ClientsService, target) {
            var isEdition = angular.isDefined(target);
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            $scope.submit = function () {
                if (isEdition) {
                	ClientsService.update({id: $scope.entity.id}, $scope.entity);
                } else {
                	ClientsService.save($scope.entity);

                }
                $mdDialog.hide();
            };

            if (target) {
            	ClientsService.get({id: target.id}, function (data) {
                    $scope.entity = data;
                });
            }

        }]);


}());
