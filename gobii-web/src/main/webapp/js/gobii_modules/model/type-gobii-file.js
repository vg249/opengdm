System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiFileType;
    return {
        setters:[],
        execute: function() {
            (function (GobiiFileType) {
                GobiiFileType[GobiiFileType["HAPMAP"] = 0] = "HAPMAP";
                GobiiFileType[GobiiFileType["GENERIC"] = 1] = "GENERIC";
                GobiiFileType[GobiiFileType["VCF"] = 2] = "VCF";
            })(GobiiFileType || (GobiiFileType = {}));
            exports_1("GobiiFileType", GobiiFileType);
        }
    }
});
//# sourceMappingURL=type-gobii-file.js.map