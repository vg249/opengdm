System.register(["@angular/core", "../services/core/dto-request.service", "../model/type-process", "../model/gobii-file-item", "../model/server-config", "../model/type-entity", "../services/app/dto-request-item-serverconfigs", "../model/type-entity-filter", "../model/type-extractor-filter", "../model/cv-filter-type", "../model/file-model-node", "../model/type-extract-format", "../model/name-id-request-params", "../model/file_name", "../services/app/dto-request-item-contact", "../services/core/authentication.service", "../model/name-id-label-type", "../model/type-status-level", "@ngrx/store", "../store/reducers", "../store/actions/fileitem-action", "../model/type-nameid-filter-params", "../services/core/file-item-service", "../services/core/instruction-submission-service"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, type_process_1, gobii_file_item_1, server_config_1, type_entity_1, dto_request_item_serverconfigs_1, type_entity_filter_1, type_extractor_filter_1, cv_filter_type_1, file_model_node_1, type_extract_format_1, name_id_request_params_1, file_name_1, dto_request_item_contact_1, authentication_service_1, name_id_label_type_1, type_status_level_1, store_1, fromRoot, fileItemAction, type_nameid_filter_params_1, file_item_service_1, instruction_submission_service_1, ExtractorRoot;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (name_id_request_params_1_1) {
                name_id_request_params_1 = name_id_request_params_1_1;
            },
            function (file_name_1_1) {
                file_name_1 = file_name_1_1;
            },
            function (dto_request_item_contact_1_1) {
                dto_request_item_contact_1 = dto_request_item_contact_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (fileItemAction_1) {
                fileItemAction = fileItemAction_1;
            },
            function (type_nameid_filter_params_1_1) {
                type_nameid_filter_params_1 = type_nameid_filter_params_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (instruction_submission_service_1_1) {
                instruction_submission_service_1 = instruction_submission_service_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceContact, _authenticationService, _dtoRequestServiceServerConfigs, store, fileItemService, instructionSubmissionService) {
                    this._dtoRequestServiceContact = _dtoRequestServiceContact;
                    this._authenticationService = _authenticationService;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.instructionSubmissionService = instructionSubmissionService;
                    this.title = 'Gobii Web';
                    // ************************************************************************
                    // unfiltered
                    this.fileItemsContactsPI$ = this.store.select(fromRoot.getContacts);
                    this.fileItemsMapsets$ = this.store.select(fromRoot.getMapsets);
                    this.fileItemsDatasetTypes$ = this.store.select(fromRoot.getCvTerms);
                    this.fileItemsPlatforms = this.store.select(fromRoot.getPlatforms);
                    // filtered
                    this.fileItemsProjects$ = this.store.select(fromRoot.getProjectsByPI);
                    this.fileItemsExperiments$ = this.store.select(fromRoot.getExperimentsByProject);
                    this.fileItemsDatasets$ = this.store.select(fromRoot.getDatasetsByExperiment);
                    this.selectedExtractFormat$ = this.store.select(fromRoot.getSelectedFileFormat);
                    //    private selectedExportTypeEvent:GobiiExtractFilterType;
                    this.datasetFileItemEvents = [];
                    this.gobiiDatasetExtracts = [];
                    this.criteriaInvalid = true;
                    this.loggedInUser = null;
                    this.messages = [];
                    // ********************************************************************
                    // ********************************************** EXPORT TYPE SELECTION AND FLAGS
                    this.displayAvailableDatasets = true;
                    this.displaySelectorPi = true;
                    this.doPrincipleInvestigatorTreeNotifications = false;
                    this.displaySelectorProject = true;
                    this.displaySelectorExperiment = true;
                    this.displaySelectorDataType = false;
                    this.displaySelectorPlatform = false;
                    this.displayIncludedDatasetsGrid = true;
                    this.displaySampleListTypeSelector = false;
                    this.displaySampleMarkerBox = false;
                    this.reinitProjectList = false;
                    // ********************************************************************
                    // ********************************************** HAPMAP SELECTION
                    this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP];
                    // ********************************************************************
                    // ********************************************** EXPERIMENT ID
                    this.displayExperimentDetail = false;
                    // ********************************************************************
                    // ********************************************** PLATFORM SELECTION
                    //     private platformsNameIdList: NameId[];
                    //     private selectedPlatformId: string;
                    //
                    //     private handlePlatformSelected(arg) {
                    //         this.selectedPlatformId = arg.id;
                    //     }
                    //
                    //     private handlePlatformChecked(fileItemEvent: GobiiFileItem) {
                    //
                    //
                    //         this._fileModelTreeService.put(fileItemEvent).subscribe(
                    //             null,
                    //             headerResponse => {
                    //                 this.handleHeaderStatusMessage(headerResponse)
                    //             });
                    //
                    //     }
                    //
                    //     private initializePlatforms() {
                    //         let scope$ = this;
                    //         scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
                    //             EntityType.Platforms,
                    //             EntityFilter.NONE)).subscribe(nameIds => {
                    //
                    //                 if (nameIds && ( nameIds.length > 0 )) {
                    //                     scope$.platformsNameIdList = nameIds;
                    //                     scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                    //                 } else {
                    //                     scope$.platformsNameIdList = [new NameId("0", "ERROR NO PLATFORMS", EntityType.Platforms)];
                    //                 }
                    //             },
                    //             dtoHeaderResponse => {
                    //                 dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving PlatformTypes: "
                    //                     + m.message))
                    //             });
                    //     }
                    // ********************************************************************
                    // ********************************************** DATASET ID
                    this.displayDataSetDetail = false;
                    // ********************************************************************
                    // ********************************************** MARKER/SAMPLE selection
                    // ********************************************************************
                    // ********************************************** Extract file submission
                    this.treeStatusNotification = null;
                    this.submitButtonStyleDefault = "btn btn-primary";
                    this.buttonStyleSubmitReady = "btn btn-success";
                    this.buttonStyleSubmitNotReady = "btn btn-warning";
                    this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    this.clearButtonStyle = this.submitButtonStyleDefault;
                    this.selectedExtractFormat$.subscribe(function (format) { return console.log("new extract format @ root: " + format.getItemId()); });
                    this.fileItemsProjects$.subscribe(function (items) {
                        console.log("Project item count: " + items.length);
                    });
                    // this.store
                    //     .select(fromRoot.getSelectedFileItems)
                    //     .subscribe(all => {
                    //
                    //         let extractFormatItem: GobiiFileItem = all
                    //             .find(fi => fi.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT);
                    //
                    //         if (extractFormatItem) {
                    //             this.selectedExtractFormat = extractFormatItem.getItemId();
                    //         }
                    //         // else {
                    //         //     this.fileFormat = GobiiExtractFormat[GobiiExtractFormat.META_DATA_ONLY];
                    //         // }
                    //
                    //         console.log(this.selectedExtractFormat);
                    //     });
                    //
                    //unfiltered requests
                    this.nameIdRequestParamsDatasetType = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CvTerms)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setEntityFilter(type_entity_filter_1.EntityFilter.BYTYPENAME)
                        .setFkEntityFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A);
                    this.nameIdRequestParamsPlatforms = name_id_request_params_1.FileItemParams
                        .build(type_nameid_filter_params_1.NameIdFilterParamTypes.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.Platforms);
                    //filtered requests
                }
                ExtractorRoot.prototype.initializeServerConfigs = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceServerConfigs.get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            var serverCrop_1 = _this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            var gobiiVersion = _this._dtoRequestServiceServerConfigs.getGobbiiVersion();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(function (c) {
                                    return c.crop === serverCrop_1;
                                })[0];
                            _this.handleExportTypeSelected(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                            //                    scope$.initializeSubmissionContact();
                            scope$.currentStatus = "GOBII Server " + gobiiVersion;
                            scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving server configs: "
                            + m.message); });
                    });
                }; // initializeServerConfigs()
                ExtractorRoot.prototype.initializeSubmissionContact = function () {
                    var _this = this;
                    this.loggedInUser = this._authenticationService.getUserName();
                    var scope$ = this;
                    scope$._dtoRequestServiceContact.get(new dto_request_item_contact_1.DtoRequestItemContact(dto_request_item_contact_1.ContactSearchType.BY_USERNAME, this.loggedInUser)).subscribe(function (contact) {
                        if (contact && contact.contactId && contact.contactId > 0) {
                            //loggedInUser
                            _this.fileItemService.locadFileItem(gobii_file_item_1.GobiiFileItem.build(scope$.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setEntityType(type_entity_1.EntityType.Contacts)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                .setItemName(contact.email)
                                .setItemId(contact.contactId.toLocaleString()), true);
                            // scope$._fileModelTreeService.put(
                            //     GobiiFileItem.build(scope$.gobiiExtractFilterType, ProcessType.CREATE)
                            //         .setEntityType(EntityType.Contacts)
                            //         .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                            //         .setCvFilterType(CvFilterType.UNKNOWN)
                            //         .setExtractorItemType(ExtractorItemType.ENTITY)
                            //         .setItemName(contact.email)
                            //         .setItemId(contact.contactId.toLocaleString())).subscribe(
                            //     null,
                            //     headerStatusMessage => {
                            //         this.handleHeaderStatusMessage(headerStatusMessage)
                            //     }
                            // );
                        }
                        else {
                            scope$.handleAddMessage("There is no contact associated with user " + _this.loggedInUser);
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving contacts for submission: "
                            + m.message); });
                    });
                    //   _dtoRequestServiceContact
                };
                ExtractorRoot.prototype.handleServerSelected = function (arg) {
                    this.selectedServerConfig = arg;
                    // this._dtoRequestServiceNameIds
                    //     .setCropType(GobiiCropType[this.selectedServerConfig.crop]);
                    var currentPath = window.location.pathname;
                    var currentPage = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
                    var newDestination = "http://"
                        + this.selectedServerConfig.domain
                        + ":"
                        + this.selectedServerConfig.port
                        + this.selectedServerConfig.contextRoot
                        + currentPage;
                    //        console.log(newDestination);
                    window.location.href = newDestination;
                }; // handleServerSelected()
                ExtractorRoot.prototype.handleExportTypeSelected = function (arg) {
                    //
                    this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
                    this.store.dispatch(new fileItemAction.SetExtractType({ gobiiExtractFilterType: arg }));
                    // this will trigger onchange events in child components
                    this.gobiiExtractFilterType = arg;
                    var jobId = file_name_1.FileName.makeUniqueFileId();
                    this.fileItemService.locadFileItem(gobii_file_item_1.GobiiFileItem.build(arg, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.JOB_ID)
                        .setItemId(jobId)
                        .setItemName(jobId), true);
                    //         this._fileModelTreeService
                    //             .fileItemNotifications()
                    //             .subscribe(fileItem => {
                    //                 if (fileItem.getProcessType() === ProcessType.NOTIFY
                    //                     && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                    //
                    //                     let jobId: string = FileName.makeUniqueFileId();
                    //
                    //                     this._fileModelTreeService
                    //                         .put(GobiiFileItem
                    //                             .build(arg, ProcessType.CREATE)
                    //                             .setExtractorItemType(ExtractorItemType.JOB_ID)
                    //                             .setItemId(jobId)
                    //                             .setItemName(jobId))
                    //                         .subscribe(
                    //                             fmte => {
                    //                                 this._fileModelTreeService
                    //                                     .getTreeState(this.gobiiExtractFilterType)
                    //                                     .subscribe(
                    //                                         ts => {
                    //                                             this.handleTreeStatusChanged(ts)
                    //                                         },
                    //                                         hsm => {
                    //                                             this.handleHeaderStatusMessage(hsm)
                    //                                         }
                    //                                     )
                    //                             },
                    //                             headerStatusMessage => {
                    //                                 this.handleHeaderStatusMessage(headerStatusMessage)
                    //                             }
                    //                         );
                    //                 }
                    //             });
                    //
                    //
                    // //        let extractorFilterItemType: GobiiFileItem = GobiiFileItem.bui(this.gobiiExtractFilterType)
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.doPrincipleInvestigatorTreeNotifications = false;
                        this.fileItemService.setItemLabelType(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.CONTACT_PI, name_id_label_type_1.NameIdLabelType.UNKNOWN);
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorExperiment = true;
                        this.displayAvailableDatasets = true;
                        this.displayIncludedDatasetsGrid = true;
                        this.displaySelectorDataType = false;
                        this.displaySelectorPlatform = false;
                        this.displaySampleListTypeSelector = false;
                        this.displaySampleMarkerBox = false;
                        this.reinitProjectList = false;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        //            this.initializePlatforms();
                        this.displaySelectorPi = true;
                        this.doPrincipleInvestigatorTreeNotifications = true;
                        this.fileItemService.setItemLabelType(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.CONTACT_PI, name_id_label_type_1.NameIdLabelType.ALL);
                        this.displaySelectorProject = true;
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleListTypeSelector = true;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleMarkerBox = false;
                        this.reinitProjectList = true;
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        //            this.initializePlatforms();
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleMarkerBox = true;
                        this.displaySelectorPi = false;
                        this.doPrincipleInvestigatorTreeNotifications = false;
                        this.fileItemService.setItemLabelType(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.CONTACT_PI, name_id_label_type_1.NameIdLabelType.UNKNOWN);
                        this.displaySelectorProject = false;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleListTypeSelector = false;
                        this.reinitProjectList = false;
                    }
                    this.initializeSubmissionContact();
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.CONTACT_PI, null);
                    // this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType,
                    //     this.nameIdRequestParamsExperiments);
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.MAPSETS, null);
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.CV_DATATYPE, null);
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.PLATFORMS, null);
                    // this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType,
                    //     this.nameIdRequestParamsDataset);
                    //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
                    var formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                    this.fileItemService.locadFileItem(formatItem, true);
                };
                ExtractorRoot.prototype.handleContactForPiSelected = function (arg) {
                    this.selectedContactIdForPi = arg.id;
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.PROJECTS_BY_CONTACT, this.selectedContactIdForPi);
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.handleFormatSelected = function (arg) {
                    //        this.selectedExtractFormat = arg;
                    // let extractFilterTypeFileItem: GobiiFileItem = GobiiFileItem
                    //     .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
                    //     .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
                    //     .setItemId(GobiiExtractFormat[arg])
                    //     .setItemName(GobiiExtractFormat[GobiiExtractFormat[arg]]);
                    //
                    // this._fileModelTreeService.put(extractFilterTypeFileItem)
                    //     .subscribe(
                    //         null,
                    //         headerResponse => {
                    //             this.handleResponseHeader(headerResponse)
                    //         });
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.handleProjectSelected = function (arg) {
                    this.selectedProjectId = arg.id;
                    this.displayExperimentDetail = false;
                    this.displayDataSetDetail = false;
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT, this.selectedProjectId);
                };
                ExtractorRoot.prototype.handleExperimentSelected = function (arg) {
                    this.selectedExperimentId = arg.id;
                    this.fileItemService.loadWithFilterParams(this.gobiiExtractFilterType, type_nameid_filter_params_1.NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT, this.selectedExperimentId);
                    // this.store.dispatch(new fileItemAction.SetFilterValueAction({
                    //     gobiiExtractFilterType: this.gobiiExtractFilterType,
                    //     nameIdRequestParams: this.nameIdRequestParamsDataset
                    // }));
                    this.selectedExperimentDetailId = arg.id;
                    this.displayExperimentDetail = true;
                    //console.log("selected contact itemId:" + arg);
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.messages.unshift(arg);
                };
                ExtractorRoot.prototype.handleClearMessages = function () {
                    this.messages = [];
                };
                ExtractorRoot.prototype.handleHeaderStatusMessage = function (statusMessage) {
                    if (!statusMessage.statusLevel || statusMessage.statusLevel != type_status_level_1.StatusLevel.WARNING) {
                        this.handleAddMessage(statusMessage.message);
                    }
                    else {
                        console.log(statusMessage.message);
                    }
                };
                ExtractorRoot.prototype.handleResponseHeader = function (header) {
                    var _this = this;
                    if (header.status !== null && header.status.statusMessages != null) {
                        header.status.statusMessages.forEach(function (statusMessage) {
                            _this.handleHeaderStatusMessage(statusMessage);
                        });
                    }
                };
                ExtractorRoot.prototype.handleStatusTreeReady = function (headerStatusMessage) {
                    //this.handleFormatSelected(GobiiExtractFormat.HAPMAP);
                };
                // private handleTreeStatusChanged(treeStatusNotification: TreeStatusNotification) {
                //
                //     if (treeStatusNotification.gobiiExractFilterType === this.gobiiExtractFilterType) {
                //         this.treeStatusNotification = treeStatusNotification;
                //         this.setSubmitButtonState();
                //     } // does the filter type match
                // }
                ExtractorRoot.prototype.setSubmitButtonState = function () {
                    var returnVal = false;
                    // if (this.treeStatusNotification.fileModelState == FileModelState.SUBMISSION_READY) {
                    //     this.submitButtonStyle = this.buttonStyleSubmitReady;
                    //     returnVal = true;
                    // } else {
                    //     this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    //     returnVal = false;
                    //
                    // }
                    return returnVal;
                };
                ExtractorRoot.prototype.handleOnMouseOverSubmit = function (arg, isEnter) {
                    // this.criteriaInvalid = true;
                    if (isEnter) {
                        this.setSubmitButtonState();
                        // this.treeStatusNotification.modelTreeValidationErrors.forEach(mtv => {
                        //
                        //     let currentMessage: string;
                        //
                        //     if (mtv.fileModelNode.getItemType() === ExtractorItemType.ENTITY) {
                        //         currentMessage = mtv.fileModelNode.getEntityName();
                        //
                        //     } else {
                        //         currentMessage = Labels.instance().treeExtractorTypeLabels[mtv.fileModelNode.getItemType()];
                        //     }
                        //
                        //     currentMessage += ": " + mtv.message;
                        //
                        //     this.handleAddMessage(currentMessage);
                        //
                        // });
                    }
                    // else {
                    //     this.submitButtonStyle = this.submitButtonStyleDefault;
                    // }
                    //#eee
                    var foo = "foo";
                };
                ExtractorRoot.prototype.handleClearTree = function () {
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                };
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    this.instructionSubmissionService.submit(this);
                    // if (this.setSubmitButtonState()) {
                    //
                    //     let scope$ = this;
                    //
                    //     let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
                    //     let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
                    //     let mapsetIds: number[] = [];
                    //     let submitterContactid: number = null;
                    //     let jobId: string = null;
                    //     let markerFileName: string = null;
                    //     let sampleFileName: string = null;
                    //     let sampleListType: GobiiSampleListType;
                    //
                    //     // scope$._fileModelTreeService.getFileItems(scope$.gobiiExtractFilterType).subscribe(
                    //     //     fileItems => {
                    //     //
                    //     //         // ******** JOB ID
                    //     //         let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                    //     //         });
                    //     //
                    //     //         if (fileItemJobId != null) {
                    //     //             jobId = fileItemJobId.getItemId();
                    //     //         }
                    //     //
                    //     //         // ******** MARKER FILE
                    //     //         let fileItemMarkerFile: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                    //     //         });
                    //     //
                    //     //         if (fileItemMarkerFile != null) {
                    //     //             markerFileName = fileItemMarkerFile.getItemId();
                    //     //         }
                    //     //
                    //     //         // ******** SAMPLE FILE
                    //     //         let fileItemSampleFile: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE
                    //     //         });
                    //     //
                    //     //         if (fileItemSampleFile != null) {
                    //     //             sampleFileName = fileItemSampleFile.getItemId();
                    //     //         }
                    //     //
                    //     //         // ******** SUBMITTER CONTACT
                    //     //         let submitterFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return (item.getEntityType() === EntityType.Contacts)
                    //     //                 && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                    //     //         });
                    //     //
                    //     //         submitterContactid = Number(submitterFileItem.getItemId());
                    //     //
                    //     //
                    //     //         // ******** MAPSET IDs
                    //     //         let mapsetFileItems: GobiiFileItem[] = fileItems
                    //     //             .filter(item => {
                    //     //                 return item.getEntityType() === EntityType.Mapsets
                    //     //             });
                    //     //         mapsetFileItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    //     //             EntityType.Mapsets,
                    //     //             mapsetFileItems);
                    //     //         mapsetIds = mapsetFileItems
                    //     //             .map(item => {
                    //     //                 return Number(item.getItemId())
                    //     //             });
                    //     //
                    //     //         // ******** EXPORT FORMAT
                    //     //         let exportFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
                    //     //         });
                    //     //
                    //     //         // these probably should be just one enum
                    //     //         let gobiiFileType: GobiiFileType = null;
                    //     //         let extractFormat: GobiiExtractFormat = GobiiExtractFormat[exportFileItem.getItemId()];
                    //     //         if (extractFormat === GobiiExtractFormat.FLAPJACK) {
                    //     //             gobiiFileType = GobiiFileType.FLAPJACK;
                    //     //         } else if (extractFormat === GobiiExtractFormat.HAPMAP) {
                    //     //             gobiiFileType = GobiiFileType.HAPMAP;
                    //     //         } else if (extractFormat === GobiiExtractFormat.META_DATA_ONLY) {
                    //     //             gobiiFileType = GobiiFileType.META_DATA;
                    //     //         }
                    //     //
                    //     //
                    //     //         // ******** DATA SET TYPE
                    //     //         let dataTypeFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getEntityType() === EntityType.CvTerms
                    //     //                 && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                    //     //         });
                    //     //         let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(),
                    //     //             dataTypeFileItem.getItemName(), EntityType.CvTerms) : null;
                    //     //
                    //     //
                    //     //         // ******** PRINCIPLE INVESTIGATOR CONCEPT
                    //     //         let principleInvestigatorFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getEntityType() === EntityType.Contacts
                    //     //                 && item.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR
                    //     //         });
                    //     //         let principleInvestigator: NameId = principleInvestigatorFileItem != null ? new NameId(principleInvestigatorFileItem.getItemId(),
                    //     //             principleInvestigatorFileItem.getItemName(), EntityType.Contacts) : null;
                    //     //
                    //     //
                    //     //         // ******** PROJECT
                    //     //         let projectFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getEntityType() === EntityType.Projects
                    //     //         });
                    //     //         let project: NameId = projectFileItem != null ? new NameId(projectFileItem.getItemId(),
                    //     //             projectFileItem.getItemName(), EntityType.Projects) : null;
                    //     //
                    //     //
                    //     //         // ******** PLATFORMS
                    //     //         let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                    //     //             return item.getEntityType() === EntityType.Platforms
                    //     //         });
                    //     //
                    //     //         platformFileItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    //     //             EntityType.Platforms,
                    //     //             platformFileItems);
                    //     //
                    //     //         let platforms: NameId[] = platformFileItems.map(item => {
                    //     //             return new NameId(item.getItemId(), item.getItemName(), EntityType.Platforms)
                    //     //         });
                    //     //
                    //     //         let markerGroupItems: GobiiFileItem[] = fileItems.filter(item => {
                    //     //             return item.getEntityType() === EntityType.MarkerGroups
                    //     //         });
                    //     //
                    //     //         markerGroupItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    //     //             EntityType.MarkerGroups,
                    //     //             markerGroupItems);
                    //     //
                    //     //         let markerGroups: NameId[] = markerGroupItems.map(item => {
                    //     //             return new NameId(item.getItemId(), item.getItemName(), EntityType.MarkerGroups)
                    //     //         });
                    //     //
                    //     //         // ******** MARKERS
                    //     //         let markerListItems: GobiiFileItem[] =
                    //     //             fileItems
                    //     //                 .filter(fi => {
                    //     //                     return fi.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                    //     //                 });
                    //     //
                    //     //         markerListItems = this.eliminateDuplicateEntities(ExtractorItemType.MARKER_LIST_ITEM,
                    //     //             EntityType.UNKNOWN,
                    //     //             markerListItems);
                    //     //         let markerList: string[] = markerListItems
                    //     //             .map(mi => {
                    //     //                 return mi.getItemId()
                    //     //             });
                    //     //
                    //     //
                    //     //         // ******** SAMPLES
                    //     //         let sampleListItems: GobiiFileItem[] =
                    //     //             fileItems
                    //     //                 .filter(fi => {
                    //     //                     return fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM
                    //     //                 });
                    //     //
                    //     //         sampleListItems = this.eliminateDuplicateEntities(ExtractorItemType.SAMPLE_LIST_ITEM,
                    //     //             EntityType.UNKNOWN,
                    //     //             sampleListItems);
                    //     //         let sampleList: string[] = sampleListItems
                    //     //             .map(mi => {
                    //     //                 return mi.getItemId()
                    //     //             });
                    //     //
                    //     //
                    //     //         let sampleListTypeFileItem: GobiiFileItem = fileItems.find(item => {
                    //     //             return item.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE;
                    //     //         });
                    //     //
                    //     //         if (sampleListTypeFileItem != null) {
                    //     //             sampleListType = GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                    //     //         }
                    //     //
                    //     //         if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {
                    //     //
                    //     //             let dataSetItems: GobiiFileItem[] = fileItems
                    //     //                 .filter(item => {
                    //     //                     return item.getEntityType() === EntityType.DataSets
                    //     //                 });
                    //     //
                    //     //             dataSetItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    //     //                 EntityType.DataSets,
                    //     //                 dataSetItems);
                    //     //
                    //     //             dataSetItems.forEach(datsetFileItem => {
                    //     //
                    //     //                 let dataSet: NameId = new NameId(datsetFileItem.getItemId(),
                    //     //                     datsetFileItem.getItemName(), EntityType.CvTerms);
                    //     //
                    //     //
                    //     //                 gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                    //     //                     false,
                    //     //                     null,
                    //     //                     this.gobiiExtractFilterType,
                    //     //                     null,
                    //     //                     null,
                    //     //                     markerFileName,
                    //     //                     null,
                    //     //                     datasetType,
                    //     //                     platforms,
                    //     //                     null,
                    //     //                     null,
                    //     //                     dataSet,
                    //     //                     null));
                    //     //             });
                    //     //         } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    //     //             gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                    //     //                 false,
                    //     //                 null,
                    //     //                 this.gobiiExtractFilterType,
                    //     //                 markerList,
                    //     //                 null,
                    //     //                 markerFileName,
                    //     //                 null,
                    //     //                 datasetType,
                    //     //                 platforms,
                    //     //                 null,
                    //     //                 null,
                    //     //                 null,
                    //     //                 markerGroups));
                    //     //         } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    //     //             gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                    //     //                 false,
                    //     //                 null,
                    //     //                 this.gobiiExtractFilterType,
                    //     //                 null,
                    //     //                 sampleList,
                    //     //                 sampleFileName,
                    //     //                 sampleListType,
                    //     //                 datasetType,
                    //     //                 platforms,
                    //     //                 principleInvestigator,
                    //     //                 project,
                    //     //                 null,
                    //     //                 null));
                    //     //         } else {
                    //     //             this.handleAddMessage("Unhandled extract filter type: " + GobiiExtractFilterType[this.gobiiExtractFilterType]);
                    //     //         }
                    //     //     }
                    //     // );
                    //
                    //
                    //     gobiiExtractorInstructions.push(
                    //         new GobiiExtractorInstruction(
                    //             gobiiDataSetExtracts,
                    //             submitterContactid,
                    //             null,
                    //             mapsetIds)
                    //     );
                    //
                    //
                    //     let fileName: string = jobId;
                    //
                    //     let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
                    //         new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                    //             fileName);
                    //
                    //     let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;
                    //
                    //     // this._dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                    //     //     .subscribe(extractorInstructionFilesDTO => {
                    //     //             extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                    //     //             scope$.handleAddMessage("Extractor instruction file created on server: "
                    //     //                 + extractorInstructionFilesDTOResponse.getInstructionFileName());
                    //     //
                    //     //             let newJobId: string = FileName.makeUniqueFileId();
                    //     //             this._fileModelTreeService
                    //     //                 .put(GobiiFileItem
                    //     //                     .build(this.gobiiExtractFilterType, ProcessType.CREATE)
                    //     //                     .setExtractorItemType(ExtractorItemType.JOB_ID)
                    //     //                     .setItemId(newJobId)
                    //     //                     .setItemName(newJobId))
                    //     //                 .subscribe(
                    //     //                     e => {
                    //     //
                    //     //                         this.handleClearTree();
                    //     //                     },
                    //     //                     headerStatusMessage => {
                    //     //                         this.handleHeaderStatusMessage(headerStatusMessage)
                    //     //                     }
                    //     //                 );
                    //     //         },
                    //     //         headerResponse => {
                    //     //
                    //     //             scope$.handleResponseHeader(headerResponse);
                    //     //         });
                    //
                    // } // if submission state is READY
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    var _this = this;
                    this.initializeServerConfigs();
                    this.instructionSubmissionService.submitReady(this)
                        .subscribe(function (submistReady) {
                        submistReady ? _this.submitButtonStyle = _this.buttonStyleSubmitReady : _this.submitButtonStyle = _this.buttonStyleSubmitNotReady;
                    });
                }; // ngOnInit()
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        styleUrls: ['/extractor-ui.css'],
                        template: "\n        <div class=\"panel panel-default\">\n\n            <div class=\"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n\n                <div class=\"panel panel-primary\">\n                    <div class=\"panel-heading\">\n                        <h3 class=\"panel-title\">Connected to {{currentStatus}}</h3>\n                    </div>\n                    <div class=\"panel-body\">\n\n                        <div class=\"col-md-1\">\n\n                            <crops-list-box\n                                    [serverConfigList]=\"serverConfigList\"\n                                    [selectedServerConfig]=\"selectedServerConfig\"\n                                    (onServerSelected)=\"handleServerSelected($event)\"></crops-list-box>\n                        </div>\n\n                        <div class=\"col-md-5\">\n                            <export-type\n                                    (onExportTypeSelected)=\"handleExportTypeSelected($event)\"></export-type>\n                        </div>\n\n\n                        <div class=\"col-md-4\">\n                            <div class=\"well\">\n                                <table>\n                                    <tr>\n                                        <td colspan=\"2\">\n                                            <export-format\n                                                    [fileFormat$]=\"selectedExtractFormat$\"\n                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                    (onFormatSelected)=\"handleFormatSelected($event)\">\n                                            </export-format>\n                                        </td>\n                                        <td style=\"vertical-align: top;\">\n                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n                                            <name-id-list-box\n                                                    [fileItems$]=\"fileItemsMapsets$\"\n                                                    (onError)=\"handleHeaderStatusMessage($event)\">\n                                            </name-id-list-box>\n                                        </td>\n                                    </tr>\n                                </table>\n\n                            </div>\n                        </div>\n\n\n                        <div class=\"col-md-2\">\n                            <p style=\"text-align: right; font-weight: bold;\">{{loggedInUser}}</p>\n                        </div>\n\n                    </div> <!-- panel body -->\n                </div> <!-- panel primary -->\n\n            </div>\n\n            <div class=\"container-fluid\">\n\n                <div class=\"row\">\n\n                    <div class=\"col-md-4\">\n\n                        <div class=\"panel panel-primary\">\n                            <div class=\"panel-heading\">\n                                <h3 class=\"panel-title\">Filters</h3>\n                            </div>\n                            <div class=\"panel-body\">\n\n                                <div *ngIf=\"displaySelectorPi\">\n                                    <label class=\"the-label\">Principle Investigator:</label><BR>\n                                    <name-id-list-box\n                                            [fileItems$]=\"fileItemsContactsPI$\"\n                                            (onNameIdSelected)=\"handleContactForPiSelected($event)\"\n                                            (onError)=\"handleHeaderStatusMessage($event)\">\n                                    </name-id-list-box>\n\n                                </div>\n\n                                <div *ngIf=\"displaySelectorProject\">\n                                    <BR>\n                                    <BR>\n                                    <label class=\"the-label\">Project:</label><BR>\n                                    <name-id-list-box\n                                            [fileItems$]=\"fileItemsProjects$\"\n                                            (onNameIdSelected)=\"handleProjectSelected($event)\"\n                                            (onError)=\"handleHeaderStatusMessage($event)\">\n                                    </name-id-list-box>\n                                </div>\n\n                                <div *ngIf=\"displaySelectorDataType\">\n                                    <BR>\n                                    <BR>\n                                    <label class=\"the-label\">Dataset Types:</label><BR>\n                                    <name-id-list-box\n                                            [fileItems$]=\"fileItemsDatasetTypes$\"\n                                            (onError)=\"handleHeaderStatusMessage($event)\">\n                                    </name-id-list-box>\n                                </div>\n\n\n                                <div *ngIf=\"displaySelectorExperiment\">\n                                    <BR>\n                                    <BR>\n                                    <label class=\"the-label\">Experiment:</label><BR>\n                                    <name-id-list-box\n                                            [fileItems$]=\"fileItemsExperiments$\"\n                                            (onNameIdSelected)=\"handleExperimentSelected($event)\"\n                                            (onError)=\"handleHeaderStatusMessage($event)\">\n                                    </name-id-list-box>\n\n                                </div>\n\n                                <div *ngIf=\"displaySelectorPlatform\">\n                                    <BR>\n                                    <BR>\n                                    <label class=\"the-label\">Platforms:</label><BR>\n                                    <checklist-box\n                                            [nameIdRequestParams]=\"nameIdRequestParamsPlatforms\"\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                            [retainHistory]=\"false\"\n                                            (onAddStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                    </checklist-box>\n                                </div>\n\n\n                                <div *ngIf=\"displayAvailableDatasets\">\n                                    <BR>\n                                    <BR>\n                                    <label class=\"the-label\">Data Sets</label><BR>\n                                    <checklist-box\n                                            [gobiiFileItems$]=\"fileItemsDatasets$\"\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                            [retainHistory]=\"true\"\n                                            (onError)=\"handleHeaderStatusMessage($event)\">\n                                    </checklist-box>\n                                    <!--<dataset-checklist-box-->\n                                    <!--[gobiiExtractFilterType]=\"gobiiExtractFilterType\"-->\n                                    <!--[experimentId]=\"selectedExperimentId\"-->\n                                    <!--(onAddStatusMessage)=\"handleHeaderStatusMessage($event)\">-->\n                                    <!--</dataset-checklist-box>-->\n                                </div>\n                            </div> <!-- panel body -->\n                        </div> <!-- panel primary -->\n\n\n                        <div *ngIf=\"displaySampleListTypeSelector\">\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-heading\">\n                                    <h3 class=\"panel-title\">Included Samples</h3>\n                                </div>\n                                <div class=\"panel-body\">\n                                    <sample-list-type\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                            (onHeaderStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                    </sample-list-type>\n                                    <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\"/>\n                                    <sample-marker-box\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                            (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                    </sample-marker-box>\n                                </div> <!-- panel body -->\n                            </div> <!-- panel primary -->\n                        </div>\n\n                        <div *ngIf=\"displaySampleMarkerBox\">\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-heading\">\n                                    <h3 class=\"panel-title\">Included Markers</h3>\n                                </div>\n                                <div class=\"panel-body\">\n                                    <sample-marker-box\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                            (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                    </sample-marker-box>\n                                </div> <!-- panel body -->\n                            </div> <!-- panel primary -->\n                        </div>\n\n                    </div>  <!-- outer grid column 1-->\n\n\n                    <div class=\"col-md-4\">\n\n                        <div class=\"panel panel-primary\">\n                            <div class=\"panel-heading\">\n                                <h3 class=\"panel-title\">Extraction Criteria</h3>\n                            </div>\n                            <div class=\"panel-body\">\n                                <status-display-tree\n                                        [fileItemEventChange]=\"treeFileItemEvent\"\n                                        [gobiiExtractFilterTypeEvent]=\"gobiiExtractFilterType\"\n                                        (onAddMessage)=\"handleHeaderStatusMessage($event)\"\n                                        (onTreeReady)=\"handleStatusTreeReady($event)\">\n                                </status-display-tree>\n\n                                <BR>\n\n                                <button type=\"submit\"\n                                        [class]=\"submitButtonStyle\"\n                                        (mouseenter)=\"handleOnMouseOverSubmit($event,true)\"\n                                        (mouseleave)=\"handleOnMouseOverSubmit($event,false)\"\n                                        (click)=\"handleExtractSubmission()\">Submit\n                                </button>\n\n                                <button type=\"clear\"\n                                        [class]=\"clearButtonStyle\"\n                                        (click)=\"handleClearTree()\">Clear\n                                </button>\n\n                            </div> <!-- panel body -->\n                        </div> <!-- panel primary -->\n\n                    </div>  <!-- outer grid column 2-->\n\n\n                    <div class=\"col-md-4\">\n\n\n                        <div>\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-heading\">\n                                    <h3 class=\"panel-title\">Status Messages</h3>\n                                </div>\n                                <div class=\"panel-body\">\n                                    <status-display [messages]=\"messages\"></status-display>\n                                    <BR>\n                                    <button type=\"clear\"\n                                            class=\"btn btn-primary\"\n                                            (click)=\"handleClearMessages()\">Clear\n                                    </button>\n                                </div> <!-- panel body -->\n\n                            </div> <!-- panel primary -->\n                        </div>\n\n\n                    </div>  <!-- outer grid column 3 (inner grid)-->\n\n                </div> <!-- .row of outer grid -->\n\n                <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                    <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n\n                    </div><!-- end column 1 of outer grid -->\n\n                </div><!-- end .row 2 of outer grid-->\n\n            </div>" // end template
                    }) // @Component
                    ,
                    __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                        authentication_service_1.AuthenticationService,
                        dto_request_service_1.DtoRequestService,
                        store_1.Store,
                        file_item_service_1.FileItemService,
                        instruction_submission_service_1.InstructionSubmissionService])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map