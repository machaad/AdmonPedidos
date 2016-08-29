(function(){
    var app = angular.module('app.routes',['ui.router']);
    app.config([
        '$stateProvider','$urlRouterProvider',
        function($stateProvider,$urlRouterProvider) {
        $stateProvider.state('app',{
            url : "/app",
            abstract : true,
            controller: "MenuCtrl",
            templateUrl:'app/menu/menu.html'
        });
        $stateProvider.state('app.userList',{
            url:'/userList',
            templateUrl:'app/user/user.list.html',
            controller : 'UserListCtrl'
        });
        $stateProvider.state('app.roleList',{
            url:'/roleList',
            templateUrl:'app/role/role.list.html',
            controller : "RoleListCtrl"
        });
        $stateProvider.state('app.settings',{
            url:'/settings',
            templateUrl:'app/settings/settings.html',
            controller : "SettingsCtrl"
        });
        $stateProvider.state('app.apkManagerList',{
            url:'/apkManagerList',
            templateUrl:'app/apk/apk.list.html',
            controller : "ApkManagerListCtrl"
        });
        $stateProvider.state('app.producto',{
            url:'/productoList',
            templateUrl:'app/producto/producto.list.html',
            controller : "ProductoListCtrl"
        });

        $stateProvider.state('app.etiquetas',{
            url:'/etiquetas',
            templateUrl:'app/etiquetas/etiquetas.list.html',
            controller : "EtiquetasCtrl"
        });

        $stateProvider.state('app.channel',{
            url:'/channel',
            templateUrl:'app/channel/channel.list.html',
            controller : "ChannelCtrl"
        });


        $urlRouterProvider.otherwise('/app/userList');
    }]);
}());
