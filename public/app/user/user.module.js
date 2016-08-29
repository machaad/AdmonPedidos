(function () {
    var app = angular.module('app.user', ['ngResource']);

    app.factory("UserService", ['$resource',
        function ($resource) {
            return $resource('api/v1/crud/user/:id', {id: '@_id'}, {
                update: {
                    method: 'PUT'
                }
            });
        }]);
    app.controller("UserListCtrl", [
        '$scope', 'UserService', 'TableTemplateService', '$mdDialog', '$localStorage', '$rootScope',
        function ($scope, UserService, TableTemplateService, $mdDialog, $localStorage, $rootScope) {
            var filterSearch = ["givenName", "familyName", "email"];
            var table = TableTemplateService.setup($scope, UserService, filterSearch);
            $scope.userData = $localStorage.user;
            $scope.showForm = function ($event, target) {
                $mdDialog.show({
                    controller: 'UserFormCtrl',
                    templateUrl: 'app/user/user.form.html',
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
                UserService.delete({id: target.id}, function () {
                    table.refresh(true)
                })
            };
        }]);

    app.controller("UserFormCtrl", [
        '$scope', '$mdDialog', 'ArraySimpleFilter', 'UserService', 'RoleService', 'target',
        function ($scope, $mdDialog, ArraySimpleFilter, UserService, RoleService, target) {
            var isEdition = angular.isDefined(target);
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            $scope.submit = function () {
                if (isEdition) {
                    UserService.update({id: $scope.entity.id}, $scope.entity);
                } else {
                    UserService.save($scope.entity);

                }
                $mdDialog.hide();
            };
            $scope.misc = {
                roles: []
            };
            $scope.entity = {
                roles: []
            };
            if (target) {
                UserService.get({id: target.id}, function (data) {
                    $scope.entity = data;
                });
            }

            RoleService.get({}, function (res) {
                $scope.misc.roles = res.list;
            });
        }]);


}());