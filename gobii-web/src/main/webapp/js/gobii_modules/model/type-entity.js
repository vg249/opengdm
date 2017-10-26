System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType, EntitySubType;
    return {
        setters: [],
        execute: function () {
            (function (EntityType) {
                EntityType[EntityType["UNKNOWN"] = 0] = "UNKNOWN";
                EntityType[EntityType["Contact"] = 1] = "Contact";
                EntityType[EntityType["Project"] = 2] = "Project";
                EntityType[EntityType["Experiment"] = 3] = "Experiment";
                EntityType[EntityType["DataSet"] = 4] = "DataSet";
                EntityType[EntityType["Cv"] = 5] = "Cv";
                EntityType[EntityType["Mapset"] = 6] = "Mapset";
                EntityType[EntityType["Platform"] = 7] = "Platform";
                EntityType[EntityType["Marker_Group"] = 8] = "Marker_Group";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            (function (EntitySubType) {
                EntitySubType[EntitySubType["UNKNOWN"] = 0] = "UNKNOWN";
                EntitySubType[EntitySubType["CONTACT_PRINCIPLE_INVESTIGATOR"] = 1] = "CONTACT_PRINCIPLE_INVESTIGATOR";
                EntitySubType[EntitySubType["CONTACT_SUBMITED_BY"] = 2] = "CONTACT_SUBMITED_BY";
            })(EntitySubType || (EntitySubType = {}));
            exports_1("EntitySubType", EntitySubType);
        }
    };
});
//# sourceMappingURL=type-entity.js.map