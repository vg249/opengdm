System.register(["reselect", "../../model/gobii-file-item", "../actions/fileitem-action", "../../model/type-extractor-item", "../../model/type-entity", "../../model/file-item-param-names", "../../model/type-process", "../../views/entity-labels", "../../model/type-extractor-filter", "../../model/type-extract-format", "../../model/cv-filter-type", "../../model/type-extractor-sample-list"], function (exports_1, context_1) {
    "use strict";
    var reselect_1, gobii_file_item_1, gobiiFileItemAction, type_extractor_item_1, type_entity_1, file_item_param_names_1, type_process_1, entity_labels_1, type_extractor_filter_1, type_extract_format_1, cv_filter_type_1, type_extractor_sample_list_1, initialState, getGobiiExtractFilterType, getFileItems, getUniqueIds, getSelectedUniqueIds, getFilters, getSelected, getAll, getSelectedFileFormat, getSelectedSampleType, getJobId, getUploadFiles, getPiContacts, getProjects, getExperiments, getDatasets, getCvTermsDataType, getCvTermsJobStatus, getMapsets, getPlatforms, getMarkerGroups, getSelectedPiContacts, getProjectsForSelectedPi, getExperimentsForSelectedProject, getDatasetsForSelectedExperiment, getDatasetEntities, getDatasetEntitiesPaged, getPiContactsFilterOptional, getProjectsFilterOptional, getExperimentsFilterOptional;
    var __moduleName = context_1 && context_1.id;
    function addToExtractItems(state, gobiiFileItem) {
        if (!state.allFileItems.find(fi => fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId())) {
            console.log("Item is not in the collection: " + gobiiFileItem.getItemName() + " of type " + entity_labels_1.Labels.instance().treeExtractorTypeLabels[gobiiFileItem.getExtractorItemType()]);
        }
        gobiiFileItem.setSelected(true);
        let newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        if (!newSelectedUniqueIdsState.find(id => id === gobiiFileItem.getFileItemUniqueId())) {
            newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
        }
        let returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: state.allFileItems,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function removeFromExtractItems(state, gobiiFileItem) {
        //    gobiiFileItem.setSelected(false);
        let newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        // if we don't find it, we don't raise an error: the intent was to remove, but it just wasn't there,
        // so the state when this function exits is as expected
        let idx = newSelectedUniqueIdsState.findIndex(id => id === gobiiFileItem.getFileItemUniqueId());
        if (idx > -1) {
            newSelectedUniqueIdsState.splice(idx, 1);
        }
        let newFileItemState = state.allFileItems.slice();
        if (gobiiFileItem.getIsEphemeral()) {
            newFileItemState = newFileItemState.filter(fi => !fi.getIsEphemeral());
        }
        else {
            let fileItemInNewState = newFileItemState
                .find(fi => fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId());
            if (fileItemInNewState) {
                fileItemInNewState.setSelected(false);
            }
        }
        let returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: newFileItemState,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function fileItemsReducer(state = initialState, action) {
        let returnVal = state;
        switch (action.type) {
            case gobiiFileItemAction.LOAD_FILE_ITEM: {
                const gobiiFileItemPayloadItem = action.payload.gobiiFileItem;
                let newFileItemsItemsState = state.allFileItems.slice();
                const newSelectedFileItemUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
                let existingItem = newFileItemsItemsState.find(stateItem => stateItem.compoundIdeEquals(gobiiFileItemPayloadItem)
                    && stateItem.getItemId() === gobiiFileItemPayloadItem.getItemId());
                if (existingItem) {
                    let existingSelectedUniqueId = newSelectedFileItemUniqueIdsState
                        .find(id => id === existingItem.getFileItemUniqueId());
                    if (existingSelectedUniqueId) {
                        newSelectedFileItemUniqueIdsState.splice(newSelectedFileItemUniqueIdsState.indexOf(existingSelectedUniqueId), 1);
                        newSelectedFileItemUniqueIdsState.push(gobiiFileItemPayloadItem.getFileItemUniqueId());
                    }
                    newFileItemsItemsState = newFileItemsItemsState.filter(fi => fi.getFileItemUniqueId() != existingItem.getFileItemUniqueId());
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem);
                }
                else {
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem);
                }
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: newSelectedFileItemUniqueIdsState,
                    allFileItems: newFileItemsItemsState,
                    filters: state.filters
                };
                break;
            } // LOAD_FILE_ITEM
            case gobiiFileItemAction.LOAD_FILE_ITEM_LIST_WITH_FILTER: {
                const gobiiFileItemsPayload = action.payload.gobiiFileItems;
                const filterId = action.payload.filterId.toString();
                const filterValue = action.payload.filter;
                const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem => state
                    .allFileItems
                    .filter(stateItem => (stateItem.getGobiiExtractFilterType() === newItem.getGobiiExtractFilterType() &&
                    stateItem.compoundIdeEquals(newItem) &&
                    stateItem.getItemId() === newItem.getItemId() &&
                    ((stateItem.getEntity() === null && newItem.getEntity() === null)
                        || ((stateItem.getEntity() !== null && newItem.getEntity() !== null)
                            && (stateItem.getEntity().id === newItem.getEntity().id))))).length === 0);
                let newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterValue;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    allFileItems: [...state.allFileItems, ...newGobiiFileItems],
                    filters: newFilterState
                };
                break;
            } // LOAD_FILE_ITEM_LIST_WITH_FILTER
            case gobiiFileItemAction.LOAD_FILTER: {
                const filterId = action.payload.filterId.toString();
                const filterPayload = action.payload.filter;
                let newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterPayload;
                /***
                 * The following lines are intended to solve an oddly thorny problem.
                 On the dataset tab, if you don't set the selected label item here in state,
                 when you set the PI filter back to All PI’s, the two child lists are filtered
                 as they should be, but the top-most item in the list doesn’t change – they
                 don’t revert to “All Projects” and “All Experiments.” The problem seems to be
                 that once the list has decided what the top item is, if _none_ of the items
                 in the list has selected==true, it just stays where it is in the list: there
                 needs to be a differentiator. It also matters significantly that you set the
                 selected item in the reducer: if you do it elsewhere it does not work
                 consistently (as you would expect whenever changing state).
    
                 This particular solution is not ideal. Its conditions are organized around
                 the particular details of this problem -- it's not generaelizable. Most imporantly,
                 the filter that's applied to figure out the target list of
                 items for which the first Label needs to be selected should be exactly the same
                 filter as the one that populated the drop-down in question. I think the best way
                 to do this is for the content of the filter to be associated with with the Filterparams
                 and for the reducer to have access to that. So the selector methods in the reducer would
                 just get the FilterParams object and apply its filter semantics. The LOAD_ITEMS action would
                 then use exactly the same filter to determine the list of items to focus on. But this would
                 be a more substantial change than what's needed to fix the immediate issue.
                 Also, the isExtractCriterion condition seems a bit arbitrary. For now, it just means -- this
                 group of controls that I happen to know require this treatment.
    
                 */
                let newFileItemState = state.allFileItems.slice();
                if (!newFilterState[filterId].targetEntityUniqueId.getIsExtractCriterion()) {
                    let gobiiFileItemCompoundId = newFilterState[filterId].targetEntityUniqueId;
                    let allItemsForFilter = newFileItemState
                        .filter(gfi => {
                        return (gfi.getGobiiExtractFilterType() === state.gobiiExtractFilterType
                            && gfi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            || gfi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                            && gfi.getProcessType() !== type_process_1.ProcessType.DUMMY
                            && gfi.getEntityType() === gobiiFileItemCompoundId.getEntityType();
                    });
                    allItemsForFilter.forEach(gfi => {
                        gfi.setSelected(false);
                    });
                    if (filterPayload.targetEntityFilterValue && +filterPayload.targetEntityFilterValue > 0) {
                        let selectedItem = allItemsForFilter.find(gfi => gfi.getItemId() === filterPayload.targetEntityFilterValue);
                        if (selectedItem) {
                            selectedItem.setSelected(true);
                        }
                    }
                    else {
                        if (allItemsForFilter[0]) {
                            if ((!filterPayload.relatedEntityFilterValue || +filterPayload.relatedEntityFilterValue < 1)) {
                                allItemsForFilter[0].setSelected(true);
                            }
                        }
                    }
                }
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    allFileItems: newFileItemState,
                    filters: newFilterState
                };
                break;
            } // LOAD_FILTER
            case gobiiFileItemAction.ADD_TO_EXTRACT:
                {
                    const gobiiFileItemPayload = action.payload;
                    returnVal = addToExtractItems(state, gobiiFileItemPayload);
                    break;
                } // ADD_TO_EXTRACT
            case gobiiFileItemAction.ADD_TO_EXTRACT_BY_ITEM_ID: {
                const fileItemUniqueIdPayload = action.payload;
                let gobiiFileItem = state
                    .allFileItems
                    .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);
                returnVal = addToExtractItems(state, gobiiFileItem);
                break;
            } //
            case gobiiFileItemAction.REPLACE_BY_ITEM_ID:
                {
                    let itemCurrentlyInExtract = state
                        .allFileItems
                        .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdCurrentlyInExtract);
                    if (itemCurrentlyInExtract.getIsExtractCriterion()) {
                        let itemToReplaceItWith = state
                            .allFileItems
                            .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdToReplaceItWith);
                        let stateAfterRemove = removeFromExtractItems(state, itemCurrentlyInExtract);
                        returnVal = addToExtractItems(stateAfterRemove, itemToReplaceItWith);
                    }
                    else {
                        // there is also an effect that does additional things with this action
                        return state;
                    }
                    break;
                }
            case gobiiFileItemAction.REPLACE_ITEM_OF_SAME_COMPOUND_ID: {
                let newItemToAdd = action.payload.gobiiFileitemToReplaceWith;
                let fileItemToReplace = state.allFileItems
                    .find(fi => fi.getGobiiExtractFilterType() === newItemToAdd.getGobiiExtractFilterType()
                    && fi.compoundIdeEquals(newItemToAdd));
                let stateWithNewFileItem = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: state.filters
                };
                // remove existing item if applicable
                if (fileItemToReplace) {
                    stateWithNewFileItem.allFileItems =
                        stateWithNewFileItem.allFileItems.filter(fi => fi.getFileItemUniqueId() !== fileItemToReplace.getFileItemUniqueId());
                }
                // add new item
                stateWithNewFileItem.allFileItems.push(newItemToAdd);
                // now add new item to selection if applicable
                let stateWithItemSelection;
                if (newItemToAdd.getIsExtractCriterion()) {
                    if (fileItemToReplace) {
                        let oldItemUnselectedState = removeFromExtractItems(stateWithNewFileItem, fileItemToReplace);
                        stateWithItemSelection = addToExtractItems(oldItemUnselectedState, newItemToAdd);
                    }
                    else {
                        stateWithItemSelection = addToExtractItems(stateWithNewFileItem, newItemToAdd);
                    }
                }
                returnVal = stateWithItemSelection;
                break;
            }
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT: {
                let gobiiFileItemPayload = action.payload;
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT_BY_ITEM_ID: {
                const fileItemUniqueIdPayload = action.payload;
                let gobiiFileItemPayload = state
                    .allFileItems
                    .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_ALL_FROM_EXTRACT: {
                // only those not of the same extract filter type should remain selected
                let newFIleItemState = state.allFileItems.slice();
                let itemsToDeselect = newFIleItemState
                    .filter(fi => fi.getGobiiExtractFilterType() === action.payload);
                itemsToDeselect.forEach(fi => fi.setSelected(false));
                let newSelectedItems = state.uniqueIdsOfExtractFileItems
                    .filter(id => !itemsToDeselect
                    .find(fi => fi.getFileItemUniqueId() === id));
                // remove emphemeral items from state
                newFIleItemState = newFIleItemState
                    .filter(fi => !fi.getIsEphemeral());
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    allFileItems: newFIleItemState,
                    uniqueIdsOfExtractFileItems: newSelectedItems,
                    filters: state.filters
                };
                break;
            }
            case gobiiFileItemAction.SET_EXTRACT_TYPE: {
                const gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                returnVal = {
                    gobiiExtractFilterType: gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: state.filters
                };
                break;
            }
        }
        return returnVal;
    }
    exports_1("fileItemsReducer", fileItemsReducer);
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (gobiiFileItemAction_1) {
                gobiiFileItemAction = gobiiFileItemAction_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                gobiiExtractFilterType: type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN,
                uniqueIdsOfExtractFileItems: [],
                allFileItems: [],
                filters: new Map()
            });
            /**
             * Because the data structure is defined within the fileItemsReducer it is optimal to
             * locate our selector functions at this level. If store is to be thought of
             * as a database, and reducers the tables, selectors can be considered the
             * queries into said database. Remember to keep your selectors small and
             * focused so they can be combined and composed to fit each particular
             * use-case.
             *
             * WARNING: as of 4.0.2, ngrx/platform  issue # 208: https://github.com/ngrx/platform/issues/208
             * has _not_ been resulved. So selectors _must_ return non-primtive objects. See my comment
             * in export-format-component.ts
             */
            exports_1("getGobiiExtractFilterType", getGobiiExtractFilterType = (state) => state.gobiiExtractFilterType);
            exports_1("getFileItems", getFileItems = (state) => state.allFileItems.filter(fi => fi.getGobiiExtractFilterType() === state.gobiiExtractFilterType));
            exports_1("getUniqueIds", getUniqueIds = (state) => state.allFileItems.map(fileItem => fileItem.getFileItemUniqueId()));
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = (state) => state.uniqueIdsOfExtractFileItems);
            exports_1("getFilters", getFilters = (state) => state.filters);
            exports_1("getSelected", getSelected = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
                let returnVal = fileItems
                    .filter(fileItem => selectedUniqueIds
                    .find(uniqueId => fileItem.getFileItemUniqueId() === uniqueId));
                return returnVal;
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getFileItems, getUniqueIds, (entities, ids) => {
                return entities;
            }));
            // ideally, the redux way of selecting data would be for the selection to be based
            // on which entity is "selected" The problem is that we have controlls in which the
            // entity type is parameterized -- it is not global state
            /// ****************** SYNCHRONOUS METHODS
            /// **************** GET SELECTED PER ENTITY TYPE
            exports_1("getSelectedFileFormat", getSelectedFileFormat = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
                // default
                let returnVal = gobii_file_item_1.GobiiFileItem
                    .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                    .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                    .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                let formatItem = fileItems
                    .find(fi => fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT
                    && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));
                if (formatItem) {
                    returnVal = formatItem;
                }
                return returnVal;
            }));
            exports_1("getSelectedSampleType", getSelectedSampleType = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
                // default
                let returnVal = gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                    .setItemName(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME])
                    .setItemId(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME]);
                let formatItem = fileItems
                    .find(fi => fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE
                    && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));
                if (formatItem) {
                    returnVal = formatItem;
                }
                return returnVal;
            }));
            exports_1("getJobId", getJobId = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
                // default
                let returnVal = gobii_file_item_1.GobiiFileItem
                    .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.JOB_ID)
                    .setItemId("UNSET")
                    .setItemName("UNSET");
                let jobIdItem = fileItems
                    .find(fi => (fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID)
                    && (selectedUniqueIds.indexOf(fi.getFileItemUniqueId()) > -1));
                if (jobIdItem) {
                    returnVal = jobIdItem;
                }
                return returnVal;
            }));
            exports_1("getUploadFiles", getUploadFiles = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
                // default
                let returnVal = fileItems
                    .filter(fi => fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE
                    || fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE);
                return returnVal;
            }));
            exports_1("getPiContacts", getPiContacts = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                let returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.CONTACT
                    && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                    .map(fi => fi);
                return returnVal;
            }));
            exports_1("getProjects", getProjects = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                let returnVal = [];
                returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.PROJECT
                    && e.getProcessType() !== type_process_1.ProcessType.DUMMY)
                    .map(fi => fi);
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                        && (e.getEntityType() === type_entity_1.EntityType.PROJECT)
                        && e.getProcessType() === type_process_1.ProcessType.DUMMY)
                        .map(fi => fi);
                }
                return returnVal;
            }));
            exports_1("getExperiments", getExperiments = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT)
                    .map(fi => fi);
            }));
            exports_1("getDatasets", getDatasets = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.DATASET)
                    .map(fi => fi);
            }));
            exports_1("getCvTermsDataType", getCvTermsDataType = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                let returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.CV
                    && e.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE)
                    .map(fi => fi);
                return returnVal;
            }));
            exports_1("getCvTermsJobStatus", getCvTermsJobStatus = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                let returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.CV
                    && e.getCvFilterType() === cv_filter_type_1.CvFilterType.JOB_STATUS)
                    .map(fi => fi);
                return returnVal;
            }));
            exports_1("getMapsets", getMapsets = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.MAPSET)
                    .map(fi => fi);
            }));
            exports_1("getPlatforms", getPlatforms = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.PLATFORM)
                    .map(fi => fi);
            }));
            exports_1("getMarkerGroups", getMarkerGroups = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.MARKER_GROUP)
                    .map(fi => fi);
            }));
            // **************** GET SELECTED PER ENTITY TYPE
            exports_1("getSelectedPiContacts", getSelectedPiContacts = reselect_1.createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {
                return fileItems.filter(e => ids.find(id => id === e.getFileItemUniqueId())
                    && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    && e.getEntityType() === type_entity_1.EntityType.CONTACT
                    && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                    .map(fi => fi);
            }));
            // here
            exports_1("getProjectsForSelectedPi", getProjectsForSelectedPi = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {
                let returnVal = [];
                let contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
                }
                let projectId;
                returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                    && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                    && e.getEntityType() === type_entity_1.EntityType.PROJECT
                    && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
                        || +e.getItemId() === 0 // Inlcude label "All Projects"
                        || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) // the item has an fk value
                            && e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) === contactId)) // and it matches
                ).map(fi => fi);
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.PROJECT
                        && e.getProcessType() === type_process_1.ProcessType.DUMMY))
                        .map(fi => fi);
                }
                return returnVal;
                /*
                let returnVal: GobiiFileItem[] = [];
            
                if (filters[FilterParamNames.PROJECTS_BY_CONTACT]) {
            
                    let contactId: string = filters[FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(e =>
                        (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                        && (e.getEntityType() === EntityType.PROJECT)
                        && (e.getParentItemId() === contactId)
                        && e.getProcessType() !== ProcessType.DUMMY)
                        .map(fi => fi);
            
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(e =>
                            (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                            && (e.getEntityType() === EntityType.PROJECT)
                            && e.getProcessType() === ProcessType.DUMMY)
                            .map(fi => fi);
                    }
                }
            
                return returnVal;
                */
            }));
            exports_1("getExperimentsForSelectedProject", getExperimentsForSelectedProject = reselect_1.createSelector(getFileItems, getFilters, (fileItems, filters) => {
                let returnVal = [];
                if (filters[file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT]) {
                    let projectId = filters[file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                        && (e.getEntityType() === type_entity_1.EntityType.EXPERIMENT)
                        && (e.getParentItemId() === projectId)
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY)
                        .map(fi => fi);
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.EXPERIMENT)
                            //                && (e.getParentItemId() === projectId )
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY)
                            .map(fi => fi);
                    }
                }
                return returnVal;
            }));
            exports_1("getDatasetsForSelectedExperiment", getDatasetsForSelectedExperiment = reselect_1.createSelector(getFileItems, getFilters, (fileItems, filters) => {
                let returnVal = [];
                if (filters[file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT]) {
                    let experimentId = filters[file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.DATASET
                        && e.getParentItemId() === experimentId
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY))
                        .map(fi => fi);
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.DATASET
                            //                    && e.getParentItemId() === experimentId
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY))
                            .map(fi => fi);
                    }
                    ``;
                }
                return returnVal;
            }));
            exports_1("getDatasetEntities", getDatasetEntities = reselect_1.createSelector(getFileItems, getFilters, (fileItems, filters) => {
                let returnVal = [];
                // the child filter has the parent fk value
                let contactId = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue : null;
                let compounUniqueIdForContacts = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId : null;
                let projectId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue : null;
                let compounUniqueIdForProjectsByContact = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId : null;
                let experimentId = filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityFilterValue : null;
                let compounUniqueIdForExperimentsByProject = filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityUniqueId : null;
                let datasetEntitiesFilteredByExperiment = [];
                datasetEntitiesFilteredByExperiment = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    && e.getEntityType() === type_entity_1.EntityType.DATASET
                    && ((contactId === null) || +contactId < 1 || compounUniqueIdForContacts === null || e.getRelatedEntityFilterValue(compounUniqueIdForContacts) === contactId)
                    && ((projectId === null) || +projectId < 1 || compounUniqueIdForProjectsByContact === null || e.getRelatedEntityFilterValue(compounUniqueIdForProjectsByContact) === projectId)
                    && ((experimentId === null) || +experimentId < 1 || compounUniqueIdForExperimentsByProject === null || e.getRelatedEntityFilterValue(compounUniqueIdForExperimentsByProject) === experimentId)
                    && e.hasEntity()
                    && e.getProcessType() !== type_process_1.ProcessType.DUMMY))
                    .map(fi => fi);
                let jobStatusFilterParams = filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS];
                if (jobStatusFilterParams
                    && jobStatusFilterParams.jobStatusFilterValues != null) {
                    returnVal = datasetEntitiesFilteredByExperiment
                        .filter(function (fi) {
                        return jobStatusFilterParams.checkExtractReady(fi);
                    });
                }
                else {
                    returnVal = datasetEntitiesFilteredByExperiment;
                }
                return returnVal;
            }));
            exports_1("getDatasetEntitiesPaged", getDatasetEntitiesPaged = reselect_1.createSelector(getFileItems, getFilters, (fileItems, filters) => {
                let intermediateResult = getDatasetEntities.resultFunc(fileItems, filters);
                let payloadFilter = filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED];
                let returnVal = intermediateResult.filter(gfi => gfi.getPageNumber() === payloadFilter.pagination.currentPage);
                return returnVal;
            }));
            exports_1("getPiContactsFilterOptional", getPiContactsFilterOptional = reselect_1.createSelector(getFileItems, getGobiiExtractFilterType, (fileItems, gobiiExtractFilterType) => {
                let returnVal = [];
                returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                    && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getEntityType() === type_entity_1.EntityType.CONTACT
                    && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                    .map(fi => fi);
                return returnVal;
            }));
            exports_1("getProjectsFilterOptional", getProjectsFilterOptional = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {
                let returnVal = [];
                // The project filter's value is a contactId. So we want only
                // those projects that have an fk reference to the specified contact.
                let contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].relatedEntityFilterValue;
                }
                returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                    && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                    && e.getEntityType() === type_entity_1.EntityType.PROJECT
                    && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
                        || +e.getItemId() === 0 // Inlcude label "All Projects"
                        || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                            && e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId)) // and it matches
                ).map(fi => fi);
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.PROJECT
                        && e.getProcessType() === type_process_1.ProcessType.DUMMY))
                        .map(fi => fi);
                }
                return returnVal;
            }));
            // export const getExperimentsFilterOptional = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {
            //
            //     let returnVal: GobiiFileItem[] = [];
            //
            //     let projectId: string = null;
            //     if (filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL]) {
            //         projectId = filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].relatedEntityFilterValue;
            //     }
            //
            //     returnVal = fileItems.filter(
            //         e =>
            //             (e.getGobiiExtractFilterType() == gobiiExtractFilterType
            //                 && e.getExtractorItemType() === ExtractorItemType.ENTITY
            //                 || e.getExtractorItemType() === ExtractorItemType.LABEL)
            //             && e.getProcessType() !== ProcessType.DUMMY
            //             && e.getEntityType() === EntityType.EXPERIMENT
            //             && ((!projectId || (+projectId < 0)) // state is not filtered -- we don't care, or . . .
            //             || +e.getItemId() === 0 // Inlcude label "All Projects"
            //             || (e.getRelatedEntityFilterValue(filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].gobiiCompoundUniqueId) // the item has an fk value
            //                 && e.getRelatedEntityFilterValue(filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].gobiiCompoundUniqueId) === projectId)) // and it matches
            //     ).map(fi => fi);
            //
            //
            //     if (returnVal.length <= 0) {
            //         returnVal = fileItems.filter(e =>
            //             (e.getExtractorItemType() === ExtractorItemType.ENTITY
            //                 && e.getEntityType() === EntityType.EXPERIMENT
            //                 && e.getProcessType() === ProcessType.DUMMY))
            //             .map(fi => fi);
            //     }
            //
            //     return returnVal;
            // });
            exports_1("getExperimentsFilterOptional", getExperimentsFilterOptional = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {
                let returnVal = [];
                let projectId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
                    projectId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue;
                }
                let contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue;
                }
                let projectIds = [];
                if ((projectId && +projectId > 0)) {
                    projectIds.push(projectId);
                }
                else if (contactId && +contactId) {
                    projectIds = fileItems
                        .filter(fi => fi.compoundIdeEquals(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId)
                        && fi.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId)
                        .map(fi => fi.getItemId());
                }
                returnVal = fileItems.filter(e => (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                    || e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL)
                    && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                    && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT
                    && ((!projectId && !contactId) // state is not filtered -- we don't care, or . . .
                        || +e.getItemId() === 0 // Inlcude label "All Projects"
                        || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                            && projectIds.find(pid => e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) === pid))) // and it matches
                ).map(fi => fi);
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(e => (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT
                        && e.getProcessType() === type_process_1.ProcessType.DUMMY))
                        .map(fi => fi);
                }
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=fileitems-reducer.js.map