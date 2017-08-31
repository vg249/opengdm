System.register(["reselect", "../actions/fileitem-action", "../../model/file-model-node", "../../model/type-entity", "../../model/type-nameid-filter-params"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function fileItemsReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiFileItemAction.LOAD: {
                var gobiiFileItemsPayload = action.payload;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (newItem) {
                    return state
                        .fileItems
                        .filter(function (stateItem) {
                        return (stateItem.getExtractorItemType() === newItem.getExtractorItemType() &&
                            stateItem.getEntityType() === newItem.getEntityType() &&
                            stateItem.getEntitySubType() === newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() === newItem.getCvFilterType() &&
                            stateItem.getItemId() === newItem.getItemId());
                    }).length === 0;
                });
                returnVal = {
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                    fileItems: state.fileItems.concat(newGobiiFileItems),
                    filters: {}
                };
                break;
            } // LOAD
            case gobiiFileItemAction.LOAD_FILTERED_ITEMS: {
                var gobiiFileItemsPayload = action.payload.gobiiFileItems;
                var nameIdRequestParamsPayload = action.payload.nameIdRequestParams;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (newItem) {
                    return state
                        .fileItems
                        .filter(function (stateItem) {
                        return (stateItem.getExtractorItemType() === newItem.getExtractorItemType() &&
                            stateItem.getEntityType() === newItem.getEntityType() &&
                            stateItem.getEntitySubType() === newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() === newItem.getCvFilterType() &&
                            stateItem.getItemId() === newItem.getItemId());
                    }).length === 0;
                });
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[nameIdRequestParamsPayload.getQueryName()] = nameIdRequestParamsPayload;
                returnVal = {
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                    fileItems: state.fileItems.concat(newGobiiFileItems),
                    filters: newFilterState
                };
                break;
            } // LOAD_FILTERED_ITEMS
            // Technically, and according to the ngrx/store example app,
            // it should be possible for different actions to have a different
            // payload type, such that it's possible for a payload to be a single
            // GobiiFileItem rather than an array of. However, I cannot get this
            // to compile properly. See this issue I submitted:
            // https://github.com/ngrx/platform/issues/255
            // For now in the interest of making progress I am using
            // an array type for all action payloads
            case gobiiFileItemAction.SELECT_FOR_EXTRACT: {
                var gobiiFileItemPayload_1 = action.payload;
                var selectedUniqueItemIds = state
                    .fileItems
                    .filter(function (fileItem) {
                    return gobiiFileItemPayload_1.getFileItemUniqueId() !== fileItem.getFileItemUniqueId();
                })
                    .map(function (selectedFileItem) { return selectedFileItem.getFileItemUniqueId(); });
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected.concat(selectedUniqueItemIds),
                    filters: {} // spread syntax
                };
                break;
            } // SELECT_FOR_EXTRACT
            case gobiiFileItemAction.DESELECT_FOR_EXTRACT: {
                var gobiiFileItemPayload_2 = action.payload;
                var newSelectedUniqueItemIds = state
                    .fileItemUniqueIdsSelected
                    .filter(function (selectedId) {
                    return gobiiFileItemPayload_2.getFileItemUniqueId() != selectedId;
                });
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: newSelectedUniqueItemIds,
                    filters: {}
                };
                break;
            }
            case gobiiFileItemAction.SET_ENTITY_FILTER: {
                var nameIdRequestParamsPayload = action.payload.nameIdRequestParams;
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[nameIdRequestParamsPayload.getQueryName()] = nameIdRequestParamsPayload;
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                    filters: newFilterState
                };
                break;
            } //
            case gobiiFileItemAction.SELECT_FOR_EXTRACT_BY_FILE_ITEM_ID: {
                var fileItemUniqueIdPayload_1 = action.payload;
                var newUniqueId = [];
                if (!state
                    .fileItemUniqueIdsSelected
                    .find(function (i) { return i === fileItemUniqueIdPayload_1; })) {
                    if (state
                        .fileItems
                        .find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueIdPayload_1; })) {
                        newUniqueId.push(fileItemUniqueIdPayload_1);
                    }
                }
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected.concat(newUniqueId),
                    filters: state.filters
                };
                break;
            } //
        }
        return returnVal;
    }
    exports_1("fileItemsReducer", fileItemsReducer);
    var reselect_1, gobiiFileItemAction, file_model_node_1, type_entity_1, type_nameid_filter_params_1, initialState, getFileItems, getUniqueIds, getSelectedUniqueIds, getFilters, getSelected, getAll, getContacts, getFirstContact, getProjects, getFirstProject, getExperiments, getFirstExperiment, getDatasets, getFirstDataset, getCvTerms, getFirstCvTerm, getMapsets, getFirstmapset, getPlatforms, getFirstPlatform, getMarkerGroups, getFirstMarkerGroup, getSelectedPiContacts, getProjectsForSelectedPi, getExperimentsForSelectedProject, getDatasetsForSelectedExperiment;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiFileItemAction_1) {
                gobiiFileItemAction = gobiiFileItemAction_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_nameid_filter_params_1_1) {
                type_nameid_filter_params_1 = type_nameid_filter_params_1_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                fileItemUniqueIdsSelected: [],
                fileItems: [],
                filters: {}
            });
            /**
             * Because the data structure is defined within the fileItemsReducer it is optimal to
             * locate our selector functions at this level. If store is to be thought of
             * as a database, and reducers the tables, selectors can be considered the
             * queries into said database. Remember to keep your selectors small and
             * focused so they can be combined and composed to fit each particular
             * use-case.
             */
            exports_1("getFileItems", getFileItems = function (state) { return state.fileItems; });
            exports_1("getUniqueIds", getUniqueIds = function (state) { return state.fileItems.map(function (fileItem) { return fileItem.getFileItemUniqueId(); }); });
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = function (state) { return state.fileItemUniqueIdsSelected; });
            exports_1("getFilters", getFilters = function (state) { return state.filters; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                return fileItems.filter(function (fileItem) {
                    selectedUniqueIds.filter(function (uniqueId) { return fileItem.getFileItemUniqueId() === uniqueId; });
                });
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getFileItems, getUniqueIds, function (entities, ids) {
                return ids.map(function (id) { return entities[id]; });
            }));
            // ideally, the redux way of selecting data would be for the selection to be based
            // on which entity is "selected" The problem is that we have controlls in which the
            // entity type is parameterized -- it is not global state
            exports_1("getContacts", getContacts = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.Contacts;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstContact", getFirstContact = reselect_1.createSelector(getContacts, function (contacts) {
                return contacts[0];
            }));
            exports_1("getProjects", getProjects = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.Projects;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstProject", getFirstProject = reselect_1.createSelector(getProjects, function (projects) {
                return projects[0];
            }));
            exports_1("getExperiments", getExperiments = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.Experiments;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstExperiment", getFirstExperiment = reselect_1.createSelector(getExperiments, function (experiments) {
                return experiments[0];
            }));
            exports_1("getDatasets", getDatasets = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.DataSets;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstDataset", getFirstDataset = reselect_1.createSelector(getDatasets, function (datasets) {
                return datasets[0];
            }));
            exports_1("getCvTerms", getCvTerms = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.CvTerms;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstCvTerm", getFirstCvTerm = reselect_1.createSelector(getCvTerms, function (cvterms) {
                return cvterms[0];
            }));
            exports_1("getMapsets", getMapsets = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.Mapsets;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstmapset", getFirstmapset = reselect_1.createSelector(getMapsets, function (mapsets) {
                return mapsets[0];
            }));
            exports_1("getPlatforms", getPlatforms = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.Platforms;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstPlatform", getFirstPlatform = reselect_1.createSelector(getCvTerms, function (platforms) {
                return platforms[0];
            }));
            exports_1("getMarkerGroups", getMarkerGroups = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        || e.getExtractorItemType() === file_model_node_1.ExtractorItemType.LABEL)
                        && e.getEntityType() === type_entity_1.EntityType.MarkerGroups;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getFirstMarkerGroup", getFirstMarkerGroup = reselect_1.createSelector(getCvTerms, function (markergroups) {
                return markergroups[0];
            }));
            /// ****************** SYNCHRONOUS METHODS
            /// **************** GET SELECTED PER ENTITY TYPE
            exports_1("getSelectedPiContacts", getSelectedPiContacts = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return ids.find(function (id) { return id === e.getFileItemUniqueId(); })
                        && e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.Contacts
                        && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getProjectsForSelectedPi", getProjectsForSelectedPi = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[type_nameid_filter_params_1.NameIdFilterParamTypes.PROJECTS_BY_CONTACT]) {
                    var contactId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.PROJECTS_BY_CONTACT].getEntityFilterValue();
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.Projects)
                            && (e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                            && (e.getParentItemId() === contactId_1);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
            exports_1("getExperimentsForSelectedProject", getExperimentsForSelectedProject = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT]) {
                    var projectId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT].getEntityFilterValue();
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.Experiments)
                            && (e.getParentItemId() === projectId_1);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
            exports_1("getDatasetsForSelectedExperiment", getDatasetsForSelectedExperiment = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT]) {
                    var experimentId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT].getEntityFilterValue();
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.DataSets)
                            && (e.getParentItemId() === experimentId_1);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=fileitems-reducer.js.map