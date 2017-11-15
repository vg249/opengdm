System.register(["./filter-type", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./name-id-label-type", "./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
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
    var filter_type_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, name_id_label_type_1, type_extractor_item_1, gobii_file_item_compound_id_1, FilterParams;
    return {
        setters: [
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
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
            /***
             * This class is used extensively for the purpose of retrieving and
             * managing the results of queries to the GOBII /names/{entity} service. In this
             * case, in the NameIdService's get() method, values from this class
             * are used to set up the GET request to the /names/{entityResource}. Of particular
             * note is the use fo the _fkEntityFilterValue value for the purpose of retrieving
             * names for a given entity when that entity must be filtered according to a foreign key.
             * For example, when retrieving projects by contact_id (i.e., by principle investigator contact
             * id), the _fkEntityFilterValue will be the value of the PI according to which the project names
             * should be filtered.
             *
             * This class also has more general application for retrieving whole entities (e.g., dataset records.
             *
             * The _parentFileItemParams and _childFileItemParams can be used to create a tree of instances
             * of this class that can be used for hierarchical filtering. That is to say, the parent/child
             * relationships of FileItemParam instances corresponds to the primary/foreign key relationships of the
             * tables involved in generating the query. In our example, the project-by-contact FileFilterParams would be a
             * child of the contact FileFilterParams.
             *
             * When an array of GobiiFileItem instances is created from a query resulting from a FileFilterParams,
             * their parentItemId value is set to the _fkEntityFilterValue value of the FileFilterParams. Moreover,
             * for all filters, the _fkEntityFilterValue for the current state of the UI is preserved in the store.
             * Thus, for any given state of the store, with a given filter value, a selector can retrieve the
             * entities for a given filter value. For example, when projects are retrieved for a given contact id,
             * the project query's filter is set to that contact when the project file items are added to the store.
             * When we want to get the "currently selected" projects from the store (i.e., the projects filtered for
             * the pi who is currently selected in the UI), the selector returns the file items whose parent id
             * matches current contact ID in state.
             *
             */
            FilterParams = (function (_super) {
                __extends(FilterParams, _super);
                function FilterParams(_entityType, //first four args are passed to base class ctor
                    _entitySubType, _cvFilterType, _extractorItemType, _queryName, _filterType, _fkEntityFilterValue, _gobiiExtractFilterType, _nameIdLabelType, _parentFileItemParams, _childFileItemParams, _isDynamicFilterValue) {
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvFilterType === void 0) { _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (_queryName === void 0) { _queryName = null; }
                    if (_filterType === void 0) { _filterType = filter_type_1.FilterType.NONE; }
                    if (_fkEntityFilterValue === void 0) { _fkEntityFilterValue = null; }
                    if (_gobiiExtractFilterType === void 0) { _gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN; }
                    var _this = _super.call(this, _extractorItemType, _entityType, _entitySubType, _cvFilterType) || this;
                    _this._queryName = _queryName;
                    _this._filterType = _filterType;
                    _this._fkEntityFilterValue = _fkEntityFilterValue;
                    _this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    _this._nameIdLabelType = _nameIdLabelType;
                    _this._parentFileItemParams = _parentFileItemParams;
                    _this._childFileItemParams = _childFileItemParams;
                    _this._isDynamicFilterValue = _isDynamicFilterValue;
                    return _this;
                }
                FilterParams.build = function (queryName, gobiiExtractFilterType, entityType) {
                    return (new FilterParams(entityType, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, type_extractor_item_1.ExtractorItemType.ENTITY, queryName, filter_type_1.FilterType.NONE, null, gobiiExtractFilterType, name_id_label_type_1.NameIdLabelType.UNKNOWN, null, [], true));
                };
                FilterParams.prototype.getQueryName = function () {
                    return this._queryName;
                };
                FilterParams.prototype.getExtractorItemType = function () {
                    return _super.prototype.getExtractorItemType.call(this);
                };
                FilterParams.prototype.setExtractorItemType = function (value) {
                    _super.prototype.setExtractorItemType.call(this, value);
                    return this;
                };
                FilterParams.prototype.getEntityType = function () {
                    return _super.prototype.getEntityType.call(this);
                };
                FilterParams.prototype.setEntityType = function (value) {
                    _super.prototype.setEntityType.call(this, value);
                    return this;
                };
                FilterParams.prototype.getEntitySubType = function () {
                    return _super.prototype.getEntitySubType.call(this);
                };
                FilterParams.prototype.setEntitySubType = function (value) {
                    _super.prototype.setEntitySubType.call(this, value);
                    return this;
                };
                FilterParams.prototype.getCvFilterType = function () {
                    return _super.prototype.getCvFilterType.call(this);
                };
                FilterParams.prototype.setCvFilterType = function (value) {
                    _super.prototype.setCvFilterType.call(this, value);
                    return this;
                };
                FilterParams.prototype.getFilterType = function () {
                    return this._filterType;
                };
                FilterParams.prototype.setFilterType = function (value) {
                    this._filterType = value;
                    return this;
                };
                FilterParams.prototype.getFkEntityFilterValue = function () {
                    return this._fkEntityFilterValue;
                };
                FilterParams.prototype.setFkEntityFilterValue = function (value) {
                    this._fkEntityFilterValue = value;
                    return this;
                };
                FilterParams.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                FilterParams.prototype.setGobiiExtractFilterType = function (value) {
                    this._gobiiExtractFilterType = value;
                    return this;
                };
                FilterParams.prototype.setNameIdLabelType = function (nameIdLabelType) {
                    this._nameIdLabelType = nameIdLabelType;
                    return this;
                };
                FilterParams.prototype.getMameIdLabelType = function () {
                    return this._nameIdLabelType;
                };
                FilterParams.prototype.setParentFileItemParams = function (fileItemParams) {
                    this._parentFileItemParams = fileItemParams;
                    return this;
                };
                FilterParams.prototype.getChildFileItemParams = function () {
                    return this._childFileItemParams;
                };
                FilterParams.prototype.setChildNameIdRequestParams = function (childNameIdRequestParams) {
                    this._childFileItemParams = childNameIdRequestParams;
                    return this;
                };
                FilterParams.prototype.setIsDynamicFilterValue = function (dynamicFilterValue) {
                    this._isDynamicFilterValue = dynamicFilterValue;
                    return this;
                };
                FilterParams.prototype.getIsDynamicFilterValue = function () {
                    return this._isDynamicFilterValue;
                };
                return FilterParams;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("FilterParams", FilterParams);
        }
    };
});
//# sourceMappingURL=file-item-params.js.map