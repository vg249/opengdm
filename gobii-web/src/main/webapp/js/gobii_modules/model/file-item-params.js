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
             * are used to set up the GET request to the /names/{entityResource}.
             *
             * There are several ways in which FilterParams are used to drive queries.
             *
             * 1) The params are used for the fileItemService.loadNameIdsFromFilterParams() method. In the
             *    Extract root class there are many examples of this -- when you switch to a new export type,
             *    the file items pertinent to that type are loaded into the ngrx/store so that they can then
             *    be retrieved via reducer selectors. Usually, the FilterParam instance has no parent or child
             *    FilterParam instances, and so only those entities are loaded.
             *
             * 2) In hierarchical filtering, there is a chain of FilterParam items identified via the parent
             *    and child properties. For example, If we want to display datasets for a particular experiment
             *    for a particular project for a particular PI, we will have a hierarchy of four such FilterParam
             *    instances. The Hierarchy will be recursed so that the filter values will be set up the ngrx/store
             *    and the selectors will retrieve the correctly filtered items.
             *
             * There are more details to be aware of for hierarchical filtering.
            
             * The parent/child relationships of FilterParams instances corresponds to the primary/foreign key
             * relationships of the tables involved in generating the query. In our example, the PROJECTS_BY_CONTACT FileFilterParams is a
             * child of the CONTACT_PI FileFilterParams. The PROJECTS_BY_CONTACT query will be run along with a
             * contactId value, which will serve to filter the results of the project query. That contactId value
             * will now be the _fkEntityFilterValue of the PROJECTS_BY_CONTACT FilterValues. Moreover, each
             * GobiiFileItem resulting from the PROJECTS_BY_CONTACT query will be assigned the contactId as its
             * parentItemId value. Thus, once the GobiiFileItems have been retrieved from the server, they can
             * subsequently be retrieved from the store such that the GobiiFileItems of EntityType PROJECT are
             * filtered as follows: the current _fkEntityFilterValue of the PROJECTS_BY_CONTACT filter matches
             * the parentItemId of the GobiiFileItems of EntityType PROJECT. Thus, the PROJECTS_BY_CONTACT filter,
             * with an arbitrary _fkEntityFilterValue, can be dispatched to the store at any time and the   reby change
             * the set of GobiiFileItems that are filtered in this way. In other words, when we want to get the
             * "currently selected" projects from the store (i.e., the projects filtered for
             * the pi who is currently selected in the UI), the selector returns the file items whose parent id
             * matches current contact ID in state.
             *
             * The first time this was used, the retrievals from the server were filtered. That is to say,
             * the hierarchy was traversed each time that a new value was selected. Filters are
             * stored in the history, uniquely identified by filter name and value. Thus, if that
             * particular filter with that value had already been retrieved, it would not be re-retrieved.
             * So the top-most parent in the hierarchy would be loaded, and the children recursed for
             * the top-items in the list, filtering as they go. Then, when a new value is selected from
             * a drop down list, the ReplaceInExtractByItemIdAction is triggered, and handled in
             * file-item-effects. So at any level in the hierarchy, an item is filtered down. Here again,
             * if the filter-value combination has been encountered, it does not need to be re-retrieved. Whether or
             * not this type of retrieval is done is controlled by the value of the _isDynamicLoad property.
             *
             * Subsequently, a second methodology has evolved. Here, reather than filtering for these items
             * piecemeal, all items for the particular entity are retrieved at once. This was done in order to
             * support drop downs that list All of a given entity, but then allow subseqent filtering. So here
             * the items are all initially loaded, and then filtering only applies to what's already in the
             * ngrx/store. Here again you can see this functionality operating depending on the value of _isDynamicLoad.
             *
             *
             * Particular note should be taken of the  _fkEntityFilterValue value for the purpose of retrieving
             * names for a given entity when that entity must be filtered according to a foreign key.
             * For example, when retrieving projects by contact_id (i.e., by principle investigator contact
             * id), the _fkEntityFilterValue will be the value of the PI according to which the project names
             * should be filtered. The fact that the filter value corresponds to the PK id of the parent entity
             * by which to filter the target item (i.e., the Project filter's filter value is the contactId) is
             * awkward and difficult to understand. There probably needs to be better semantics for this. Note
             * that in the FileItemService.makeFileActionsFromFilterParamName() method, there is no way to apply a
             * filter value to the leaf of the hierarchy. That doesn't seem to matter for now. But there are cases
             * where it could matter.
             *
             * Note that there is also an idiom for filtering where you want to retrieve whole entities rather than
             * name ids. This is done with the FileItemService.makeFileItemActionsFromEntities() method. Because whole
             * entities can have FK relationships to multiple entities, the GobiiFileItemEntityRelation[] array was
             * added to GobiiFileItem. This allows for an arbitrary number of FK relationships to be set up.
             *
            
             *
             */
            FilterParams = (function (_super) {
                __extends(FilterParams, _super);
                function FilterParams(_entityType, //first four args are passed to base class ctor
                    _entitySubType, _cvFilterType, _cvFilterValue, _extractorItemType, _queryName, _filterType, _fkEntityFilterValue, _gobiiExtractFilterType, _nameIdLabelType, _parentFileItemParams, _childFileItemParams, _isDynamicFilterValue, _isDynamicDataLoad, _isPaged, _pageSize, _pageNum, _pagedQueryId, onLoadFilteredItemsAction, dtoRequestItem, dtoRequestService) {
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvFilterType === void 0) { _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (_cvFilterValue === void 0) { _cvFilterValue = null; }
                    if (_queryName === void 0) { _queryName = null; }
                    if (_filterType === void 0) { _filterType = filter_type_1.FilterType.NONE; }
                    if (_fkEntityFilterValue === void 0) { _fkEntityFilterValue = null; }
                    if (_gobiiExtractFilterType === void 0) { _gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN; }
                    var _this = _super.call(this, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue) || this;
                    _this._queryName = _queryName;
                    _this._filterType = _filterType;
                    _this._fkEntityFilterValue = _fkEntityFilterValue;
                    _this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    _this._nameIdLabelType = _nameIdLabelType;
                    _this._parentFileItemParams = _parentFileItemParams;
                    _this._childFileItemParams = _childFileItemParams;
                    _this._isDynamicFilterValue = _isDynamicFilterValue;
                    _this._isDynamicDataLoad = _isDynamicDataLoad;
                    _this._isPaged = _isPaged;
                    _this._pageSize = _pageSize;
                    _this._pageNum = _pageNum;
                    _this._pagedQueryId = _pagedQueryId;
                    _this.onLoadFilteredItemsAction = onLoadFilteredItemsAction;
                    _this.dtoRequestItem = dtoRequestItem;
                    _this.dtoRequestService = dtoRequestService;
                    return _this;
                }
                FilterParams.build = function (queryName, gobiiExtractFilterType, entityType) {
                    return (new FilterParams(entityType, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null, type_extractor_item_1.ExtractorItemType.ENTITY, queryName, filter_type_1.FilterType.NONE, null, gobiiExtractFilterType, name_id_label_type_1.NameIdLabelType.UNKNOWN, null, [], true, true, false, null, null, null, null, null, null));
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
                FilterParams.prototype.getCvFilterValue = function () {
                    return _super.prototype.getCvFilterValue.call(this);
                };
                FilterParams.prototype.setCvFilterValue = function (value) {
                    _super.prototype.setCvFilterValue.call(this, value);
                    return this;
                };
                FilterParams.prototype.getIsExtractCriterion = function () {
                    return _super.prototype.getIsExtractCriterion.call(this);
                };
                FilterParams.prototype.setIsExtractCriterion = function (value) {
                    _super.prototype.setIsExtractCriterion.call(this, value);
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
                FilterParams.prototype.getParentFileItemParams = function () {
                    return this._parentFileItemParams;
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
                FilterParams.prototype.getIsDynamicDataLoad = function () {
                    return this._isDynamicDataLoad;
                };
                FilterParams.prototype.setIsDynamicDataLoad = function (value) {
                    this._isDynamicDataLoad = value;
                    return this;
                };
                FilterParams.prototype.setOnLoadFilteredItemsAction = function (initializeTransform) {
                    this.onLoadFilteredItemsAction = initializeTransform;
                    return this;
                };
                FilterParams.prototype.getOnLoadFilteredItemsAction = function () {
                    return this.onLoadFilteredItemsAction;
                };
                FilterParams.prototype.getIsPaged = function () {
                    return this._isPaged;
                };
                FilterParams.prototype.setIsPaged = function (value) {
                    this._isPaged = value;
                    return this;
                };
                FilterParams.prototype.getPageNum = function () {
                    return this._pageNum;
                };
                FilterParams.prototype.setPageNum = function (value) {
                    this._pageNum = value;
                    return this;
                };
                FilterParams.prototype.getPageSize = function () {
                    return this._pageSize;
                };
                FilterParams.prototype.setPageSize = function (value) {
                    this._pageSize = value;
                    return this;
                };
                FilterParams.prototype.getPagedQueryId = function () {
                    return this._pagedQueryId;
                };
                FilterParams.prototype.setPagedQueryId = function (value) {
                    this._pagedQueryId;
                    return this;
                };
                FilterParams.prototype.getDtoRequestItem = function () {
                    return this.dtoRequestItem;
                };
                FilterParams.prototype.setDtoRequestItem = function (value) {
                    this.dtoRequestItem = value;
                    return this;
                };
                FilterParams.prototype.getDtoRequestService = function () {
                    return this.dtoRequestService;
                };
                FilterParams.prototype.setDtoRequestService = function (value) {
                    this.dtoRequestService = value;
                    return this;
                };
                return FilterParams;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("FilterParams", FilterParams);
        }
    };
});
//# sourceMappingURL=file-item-params.js.map