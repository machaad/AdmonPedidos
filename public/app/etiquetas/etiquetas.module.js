(function(){
    var app  = angular.module('app.etiquetas',['ui.router','ngResource']);

    /**
     * Servicio para las acciónes relacionadas a los canales de venta
     */
    app.factory("EtiquetasService",function($resource){
        return $resource('api/v1/crud/etiquetas/:id',{id:'@_id'},{
            update: {
                method: 'PUT'
            }
        });
    });

    /**
     * Controlador para la pantalla de lista de canales de venta.
     */
    app.controller("EtiquetasListCtrl",function($scope, EtiquetasService, TableTemplateService,
            $mdDialog, AuthStorage, Permission,$mdToast,$filter){

        var filterSearch = ["name"];

        var table = TableTemplateService.setup($scope, EtiquetasService, filterSearch);

        $scope.userData = AuthStorage.get("data");

        /**
         * Valida si la acción ingresada es permitida
         */
        $scope.isAllowed = function(action){
            return Permission.isAllowed("Etiquetas", action);
        };

        /**
         * Abre el formulario para la creación de un
         * nuevo canal de venta.
         */
        $scope.showForm = function($event, target){
            $mdDialog.show({
                    controller: 'EtiquetasFormCtrl',
                    templateUrl: 'app/Etiquetas/Etiquetas.form.html',
                    parent: angular.element(document.body),
                    targetEvent: $event,
                    clickOutsideToClose:false,
                    locals :{
                        target : target
                    },
                    fullscreen: true
                })
                .then(function(answer) {
                    table.refresh(true);
                    console.log("#########" + answer);
                }, function() {
                    console.log("cancel");
                });
        };


        $scope.onEdit = function($event,target){
            $scope.showForm($event,target)
        };

        $scope.showConfirm = function(ev,target) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title('Deseas borrar el canal?')
                .textContent('El canal se borrara permanentemente y no podrá visualizarse nuevamente')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('OK')
                .cancel('Cancelar');
            $mdDialog.show(confirm).then(function() {
                $scope.onDelete(target);

            }, function() {

            });
        };

        $scope.onDelete = function(target){
            EtiquetasService.delete({id: target.id}, function success(data,headers) {
                table.refresh(true);
                var toast = $mdToast.simple();
                toast.textContent($filter('translate')('view.Etiquetas.deleted'));
                $mdToast.show(toast);
            },function error(data,headers){
                console.log("error");
            })
        }
    });


    /**
     * Controlador para el formulario de los canales de venta.
     */
    app.controller("EtiquetasFormCtrl",function($scope, $mdDialog,
            ArraySimpleFilter, EtiquetasService, target,$mdToast,$filter){

        var isEdition = angular.isDefined(target);

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.submit = function() {
            if(isEdition){
                EtiquetasService.update({id:$scope.entity.id},$scope.entity,
                function success(data,headers){
                    var toast = $mdToast.simple();
                    toast.textContent($filter('translate')('view.Etiquetas.updated'));
                    $mdToast.show(toast);
                },function error (data,headers){
                            console.log("error");
                    }
                );
            }else{
                EtiquetasService.save($scope.entity, function success(data,headers){
                        var toast = $mdToast.simple();
                        toast.textContent($filter('translate')('view.Etiquetas.saved'));
                        $mdToast.show(toast);
                },function error (data,headers){
                        console.log("error");
                    }
                );
            }
            $mdDialog.hide();
        };

        $scope.entity = {};

        if(target){
            EtiquetasService.get({id: target.id},function(data){
                $scope.entity = data;
            });
        }
    });


}());