System.register(["./type-entity-filter", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./name-id-label-type", "./file-model-node"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_filter_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, name_id_label_type_1, file_model_node_1, NameIdRequestParams;
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
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            /**
             * Created by Phil on 3/9/2017.
             */
            NameIdRequestParams = (function () {
                function NameIdRequestParams(_queryName, _entityType, _entityFilter, _fkEntityFilterValue, _selectedItemId, _entitySubType, _cvFilterType, _gobiiExtractFilterType, _nameIdLabelType, _extractorItemType, _parentNameIdRequestParams, _childNameIdRequestParams) {
                    if (_queryName === void 0) { _queryName = null; }
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entityFilter === void 0) { _entityFilter = type_entity_filter_1.EntityFilter.NONE; }
                    if (_fkEntityFilterValue === void 0) { _fkEntityFilterValue = null; }
                    if (_selectedItemId === void 0) { _selectedItemId = null; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvFilterType === void 0) { _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (_gobiiExtractFilterType === void 0) { _gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN; }
                    this._queryName = _queryName;
                    this._entityType = _entityType;
                    this._entityFilter = _entityFilter;
                    this._fkEntityFilterValue = _fkEntityFilterValue;
                    this._selectedItemId = _selectedItemId;
                    this._entitySubType = _entitySubType;
                    this._cvFilterType = _cvFilterType;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._nameIdLabelType = _nameIdLabelType;
                    this._extractorItemType = _extractorItemType;
                    this._parentNameIdRequestParams = _parentNameIdRequestParams;
                    this._childNameIdRequestParams = _childNameIdRequestParams;
                }
                NameIdRequestParams.build = function (queryName, gobiiExtractFilterType, entityType) {
                    return (new NameIdRequestParams(queryName, entityType, type_entity_filter_1.EntityFilter.NONE, null, null, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, gobiiExtractFilterType, name_id_label_type_1.NameIdLabelType.UNKNOWN, file_model_node_1.ExtractorItemType.ENTITY, null, []));
                };
                NameIdRequestParams.prototype.getEntityType = function () {
                    return this._entityType;
                };
                NameIdRequestParams.prototype.setEntityType = function (value) {
                    this._entityType = value;
                    return this;
                };
                NameIdRequestParams.prototype.getEntityFilter = function () {
                    return this._entityFilter;
                };
                NameIdRequestParams.prototype.setEntityFilter = function (value) {
                    this._entityFilter = value;
                    return this;
                };
                NameIdRequestParams.prototype.getFkEntityFilterValue = function () {
                    return this._fkEntityFilterValue;
                };
                NameIdRequestParams.prototype.setFkEntityFilterValue = function (value) {
                    this._fkEntityFilterValue = value;
                    return this;
                };
                NameIdRequestParams.prototype.getSelectedItemId = function () {
                    return this._selectedItemId;
                };
                NameIdRequestParams.prototype.setSelectedItemId = function (id) {
                    this._selectedItemId = id;
                    return this;
                };
                NameIdRequestParams.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                NameIdRequestParams.prototype.setEntitySubType = function (value) {
                    this._entitySubType = value;
                    return this;
                };
                NameIdRequestParams.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                NameIdRequestParams.prototype.setCvFilterType = function (value) {
                    this._cvFilterType = value;
                    return this;
                };
                NameIdRequestParams.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                NameIdRequestParams.prototype.setGobiiExtractFilterType = function (value) {
                    this._gobiiExtractFilterType = value;
                    return this;
                };
                NameIdRequestParams.prototype.getQueryName = function () {
                    return this._queryName;
                };
                NameIdRequestParams.prototype.setNameIdLabelType = function (nameIdLabelType) {
                    this._nameIdLabelType = nameIdLabelType;
                    return this;
                };
                NameIdRequestParams.prototype.getMameIdLabelType = function () {
                    return this._nameIdLabelType;
                };
                NameIdRequestParams.prototype.setExtractorItemType = function (extractorItemType) {
                    this._extractorItemType = extractorItemType;
                    return this;
                };
                NameIdRequestParams.prototype.getExtractorItemType = function () {
                    return this._extractorItemType;
                };
                NameIdRequestParams.prototype.setParentNameIdRequestParams = function (nameIdRequestParams) {
                    this._parentNameIdRequestParams = nameIdRequestParams;
                    return this;
                };
                NameIdRequestParams.prototype.getParentNameIdRequestParams = function () {
                    return this;
                };
                NameIdRequestParams.prototype.getChildNameIdRequestParams = function () {
                    return this._childNameIdRequestParams;
                };
                NameIdRequestParams.prototype.setChildNameIdRequestParams = function (childNameIdRequestParams) {
                    this._childNameIdRequestParams = childNameIdRequestParams;
                    return this;
                };
                return NameIdRequestParams;
            }());
            exports_1("NameIdRequestParams", NameIdRequestParams);
        }
    };
});
//# sourceMappingURL=name-id-request-params.js.map