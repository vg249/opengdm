System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType;
    return {
        setters:[],
        execute: function() {
            (function (EntityType) {
                EntityType[EntityType["DataSetNames"] = 0] = "DataSetNames";
                EntityType[EntityType["Contact"] = 1] = "Contact";
                EntityType[EntityType["Project"] = 2] = "Project";
                EntityType[EntityType["Experiment"] = 3] = "Experiment";
                EntityType[EntityType["DataSetNamesByExperimentId"] = 4] = "DataSetNamesByExperimentId";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            ;
        }
    }
});
//# sourceMappingURL=type-entity.js.map