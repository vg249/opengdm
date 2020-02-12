System.register([], function (exports_1, context_1) {
    "use strict";
    var NameId;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            NameId = class NameId {
                constructor(id, fkId, name, entityType, fkEntityType, entityLasetModified) {
                    this.id = id;
                    this.fkId = fkId;
                    this.name = name;
                    this.entityType = entityType;
                    this.fkEntityType = fkEntityType;
                    this.entityLasetModified = entityLasetModified;
                }
            };
            exports_1("NameId", NameId);
        }
    };
});
//# sourceMappingURL=name-id.js.map