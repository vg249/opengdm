System.register([], function (exports_1, context_1) {
    "use strict";
    var SampleMarkerList;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            SampleMarkerList = class SampleMarkerList {
                constructor(isArray, items, uploadFileName) {
                    this.isArray = isArray;
                    this.items = items;
                    this.uploadFileName = uploadFileName;
                }
            };
            exports_1("SampleMarkerList", SampleMarkerList);
        }
    };
});
//# sourceMappingURL=sample-marker-list.js.map