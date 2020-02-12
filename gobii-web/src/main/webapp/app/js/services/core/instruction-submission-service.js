System.register(["@angular/core", "../../model/type-entity", "../../model/type-extractor-item", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/type-extract-format", "../../store/reducers", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id", "rxjs", "../../model/type-extractor-sample-list", "../../model/extractor-instructions/data-set-extract", "../../model/extractor-instructions/gobii-extractor-instruction", "../../model/extractor-instructions/dto-extractor-instruction-files", "../app/dto-request-item-extractor-submission", "../../model/type-gobii-file", "./dto-request.service", "../../model/gobii-file-item-compound-id", "../../model/gobii-file-item-criterion", "./tree-structure-service"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_item_1, type_extractor_filter_1, cv_filter_type_1, type_extract_format_1, fromRoot, historyAction, store_1, name_id_1, rxjs_1, type_extractor_sample_list_1, data_set_extract_1, gobii_extractor_instruction_1, dto_extractor_instruction_files_1, dto_request_item_extractor_submission_1, type_gobii_file_1, dto_request_service_1, gobii_file_item_compound_id_1, gobii_file_item_criterion_1, tree_structure_service_1, InstructionSubmissionService;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
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
            function (rxjs_1_1) {
                rxjs_1 = rxjs_1_1;
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
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (gobii_file_item_criterion_1_1) {
                gobii_file_item_criterion_1 = gobii_file_item_criterion_1_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            }
        ],
        execute: function () {
            InstructionSubmissionService = class InstructionSubmissionService {
                constructor(store, dtoRequestServiceExtractorFile, treeStructureService) {
                    this.store = store;
                    this.dtoRequestServiceExtractorFile = dtoRequestServiceExtractorFile;
                    this.treeStructureService = treeStructureService;
                    this.datasetCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DATASET, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.sampleItemCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.samplefileCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.SAMPLE_FILE, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.piContactCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CONTACT, type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.projectsCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.PROJECT, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.datasetTypesCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CV, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.DATASET_TYPE, null), false);
                    this.markerListItemCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.markerListFileCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.MARKER_FILE, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.markergGroupCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.MARKER_GROUP, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                    this.platformCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.PLATFORM, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null), false);
                }
                isItemPresent(gobiiFileItems, gobiiFileItemCriterion) {
                    return gobiiFileItems.filter(fi => gobiiFileItemCriterion.compoundIdeEquals(fi)).length > 0;
                }
                unmarkMissingItems(gobiiExtractFilterType) {
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe(all => {
                        if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                            if (!this.isItemPresent(all, this.datasetCriterion)) {
                                this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetCriterion);
                            }
                        }
                        else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.samplefileCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.sampleItemCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.projectsCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.piContactCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                        }
                        else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markerListItemCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markerListFileCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markergGroupCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.platformCriterion);
                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                        }
                    });
                }
                markMissingItems(gobiiExtractFilterType) {
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe((all) => {
                        if (!this.areCriteriaMet(all, gobiiExtractFilterType)) {
                            if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                if (!this.isItemPresent(all, this.datasetCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetCriterion);
                                }
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                if (!this.isItemPresent(all, this.samplefileCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.samplefileCriterion);
                                }
                                if (!this.isItemPresent(all, this.sampleItemCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.sampleItemCriterion);
                                }
                                if (!this.isItemPresent(all, this.projectsCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.projectsCriterion);
                                }
                                if (!this.isItemPresent(all, this.piContactCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.piContactCriterion);
                                }
                                if (!this.isItemPresent(all, this.datasetTypesCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                                }
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                if (!this.isItemPresent(all, this.markerListItemCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListItemCriterion);
                                }
                                if (!this.isItemPresent(all, this.markerListFileCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListFileCriterion);
                                }
                                if (!this.isItemPresent(all, this.markergGroupCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markergGroupCriterion);
                                }
                                if (!this.isItemPresent(all, this.platformCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.platformCriterion);
                                }
                                if (!this.isItemPresent(all, this.datasetTypesCriterion)) {
                                    this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                                }
                            }
                            else {
                                this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                            }
                        }
                    });
                } // markMissingItems()
                areCriteriaMet(all, gobiiExtractFilterType) {
                    let returnVal;
                    if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        returnVal = this.isItemPresent(all, this.datasetCriterion);
                    }
                    else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        let samplesArePresent = this.isItemPresent(all, this.samplefileCriterion)
                            || this.isItemPresent(all, this.sampleItemCriterion);
                        let projectIsPresent = this.isItemPresent(all, this.projectsCriterion);
                        let pIIsPresent = this.isItemPresent(all, this.piContactCriterion);
                        let datasetTypeIsPresent = this.isItemPresent(all, this.datasetTypesCriterion);
                        returnVal =
                            datasetTypeIsPresent &&
                                (samplesArePresent
                                    || projectIsPresent
                                    || pIIsPresent);
                    }
                    else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        let markersArePresent = this.isItemPresent(all, this.markerListItemCriterion)
                            || this.isItemPresent(all, this.markerListFileCriterion);
                        let markerGroupIsPresent = this.isItemPresent(all, this.markergGroupCriterion);
                        let platformIsPresent = this.isItemPresent(all, this.platformCriterion);
                        let datasetTypeIsPresent = this.isItemPresent(all, this.datasetTypesCriterion);
                        returnVal =
                            datasetTypeIsPresent
                                && (markersArePresent
                                    || markerGroupIsPresent
                                    || platformIsPresent);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                    return returnVal;
                }
                submitReady(gobiiExtractFilterType) {
                    return rxjs_1.Observable.create(observer => {
                        this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(all => {
                            let submistReady = this.areCriteriaMet(all, gobiiExtractFilterType);
                            observer.next(submistReady);
                        }); // inner subscribe
                    } //observer lambda
                    ); // Observable.crate
                } // function()
                expurgateZero(nameId) {
                    if (nameId && nameId.id === "0") {
                        nameId.id = undefined;
                    }
                }
                submit(gobiiExtractFilterType) {
                    return rxjs_1.Observable.create(observer => {
                        let gobiiExtractorInstructions = [];
                        let gobiiDataSetExtracts = [];
                        let mapsetIds = [];
                        let submitterContactid = null;
                        let jobId = null;
                        let markerFileName = null;
                        let sampleFileName = null;
                        let sampleListType;
                        this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe((fileItems) => {
                            // ******** JOB ID
                            let fileItemJobId = fileItems.find(item => {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID;
                            });
                            if (fileItemJobId != null) {
                                jobId = fileItemJobId.getItemId();
                            }
                            // ******** MARKER FILE
                            let fileItemMarkerFile = fileItems.find(item => {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE;
                            });
                            if (fileItemMarkerFile != null) {
                                markerFileName = fileItemMarkerFile.getItemId();
                            }
                            // ******** SAMPLE FILE
                            let fileItemSampleFile = fileItems.find(item => {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                            });
                            if (fileItemSampleFile != null) {
                                sampleFileName = fileItemSampleFile.getItemId();
                            }
                            // ******** SUBMITTER CONTACT
                            let submitterFileItem = fileItems.find(item => {
                                return (item.getEntityType() === type_entity_1.EntityType.CONTACT)
                                    && (item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_SUBMITED_BY);
                            });
                            submitterContactid = Number(submitterFileItem.getItemId());
                            // ******** MAPSET IDs
                            let mapsetFileItems = fileItems
                                .filter(item => {
                                return item.getEntityType() === type_entity_1.EntityType.MAPSET;
                            });
                            mapsetIds = mapsetFileItems
                                .map(item => {
                                return Number(item.getItemId());
                            });
                            // ******** EXPORT FORMAT
                            let exportFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT;
                            });
                            // these probably should be just one enum
                            let gobiiFileType = null;
                            let extractFormat = type_extract_format_1.GobiiExtractFormat[exportFileItem.getItemId()];
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
                            let dataTypeFileItem = fileItems.find(item => {
                                return item.getEntityType() === type_entity_1.EntityType.CV
                                    && item.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE;
                            });
                            let datasetType = dataTypeFileItem != null ? new name_id_1.NameId(dataTypeFileItem.getItemId(), null, dataTypeFileItem.getItemName(), type_entity_1.EntityType.CV, null, null) : null;
                            // ******** PRINCIPLE INVESTIGATOR CONCEPT
                            let principleInvestigatorFileItem = fileItems.find(item => {
                                return item.getEntityType() === type_entity_1.EntityType.CONTACT
                                    && item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                            });
                            let principleInvestigator = principleInvestigatorFileItem != null ? new name_id_1.NameId(principleInvestigatorFileItem.getItemId(), null, principleInvestigatorFileItem.getItemName(), type_entity_1.EntityType.CONTACT, null, null) : null;
                            this.expurgateZero(principleInvestigator);
                            // ******** PROJECT
                            let projectFileItem = fileItems.find(item => {
                                return item.getEntityType() === type_entity_1.EntityType.PROJECT;
                            });
                            let project = projectFileItem != null ? new name_id_1.NameId(projectFileItem.getItemId(), null, projectFileItem.getItemName(), type_entity_1.EntityType.PROJECT, null, null) : null;
                            this.expurgateZero(project);
                            // ******** PLATFORM
                            let platformFileItems = fileItems.filter(item => {
                                return item.getEntityType() === type_entity_1.EntityType.PLATFORM;
                            });
                            let platforms = platformFileItems.map(item => {
                                return new name_id_1.NameId(item.getItemId(), null, item.getItemName(), type_entity_1.EntityType.PLATFORM, null, null);
                            });
                            let markerGroupItems = fileItems.filter(item => {
                                return item.getEntityType() === type_entity_1.EntityType.MARKER_GROUP;
                            });
                            let markerGroups = markerGroupItems.map(item => {
                                return new name_id_1.NameId(item.getItemId(), null, item.getItemName(), type_entity_1.EntityType.MARKER_GROUP, null, null);
                            });
                            // ******** MARKERS
                            let markerListItems = fileItems
                                .filter(fi => {
                                return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM;
                            });
                            let markerList = markerListItems
                                .map(mi => {
                                return mi.getItemId();
                            });
                            // ******** SAMPLES
                            let sampleListItems = fileItems
                                .filter(fi => {
                                return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            });
                            let sampleList = sampleListItems
                                .map(mi => {
                                return mi.getItemId();
                            });
                            let sampleListTypeFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE;
                            });
                            if (sampleListTypeFileItem != null) {
                                sampleListType = type_extractor_sample_list_1.GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                            }
                            if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                let dataSetItems = fileItems
                                    .filter(item => {
                                    return item.getEntityType() === type_entity_1.EntityType.DATASET;
                                });
                                dataSetItems.forEach(datsetFileItem => {
                                    let dataSet = new name_id_1.NameId(datsetFileItem.getItemId(), null, datsetFileItem.getItemName(), type_entity_1.EntityType.CV, null, null);
                                    gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, null, null, markerFileName, null, datasetType, platforms, null, null, dataSet, null));
                                });
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, markerList, null, markerFileName, null, datasetType, platforms, null, null, null, markerGroups));
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, null, sampleList, sampleFileName, sampleListType, datasetType, platforms, principleInvestigator, project, null, null));
                            }
                            else {
                                this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                            }
                        }).unsubscribe();
                        gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(gobiiDataSetExtracts, submitterContactid, null, mapsetIds));
                        let fileName = jobId;
                        let extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName);
                        let extractorInstructionFilesDTOResponse = null;
                        this.dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                            .subscribe(extractorInstructionFilesDTO => {
                            extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                            this.store.dispatch(new historyAction
                                .AddStatusMessageAction("Extractor instruction file created on server: "
                                + extractorInstructionFilesDTOResponse.getInstructionFileName()));
                            observer.next(extractorInstructionFilesDTORequest.getGobiiExtractorInstructions());
                            observer.complete();
                        }, headerResponse => {
                            headerResponse.status.statusMessages.forEach(statusMessage => {
                                this.store.dispatch(new historyAction.AddStatusAction(statusMessage));
                            });
                            observer.complete();
                        });
                    }); //return observer create
                }
            };
            InstructionSubmissionService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [store_1.Store,
                    dto_request_service_1.DtoRequestService,
                    tree_structure_service_1.TreeStructureService])
            ], InstructionSubmissionService);
            exports_1("InstructionSubmissionService", InstructionSubmissionService);
        }
    };
});
//# sourceMappingURL=instruction-submission-service.js.map