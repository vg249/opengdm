System.register([], function (exports_1, context_1) {
    "use strict";
    var HeaderNames;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            HeaderNames = class HeaderNames {
            };
            exports_1("HeaderNames", HeaderNames);
            HeaderNames.headerToken = "X-Auth-Token";
            HeaderNames.headerUserName = "X-Username";
            HeaderNames.headerPassword = "X-Password";
            HeaderNames.headerGobiiCrop = "Gobii-Crop-Type";
        }
    };
});
//# sourceMappingURL=header-names.js.map