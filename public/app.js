(function () {
    var app = angular.module('appName', [
        'ngMessages',
        'ngMaterial',
        'ngMdIcons',
        'ngResource',
        'ngStorage',
        'ngSanitize',
        'md.data.table',
        'pascalprecht.translate',
        'underscore',
        'settings',
        'app.common.directives',
        'app.common.services',
        'app.routes',
        'app.menu',
        'app.login',
        'app.role',
        'app.user',
        'app.settings',
        'app.etiquetas',
        'app.producto',
        'app.channel'
    ]);

    app.config([
        '$translateProvider', function ($translateProvider) {
            $translateProvider.useStaticFilesLoader({
                prefix: 'i18n/locale-',// path to translations files
                suffix: '.json'// suffix, currently- extension of the translations
            });
            $translateProvider.preferredLanguage('es');// is applied on first load
            //$translateProvider.useLocalStorage();// saves selected language to localStorage
            $translateProvider.useMissingTranslationHandlerLog();
            $translateProvider.useSanitizeValueStrategy('escape');
        }]);

    app.config([
        '$mdThemingProvider',
        function ($mdThemingProvider) {
            $mdThemingProvider.theme('default')
                .primaryPalette('blue-grey')
                .accentPalette('deep-orange');

            $mdThemingProvider.theme('menu-theme', 'default')
                .primaryPalette('blue-grey')
                .dark();
            // Configure a dark theme with primary foreground yellow
            $mdThemingProvider.theme('docs-dark', 'default')
                .primaryPalette('yellow')
                .dark();

        }]);
}());