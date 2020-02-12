System.register(["./filter-type", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./name-id-label-type", "./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var filter_type_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, name_id_label_type_1, type_extractor_item_1, gobii_file_item_compound_id_1, FilterParams;
    var __moduleName = context_1 && context_1.id;
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
             * will now be the _relatedEntityFilterValue of the PROJECTS_BY_CONTACT FilterValues. Moreover, each
             * GobiiFileItem resulting from the PROJECTS_BY_CONTACT query will be assigned the contactId as its
             * parentItemId value. Thus, once the GobiiFileItems have been retrieved from the server, they can
             * subsequently be retrieved from the store such that the GobiiFileItems of EntityType PROJECT are
             * filtered as follows: the current _relatedEntityFilterValue of the PROJECTS_BY_CONTACT filter in the store matches
             * the parentItemId of the GobiiFileItems of EntityType PROJECT. Thus, a value for the PROJECTS_BY_CONTACT filter ,
             * with an arbitrary value, can be dispatched to the store at any time and the thereby change
             * the set of GobiiFileItems that are retrieved from the select methods. In other words, when we want to get the
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
             * Particular note should be taken of the way that the relatedEntityFilterValue value in the store works for the purpose of retrieving
             * names for a given entity when that entity must be filtered according to a foreign key.
             * For example, when retrieving projects by contact_id (i.e., by principle investigator contact
             * id), the relatedEntityFilterValue of the filter in the store will be the value of the PI according to which the project names
             * should be filtered.
             *
             * In recent changes, better semantics have been added for expressing the entities to which FilterParam items
             * pertain and their respective filter values. Thus, the target unique ID references the compounduniqueid of the
             * entity filtered by the filter, whilst the related id is the compounduniqueid references the related entity.
             * Thus, in the case of the project filter, the target is project, and the related id is for principle investigator
             * contact. The target and related filter values in the store are the actual filter values for these things. See the comment on
             * makeFileActionsFromFilterParamName() in file item service for further details.
             *
             * Note that there is also an idiom for filtering where you want to retrieve whole entities rather than
             * name ids. This is done with the FileItemService.makeFileItemActionsFromEntities() method. Because whole
             * entities can have FK relationships to multiple entities, the GobiiFileItemEntityRelation[] array was
             * added to GobiiFileItem. This allows for an arbitrary number of FK relationships to be set up.
             *
             * Care must be taken to avoid confusing FilterParams, this class, with the PayloadFilter class, which is used
             * to store actual target and related filter values. These two classes share some properties in common, particular the
             * target and related unique IDs. In the version of FilterParams that this current version replaces, I had very mistakenly
             * stored the related and target filter _values_ here in FilterParams. This is a violation of the sacrosanct rule about
             * keeping state _only_ in the store. Violating this rule eventually catches up with you. So I have removed these properties
             * and refactored where necessary.
             *
            
             *
             */
            FilterParams = class FilterParams {
                constructor(_entityType = type_entity_1.EntityType.UNKNOWN, //first four args are passed to base class ctor
                _entitySubType = type_entity_1.EntitySubType.UNKNOWN, _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN, _cvFilterValue = null, _extractorItemType, targetEntityUniqueId, relatedEntityUniqueId, _queryName = null, _filterType = filter_type_1.FilterType.NONE, 
                // private _targetEntityFilterValue: string = null,
                // private _relatedEntityFilterValue: string = null,
                _gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, _nameIdLabelType, _parentFileItemParams, _childFileItemParams, _isDynamicFilterValue, _isDynamicDataLoad, _isPaged, _pageSize, _pageNum, _pagedQueryId, onLoadFilteredItemsAction, dtoRequestItem, dtoRequestService) {
                    this.targetEntityUniqueId = targetEntityUniqueId;
                    this.relatedEntityUniqueId = relatedEntityUniqueId;
                    this._queryName = _queryName;
                    this._filterType = _filterType;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._nameIdLabelType = _nameIdLabelType;
                    this._parentFileItemParams = _parentFileItemParams;
                    this._childFileItemParams = _childFileItemParams;
                    this._isDynamicFilterValue = _isDynamicFilterValue;
                    this._isDynamicDataLoad = _isDynamicDataLoad;
                    this._isPaged = _isPaged;
                    this._pageSize = _pageSize;
                    this._pageNum = _pageNum;
                    this._pagedQueryId = _pagedQueryId;
                    this.onLoadFilteredItemsAction = onLoadFilteredItemsAction;
                    this.dtoRequestItem = dtoRequestItem;
                    this.dtoRequestService = dtoRequestService;
                    this.targetEntityUniqueId = new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(_extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue);
                }
                static build(queryName, gobiiExtractFilterType, entityType) {
                    return (new FilterParams(entityType, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null, type_extractor_item_1.ExtractorItemType.ENTITY, null, null, queryName, filter_type_1.FilterType.NONE, 
                    // null,
                    // null,
                    gobiiExtractFilterType, name_id_label_type_1.NameIdLabelType.UNKNOWN, null, [], true, true, false, null, null, null, null, null, null));
                }
                getQueryName() {
                    return this._queryName;
                }
                getExtractorItemType() {
                    return this.targetEntityUniqueId.getExtractorItemType();
                }
                setExtractorItemType(value) {
                    this.targetEntityUniqueId.setExtractorItemType(value);
                    return this;
                }
                getEntityType() {
                    return this.targetEntityUniqueId.getEntityType();
                }
                setEntityType(value) {
                    this.targetEntityUniqueId.setEntityType(value);
                    return this;
                }
                getEntitySubType() {
                    return this.targetEntityUniqueId.getEntitySubType();
                }
                setEntitySubType(value) {
                    this.targetEntityUniqueId.setEntitySubType(value);
                    return this;
                }
                getCvFilterType() {
                    return this.targetEntityUniqueId.getCvFilterType();
                }
                setCvFilterType(value) {
                    this.targetEntityUniqueId.setCvFilterType(value);
                    return this;
                }
                getCvFilterValue() {
                    return this.targetEntityUniqueId.getCvFilterValue();
                }
                setCvFilterValue(value) {
                    this.targetEntityUniqueId.setCvFilterValue(value);
                    return this;
                }
                getIsExtractCriterion() {
                    return this.targetEntityUniqueId.getIsExtractCriterion();
                }
                setIsExtractCriterion(value) {
                    this.targetEntityUniqueId.setIsExtractCriterion(value);
                    return this;
                }
                getTargetEtityUniqueId() {
                    return this.targetEntityUniqueId;
                }
                setRelatedEntityUniqueId(value) {
                    this.relatedEntityUniqueId = value;
                    return this;
                }
                getRelatedEntityUniqueId() {
                    return this.relatedEntityUniqueId;
                }
                getFilterType() {
                    return this._filterType;
                }
                setFilterType(value) {
                    this._filterType = value;
                    return this;
                }
                // getRelatedEntityFilterValue(): string {
                //     return this._relatedEntityFilterValue;
                // }
                //
                // setRelatedEntityFilterValue(value: string): FilterParams {
                //     this._relatedEntityFilterValue = value;
                //     return this;
                // }
                // getTargetEntityFilterValue(): string {
                //     return this._targetEntityFilterValue;
                // }
                //
                // setTargetEntityFilterValue(value: string): FilterParams {
                //     this._targetEntityFilterValue = value;
                //     return this;
                // }
                getGobiiExtractFilterType() {
                    return this._gobiiExtractFilterType;
                }
                setGobiiExtractFilterType(value) {
                    this._gobiiExtractFilterType = value;
                    return this;
                }
                setNameIdLabelType(nameIdLabelType) {
                    this._nameIdLabelType = nameIdLabelType;
                    return this;
                }
                getMameIdLabelType() {
                    return this._nameIdLabelType;
                }
                setParentFileItemParams(fileItemParams) {
                    this._parentFileItemParams = fileItemParams;
                    return this;
                }
                getParentFileItemParams() {
                    return this._parentFileItemParams;
                }
                getChildFileItemParams() {
                    return this._childFileItemParams;
                }
                setChildNameIdRequestParams(childNameIdRequestParams) {
                    this._childFileItemParams = childNameIdRequestParams;
                    return this;
                }
                setIsDynamicFilterValue(dynamicFilterValue) {
                    this._isDynamicFilterValue = dynamicFilterValue;
                    return this;
                }
                getIsDynamicFilterValue() {
                    return this._isDynamicFilterValue;
                }
                getIsDynamicDataLoad() {
                    return this._isDynamicDataLoad;
                }
                setIsDynamicDataLoad(value) {
                    this._isDynamicDataLoad = value;
                    return this;
                }
                setOnLoadFilteredItemsAction(initializeTransform) {
                    this.onLoadFilteredItemsAction = initializeTransform;
                    return this;
                }
                getOnLoadFilteredItemsAction() {
                    return this.onLoadFilteredItemsAction;
                }
                getIsPaged() {
                    return this._isPaged;
                }
                setIsPaged(value) {
                    this._isPaged = value;
                    return this;
                }
                getPageNum() {
                    return this._pageNum;
                }
                setPageNum(value) {
                    this._pageNum = value;
                    return this;
                }
                getPageSize() {
                    return this._pageSize;
                }
                setPageSize(value) {
                    this._pageSize = value;
                    return this;
                }
                getPagedQueryId() {
                    return this._pagedQueryId;
                }
                setPagedQueryId(value) {
                    this._pagedQueryId = value;
                    return this;
                }
                getDtoRequestItem() {
                    return this.dtoRequestItem;
                }
                setDtoRequestItem(value) {
                    this.dtoRequestItem = value;
                    return this;
                }
                getDtoRequestService() {
                    return this.dtoRequestService;
                }
                setDtoRequestService(value) {
                    this.dtoRequestService = value;
                    return this;
                }
            };
            exports_1("FilterParams", FilterParams);
        }
    };
});
//# sourceMappingURL=filter-params.js.map