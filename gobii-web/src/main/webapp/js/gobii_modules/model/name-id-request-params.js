System.register(["./type-entity-filter", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./name-id-label-type", "./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var __extends = (this && this.__extends) || (function () {
        var extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return function (d, b) {
            extendStatics(d, b);
            function __() { this.constructor = d; }
            d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
        };
    })();
    var __moduleName = context_1 && context_1.id;
    var type_entity_filter_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, name_id_label_type_1, type_extractor_item_1, gobii_file_item_compound_id_1, FileItemParams;
    return {
        setters: [
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            /**
             * Created by Phil on 3/9/2017.
             */
            FileItemParams = (function (_super) {
                __extends(FileItemParams, _super);
                function FileItemParams(_queryName, _entityType, _entityFilter, _fkEntityFilterValue, _selectedItemId, _entitySubType, _cvFilterType, _gobiiExtractFilterType, _nameIdLabelType, _extractorItemType, _parentNameIdRequestParams, _childNameIdRequestParams, _isDynamicFilterValue) {
                    if (_queryName === void 0) { _queryName = null; }
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entityFilter === void 0) { _entityFilter = type_entity_filter_1.EntityFilter.NONE; }
                    if (_fkEntityFilterValue === void 0) { _fkEntityFilterValue = null; }
                    if (_selectedItemId === void 0) { _selectedItemId = null; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvFilterType === void 0) { _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (_gobiiExtractFilterType === void 0) { _gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN; }
                    var _this = _super.call(this, _extractorItemType, _entityType, _entitySubType, _cvFilterType) || this;
                    _this._queryName = _queryName;
                    _this._entityFilter = _entityFilter;
                    _this._fkEntityFilterValue = _fkEntityFilterValue;
                    _this._selectedItemId = _selectedItemId;
                    _this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    _this._nameIdLabelType = _nameIdLabelType;
                    _this._parentNameIdRequestParams = _parentNameIdRequestParams;
                    _this._childNameIdRequestParams = _childNameIdRequestParams;
                    _this._isDynamicFilterValue = _isDynamicFilterValue;
                    return _this;
                }
                FileItemParams.build = function (queryName, gobiiExtractFilterType, entityType) {
                    return (new FileItemParams(queryName, entityType, type_entity_filter_1.EntityFilter.NONE, null, null, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, gobiiExtractFilterType, name_id_label_type_1.NameIdLabelType.UNKNOWN, type_extractor_item_1.ExtractorItemType.ENTITY, null, [], true));
                };
                FileItemParams.prototype.getQueryName = function () {
                    return this._queryName;
                };
                FileItemParams.prototype.getExtractorItemType = function () {
                    return _super.prototype.getExtractorItemType.call(this);
                };
                FileItemParams.prototype.setExtractorItemType = function (value) {
                    _super.prototype.setExtractorItemType.call(this, value);
                    return this;
                };
                FileItemParams.prototype.getEntityType = function () {
                    return _super.prototype.getEntityType.call(this);
                };
                FileItemParams.prototype.setEntityType = function (value) {
                    _super.prototype.setEntityType.call(this, value);
                    return this;
                };
                FileItemParams.prototype.getEntitySubType = function () {
                    return _super.prototype.getEntitySubType.call(this);
                };
                FileItemParams.prototype.setEntitySubType = function (value) {
                    _super.prototype.setEntitySubType.call(this, value);
                    return this;
                };
                FileItemParams.prototype.getCvFilterType = function () {
                    return _super.prototype.getCvFilterType.call(this);
                };
                FileItemParams.prototype.setCvFilterType = function (value) {
                    _super.prototype.setCvFilterType.call(this, value);
                    return this;
                };
                FileItemParams.prototype.getEntityFilter = function () {
                    return this._entityFilter;
                };
                FileItemParams.prototype.setEntityFilter = function (value) {
                    this._entityFilter = value;
                    return this;
                };
                FileItemParams.prototype.getFkEntityFilterValue = function () {
                    return this._fkEntityFilterValue;
                };
                FileItemParams.prototype.setFkEntityFilterValue = function (value) {
                    this._fkEntityFilterValue = value;
                    return this;
                };
                FileItemParams.prototype.getSelectedItemId = function () {
                    return this._selectedItemId;
                };
                FileItemParams.prototype.setSelectedItemId = function (id) {
                    this._selectedItemId = id;
                    return this;
                };
                FileItemParams.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                FileItemParams.prototype.setGobiiExtractFilterType = function (value) {
                    this._gobiiExtractFilterType = value;
                    return this;
                };
                FileItemParams.prototype.setNameIdLabelType = function (nameIdLabelType) {
                    this._nameIdLabelType = nameIdLabelType;
                    return this;
                };
                FileItemParams.prototype.getMameIdLabelType = function () {
                    return this._nameIdLabelType;
                };
                FileItemParams.prototype.setParentNameIdRequestParams = function (nameIdRequestParams) {
                    this._parentNameIdRequestParams = nameIdRequestParams;
                    return this;
                };
                FileItemParams.prototype.getParentNameIdRequestParams = function () {
                    return this;
                };
                FileItemParams.prototype.getChildNameIdRequestParams = function () {
                    return this._childNameIdRequestParams;
                };
                FileItemParams.prototype.setChildNameIdRequestParams = function (childNameIdRequestParams) {
                    this._childNameIdRequestParams = childNameIdRequestParams;
                    return this;
                };
                FileItemParams.prototype.setIsDynamicFilterValue = function (dynamicFilterValue) {
                    this._isDynamicFilterValue = dynamicFilterValue;
                    return this;
                };
                FileItemParams.prototype.getIsDynamicFilterValue = function () {
                    return this._isDynamicFilterValue;
                };
                return FileItemParams;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("FileItemParams", FileItemParams);
        }
    };
});
//# sourceMappingURL=name-id-request-params.js.map