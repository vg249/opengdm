System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PayloadFilter;
    return {
        setters: [],
        execute: function () {
            PayloadFilter = (function () {
                function PayloadFilter(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityFilterValue, entityLasteUpdated, pagination) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.targetEntityUniqueId = targetEntityUniqueId;
                    this.relatedEntityFilterValue = relatedEntityFilterValue;
                    this.entityLasteUpdated = entityLasteUpdated;
                    this.pagination = pagination;
                }
                return PayloadFilter;
            }());
            exports_1("PayloadFilter", PayloadFilter);
        }
    };
});
//# sourceMappingURL=action-payload-filter.js.map