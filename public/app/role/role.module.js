(function () {
    var app = angular.module('app.role', []);

    app.factory("RoleService", [
        '$resource', function ($resource) {
            return $resource('api/v1/crud/role/:id', {id: '@_id'}, {
                update: {
                    method: 'PUT'
                }
            });
        }]);
    app.factory("ModuleService", [
        '$resource',
        function ($resource) {
            return $resource('api/v1/crud/module');
        }]);
    app.controller("RoleListCtrl", [
        '$scope', 'RoleService', 'TableTemplateService', '$state', '$mdDialog',
        function ($scope, RoleService, TableTemplateService, $state, $mdDialog) {
            var filterSearch = ["name"];
            var table = TableTemplateService.setup($scope, RoleService, filterSearch);
            $scope.showForm = function ($event, target) {
                $mdDialog.show({
                    controller: 'RoleFormCtrl',
                    templateUrl: 'app/role/role.form.html',
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
                    }, function () {
                        console.log("cancel");
                    });
            };
            $scope.onEdit = function ($event, target) {
                $scope.showForm($event, target)
            };
            $scope.onDelete = function ($event, target) {
                RoleService.delete({id: target.id}, function () {
                    table.refresh(true)
                })
            }
        }]);
    app.controller("RoleFormCtrl", [
        '$scope', '$mdDialog', 'RoleService', 'ModuleService', '_', 'target',
        function ($scope, $mdDialog, RoleService, ModuleService, _, target) {
            var isEdition = angular.isDefined(target);
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            $scope.submit = function () {
                if (isEdition) {
                    RoleService.update({id: $scope.entity.id}, $scope.entity);
                } else {
                    RoleService.save($scope.entity);

                }
                $mdDialog.hide();
            };

            $scope.entity = {
                permissions: []
            };
            if (target) {
                RoleService.get({id: target.id}, function (data) {
                    $scope.entity = data;
                });
                $scope.allModules = ModuleService.query({id: target.id});
            } else {
                $scope.allModules = ModuleService.query();
            }
            $scope.addPermission = function () {
                if ($scope.selectedModule) {
                    $scope.entity.permissions.unshift({
                        module: $scope.selectedModule
                    });
                    $scope.allModules = _.without($scope.allModules, $scope.selectedModule);
                    $scope.selectedModule = undefined;
                }
            };
            $scope.removePermission = function (permission) {
                $scope.entity.permissions = _.without($scope.entity.permissions, permission);
                $scope.allModules.push(permission.module);

            };
        }]);
}());