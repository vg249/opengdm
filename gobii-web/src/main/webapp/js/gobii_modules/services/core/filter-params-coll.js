System.register(["@angular/core", "../../model/type-entity", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/file-item-params", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id-label-type", "../../model/filter-type", "../../model/file-item-param-names", "rxjs/add/operator/expand", "../../model/type-extractor-item", "../../store/actions/fileitem-action", "../../model/gobii-file-item-compound-id"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_filter_1, cv_filter_type_1, file_item_params_1, historyAction, store_1, name_id_label_type_1, filter_type_1, file_item_param_names_1, type_extractor_item_1, fileAction, gobii_file_item_compound_id_1, FilterParamsColl;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_item_params_1_1) {
                file_item_params_1 = file_item_params_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (_1) {
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            FilterParamsColl = (function () {
                function FilterParamsColl(store) {
                    this.store = store;
                    this.filterParams = [];
                    // For non-hierarchically filtered request params, we just create them simply
                    // as we add them to the flat map
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.CV)
                        .setIsDynamicFilterValue(false)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setCvFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.CV)
                        .setIsDynamicFilterValue(false)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setCvFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.PLATFORM)
                        .setIsDynamicFilterValue(false));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.PLATFORM)
                        .setIsDynamicFilterValue(false));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MARKER_GROUPS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.MARKER_GROUP)
                        .setIsDynamicFilterValue(false));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PROJECTS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.PROJECT)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CONTACT_PI, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.CONTACT)
                        .setIsDynamicFilterValue(false)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR));
                    var cvJobStatusCompoundUniqueId = new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CV, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.JOB_STATUS, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.JOB_STATUS));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, cvJobStatusCompoundUniqueId.getEntityType())
                        .setIsDynamicFilterValue(true)
                        .setCvFilterType(cvJobStatusCompoundUniqueId.getCvFilterType())
                        .setCvFilterValue(cvJobStatusCompoundUniqueId.getCvFilterValue())
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setOnLoadFilteredItemsAction(function (fileItems, filterValue) {
                        /***
                         * This event will cause the initially selected job status to be completed and the
                         *  dataset grid items to be filtered accordingly.
                         *
                         * I am a little uneasy with the implementation. For one thing, it sets the
                         * completedItem's selected property. Ideally, the semantics of the action
                         * should be such that the reducer knows to set the selected property. We're sort
                         * of monkeying with state here. In essence,
                         * we really need a new action and corresponding reducer code to handle this;
                         * I'm also not in comfortable with the fact that we are testing for a filter value
                         * to determine whether or not to apply the initial select state and filter value. Here again,
                         * there should be semantics in the load filtered items action or something that would indicate
                         * that it's an initial load. But that will make things more complicated.
                         *
                         * For now this is fine. If we expand this type of thing to include other types of initial select
                         * actions, we will probably want to revisit the design.
                         *
                         */
                        var returnVal = null;
                        if (!filterValue) {
                            var completedItem = fileItems.find(function (fi) { return fi.getItemName() === "completed"; });
                            var labelItem = fileItems.find(function (fi) { return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL; });
                            if (completedItem && labelItem) {
                                completedItem.setSelected(true);
                                returnVal = new fileAction.LoadFilterAction({
                                    filterId: file_item_param_names_1.FilterParamNames.CV_JOB_STATUS,
                                    filter: {
                                        gobiiExtractFilterType: type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET,
                                        gobiiCompoundUniqueId: cvJobStatusCompoundUniqueId,
                                        filterValue: completedItem.getItemId(),
                                        entityLasteUpdated: null
                                    }
                                });
                            }
                        }
                        return returnVal;
                    }));
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASETS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setFilterType(filter_type_1.FilterType.ENTITY_LIST));
                    //for hierarchical items, we need to crate the nameid requests separately from the
                    //flat map: they _will_ need to be in the flat map; however, they all need to be
                    //useed to set up the filtering hierarchy
                    var nameIdRequestParamsContactsPi = file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CONTACT_PI, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CONTACT)
                        .setIsDynamicFilterValue(true)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);
                    var nameIdRequestParamsProjectByPiContact = file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.PROJECT)
                        .setIsDynamicFilterValue(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPEID);
                    var nameIdRequestParamsExperiments = file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.EXPERIMENT)
                        .setIsDynamicFilterValue(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPEID);
                    var nameIdRequestParamsDatasets = file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setIsDynamicFilterValue(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPEID);
                    //add the individual requests to the map
                    this.addFilter(nameIdRequestParamsContactsPi);
                    this.addFilter(nameIdRequestParamsProjectByPiContact);
                    this.addFilter(nameIdRequestParamsExperiments);
                    this.addFilter(nameIdRequestParamsDatasets);
                    //build the parent-child request params graph
                    nameIdRequestParamsContactsPi
                        .setChildNameIdRequestParams([nameIdRequestParamsProjectByPiContact
                            .setParentFileItemParams(nameIdRequestParamsContactsPi)
                            .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                                .setParentFileItemParams(nameIdRequestParamsProjectByPiContact)
                                .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                                    .setParentFileItemParams(nameIdRequestParamsExperiments)
                            ])
                        ])
                    ]);
                } // constructor
                FilterParamsColl.prototype.addFilter = function (filterParamsToAdd) {
                    var existingFilterParams = this.filterParams
                        .find(function (ffp) {
                        return ffp.getQueryName() === filterParamsToAdd.getQueryName()
                            && ffp.getGobiiExtractFilterType() === filterParamsToAdd.getGobiiExtractFilterType();
                    });
                    if (!existingFilterParams) {
                        this.filterParams.push(filterParamsToAdd);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The query "
                            + filterParamsToAdd.getQueryName()
                            + " because there is already a filter by that name for this extract type "
                            + type_extractor_filter_1.GobiiExtractFilterType[filterParamsToAdd.getQueryName()]));
                    }
                };
                FilterParamsColl.prototype.getFilter = function (nameIdFilterParamTypes, gobiiExtractFilterType) {
                    return this.filterParams.find(function (ffp) {
                        return ffp.getQueryName() === nameIdFilterParamTypes &&
                            ffp.getGobiiExtractFilterType() === gobiiExtractFilterType;
                    });
                };
                FilterParamsColl = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store])
                ], FilterParamsColl);
                return FilterParamsColl;
            }());
            exports_1("FilterParamsColl", FilterParamsColl);
        }
    };
});
//# sourceMappingURL=filter-params-coll.js.map