///<reference path="../../../../../../typings/index.d.ts"/>
System.register(["@angular/core", "@angular/http", "../views/export-type.component", "../views/export-format.component", "../services/core/dto-request.service", "../services/core/authentication.service", "../views/contacts-list-box.component", "../views/dataset-types-list-box.component", "../views/project-list-box.component", "../views/experiment-list-box.component", "../views/dataset-checklist-box.component", "../views/mapsets-list-box.component", "../model/extractor-instructions/data-set-extract", "../views/criteria-display.component", "../views/status-display-box.component", "../model/type-process", "../model/server-config", "../model/type-entity", "../views/crops-list-box.component", "../views/users-list-box.component", "../model/name-id", "../model/type-gobii-file", "../model/extractor-instructions/dto-extractor-instruction-files", "../model/extractor-instructions/gobii-extractor-instruction", "../services/app/dto-request-item-extractor-submission", "../services/app/dto-request-item-nameids", "../services/app/dto-request-item-serverconfigs", "../model/type-entity-filter", "../views/checklist-box.component"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, http_1, export_type_component_1, export_format_component_1, dto_request_service_1, authentication_service_1, contacts_list_box_component_1, dataset_types_list_box_component_1, project_list_box_component_1, experiment_list_box_component_1, dataset_checklist_box_component_1, mapsets_list_box_component_1, data_set_extract_1, criteria_display_component_1, status_display_box_component_1, type_process_1, server_config_1, type_entity_1, crops_list_box_component_1, users_list_box_component_1, name_id_1, type_gobii_file_1, dto_extractor_instruction_files_1, gobii_extractor_instruction_1, dto_request_item_extractor_submission_1, dto_request_item_nameids_1, dto_request_item_serverconfigs_1, type_entity_filter_1, checklist_box_component_1;
    var ExtractorRoot;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (export_type_component_1_1) {
                export_type_component_1 = export_type_component_1_1;
            },
            function (export_format_component_1_1) {
                export_format_component_1 = export_format_component_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (contacts_list_box_component_1_1) {
                contacts_list_box_component_1 = contacts_list_box_component_1_1;
            },
            function (dataset_types_list_box_component_1_1) {
                dataset_types_list_box_component_1 = dataset_types_list_box_component_1_1;
            },
            function (project_list_box_component_1_1) {
                project_list_box_component_1 = project_list_box_component_1_1;
            },
            function (experiment_list_box_component_1_1) {
                experiment_list_box_component_1 = experiment_list_box_component_1_1;
            },
            function (dataset_checklist_box_component_1_1) {
                dataset_checklist_box_component_1 = dataset_checklist_box_component_1_1;
            },
            function (mapsets_list_box_component_1_1) {
                mapsets_list_box_component_1 = mapsets_list_box_component_1_1;
            },
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            },
            function (criteria_display_component_1_1) {
                criteria_display_component_1 = criteria_display_component_1_1;
            },
            function (status_display_box_component_1_1) {
                status_display_box_component_1 = status_display_box_component_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (crops_list_box_component_1_1) {
                crops_list_box_component_1 = crops_list_box_component_1_1;
            },
            function (users_list_box_component_1_1) {
                users_list_box_component_1 = users_list_box_component_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (type_gobii_file_1_1) {
                type_gobii_file_1 = type_gobii_file_1_1;
            },
            function (dto_extractor_instruction_files_1_1) {
                dto_extractor_instruction_files_1 = dto_extractor_instruction_files_1_1;
            },
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            },
            function (dto_request_item_extractor_submission_1_1) {
                dto_request_item_extractor_submission_1 = dto_request_item_extractor_submission_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (checklist_box_component_1_1) {
                checklist_box_component_1 = checklist_box_component_1_1;
            }],
        execute: function() {
            // import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';
            // GOBii Imports
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceExtractorFile, _dtoRequestServiceNameIds, _dtoRequestServiceServerConfigs) {
                    this._dtoRequestServiceExtractorFile = _dtoRequestServiceExtractorFile;
                    this._dtoRequestServiceNameIds = _dtoRequestServiceNameIds;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.title = 'Gobii Web';
                    this.dataSetCheckBoxEvents = [];
                    this.gobiiDatasetExtracts = [];
                    this.messages = [];
                    // ********************************************************************
                    // ********************************************** EXPORT TYPE SELECTION AND FLAGS
                    this.displayAvailableDatasets = true;
                    this.displaySelectorPi = true;
                    this.displaySelectorProject = true;
                    this.displaySelectorExperiment = true;
                    this.displaySelectorDataType = false;
                    this.displaySelectorPlatform = false;
                    this.displayIncludedDatasetsGrid = true;
                    this.displaySampleListTypeSelector = false;
                    this.displaySampleMarkerBox = false;
                    // ********************************************************************
                    // ********************************************** HAPMAP SELECTION
                    this.selectedFormatName = "Hapmap";
                    // ********************************************************************
                    // ********************************************** EXPERIMENT ID
                    this.displayExperimentDetail = false;
                    // ********************************************************************
                    // ********************************************** DATASET ID
                    this.displayDataSetDetail = false;
                    this.changeTrigger = 0;
                }
                ExtractorRoot.prototype.initializeServerConfigs = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceServerConfigs.get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            var serverCrop_1 = _this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(function (c) {
                                    return c.crop === serverCrop_1;
                                })[0];
                            scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                            scope$.initializeContactsForSumission();
                            scope$.initializeContactsForPi();
                            scope$.initializeMapsetsForSumission();
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving server configs: "
                            + m.message); });
                    });
                }; // initializeServerConfigs()
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
                    this.selectedExportType = arg;
                    if (this.selectedExportType === "byDataSet") {
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorExperiment = true;
                        this.displayAvailableDatasets = true;
                        this.displayIncludedDatasetsGrid = true;
                        this.displaySelectorDataType = false;
                        this.displaySelectorPlatform = false;
                        this.displaySampleListTypeSelector = false;
                        this.displaySampleMarkerBox = false;
                    }
                    else if (this.selectedExportType === "bySample") {
                        this.initializeDatasetTypes();
                        this.initializePlatforms();
                        this.displaySelectorPi = true;
                        this.displaySelectorProject = true;
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleListTypeSelector = true;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleMarkerBox = false;
                    }
                    else if (this.selectedExportType === "byMarker") {
                        this.initializeDatasetTypes();
                        this.initializePlatforms();
                        this.displaySelectorDataType = true;
                        this.displaySelectorPlatform = true;
                        this.displaySampleMarkerBox = true;
                        this.displaySelectorPi = false;
                        this.displaySelectorProject = false;
                        this.displaySelectorExperiment = false;
                        this.displayAvailableDatasets = false;
                        this.displayIncludedDatasetsGrid = false;
                        this.displaySampleListTypeSelector = false;
                    }
                };
                ExtractorRoot.prototype.handleContactForSubmissionSelected = function (arg) {
                    this.selectedContactIdForSubmitter = arg;
                };
                ExtractorRoot.prototype.initializeContactsForSumission = function () {
                    var scope$ = this;
                    this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Contacts)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForSubmitter = nameIds;
                            scope$.selectedContactIdForSubmitter = nameIds[0].id;
                        }
                        else {
                            scope$.contactNameIdListForSubmitter = [new name_id_1.NameId(0, "ERROR NO USERS")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Rettrieving contacts: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleContactForPiSelected = function (arg) {
                    this.selectedContactIdForPi = arg;
                    this.initializeProjectNameIds();
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot.prototype.initializeContactsForPi = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Contacts, type_entity_filter_1.EntityFilter.NONE)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.contactNameIdListForPi = nameIds;
                            scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                        }
                        else {
                            scope$.contactNameIdListForPi = [new name_id_1.NameId(0, "ERROR NO USERS")];
                        }
                        scope$.initializeProjectNameIds();
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving contacts for PIs: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleFormatSelected = function (arg) {
                    this.selectedFormatName = arg;
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot.prototype.handleProjectSelected = function (arg) {
                    this.selectedProjectId = arg;
                    this.displayExperimentDetail = false;
                    this.displayDataSetDetail = false;
                    this.initializeExperimentNameIds();
                };
                ExtractorRoot.prototype.initializeProjectNameIds = function () {
                    var _this = this;
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Projects, type_entity_filter_1.EntityFilter.BYTYPEID, this.selectedContactIdForPi)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.projectNameIdList = nameIds;
                            scope$.selectedProjectId = nameIds[0].id;
                        }
                        else {
                            scope$.projectNameIdList = [new name_id_1.NameId(0, "<none>")];
                            scope$.selectedProjectId = undefined;
                        }
                        _this.initializeExperimentNameIds();
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retriving project names: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleExperimentSelected = function (arg) {
                    this.selectedExperimentId = arg;
                    this.selectedExperimentDetailId = arg;
                    this.displayExperimentDetail = true;
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot.prototype.initializeExperimentNameIds = function () {
                    var scope$ = this;
                    if (this.selectedProjectId) {
                        this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Experiments, type_entity_filter_1.EntityFilter.BYTYPEID, this.selectedProjectId)).subscribe(function (nameIds) {
                            if (nameIds && (nameIds.length > 0)) {
                                scope$.experimentNameIdList = nameIds;
                                scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                            }
                            else {
                                scope$.experimentNameIdList = [new name_id_1.NameId(0, "<none>")];
                                scope$.selectedExperimentId = undefined;
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retreving experiment names: "
                                + m.message); });
                        });
                    }
                    else {
                        scope$.experimentNameIdList = [new name_id_1.NameId(0, "<none>")];
                        scope$.selectedExperimentId = undefined;
                    }
                };
                ExtractorRoot.prototype.handleDatasetTypeSelected = function (arg) {
                    this.selectedDatasetTypeId = arg;
                };
                ExtractorRoot.prototype.initializeDatasetTypes = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.CvTerms, type_entity_filter_1.EntityFilter.BYTYPENAME, "dataset_type")).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.datasetTypeNameIdList = nameIds;
                            scope$.selectedDatasetTypeId = scope$.datasetTypeNameIdList[0].id;
                        }
                        else {
                            scope$.datasetTypeNameIdList = [new name_id_1.NameId(0, "ERROR NO DATASET TYPES")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving DatasetTypes: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handlePlatformSelected = function (arg) {
                    this.selectedPlatformId = arg.id;
                };
                ExtractorRoot.prototype.handlePlatformChecked = function (arg) {
                    this.checkedPlatformId = arg.id;
                };
                ExtractorRoot.prototype.initializePlatforms = function () {
                    var scope$ = this;
                    scope$._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Platforms, type_entity_filter_1.EntityFilter.NONE)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.platformsNameIdList = nameIds;
                            scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                        }
                        else {
                            scope$.platformsNameIdList = [new name_id_1.NameId(0, "ERROR NO PLATFORMS")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Retrieving PlatformTypes: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.messages.push(arg);
                };
                ExtractorRoot.prototype.handleCheckedDataSetItem = function (arg) {
                    if (type_process_1.ProcessType.CREATE == arg.processType) {
                        this.dataSetCheckBoxEvents.push(arg);
                        this.gobiiDatasetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(type_gobii_file_1.GobiiFileType.GENERIC, false, Number(arg.id), arg.name, null));
                    }
                    else {
                        var indexOfEventToRemove = this.dataSetCheckBoxEvents.indexOf(arg);
                        this.dataSetCheckBoxEvents.splice(indexOfEventToRemove, 1);
                        this.gobiiDatasetExtracts =
                            this.gobiiDatasetExtracts
                                .filter(function (item) {
                                return item.getDataSetId() != Number(arg.id);
                            });
                    } // if-else we're adding
                };
                ExtractorRoot.prototype.handleExtractDataSetUnchecked = function (arg) {
                    // this.changeTrigger++;
                    // this.dataSetIdToUncheck = Number(arg.id);
                    var dataSetExtractsToRemove = this.gobiiDatasetExtracts
                        .filter(function (e) {
                        return e.getDataSetId() === Number(arg.id);
                    });
                    if (dataSetExtractsToRemove.length > 0) {
                        var idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);
                        this.gobiiDatasetExtracts.splice(idxToRemove, 1);
                    }
                    this.checkBoxEventChange = arg;
                };
                ExtractorRoot.prototype.handleMapsetSelected = function (arg) {
                    if (arg > 0) {
                        this.selectedMapsetId = arg;
                    }
                    else {
                        this.selectedMapsetId = undefined;
                    }
                };
                ExtractorRoot.prototype.initializeMapsetsForSumission = function () {
                    var scope$ = this;
                    scope$.nullMapsetName = "<none>";
                    this._dtoRequestServiceNameIds.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_entity_1.EntityType.Mapsets)).subscribe(function (nameIds) {
                        scope$.mapsetNameIdList = [new name_id_1.NameId(0, scope$.nullMapsetName)];
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.mapsetNameIdList = scope$.mapsetNameIdList.concat(nameIds);
                            scope$.selectedMapsetId = nameIds[0].id;
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Rettrieving mapsets: "
                            + m.message); });
                    });
                };
                // ********************************************************************
                // ********************************************** Extract file submission
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    var scope$ = this;
                    var gobiiExtractorInstructions = [];
                    var gobiiFileType = type_gobii_file_1.GobiiFileType[this.selectedFormatName.toUpperCase()];
                    this.gobiiDatasetExtracts.forEach(function (e) { return e.setGobiiFileType(gobiiFileType); });
                    var mapsetIds = [];
                    if ((scope$.selectedMapsetId !== undefined)) {
                        mapsetIds.push(Number(scope$.selectedMapsetId));
                    }
                    gobiiExtractorInstructions.push(new gobii_extractor_instruction_1.GobiiExtractorInstruction(this.gobiiDatasetExtracts, Number(this.selectedContactIdForSubmitter), null, mapsetIds));
                    var date = new Date();
                    var fileName = "extractor_"
                        + date.getFullYear()
                        + "_"
                        + (date.getMonth() + 1)
                        + "_"
                        + date.getDay()
                        + "_"
                        + date.getHours()
                        + "_"
                        + date.getMinutes()
                        + "_"
                        + date.getSeconds();
                    var extractorInstructionFilesDTORequest = new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO(gobiiExtractorInstructions, fileName);
                    //this.selectedServerConfig.crop
                    var extractorInstructionFilesDTOResponse = null;
                    this._dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                        .subscribe(function (extractorInstructionFilesDTO) {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        scope$.messages.push("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName());
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.messages.push("Submitting extractor instructions: "
                            + m.message); });
                    });
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    this.initializeServerConfigs();
                };
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        directives: [export_format_component_1.ExportFormatComponent,
                            contacts_list_box_component_1.ContactsListBoxComponent,
                            project_list_box_component_1.ProjectListBoxComponent,
                            experiment_list_box_component_1.ExperimentListBoxComponent,
                            dataset_checklist_box_component_1.DataSetCheckListBoxComponent,
                            mapsets_list_box_component_1.MapsetsListBoxComponent,
                            criteria_display_component_1.CriteriaDisplayComponent,
                            status_display_box_component_1.StatusDisplayComponent,
                            crops_list_box_component_1.CropsListBoxComponent,
                            users_list_box_component_1.UsersListBoxComponent,
                            export_type_component_1.ExportTypeComponent,
                            dataset_types_list_box_component_1.DatasetTypeListBoxComponent,
                            checklist_box_component_1.CheckListBoxComponent],
                        styleUrls: ['/extractor-ui.css'],
                        providers: [
                            http_1.HTTP_PROVIDERS,
                            authentication_service_1.AuthenticationService,
                            dto_request_service_1.DtoRequestService
                        ],
                        template: "\n        <div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n\n                <fieldset class=\"well the-fieldset\">\n                    <div class=\"col-md-2\">\n                        <crops-list-box\n                            [serverConfigList]=\"serverConfigList\"\n                            [selectedServerConfig]=\"selectedServerConfig\"\n                            (onServerSelected)=\"handleServerSelected($event)\"></crops-list-box>\n                    </div>\n                    \n                    <div class=\"col-md-3\">\n                       <export-type\n                        (onExportTypeSelected)=\"handleExportTypeSelected($event)\"></export-type>\n                     </div>\n                     \n                </fieldset>\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-4\">\n                    \n                    <!--\n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Submit As</legend>\n                        <users-list-box\n                            [nameIdList]=\"contactNameIdListForSubmitter\"\n                            (onUserSelected)=\"handleContactForSubmissionSelected($event)\">\n                        </users-list-box>\n                        </fieldset>\n                        -->\n                        \n                     <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Filters</legend><BR>\n                        \n                        \n                        <div *ngIf=\"displaySelectorPi\">\n                            <label class=\"the-label\">Principle Investigator:</label><BR>\n                            <contacts-list-box [nameIdList]=\"contactNameIdListForPi\" (onContactSelected)=\"handleContactForPiSelected($event)\"></contacts-list-box>\n                        </div>\n                        \n                        <div *ngIf=\"displaySelectorProject\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Project:</label><BR>\n                            <project-list-box [primaryInvestigatorId] = \"selectedContactIdForPi\"\n                                [nameIdList]=\"projectNameIdList\"\n                                [nameIdListPIs]=\"contactNameIdListForPi\"\n                                (onProjectSelected)=\"handleProjectSelected($event)\"\n                                (onAddMessage)=\"handleAddMessage($event)\"></project-list-box>\n                        </div>\n\n                        <div *ngIf=\"displaySelectorDataType\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Dataset Types:</label><BR>\n                            <dataset-types-list-box [nameIdList]=\"datasetTypeNameIdList\" (onDatasetTypeSelected)=\"handleDatasetTypeSelected($event)\"></dataset-types-list-box>\n                        </div>\n\n                        \n                        <div *ngIf=\"displaySelectorExperiment\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Experiment:</label><BR>\n                            <experiment-list-box [projectId] = \"selectedProjectId\"\n                                [nameIdList] = \"experimentNameIdList\"\n                                (onExperimentSelected)=\"handleExperimentSelected($event)\"\n                                (onAddMessage)=\"handleAddMessage($event)\"></experiment-list-box>\n                        </div>\n\n                        <div *ngIf=\"displaySelectorPlatform\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Platforms:</label><BR>\n                            <checklist-box\n                                [checkBoxEventChange] = \"platformCheckBoxEventChange\"\n                                [nameIdList] = \"platformsNameIdList\"\n                                (onItemSelected)=\"handlePlatformSelected($event)\"\n                                (onItemChecked)=\"handlePlatformChecked($event)\"\n                                (onAddMessage) = \"handleAddMessage($event)\">\n                            </checklist-box>\n                         </div>\n\n\n                        <div *ngIf=\"displayAvailableDatasets\">\n                            <BR>\n                            <BR>\n                            <label class=\"the-label\">Data Sets</label><BR>\n                            <dataset-checklist-box\n                                [checkBoxEventChange] = \"checkBoxEventChange\"\n                                [experimentId] = \"selectedExperimentId\" \n                                (onItemChecked)=\"handleCheckedDataSetItem($event)\"\n                                (onAddMessage) = \"handleAddMessage($event)\">\n                            </dataset-checklist-box>\n                        </div>\n                    </fieldset>\n                       \n                       \n                    </div>  <!-- outer grid column 1-->\n                \n                \n                \n                    <div class=\"col-md-4\"> \n                        <div *ngIf=\"displayIncludedDatasetsGrid\">\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Included Datasets</legend>\n                                <criteria-display \n                                    [dataSetCheckBoxEvents] = \"dataSetCheckBoxEvents\"\n                                    (onItemUnChecked) = \"handleExtractDataSetUnchecked($event)\"></criteria-display>\n                            </fieldset>\n                        </div>\n                        \n                        <div *ngIf=\"displaySampleListTypeSelector\">\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Included Samples</legend>\n                            </fieldset>\n                        </div>\n                        \n                        <div *ngIf=\"displaySampleMarkerBox\">\n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Included Markers</legend>\n                            </fieldset>\n                        </div>\n                        \n                    </div>  <!-- outer grid column 2-->\n                    \n                    \n                    <div class=\"col-md-4\">\n                         \n                            \n                    <form>\n\t\t\t           <fieldset class=\"well the-fieldset\">\n                \t\t\t<legend class=\"the-legend\">Export</legend>\n\t\t\t           \n                            <export-format (onFormatSelected)=\"handleFormatSelected($event)\"></export-format>\n                            <BR>\n                       \n                            <mapsets-list-box [nameIdList]=\"mapsetNameIdList\" \n                                (onMapsetSelected)=\"handleMapsetSelected($event)\"></mapsets-list-box>\n                            <BR>\n                            <BR>\n                   \n                            <input type=\"button\" \n                            value=\"Submit\"\n                             [disabled]=\"(gobiiDatasetExtracts.length === 0)\"\n                            (click)=\"handleExtractSubmission()\" >\n            \t\t\t</fieldset>\n                    </form>\n                            \n                            <fieldset class=\"well the-fieldset\" style=\"vertical-align: bottom;\">\n                                <legend class=\"the-legend\">Status</legend>\n                                <status-display [messages] = \"messages\"></status-display>\n                            </fieldset>\n                                   \n                    </div>  <!-- outer grid column 3 (inner grid)-->\n                                        \n                </div> <!-- .row of outer grid -->\n                \n                    <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                        <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n                         \n                         </div><!-- end column 1 of outer grid -->\n                    \n                    </div><!-- end .row 2 of outer grid-->\n                \n            </div>" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    }
});
//# sourceMappingURL=app.extractorroot.js.map