System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PayloadFilter;
    return {
        setters: [],
        execute: function () {
            PayloadFilter = (function () {
                function PayloadFilter(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.targetEntityUniqueId = targetEntityUniqueId;
                    this.relatedEntityUniqueId = relatedEntityUniqueId;
                    this.relatedEntityFilterValue = relatedEntityFilterValue;
                    this.targetEntityFilterValue = targetEntityFilterValue;
                    this.entityLasteUpdated = entityLasteUpdated;
                    this.pagination = pagination;
                }
                PayloadFilter.fromPayloadFilter = function (payloadFilterToCopy) {
                    return new PayloadFilter(payloadFilterToCopy.gobiiExtractFilterType, payloadFilterToCopy.targetEntityUniqueId, payloadFilterToCopy.relatedEntityUniqueId, payloadFilterToCopy.relatedEntityFilterValue, payloadFilterToCopy.targetEntityFilterValue, payloadFilterToCopy.entityLasteUpdated, payloadFilterToCopy.pagination);
                };
                return PayloadFilter;
            }());
            exports_1("PayloadFilter", PayloadFilter);
        }
    };
});
//# sourceMappingURL=action-payload-filter.js.map