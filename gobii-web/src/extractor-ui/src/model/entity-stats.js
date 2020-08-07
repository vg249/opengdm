System.register([], function (exports_1, context_1) {
    "use strict";
    var EntityStats;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            EntityStats = /** @class */ (function () {
                function EntityStats(count, lastModified) {
                    this.count = count;
                    this.lastModified = lastModified;
                }
                return EntityStats;
            }());
            exports_1("EntityStats", EntityStats);
        }
    };
});
//# sourceMappingURL=entity-stats.js.map