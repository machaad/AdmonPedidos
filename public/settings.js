(function(){
    var app = angular.module('settings',[]);

    /**
     * Valores de configuracion por defecto. Agregar cuantos sean necesarios
     */
    var defaultSettings = {
    };
    /**
     * Prefijo  para guardar la configuracion por defecto en localstorage
     */
    var prefix = "_appSettings.";
    var appSettings = {
        clientId : 'qAV40pgxhpXMNmLqgLJ9VmMmhrKxRoK1P9tzsdNH'
    };

    /**
     * Merge entre la configuracion por defecto y lo que se tenga en el local storage, en caso
     * de que la configuracion haya sido editada por el usuario
     */
    for (var property in defaultSettings) {
        if (defaultSettings.hasOwnProperty(property)) {
            var value = localStorage.getItem(prefix + property);
            if (!value) {
                value = defaultSettings[property];
                localStorage.setItem(prefix + property, value);
            }
            appSettings[property] = value;
        }
    }


    /**
     * Con esto nuestros contraloderes podran injectar la dependencia a appSettings y acceder a los datos
     * tal cual un objeto de JS. Ejemplo appSettings.clientId
     */
    app.provider("appSettings", function () {
        return {
            $get: function () {
                return appSettings;
            }
        }
    });
}());