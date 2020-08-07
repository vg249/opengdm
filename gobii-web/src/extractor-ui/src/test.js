// This file is required by karma.conf.js and loads recursively all the .spec and framework files
System.register(["zone.js/dist/zone-testing", "@angular/core/testing", "@angular/platform-browser-dynamic/testing"], function (exports_1, context_1) {
    "use strict";
    var testing_1, testing_2, context;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (_1) {
            },
            function (testing_1_1) {
                testing_1 = testing_1_1;
            },
            function (testing_2_1) {
                testing_2 = testing_2_1;
            }
        ],
        execute: function () {// This file is required by karma.conf.js and loads recursively all the .spec and framework files
            // First, initialize the Angular testing environment.
            testing_1.getTestBed().initTestEnvironment(testing_2.BrowserDynamicTestingModule, testing_2.platformBrowserDynamicTesting());
            // Then we find all the tests.
            context = require.context('./', true, /\.spec\.ts$/);
            // And load the modules.
            context.keys().map(context);
        }
    };
});
//# sourceMappingURL=test.js.map