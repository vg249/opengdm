System.register([], function (exports_1, context_1) {
    "use strict";
    var StatusLevel;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            (function (StatusLevel) {
                StatusLevel[StatusLevel["ERROR"] = 0] = "ERROR";
                StatusLevel[StatusLevel["VALIDATION"] = 1] = "VALIDATION";
                StatusLevel[StatusLevel["WARNING"] = 2] = "WARNING";
                StatusLevel[StatusLevel["INFO"] = 3] = "INFO";
                StatusLevel[StatusLevel["OK"] = 4] = "OK";
                StatusLevel[StatusLevel["UNKNOWN"] = 5] = "UNKNOWN";
            })(StatusLevel || (StatusLevel = {}));
            exports_1("StatusLevel", StatusLevel);
        }
    };
});
//# sourceMappingURL=type-status-level.js.map