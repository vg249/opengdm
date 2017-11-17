System.register(["@angular/core", "../../model/type-entity", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/file-item-params", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id-label-type", "../../model/filter-type", "../../model/file-item-param-names", "rxjs/add/operator/expand"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_filter_1, cv_filter_type_1, file_item_params_1, historyAction, store_1, name_id_label_type_1, filter_type_1, file_item_param_names_1, FilterParamsColl;
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
                    this.addFilter(file_item_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CV)
                        .setIsDynamicFilterValue(true)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.JOB_STATUS)
                        .setCvFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.JOB_STATUS))
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL));
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