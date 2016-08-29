(function () {
    var app = angular.module('app.common.services', []);

    app.factory("ServerMessage", [
        '$filter', '$mdToast',
        function ($filter, $mdToast) {
            this.showError = function (error) {
                if (error || error.module || error.field || error.message) {
                    var label = "error.server." + error.module + "." + error.field + "." + error.message;
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent($filter('translate')(label))
                            .hideDelay(3000)
                    );
                }
            }
            return this;
        }]);
    app.factory("ArraySimpleFilter", function () {

        /**
         * Filtro simple de un arreglo. Si query es nulo o vacio, regresa el arreglo completo
         * @param array Arreglo con objetos sobre los que se hara la busqueda
         * @param query Texto a buscar
         * @param fieldName Campo sobre el que se buscara el texto
         * @returns Arreglo filtrado
         */
        this.filter = function (array, query, fieldName) {
            return query ? array.filter(createFilterFor(query, fieldName)) : array.filter(createFilterFor('', fieldName));
        };

        function createFilterFor(query, fieldName) {
            var lowercaseQuery = angular.lowercase(query);
            return function filterFn(item) {
                return (angular.lowercase(item[fieldName]).indexOf(lowercaseQuery) === 0) ||
                    (angular.lowercase(item[fieldName]).indexOf(lowercaseQuery) === 0);
            };
        }

        return this;
    });

    app.factory("TableTemplateService", [
        '$q',
        function ($q) {
            return {
                setup: function ($scope, dataService, filterSearch) {
                    $scope.table = {};
                    $scope.table.selected = [];
                    if (!filterSearch) {
                        filterSearch = [];
                    }
                    $scope.table.query = {
                        order: '',
                        limit: 10,
                        page: 1,
                        search: '',
                        fields: filterSearch
                    };
                    $scope.table.refresh = function (clearSelection) {
                        var deferred = $q.defer();
                        $scope.table.promise = deferred.promise;
                        dataService.get($scope.table.query, function (res) {
                            $scope.table.data = res;
                            deferred.resolve(res);
                            if (clearSelection) {
                                $scope.table.selected = [];
                            }
                        }, function (res) {
                            deferred.reject();
                        });
                    };
                    var onQueryChange = function (oldValue, newValue) {
                        if (oldValue !== newValue) {
                            $scope.table.refresh(false);
                        }
                    };
                    $scope.table.refresh(false);
                    $scope.$watch("table.query.order", onQueryChange);
                    $scope.$watch("table.query.limit", onQueryChange);
                    $scope.$watch("table.query.page", onQueryChange);
                    $scope.$watch("table.query.search", onQueryChange);
                    $scope.$watch("table.query.fields", onQueryChange);
                    return $scope.table;
                }
            }
        }]);


}());