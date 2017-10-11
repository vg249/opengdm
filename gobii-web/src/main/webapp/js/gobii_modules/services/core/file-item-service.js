System.register(["@angular/core", "../../model/type-entity", "../../views/entity-labels", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/gobii-file-item", "../../model/dto-header-status-message", "../../model/type-process", "./name-id-service", "../../model/name-id-request-params", "../../store/actions/history-action", "../../store/actions/fileitem-action", "@ngrx/store", "../../model/name-id-label-type", "../../model/type-entity-filter", "../../model/type-nameid-filter-params", "rxjs/Observable", "rxjs/add/operator/expand"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, type_entity_1, entity_labels_1, file_model_node_1, type_extractor_filter_1, cv_filter_type_1, gobii_file_item_1, dto_header_status_message_1, type_process_1, name_id_service_1, name_id_request_params_1, historyAction, fileItemActions, store_1, name_id_label_type_1, type_entity_filter_1, type_nameid_filter_params_1, Observable_1, FileItemService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (name_id_request_params_1_1) {
                name_id_request_params_1 = name_id_request_params_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (fileItemActions_1) {
                fileItemActions = fileItemActions_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (type_nameid_filter_params_1_1) {
                type_nameid_filter_params_1 = type_nameid_filter_params_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {
            }
        ],
        execute: function () {
            FileItemService = (function () {
                function FileItemService(nameIdService, store) {
                    this.nameIdService = nameIdService;
                    this.store = store;
                    this.nameIdRequestParams = new Map();
                    // For non-hierarchically filtered request params, we just create them simply
                    // as we add them to the flat map
                    this.nameIdRequestParams.set(type_nameid_filter_params_1.NameIdFilterParamTypes.CV_DATATYPE, name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.CvTerms)
                        .setDynamicFilterValue(false)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPENAME)
                        .setFkEntityFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A));
                    this.nameIdRequestParams.set(type_nameid_filter_params_1.NameIdFilterParamTypes.MAPSETS, name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Mapsets)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.nameIdRequestParams.set(type_nameid_filter_params_1.NameIdFilterParamTypes.PLATFORMS, name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Platforms));
                    //for hierarchical items, we need to crate the nameid requests separately from the
                    //flat map: they _will_ need to be in the flat map; however, they all need to be
                    //useed to set up the filtering hierarchy
                    var nameIdRequestParamsContactsPi = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.CONTACT_PI, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Contacts)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);
                    var nameIdRequestParamsProject = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.PROJECTS_BY_CONTACT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Projects)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPEID);
                    var nameIdRequestParamsExperiments = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Experiments)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPEID);
                    var nameIdRequestParamsDatasets = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DataSets)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPEID);
                    //add the individual requests to the map
                    this.nameIdRequestParams.set(nameIdRequestParamsContactsPi.getQueryName(), nameIdRequestParamsContactsPi);
                    this.nameIdRequestParams.set(nameIdRequestParamsProject.getQueryName(), nameIdRequestParamsProject);
                    this.nameIdRequestParams.set(nameIdRequestParamsExperiments.getQueryName(), nameIdRequestParamsExperiments);
                    this.nameIdRequestParams.set(nameIdRequestParamsDatasets.getQueryName(), nameIdRequestParamsDatasets);
                    //build the parent-child request params graph
                    nameIdRequestParamsContactsPi
                        .setChildNameIdRequestParams([nameIdRequestParamsProject
                            .setParentNameIdRequestParams(nameIdRequestParamsContactsPi)
                            .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                                .setParentNameIdRequestParams(nameIdRequestParamsProject)
                                .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                                    .setParentNameIdRequestParams(nameIdRequestParamsExperiments)
                            ])
                        ])
                    ]);
                } // constructor
                FileItemService.prototype.setItemLabelType = function (gobiiExtractFilterType, nameIdFilterParamTypes, nameIdLabelType) {
                    var nameIdRequestParamsFromType = this.nameIdRequestParams.get(nameIdFilterParamTypes);
                    nameIdRequestParamsFromType.setNameIdLabelType(nameIdLabelType);
                };
                FileItemService.prototype.loadWithFilterParams = function (gobiiExtractFilterType, nameIdFilterParamTypes, filterValue) {
                    var nameIdRequestParamsFromType = this.nameIdRequestParams.get(nameIdFilterParamTypes);
                    this.loadNameIdsToFileItems(gobiiExtractFilterType, nameIdRequestParamsFromType, filterValue);
                };
                FileItemService.prototype.loadFileItem = function (gobiiFileItem, selectForExtract) {
                    var loadAction = new fileItemActions.LoadFileItemtAction({
                        gobiiFileItem: gobiiFileItem,
                        selectForExtract: selectForExtract
                    });
                    this.store.dispatch(loadAction);
                };
                FileItemService.prototype.loadNameIdsToFileItems = function (gobiiExtractFilterType, nameIdRequestParamsToLoad, filterValue) {
                    var _this = this;
                    if (nameIdRequestParamsToLoad.getDynamicFilterValue()) {
                        nameIdRequestParamsToLoad.setFkEntityFilterValue(filterValue);
                    }
                    this.nameIdService.get(nameIdRequestParamsToLoad)
                        .subscribe(function (nameIds) {
                        var fileItems = [];
                        if (nameIds && (nameIds.length > 0)) {
                            nameIds.forEach(function (n) {
                                var currentFileItem = gobii_file_item_1.GobiiFileItem.build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                    .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                    .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                    .setItemId(n.id)
                                    .setItemName(n.name)
                                    .setSelected(false)
                                    .setRequired(false)
                                    .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());
                                fileItems.push(currentFileItem);
                            });
                            var temp = "foo";
                            temp = "bar";
                            if (nameIdRequestParamsToLoad.getMameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                                var entityName = "";
                                if (nameIdRequestParamsToLoad.getCvFilterType() !== cv_filter_type_1.CvFilterType.UNKNOWN) {
                                    entityName += entity_labels_1.Labels.instance().cvFilterNodeLabels[nameIdRequestParamsToLoad.getCvFilterType()];
                                }
                                else if (nameIdRequestParamsToLoad.getEntitySubType() !== type_entity_1.EntitySubType.UNKNOWN) {
                                    entityName += entity_labels_1.Labels.instance().entitySubtypeNodeLabels[nameIdRequestParamsToLoad.getEntitySubType()];
                                }
                                else {
                                    entityName += entity_labels_1.Labels.instance().entityNodeLabels[nameIdRequestParamsToLoad.getEntityType()];
                                }
                                var label = "";
                                switch (nameIdRequestParamsToLoad.getMameIdLabelType()) {
                                    case name_id_label_type_1.NameIdLabelType.SELECT_A:
                                        label = "Select a " + entityName;
                                        break;
                                    // we require that these entity labels all be in the singular
                                    case name_id_label_type_1.NameIdLabelType.ALL:
                                        label = "All " + entityName + "s";
                                        break;
                                    case name_id_label_type_1.NameIdLabelType.NO:
                                        label = "No " + entityName;
                                        break;
                                    default:
                                        _this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unknown label type "
                                            + name_id_label_type_1.NameIdLabelType[nameIdRequestParamsToLoad.getMameIdLabelType()], null, null)));
                                }
                                var labelFileItem = gobii_file_item_1.GobiiFileItem
                                    .build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                    .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                    .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                    .setExtractorItemType(file_model_node_1.ExtractorItemType.LABEL)
                                    .setItemName(label)
                                    .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue())
                                    .setItemId("0");
                                fileItems.unshift(labelFileItem);
                                //.selectedFileItemId = "0";
                            }
                        }
                        else {
                            var noneFileItem = gobii_file_item_1.GobiiFileItem
                                .build(gobiiExtractFilterType, type_process_1.ProcessType.DUMMY)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                .setItemId("0")
                                .setItemName("<none>")
                                .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());
                            fileItems.push(noneFileItem);
                        } // if/else any nameids were retrieved
                        var loadAction = new fileItemActions.LoadFileItemListAction({
                            gobiiFileItems: fileItems,
                            filterId: nameIdRequestParamsToLoad.getQueryName(),
                            filterValue: nameIdRequestParamsToLoad.getFkEntityFilterValue()
                        });
                        _this.store.dispatch(loadAction);
                        // if there are children, we will load their data as well
                        if (nameIdRequestParamsToLoad
                            .getChildNameIdRequestParams()
                            .filter(function (rqp) { return rqp.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPEID; })
                            .length > 0) {
                            var parentId_1 = nameIdRequestParamsToLoad.getSelectedItemId();
                            if (!parentId_1) {
                                parentId_1 = fileItems[0].getItemId();
                            }
                            nameIdRequestParamsToLoad
                                .getChildNameIdRequestParams()
                                .forEach(function (rqp) {
                                if (rqp.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPEID) {
                                    rqp.setFkEntityFilterValue(parentId_1);
                                    _this.loadNameIdsToFileItems(gobiiExtractFilterType, rqp, parentId_1);
                                }
                            });
                        }
                    }, // Observer=>next
                    function (// Observer=>next
                        responseHeader) {
                        _this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                    }); // subscribe
                };
                // these next two functions are redundant with respect to the other ones that load nameids
                // the refactoring path is for the loadWithParams() methods to be deprecated and moved into
                // effects  so that all fo these things will use effects.
                FileItemService.prototype.makeFileLoadActions = function (gobiiExtractFilterType, nameIdFilterParamTypes, filterValue) {
                    var nameIdRequestParamsFromType = this.nameIdRequestParams.get(nameIdFilterParamTypes);
                    return this.recurseFileItems(gobiiExtractFilterType, nameIdRequestParamsFromType, filterValue);
                };
                FileItemService.prototype.recurseFileItems = function (gobiiExtractFilterType, nameIdRequestParamsToLoad, filterValue) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        if (nameIdRequestParamsToLoad.getDynamicFilterValue()) {
                            nameIdRequestParamsToLoad.setFkEntityFilterValue(filterValue);
                        }
                        nameIdRequestParamsToLoad.setFkEntityFilterValue(filterValue);
                        _this.nameIdService.get(nameIdRequestParamsToLoad)
                            .subscribe(function (nameIds) {
                            var fileItems = [];
                            if (nameIds && (nameIds.length > 0)) {
                                nameIds.forEach(function (n) {
                                    var currentFileItem = gobii_file_item_1.GobiiFileItem.build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                        .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                        .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                        .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                        .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                        .setItemId(n.id)
                                        .setItemName(n.name)
                                        .setSelected(false)
                                        .setRequired(false)
                                        .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());
                                    fileItems.push(currentFileItem);
                                });
                                var temp = "foo";
                                temp = "bar";
                                if (nameIdRequestParamsToLoad.getMameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                                    var entityName = "";
                                    if (nameIdRequestParamsToLoad.getCvFilterType() !== cv_filter_type_1.CvFilterType.UNKNOWN) {
                                        entityName += entity_labels_1.Labels.instance().cvFilterNodeLabels[nameIdRequestParamsToLoad.getCvFilterType()];
                                    }
                                    else if (nameIdRequestParamsToLoad.getEntitySubType() !== type_entity_1.EntitySubType.UNKNOWN) {
                                        entityName += entity_labels_1.Labels.instance().entitySubtypeNodeLabels[nameIdRequestParamsToLoad.getEntitySubType()];
                                    }
                                    else {
                                        entityName += entity_labels_1.Labels.instance().entityNodeLabels[nameIdRequestParamsToLoad.getEntityType()];
                                    }
                                    var label = "";
                                    switch (nameIdRequestParamsToLoad.getMameIdLabelType()) {
                                        case name_id_label_type_1.NameIdLabelType.SELECT_A:
                                            label = "Select a " + entityName;
                                            break;
                                        // we require that these entity labels all be in the singular
                                        case name_id_label_type_1.NameIdLabelType.ALL:
                                            label = "All " + entityName + "s";
                                            break;
                                        case name_id_label_type_1.NameIdLabelType.NO:
                                            label = "No " + entityName;
                                            break;
                                        default:
                                            _this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unknown label type "
                                                + name_id_label_type_1.NameIdLabelType[nameIdRequestParamsToLoad.getMameIdLabelType()], null, null)));
                                    }
                                    var labelFileItem = gobii_file_item_1.GobiiFileItem
                                        .build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                        .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                        .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                        .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                        .setExtractorItemType(file_model_node_1.ExtractorItemType.LABEL)
                                        .setItemName(label)
                                        .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue())
                                        .setItemId("0");
                                    fileItems.unshift(labelFileItem);
                                    //.selectedFileItemId = "0";
                                }
                            }
                            else {
                                var noneFileItem = gobii_file_item_1.GobiiFileItem
                                    .build(gobiiExtractFilterType, type_process_1.ProcessType.DUMMY)
                                    .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                    .setItemId("0")
                                    .setItemName("<none>")
                                    .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());
                                fileItems.push(noneFileItem);
                            } // if/else any nameids were retrieved
                            var loadAction = new fileItemActions.LoadFileItemListAction({
                                gobiiFileItems: fileItems,
                                filterId: nameIdRequestParamsToLoad.getQueryName(),
                                filterValue: nameIdRequestParamsToLoad.getFkEntityFilterValue()
                            });
                            observer.next(loadAction);
                            // if there are children, we will load their data as well
                            if (nameIdRequestParamsToLoad
                                .getChildNameIdRequestParams()
                                .filter(function (rqp) { return rqp.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPEID; })
                                .length > 0) {
                                var parentId_2 = nameIdRequestParamsToLoad.getSelectedItemId();
                                if (!parentId_2) {
                                    parentId_2 = fileItems[0].getItemId();
                                }
                                nameIdRequestParamsToLoad
                                    .getChildNameIdRequestParams()
                                    .forEach(function (rqp) {
                                    if (rqp.getEntityFilter() === type_entity_filter_1.EntityFilter.BYTYPEID) {
                                        rqp.setFkEntityFilterValue(parentId_2);
                                        _this.recurseFileItems(gobiiExtractFilterType, rqp, parentId_2)
                                            .subscribe(function (fileItems) { return observer.next(fileItems); });
                                    }
                                });
                            }
                            var bar = "bar";
                        }, // Observer=>next
                        function (// Observer=>next
                            responseHeader) {
                            _this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                        }); // subscribe
                    }); //return Observer.create
                }; // make file items from query
                FileItemService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                        store_1.Store])
                ], FileItemService);
                return FileItemService;
            }());
            exports_1("FileItemService", FileItemService);
        }
    };
});
//# sourceMappingURL=file-item-service.js.map