System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FileName;
    return {
        setters: [],
        execute: function () {
            FileName = (function () {
                function FileName() {
                }
                FileName.makeUniqueFileId = function () {
                    var date = new Date();
                    var returnVal = date.getFullYear()
                        + "_"
                        + (date.getMonth() + 1)
                        + "_"
                        + date.getDay()
                        + "_"
                        + date.getHours()
                        + "_"
                        + date.getMinutes()
                        + "_"
                        + date.getSeconds();
                    return returnVal;
                };
                ;
                return FileName;
            }());
            exports_1("FileName", FileName);
        }
    };
});
//# sourceMappingURL=file_name.js.map