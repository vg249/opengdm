System.register(["@angular/core", "../../model/type-entity", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/type-extract-format", "../../store/reducers", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id", "rxjs/Observable", "../../model/type-extractor-sample-list", "../../model/extractor-instructions/data-set-extract", "../../model/extractor-instructions/gobii-extractor-instruction", "../../model/extractor-instructions/dto-extractor-instruction-files", "../app/dto-request-item-extractor-submission", "../../model/type-gobii-file", "./dto-request.service"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, file_model_node_1, type_extractor_filter_1, cv_filter_type_1, type_extract_format_1, fromRoot, historyAction, store_1, name_id_1, Observable_1, type_extractor_sample_list_1, data_set_extract_1, gobii_extractor_instruction_1, dto_extractor_instruction_files_1, dto_request_item_extractor_submission_1, type_gobii_file_1, dto_request_service_1, InstructionSubmissionService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
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
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            },
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            },
            function (dto_extractor_instruction_files_1_1) {
                dto_extractor_instruction_files_1 = dto_extractor_instruction_files_1_1;
            },
            function (dto_request_item_extractor_submission_1_1) {
                dto_request_item_extractor_submission_1 = dto_request_item_extractor_submission_1_1;
            },
            function (type_gobii_file_1_1) {
                type_gobii_file_1 = type_gobii_file_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            }
        ],
        execute: function () {
            InstructionSubmissionService = (function () {
                function InstructionSubmissionService(store, dtoRequestServiceExtractorFile) {
                    this.store = store;
                    this.dtoRequestServiceExtractorFile = dtoRequestServiceExtractorFile;
                }
                InstructionSubmissionService.prototype.submitReady = function (scope$) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        _this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (all) {
                            var submistReady = false;
                            if (scope$.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                submistReady =
                                    all
                                        .filter(function (fi) {
                                        return fi.getGobiiExtractFilterType() === scope$.gobiiExtractFilterType
                                            && fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                                            && fi.getEntityType() === type_entity_1.EntityType.DataSets;
                                    })
                                        .length > 0;
                            } // if-else on extract type
                            var temp = "foo";
                            observer.next(submistReady);
                        }); // inner subscribe
                    } //observer lambda
                    ); // Observable.crate
                }; // function()
                // In theory this method should be unnecessary because there should not be any duplicates;
                // however, in testing, it was discovered that there can be duplicate datasets and
                // duplicate platforms. I suspect that the root cause of this issue is the checkbox component:
                // because it keeps a history of selected items, it may be reposting existing items in a way that
                // is not detected by the file item service. In particular, it strikes me that if an item is added
                // in one extract type (e.g., by data set), and then selected again in another (by samples), there
                // could be duplicate items in the tree service, because it is specific to extract filter type.
                // TreeService::getFileItems() should be filtering correctly, but perhaps it's not. In any case,
                // at this point in the release cycle, it is too late to to do the trouble shooting to figure this out,
                // because I am unable to reproduce the issue in my local testing. This method at leaset reports
                // warnings to the effect that the problem exists, but results in an extract that is free of duplicates.
                // Technically, sample and marker list item duplicates should be eliminated in the list item control,
                // but it is also too late for that.
                InstructionSubmissionService.prototype.eliminateDuplicateEntities = function (extractorItemType, entityType, fileItems) {
                    return fileItems;
                    // let returnVal: GobiiFileItem[] = [];
                    //
                    // if (fileItems
                    //         .filter(fi => {
                    //             return fi.getExtractorItemType() === extractorItemType
                    //                 && fi.getEntityType() === entityType
                    //         })
                    //         .length == fileItems.length) {
                    //
                    //     fileItems.forEach(ifi => {
                    //
                    //         if (returnVal.filter(rfi => {
                    //                 return rfi.getItemId() === ifi.getItemId()
                    //             }).length === 0) {
                    //
                    //             returnVal.push(ifi);
                    //         } else {
                    //             let message: string = "A duplicate ";
                    //             message += ExtractorItemType[extractorItemType];
                    //             message += " (" + EntityType[entityType] + ") ";
                    //             message += "item was found; ";
                    //             if (ifi.getItemName()) {
                    //                 message += "name: " + ifi.getItemName() + "; "
                    //             }
                    //
                    //             if (ifi.getItemId()) {
                    //                 message += "id: " + ifi.getItemId();
                    //             }
                    //
                    //             // this.handleHeaderStatusMessage(new HeaderStatusMessage(message,
                    //             //     StatusLevel.WARNING,
                    //             //     null));
                    //         }
                    //     });
                    //
                    // } else {
                    //
                    //     // this.handleHeaderStatusMessage(new HeaderStatusMessage(
                    //     //     "The elimination array contains mixed entities",
                    //     //     StatusLevel.WARNING,
                    //     //     null));
                    //
                    // }
                    //
                    //
                    // return returnVal;
                };
                InstructionSubmissionService.prototype.submit = function (scope$) {
                    var _this = this;
                    var gobiiExtractorInstructions = [];
                    var gobiiDataSetExtracts = [];
                    var mapsetIds = [];
                    var submitterContactid = null;
                    var jobId = null;
                    var markerFileName = null;
                    var sampleFileName = null;
                    var sampleListType;
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe(function (fileItems) {
                        // ******** JOB ID
                        var fileItemJobId = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.JOB_ID;
                        });
                        if (fileItemJobId != null) {
                            jobId = fileItemJobId.getItemId();
                        }
                        // ******** MARKER FILE
                        var fileItemMarkerFile = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE;
                        });
                        if (fileItemMarkerFile != null) {
                            markerFileName = fileItemMarkerFile.getItemId();
                        }
                        // ******** SAMPLE FILE
                        var fileItemSampleFile = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE;
                        });
                        if (fileItemSampleFile != null) {
                            sampleFileName = fileItemSampleFile.getItemId();
                        }
                        // ******** SUBMITTER CONTACT
                        var submitterFileItem = fileItems.find(function (item) {
                            return (item.getEntityType() === type_entity_1.EntityType.Contacts)
                                && (item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_SUBMITED_BY);
                        });
                        submitterContactid = Number(submitterFileItem.getItemId());
                        // ******** MAPSET IDs
                        var mapsetFileItems = fileItems
                            .filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Mapsets;
                        });
                        mapsetFileItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.Mapsets, mapsetFileItems);
                        mapsetIds = mapsetFileItems
                            .map(function (item) {
                            return Number(item.getItemId());
                        });
                        // ******** EXPORT FORMAT
                        var exportFileItem = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT;
                        });
                        // these probably should be just one enum
                        var gobiiFileType = null;
                        var extractFormat = type_extract_format_1.GobiiExtractFormat[exportFileItem.getItemId()];
                        if (extractFormat === type_extract_format_1.GobiiExtractFormat.FLAPJACK) {
                            gobiiFileType = type_gobii_file_1.GobiiFileType.FLAPJACK;
                        }
                        else if (extractFormat === type_extract_format_1.GobiiExtractFormat.HAPMAP) {
                            gobiiFileType = type_gobii_file_1.GobiiFileType.HAPMAP;
                        }
                        else if (extractFormat === type_extract_format_1.GobiiExtractFormat.META_DATA_ONLY) {
                            gobiiFileType = type_gobii_file_1.GobiiFileType.META_DATA;
                        }
                        // ******** DATA SET TYPE
                        var dataTypeFileItem = fileItems.find(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.CvTerms
                                && item.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE;
                        });
                        var datasetType = dataTypeFileItem != null ? new name_id_1.NameId(dataTypeFileItem.getItemId(), dataTypeFileItem.getItemName(), type_entity_1.EntityType.CvTerms) : null;
                        // ******** PRINCIPLE INVESTIGATOR CONCEPT
                        var principleInvestigatorFileItem = fileItems.find(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Contacts
                                && item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                        });
                        var principleInvestigator = principleInvestigatorFileItem != null ? new name_id_1.NameId(principleInvestigatorFileItem.getItemId(), principleInvestigatorFileItem.getItemName(), type_entity_1.EntityType.Contacts) : null;
                        // ******** PROJECT
                        var projectFileItem = fileItems.find(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Projects;
                        });
                        var project = projectFileItem != null ? new name_id_1.NameId(projectFileItem.getItemId(), projectFileItem.getItemName(), type_entity_1.EntityType.Projects) : null;
                        // ******** PLATFORMS
                        var platformFileItems = fileItems.filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.Platforms;
                        });
                        platformFileItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.Platforms, platformFileItems);
                        var platforms = platformFileItems.map(function (item) {
                            return new name_id_1.NameId(item.getItemId(), item.getItemName(), type_entity_1.EntityType.Platforms);
                        });
                        var markerGroupItems = fileItems.filter(function (item) {
                            return item.getEntityType() === type_entity_1.EntityType.MarkerGroups;
                        });
                        markerGroupItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.MarkerGroups, markerGroupItems);
                        var markerGroups = markerGroupItems.map(function (item) {
                            return new name_id_1.NameId(item.getItemId(), item.getItemName(), type_entity_1.EntityType.MarkerGroups);
                        });
                        // ******** MARKERS
                        var markerListItems = fileItems
                            .filter(function (fi) {
                            return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM;
                        });
                        markerListItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, markerListItems);
                        var markerList = markerListItems
                            .map(function (mi) {
                            return mi.getItemId();
                        });
                        // ******** SAMPLES
                        var sampleListItems = fileItems
                            .filter(function (fi) {
                            return fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                        });
                        sampleListItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, sampleListItems);
                        var sampleList = sampleListItems
                            .map(function (mi) {
                            return mi.getItemId();
                        });
                        var sampleListTypeFileItem = fileItems.find(function (item) {
                            return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE;
                        });
                        if (sampleListTypeFileItem != null) {
                            sampleListType = type_extractor_sample_list_1.GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                        }
                        if (scope$.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                            var dataSetItems = fileItems
                                .filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.DataSets;
                            });
                            dataSetItems = _this.eliminateDuplicateEntities(file_model_node_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DataSets, dataSetItems);
                            dataSetItems.forEach(function (datsetFileItem) {
                                var dataSet = new name_id_1.NameId(datsetFileItem.getItemId(), datsetFileItem.getItemName(), type_entity_1.EntityType.CvTerms);
                                gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, scope$.gobiiExtractFilterType, null, null, markerFileName, null, datasetType, platforms, null, null, dataSet, null));
                            });
                        }
                        else if (scope$.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, scope$.gobiiExtractFilterType, markerList, null, markerFileName, null, datasetType, platforms, null, null, null, markerGroups));
                        }
                        else if (scope$.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, scope$.gobiiExtractFilterType, null, sampleList, sampleFileName, sampleListType, datasetType, platforms, principleInvestigator, project, null, null));
                        }
                        else {
                            _this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[scope$.gobiiExtractFilterType]));
                        }
                    });
                    gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(gobiiDataSetExtracts, submitterContactid, null, mapsetIds));
                    var fileName = jobId;
                    var extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName);
                    var extractorInstructionFilesDTOResponse = null;
                    this.dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                        .subscribe(function (extractorInstructionFilesDTO) {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        _this.store.dispatch(new historyAction
                            .AddStatusMessageAction("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName()));
                    }, function (headerResponse) {
                        headerResponse.status.statusMessages.forEach(function (statusMessage) {
                            _this.store.dispatch(new historyAction.AddStatusAction(statusMessage));
                        });
                    });
                };
                InstructionSubmissionService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store,
                        dto_request_service_1.DtoRequestService])
                ], InstructionSubmissionService);
                return InstructionSubmissionService;
            }());
            exports_1("InstructionSubmissionService", InstructionSubmissionService);
        }
    };
});
//# sourceMappingURL=instruction-submission-service.js.map