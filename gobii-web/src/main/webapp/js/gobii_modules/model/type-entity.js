System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType;
    return {
        setters: [],
        execute: function () {
            (function (EntityType) {
                EntityType[EntityType["Unknown"] = 0] = "Unknown";
                EntityType[EntityType["DataSetNames"] = 1] = "DataSetNames";
                EntityType[EntityType["Contacts"] = 2] = "Contacts";
                EntityType[EntityType["Projects"] = 3] = "Projects";
                EntityType[EntityType["Experiments"] = 4] = "Experiments";
                EntityType[EntityType["DataSets"] = 5] = "DataSets";
                EntityType[EntityType["CvTerms"] = 6] = "CvTerms";
                EntityType[EntityType["Mapsets"] = 7] = "Mapsets";
                EntityType[EntityType["Platforms"] = 8] = "Platforms";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            ;
        }
    };
});
//# sourceMappingURL=type-entity.js.map