(function () {
    var app = angular.module('app.login', ['ui.router', 'ngResource']);
    app.controller('LoginCtrl', [
        '$scope', 'AuthService', '$mdDialog',
        function ($scope, AuthService, $mdDialog) {
            $scope.data = {};
            $scope.login = function () {
                AuthService.signIn($scope.data.email, $scope.data.password);
            };
            $scope.passwordRecovery = function ($event) {
                $mdDialog.show({
                    controller: 'PasswordCtrl',
                    templateUrl: 'app/user/password.recovery.html',
                    parent: angular.element(document.body),
                    targetEvent: $event,
                    clickOutsideToClose: false,
                    fullscreen: true
                })
                .then(function (answer) {
                    // table.refresh(true);
                });
            };
        }]);

    app.controller("PasswordCtrl", [
        '$scope', 'PasswordService', '$mdDialog',
        function ($scope, PasswordService, $mdDialog) {
            $scope.recover = function () {
                PasswordService.passwordRecovery($scope.email);
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.hide();
            }
        }]);
    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider.state('signIn', {
            url: '/signIn',
            templateUrl: 'app/login/login.html',
            controller: "LoginCtrl"
        });
    }]);

    app.factory("UserAction", [
        '$localStorage', '$rootScope',
        function ($localStorage, $rootScope) {

            var permissionChecker = {
                isAllowed: function (action) {
                    return !($localStorage.user
                        && $localStorage.user.excludedActions
                        && $localStorage.user.excludedActions[action]);
                }

            };
            $rootScope.isAllowed = function (permission) {
                return permissionChecker.isAllowed(permission);
            };

            return permissionChecker;
        }]);
    app.factory("PasswordService", [
        '$http', '$mdToast', '$filter',
        function ($http, $mdToast, $filter) {
            return {
                passwordRecovery: function (email) {
                    return $http({
                        method: "GET",
                        url: "api/v1/user/passwordRecovery",
                        params: {email: email}
                    }).then(function (e) {
                        var toast = $mdToast.simple();
                        toast.textContent($filter('translate')('view.login.msg.passwordSent'));
                        $mdToast.show(toast);
                    })
                }
            }
        }]);
    app.factory("AuthService", [
        '$resource', '$state', '$localStorage', '$mdToast', '$filter', 'appSettings',
        function ($resource, $state, $localStorage, $mdToast, $filter, appSettings) {
            var res = $resource('api/v1/auth/signIn');

            return {
                signIn: function (email, password) {
                    var credentials = {email: email, password: password, clientId: appSettings.clientId};
                    res.save(credentials,
                        function success(data, headers) {
                            var excludedActions = {};
                            angular.forEach(data.permissions,function(permission){
                                angular.forEach(permission.excludedActions, function(excludedAction){
                                    excludedActions[permission.name + "." + excludedAction] = false;
                                });
                            });
                            data.excludedActions = excludedActions;
                            $localStorage.user = data;
                            $state.go("app.userList");
                        },
                        function error(data, headers) {
                            var toast = $mdToast.simple();
                            toast.textContent($filter('translate')('error.auth.invalidCredentials'));
                            $mdToast.show(toast);
                        });
                },
                logout: function () {
                    $localStorage.user = undefined;
                    $state.go("signIn")
                }
            }
        }]);

    app.factory('AuthInterceptor', [
        '$localStorage', '$injector', '$q',
        function ($localStorage, $injector, $q) {
            return {
                request: function (config) {
                    config.headers = config.headers || {};
                    var authData = $localStorage.user;
                    if (authData && authData.auth) {
                        config.headers.Authorization = authData.auth.token;
                    }
                    return config;
                },
                responseError: function (response) {
                    var $mdToast = $injector.get('$mdToast');
                    var $filter = $injector.get('$filter');
                    var AuthService = $injector.get('AuthService');
                    var toast = $mdToast.simple();
                    if (response.status === 401) {
                        var authData = $localStorage.user;
                        if (authData && authData.auth) {
                            AuthService.logout();
                            toast.textContent($filter('translate')('error.server.httpCode.401'));
                            $mdToast.show(toast);
                        }
                    } else if (response.status === 403) {
                        AuthService.logout();
                        toast.textContent($filter('translate')('error.server.httpCode.403'));
                        $mdToast.show(toast);
                    }
                    else if (response.status === 400 && response.data && response.data.errors) {
                        if (response.config.defaultErrorHandler === undefined || response.config.defaultErrorHandler) {
                            var $translate = $injector.get('$translate');
                            var error = response.data.errors[0];
                            toast.textContent($filter('translate')('error.' + error.module + "." + error.field + "." + error.message));
                            $mdToast.show(toast);
                        }
                    } else if (response.status === 500) {
                        toast.textContent($filter('translate')('error.server.httpCode.500'));
                        $mdToast.show(toast);
                    }

                    return $q.reject(response);
                }
            };
        }]);

    app.config([
        '$httpProvider',
        function ($httpProvider) {
            $httpProvider.interceptors.push('AuthInterceptor');
        }]);

    app.directive(
        "showWithPermission", [
            'UserAction', '$animate',
            function (UserAction, $animate) {
                function link(scope, element, attributes) {
                    $animate[UserAction.isAllowed(attributes.showWithPermission) ? 'removeClass' : 'addClass'](element, "ng-hide", {
                        tempClasses: "ng-hide-animate"
                    });
                }
                // Return the directive configuration.
                return ({
                    link: link,
                    restrict: "A"
                });
            }
        ]);
}());