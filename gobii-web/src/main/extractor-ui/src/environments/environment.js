// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
System.register([], function (exports_1, context_1) {
    "use strict";
    var environment;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {// This file can be replaced during build by using the `fileReplacements` array.
            // `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
            // The list of file replacements can be found in `angular.json`.
            exports_1("environment", environment = {
                production: false,
                keycloak: {
                    issuer: 'http://localhost:8181/auth',
                    realm: 'Gobii',
                    clientId: 'extractor-ui-gobii-dev'
                }
            });
            /*
             * For easier debugging in development mode, you can import the following file
             * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
             *
             * This import should be commented out in production mode because it will have a negative impact
             * on performance if an error is thrown.
             */
            // import 'zone.js/dist/zone-error';  // Included with Angular CLI.
        }
    };
});
//# sourceMappingURL=environment.js.map