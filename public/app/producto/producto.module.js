(function () {
    var app = angular.module('app.producto', ['ngResource']);

    app.factory("ProductoService", ['$resource',
        function ($resource) {
            return $resource('api/v1/crud/producto/:id', {id: '@_id'}, {
                update: {
                    method: 'PUT'
                }
            });
        }]);
    app.controller("ProductoListCtrl", [
        '$scope', 'ProductoService', 'TableTemplateService', '$mdDialog', '$localStorage', '$rootScope',
        function ($scope, ProductoService, TableTemplateService, $mdDialog, $localStorage, $rootScope) {
            var filterSearch = ["givenName"];
            var table = TableTemplateService.setup($scope, ProductoService, filterSearch);
            $scope.productoData = $localStorage.user;
            $scope.showForm = function ($event, target) {
                $mdDialog.show({
                    controller: 'ProductoFormCtrl',
                    templateUrl: 'app/producto/producto.form.html',
                    parent: angular.element(document.body),
                    targetEvent: $event,
                    clickOutsideToClose: true,
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
            	ProductoService.delete({id: target.id}, function () {
                    table.refresh(true)
                })
            };
        }]);

    app.controller("ProductoFormCtrl", [
        '$scope', '$mdDialog', 'ArraySimpleFilter', 'ProductoService', 'target',
        function ($scope, $mdDialog, ArraySimpleFilter, ProductoService, target) {
            var isEdition = angular.isDefined(target);

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            $scope.submit = function () {
                if (isEdition) {
                	ProductoService.update({id: $scope.entity.id}, $scope.entity);
                } else {
                	ProductoService.save($scope.entity);

                }
                $mdDialog.hide();
            };

            if (target) {
            	ProductoService.get({id: target.id}, function (data) {
                    $scope.entity = data;
                });
            }

        }]);


}());
