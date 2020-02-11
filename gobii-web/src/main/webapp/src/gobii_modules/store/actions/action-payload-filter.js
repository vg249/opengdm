System.register([], function (exports_1, context_1) {
    "use strict";
    var __extends = (this && this.__extends) || (function () {
        var extendStatics = function (d, b) {
            extendStatics = Object.setPrototypeOf ||
                ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
                function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
            return extendStatics(d, b);
        };
        return function (d, b) {
            extendStatics(d, b);
            function __() { this.constructor = d; }
            d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
        };
    })();
    var PayloadFilter, ExtractReadyPayloadFilter;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            PayloadFilter = /** @class */ (function () {
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
            /**
             * bug/GSD-557
             * @extends PayloadFilter
             * Existing PayloadFilter does not have provision for filters which requires
             * multiple conditions to be satisfied.
             * For Extract ready filter, two jobTypes with different values needs to
             * checked.
             * @param jobStatusFilterValues
             */
            ExtractReadyPayloadFilter = /** @class */ (function (_super) {
                __extends(ExtractReadyPayloadFilter, _super);
                function ExtractReadyPayloadFilter(gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination, jobStatusFilterValues) {
                    var _this = _super.call(this, gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId, relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated, pagination) || this;
                    _this.gobiiExtractFilterType = gobiiExtractFilterType;
                    _this.targetEntityUniqueId = targetEntityUniqueId;
                    _this.relatedEntityUniqueId = relatedEntityUniqueId;
                    _this.relatedEntityFilterValue = relatedEntityFilterValue;
                    _this.targetEntityFilterValue = targetEntityFilterValue;
                    _this.entityLasteUpdated = entityLasteUpdated;
                    _this.pagination = pagination;
                    _this.jobStatusFilterValues = jobStatusFilterValues;
                    return _this;
                }
                /**
                 * Checks whether the passed datasetItem is extract ready or not for this Instance.
                 * @param fi - datasetItem
                 */
                ExtractReadyPayloadFilter.prototype.checkExtractReady = function (fi) {
                    return ExtractReadyPayloadFilter.isExtractReady(fi, this.jobStatusFilterValues);
                };
                /**
                 * Checks whether the datasetItem is extract ready or not using the given job status filter values.
                 * @param fi - datasetItem
                 * @param filterValues - job status filter values.
                 */
                ExtractReadyPayloadFilter.isExtractReady = function (fi, filterValues) {
                    var jobTypeName = fi.getEntity().jobTypeName.trim();
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
                };
                return ExtractReadyPayloadFilter;
            }(PayloadFilter));
            exports_1("ExtractReadyPayloadFilter", ExtractReadyPayloadFilter);
        }
    };
});
//# sourceMappingURL=action-payload-filter.js.map