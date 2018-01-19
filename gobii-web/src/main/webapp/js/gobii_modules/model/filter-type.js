System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FilterType;
    return {
        setters: [],
        execute: function () {
            (function (FilterType) {
                FilterType[FilterType["NONE"] = 0] = "NONE";
                FilterType[FilterType["NAMES_BY_TYPEID"] = 1] = "NAMES_BY_TYPEID";
                FilterType[FilterType["NAMES_BY_TYPE_NAME"] = 2] = "NAMES_BY_TYPE_NAME";
                FilterType[FilterType["ENTITY_LIST"] = 3] = "ENTITY_LIST";
                FilterType[FilterType["ENTITY_BY_ID"] = 4] = "ENTITY_BY_ID";
                FilterType[FilterType["NAMES_BY_NAME_LIST"] = 5] = "NAMES_BY_NAME_LIST";
            })(FilterType || (FilterType = {}));
            exports_1("FilterType", FilterType);
        }
    };
});
//# sourceMappingURL=filter-type.js.map