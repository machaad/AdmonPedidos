(function(){
    var app  = angular.module('app.common.directives', []);


    app.directive('ixTableToolbar', [
        "UserAction",
        function(UserAction) {
        return {
            templateUrl : 'template/ix.table.toolbar.html',
            scope: {
                selected : '=',
                query : '=',
                onEdit :'&?',
                onDelete :'&?',
                title : '@',
                deletePermission : "@",
                editPermission : "@"
            },
            link: function(scope, element) {
                scope.action = {
                    edit : angular.isDefined(scope.onEdit) && UserAction.isAllowed(scope.editPermission),
                    delete : angular.isDefined(scope.onDelete)  && UserAction.isAllowed(scope.deletePermission)
                };
                scope.query.search = '';
                scope._toolbar = {};
                scope._toolbar.showSearch = false;
                scope._toolbar.toggleSearch = function(){
                    scope._toolbar.showSearch = !scope._toolbar.showSearch;
                }
            }
        };
    }]);

    app.directive('passwordConfirm', [
        function () {
        return {
            restrict: 'A',
            scope: {
                matchTarget: '='
            },
            require: 'ngModel',
            link: function link(scope, elem, attrs, ctrl) {
                var validator = function (value) {
                    ctrl.$setValidity('match', value === scope.matchTarget);
                    return value;
                };

                ctrl.$parsers.unshift(validator);
                ctrl.$formatters.push(validator);

                // This is to force validator when the original password gets changed
                scope.$watch('matchTarget', function(newval, oldval) {
                    validator(ctrl.$viewValue);
                });

            }
        };
    }]);

}());