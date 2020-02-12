System.register([], function (exports_1, context_1) {
    "use strict";
    var PayloadFilter, ExtractReadyPayloadFilter;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            PayloadFilter = class PayloadFilter {
                constructor(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.targetEntityUniqueId = targetEntityUniqueId;
                    this.relatedEntityUniqueId = relatedEntityUniqueId;
                    this.relatedEntityFilterValue = relatedEntityFilterValue;
                    this.targetEntityFilterValue = targetEntityFilterValue;
                    this.entityLasteUpdated = entityLasteUpdated;
                    this.pagination = pagination;
                }
                static fromPayloadFilter(payloadFilterToCopy) {
                    return new PayloadFilter(payloadFilterToCopy.gobiiExtractFilterType, payloadFilterToCopy.targetEntityUniqueId, payloadFilterToCopy.relatedEntityUniqueId, payloadFilterToCopy.relatedEntityFilterValue, payloadFilterToCopy.targetEntityFilterValue, payloadFilterToCopy.entityLasteUpdated, payloadFilterToCopy.pagination);
                }
            };
            exports_1("PayloadFilter", PayloadFilter);
            /**
             * bug/GSD-557
             * @extends PayloadFilter
             * Existing PayloadFilter does not have provision for filters which requires
             * multiple conditions to be satisfied.
             * For Extract ready filter, two jobTypes with different values needs to
             * checked.
             * @param jobStatusFilterValues
             */
            ExtractReadyPayloadFilter = class ExtractReadyPayloadFilter extends PayloadFilter {
                constructor(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination, jobStatusFilterValues) {
                    super(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination);
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.targetEntityUniqueId = targetEntityUniqueId;
                    this.relatedEntityUniqueId = relatedEntityUniqueId;
                    this.relatedEntityFilterValue = relatedEntityFilterValue;
                    this.targetEntityFilterValue = targetEntityFilterValue;
                    this.entityLasteUpdated = entityLasteUpdated;
                    this.pagination = pagination;
                    this.jobStatusFilterValues = jobStatusFilterValues;
                }
                /**
                 * Checks whether the passed datasetItem is extract ready or not for this Instance.
                 * @param fi - datasetItem
                 */
                checkExtractReady(fi) {
                    return ExtractReadyPayloadFilter.isExtractReady(fi, this.jobStatusFilterValues);
                }
                /**
                 * Checks whether the datasetItem is extract ready or not using the given job status filter values.
                 * @param fi - datasetItem
                 * @param filterValues - job status filter values.
                 */
                static isExtractReady(fi, filterValues) {
                    let jobTypeName = fi.getEntity().jobTypeName.trim();
                    if (jobTypeName in filterValues) {
                        if (filterValues[jobTypeName].indexOf(fi.getEntity().jobStatusName.trim()) > -1) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else if (jobTypeName === 'n/a') {
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            };
            exports_1("ExtractReadyPayloadFilter", ExtractReadyPayloadFilter);
        }
    };
});
//# sourceMappingURL=action-payload-filter.js.map