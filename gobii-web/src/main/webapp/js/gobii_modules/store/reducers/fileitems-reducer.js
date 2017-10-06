System.register(["reselect", "../../model/gobii-file-item", "../actions/fileitem-action", "../../model/file-model-node", "../../model/type-entity", "../../model/type-nameid-filter-params", "../../model/type-process", "../../views/entity-labels", "../../model/type-extractor-filter", "../../model/type-extract-format"], function (exports_1, context_1) {
    "use strict";
    _this = this;
    var __moduleName = context_1 && context_1.id;
    function addToExtractItems(state, gobiiFileItem) {
        if (!state.allFileItems.find(function (fi) { return fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId(); })) {
            console.log("Item is not in the collection: " + gobiiFileItem.getItemName() + " of type " + entity_labels_1.Labels.instance().treeExtractorTypeLabels[gobiiFileItem.getExtractorItemType()]);
        }
        gobiiFileItem.setSelected(true);
        var newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        if (!newSelectedUniqueIdsState.find(function (id) { return id === gobiiFileItem.getFileItemUniqueId(); })) {
            newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
        }
        var returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: state.allFileItems,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function removeFromExtractItems(state, gobiiFileItem) {
        gobiiFileItem.setSelected(false);
        var newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        var idx = newSelectedUniqueIdsState.findIndex(function (id) { return id === gobiiFileItem.getFileItemUniqueId(); });
        if (idx) {
            newSelectedUniqueIdsState.splice(idx, 1);
        }
        var returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: state.allFileItems,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function fileItemsReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiFileItemAction.LOAD_FILE_ITEM: {
                var gobiiFileItemPayloadItem_1 = action.payload.gobiiFileItem;
                var newFileItemsItemsState = state.allFileItems.slice();
                var newSelectedFileItemUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
                var existingItem_1 = newFileItemsItemsState.find(function (stateItem) {
                    return stateItem.equals(gobiiFileItemPayloadItem_1);
                });
                if (existingItem_1) {
                    var existingSelectedUniqueId = newSelectedFileItemUniqueIdsState
                        .find(function (id) { return id === existingItem_1.getFileItemUniqueId(); });
                    if (existingSelectedUniqueId) {
                        newSelectedFileItemUniqueIdsState.splice(newSelectedFileItemUniqueIdsState.indexOf(existingSelectedUniqueId), 1);
                        newSelectedFileItemUniqueIdsState.push(gobiiFileItemPayloadItem_1.getFileItemUniqueId());
                    }
                    newFileItemsItemsState = newFileItemsItemsState.filter(function (fi) { return fi.getFileItemUniqueId() != existingItem_1.getFileItemUniqueId(); });
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem_1);
                }
                else {
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem_1);
                }
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: newSelectedFileItemUniqueIdsState,
                    allFileItems: newFileItemsItemsState,
                    filters: state.filters
                };
                break;
            } // LOAD_FILE_ITEM_LIST
            case gobiiFileItemAction.LOAD_FILE_ITEM_LIST: {
                var gobiiFileItemsPayload = action.payload.gobiiFileItems;
                var filterId = action.payload.filterId.toString();
                var filterValue = action.payload.filterValue;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (newItem) {
                    return state
                        .allFileItems
                        .filter(function (stateItem) {
                        return (stateItem.getExtractorItemType() === newItem.getExtractorItemType() &&
                            stateItem.getEntityType() === newItem.getEntityType() &&
                            stateItem.getEntitySubType() === newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() === newItem.getCvFilterType() &&
                            stateItem.getItemId() === newItem.getItemId());
                    }).length === 0;
                });
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterValue;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    allFileItems: state.allFileItems.concat(newGobiiFileItems),
                    filters: newFilterState
                };
                break;
            } // LOAD_FILE_ITEM_LIST
            case gobiiFileItemAction.ADD_TO_EXTRACT: {
                var gobiiFileItemPayload = action.payload;
                returnVal = addToExtractItems(state, gobiiFileItemPayload);
                break;
            } // ADD_TO_EXTRACT
            case gobiiFileItemAction.ADD_TO_EXTRACT_BY_ITEM_ID: {
                var fileItemUniqueIdPayload_1 = action.payload;
                var gobiiFileItem = state
                    .allFileItems
                    .find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueIdPayload_1; });
                returnVal = addToExtractItems(state, gobiiFileItem);
                break;
            } //
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT: {
                var gobiiFileItemPayload = action.payload;
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT_BY_ITEM_ID: {
                var fileItemUniqueIdPayload_2 = action.payload;
                var gobiiFileItemPayload = state
                    .allFileItems
                    .find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueIdPayload_2; });
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_ALL_FROM_EXTRACT: {
                // only those not of the same extract filter type should remain selected
                var newFIleItemState = state.allFileItems.slice();
                var itemsToDeselect_1 = newFIleItemState
                    .filter(function (fi) { return fi.getGobiiExtractFilterType() === action.payload; });
                itemsToDeselect_1.forEach(function (fi) { return fi.setSelected(false); });
                var newSelectedItems = state.uniqueIdsOfExtractFileItems
                    .filter(function (id) { return !itemsToDeselect_1
                    .find(function (fi) { return fi.getFileItemUniqueId() === id; }); });
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    allFileItems: newFIleItemState,
                    uniqueIdsOfExtractFileItems: newSelectedItems,
                    filters: state.filters
                };
                break;
            }
            case gobiiFileItemAction.SET_FILTER_VALUE: {
                var filterId = action.payload.filterId.toString();
                var filterValue = action.payload.filterValue;
                var gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterValue;
                returnVal = {
                    gobiiExtractFilterType: gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: newFilterState
                };
                break;
            }
            case gobiiFileItemAction.SET_EXTRACT_TYPE: {
                var gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                returnVal = {
                    gobiiExtractFilterType: gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: state.filters
                };
                break;
            } //
        }
        return returnVal;
    }
    exports_1("fileItemsReducer", fileItemsReducer);
    var _this, reselect_1, gobii_file_item_1, gobiiFileItemAction, file_model_node_1, type_entity_1, type_nameid_filter_params_1, type_process_1, entity_labels_1, type_extractor_filter_1, type_extract_format_1, initialState, getFileItems, getUniqueIds, getSelectedUniqueIds, getFilters, getSelected, getAll, getContacts, getFirstContact, getProjects, getFirstProject, getExperiments, getFirstExperiment, getDatasets, getFirstDataset, getCvTerms, getFirstCvTerm, getMapsets, getFirstmapset, getPlatforms, getFirstPlatform, getMarkerGroups, getFirstMarkerGroup, getSelectedPiContacts, getProjectsForSelectedPi, getExperimentsForSelectedProject, getDatasetsForSelectedExperiment, getSelectedFileFormat, getJobId, getUploadFiles;
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
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_nameid_filter_params_1_1) {
                type_nameid_filter_params_1 = type_nameid_filter_params_1_1;
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
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                gobiiExtractFilterType: type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN,
                uniqueIdsOfExtractFileItems: [],
                allFileItems: [],
                filters: {}
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
            exports_1("getFileItems", getFileItems = function (state) { return state.allFileItems; });
            exports_1("getUniqueIds", getUniqueIds = function (state) { return state.allFileItems.map(function (fileItem) { return fileItem.getFileItemUniqueId(); }); });
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = function (state) { return state.uniqueIdsOfExtractFileItems; });
            exports_1("getFilters", getFilters = function (state) { return state.filters; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                var returnVal = fileItems
                    .filter(function (fileItem) {
                    return selectedUniqueIds
                        .find(function (uniqueId) { return fileItem.getFileItemUniqueId() === uniqueId; });
                });
                return returnVal;
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getFileItems, getUniqueIds, function (entities, ids) {
                return entities;
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
                    var contactId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.PROJECTS_BY_CONTACT];
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.Projects)
                            && (e.getParentItemId() === contactId_1)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    })
                        .map(function (fi) { return fi; });
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(function (e) {
                            return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                                && (e.getEntityType() === type_entity_1.EntityType.Projects)
                                && e.getProcessType() === type_process_1.ProcessType.DUMMY;
                        })
                            .map(function (fi) { return fi; });
                    }
                }
                return returnVal;
            }));
            exports_1("getExperimentsForSelectedProject", getExperimentsForSelectedProject = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT]) {
                    var projectId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT];
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.Experiments)
                            && (e.getParentItemId() === projectId_1)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    })
                        .map(function (fi) { return fi; });
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(function (e) {
                            return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY)
                                && (e.getEntityType() === type_entity_1.EntityType.Experiments)
                                && e.getProcessType() === type_process_1.ProcessType.DUMMY;
                        })
                            .map(function (fi) { return fi; });
                    }
                }
                return returnVal;
            }));
            exports_1("getDatasetsForSelectedExperiment", getDatasetsForSelectedExperiment = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT]) {
                    var experimentId_1 = filters[type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT];
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.DataSets
                            && e.getParentItemId() === experimentId_1
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY);
                    })
                        .map(function (fi) { return fi; });
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(function (e) {
                            return (e.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                                && e.getEntityType() === type_entity_1.EntityType.DataSets
                                && e.getProcessType() === type_process_1.ProcessType.DUMMY);
                        })
                            .map(function (fi) { return fi; });
                    }
                }
                return returnVal;
            }));
            exports_1("getSelectedFileFormat", getSelectedFileFormat = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = gobii_file_item_1.GobiiFileItem
                    .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                    .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                    .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                var formatItem = fileItems
                    .find(function (fi) { return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT
                    && undefined !== selectedUniqueIds.find(function (id) { return id === fi.getFileItemUniqueId(); }); });
                if (formatItem) {
                    returnVal = formatItem;
                }
                return returnVal;
            }));
            exports_1("getJobId", getJobId = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = gobii_file_item_1.GobiiFileItem
                    .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(file_model_node_1.ExtractorItemType.JOB_ID)
                    .setItemId("UNSET")
                    .setItemName("UNSET");
                var jobIdItem = fileItems
                    .find(function (fi) { return (fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.JOB_ID)
                    && (selectedUniqueIds.indexOf(fi.getFileItemUniqueId()) > -1); });
                if (jobIdItem) {
                    returnVal = jobIdItem;
                }
                return returnVal;
            }));
            exports_1("getUploadFiles", getUploadFiles = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = fileItems
                    .filter(function (fi) { return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE
                    || fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE; });
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=fileitems-reducer.js.map