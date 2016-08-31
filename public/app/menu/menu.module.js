(function(){
    var app = angular.module("app.menu", []);
    app.factory("MenuService", [
        '$mdSidenav',
        function($mdSidenav){
        function close(){
            $mdSidenav('left').close();
        }
        function toggle(){
            $mdSidenav('left').toggle();
        }

        return {
            setupMenu : function(scope,module){
                scope._menu = {module : module};
                scope._menu.close = function () {
                    close();
                };
                scope._menu.toggle = function(){
                    toggle();
                };

            }
        }
    }]);

    app.controller('MenuCtrl', [
        '$scope','$timeout','$mdSidenav','$log','AuthService','$localStorage','MenuService',
        function ($scope, $timeout, $mdSidenav, $log,AuthService,$localStorage,MenuService) {
        MenuService.setupMenu($scope);
        $scope.logout = function(){
            AuthService.logout();
        };
        var data =  $localStorage.user;
        var permissions = data && data.permissions ? data.permissions : [];
        /**
         * Guarda la configuracion de la presentacion de la opcion de menu
         * como el icono y el state al que esta ligado
         * @type {{module1: {icon: string, state: string}, module2: {icon: string}}}
         */
        var menuInnerProperties = {
            apkManager : {icon : 'people',state:"app.apkManagerList"},
            pedidos : {icon : 'material-icons',state:"app.userList"},
            producto : {icon : 'assignment',state:"app.producto"},
            clients :  {icon : 'contact_phone',state:"app.clients"},
            user : {icon : 'people',state:"app.userList"},
            role : {icon : 'assignment_ind',state:"app.roleList"},
            settings :  {icon : 'settings',state:"app.settings"}
        };
        $scope.menu = [];
        angular.forEach(permissions,function(permission){
            var menuProperties = menuInnerProperties[permission.name];
            if(!menuProperties){
                $log.error("El modulo " + permission.name + " no tiene una configuracion de menu")
            }
            $scope.menu.push({
                name : permission.name,
                state : menuProperties ? menuProperties.state : "/",
                icon : menuProperties ? menuProperties.icon : 'info_outline'});
        });
    }]);
}());
