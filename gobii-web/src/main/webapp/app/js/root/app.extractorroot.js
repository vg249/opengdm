System.register(["@angular/core", "../services/core/dto-request.service", "../model/type-process", "../model/gobii-file-item", "../model/server-config", "../model/type-entity", "../services/app/dto-request-item-serverconfigs", "../model/type-extractor-filter", "../model/cv-filter-type", "../model/type-extractor-item", "../model/type-extract-format", "../model/dto-header-status-message", "../model/file_name", "../services/app/dto-request-item-contact", "../services/core/authentication.service", "../model/type-status-level", "@ngrx/store", "../store/reducers", "../store/actions/fileitem-action", "../store/actions/history-action", "../model/file-item-param-names", "../services/core/file-item-service", "../services/core/instruction-submission-service", "../model/type-extractor-sample-list", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, type_process_1, gobii_file_item_1, server_config_1, type_entity_1, dto_request_item_serverconfigs_1, type_extractor_filter_1, cv_filter_type_1, type_extractor_item_1, type_extract_format_1, dto_header_status_message_1, file_name_1, dto_request_item_contact_1, authentication_service_1, type_status_level_1, store_1, fromRoot, fileItemAction, historyAction, file_item_param_names_1, file_item_service_1, instruction_submission_service_1, type_extractor_sample_list_1, view_id_generator_service_1, type_control_1, ExtractorRoot;
    var __moduleName = context_1 && context_1.id;
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
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
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
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (instruction_submission_service_1_1) {
                instruction_submission_service_1 = instruction_submission_service_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = class ExtractorRoot {
                constructor(_dtoRequestServiceContact, _authenticationService, _dtoRequestServiceServerConfigs, store, fileItemService, instructionSubmissionService, changeDetectorRef, viewIdGeneratorService) {
                    this._dtoRequestServiceContact = _dtoRequestServiceContact;
                    this._authenticationService = _authenticationService;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.instructionSubmissionService = instructionSubmissionService;
                    this.changeDetectorRef = changeDetectorRef;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.title = 'Gobii Web';
                    // *** You cannot use an Enum directly as a template type parameter, so we need
                    //     to assign them to properties
                    // ************************************************************************
                    //
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                    this.typeControl = type_control_1.TypeControl;
                    this.selectedExtractFormat$ = this.store.select(fromRoot.getSelectedFileFormat);
                    this.messages$ = this.store.select(fromRoot.getStatusMessages);
                    this.gobiiExtractFilterTypeForExpressions = type_extractor_filter_1.GobiiExtractFilterType;
                    this.loggedInUser = null;
                    this.display = false;
                    this.currentStatusMessage = null;
                    // ********************************************************************
                    // ********************************************** MARKER/SAMPLE selection
                    // ********************************************************************
                    // ********************************************** Extract file submission
                    this.submitButtonStyleDefault = "btn btn-primary";
                    this.buttonStyleSubmitReady = "btn btn-success";
                    this.buttonStyleSubmitNotReady = "btn btn-warning";
                    this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    this.clearButtonStyle = this.submitButtonStyleDefault;
                    this.messages$.subscribe(messages => {
                        if (messages.length > 0) {
                            this.currentStatusMessage = messages[0];
                            this.showMessagesDialog();
                        }
                    });
                }
                showMessagesDialog() {
                    this.display = true;
                    //workaround for error when using observable in a
                    //p-dialog; see https://github.com/angular/angular/issues/17572
                    this.changeDetectorRef.detectChanges();
                }
                onHideMessageDialog($event) {
                    this.handleClearMessages();
                }
                initializeServerConfigs() {
                    let scope$ = this;
                    this._dtoRequestServiceServerConfigs.get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(serverConfigs => {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            let serverCrop = this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            let gobiiVersion = this._dtoRequestServiceServerConfigs.getGobbiiVersion();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(c => {
                                    return c.crop === serverCrop;
                                })[0];
                            this.handleExportTypeSelected(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                            //                    scope$.initializeSubmissionContact();
                            scope$.currentStatus = "GOBII Server " + gobiiVersion;
                            //scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0, "")];
                        }
                    }, dtoHeaderResponse => {
                        dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving server configs: "
                            + m.message));
                    });
                } // initializeServerConfigs()
                initializeSubmissionContact() {
                    this.loggedInUser = this._authenticationService.getUserName();
                    let scope$ = this;
                    scope$._dtoRequestServiceContact.get(new dto_request_item_contact_1.DtoRequestItemContact(dto_request_item_contact_1.ContactSearchType.BY_USERNAME, this.loggedInUser)).subscribe(contact => {
                        let foo = "foo";
                        if (contact && contact.contactId && contact.contactId > 0) {
                            //loggedInUser
                            this.fileItemService.loadFileItem(gobii_file_item_1.GobiiFileItem.build(scope$.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setEntityType(type_entity_1.EntityType.CONTACT)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setItemName(contact.email)
                                .setItemId(contact.contactId.toLocaleString()), true);
                        }
                        else {
                            scope$.handleAddMessage("There is no contact associated with user " + this.loggedInUser);
                        }
                    }, dtoHeaderResponse => {
                        dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving contacts for submission: "
                            + m.message));
                    });
                    //   _dtoRequestServiceContact
                }
                handleServerSelected(arg) {
                    this.selectedServerConfig = arg;
                    let currentPath = window.location.pathname;
                    let currentPage = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
                    let newDestination = "http://"
                        + this.selectedServerConfig.domain
                        + ":"
                        + this.selectedServerConfig.port
                        + this.selectedServerConfig.contextRoot
                        + currentPage;
                    window.location.href = newDestination;
                } // handleServerSelected()
                refreshJobId() {
                    let jobId = file_name_1.FileName.makeUniqueFileId();
                    this.fileItemService.replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.JOB_ID)
                        .setItemId(jobId)
                        .setItemName(jobId)
                        .setIsExtractCriterion(true));
                }
                handleTabPanelChange(event) {
                    let tabIndex = event.index;
                    if (tabIndex === 0) { // By Dataset
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET;
                    }
                    else if (tabIndex == 1) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE;
                    }
                    else if (tabIndex == 2) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER;
                    }
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                } // handleTabPanelChange
                handleExportTypeSelected(arg) {
                    this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
                    this.store.dispatch(new fileItemAction.SetExtractType({ gobiiExtractFilterType: arg }));
                    this.gobiiExtractFilterType = arg;
                    this.instructionSubmissionService.submitReady(this.gobiiExtractFilterType)
                        .subscribe(submistReady => {
                        submistReady ? this.submitButtonStyle = this.buttonStyleSubmitReady : this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    });
                    this.refreshJobId();
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, null);
                        this.fileItemService.loadFilter(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_FILTER_OPTIONAL, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    this.initializeSubmissionContact();
                    this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.MAPSETS, null);
                    //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
                    let formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                    this.fileItemService.replaceFileItemByCompoundId(formatItem);
                    this.fileItemService
                        .replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME]));
                }
                handleAddMessage(arg) {
                    this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage(arg, type_status_level_1.StatusLevel.OK, undefined)));
                }
                handleClearMessages() {
                    this.store.dispatch(new historyAction.ClearStatusesAction());
                }
                handleHeaderStatusMessage(statusMessage) {
                    if (!statusMessage.statusLevel || statusMessage.statusLevel != type_status_level_1.StatusLevel.WARNING) {
                        this.handleAddMessage(statusMessage.message);
                    }
                    else {
                        console.log(statusMessage.message);
                    }
                }
                handleOnMouseOverSubmit(arg, isEnter) {
                    // this.criteriaInvalid = true;
                    if (isEnter) {
                        this.instructionSubmissionService.markMissingItems(this.gobiiExtractFilterType);
                    }
                    else {
                        this.instructionSubmissionService.unmarkMissingItems(this.gobiiExtractFilterType);
                    }
                }
                handleClearTree() {
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                }
                handleExtractSubmission() {
                    this.instructionSubmissionService
                        .submit(this.gobiiExtractFilterType)
                        .subscribe(instructions => {
                        this.refreshJobId();
                        this.handleClearTree();
                    });
                }
                ngOnInit() {
                    this.initializeServerConfigs();
                } // ngOnInit()
            };
            ExtractorRoot = __decorate([
                core_1.Component({
                    selector: 'extractor-root',
                    styleUrls: ['/extractor-ui.css'],
                    template: `
        <BR>
        <BR>
        <div class="panel panel-default col-md-6 col-md-offset-0" style="width: 95%">
            <div class="panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>
            </div> <!-- panel heading -->

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">{{currentStatus}}
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        {{loggedInUser}}</h3>
                </div> <!-- panel heading  -->

                <BR>
                <BR>
                <div class="container-fluid">

                    <div class="row">

                        <div class="col-md-8"> <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->

                            <p-panel header="Extract Filtering">
                                <!--<p-tabView [style]="{'border': '1px solid #336699', 'padding-left': '5px'}">-->
                                <p-tabView
                                        (onChange)="handleTabPanelChange($event)"
                                        styleClass="ui-tabview-panels"
                                [id]="viewIdGeneratorService.makeStandardId(typeControl.NAVIGATION_TABS)">
                                    <p-tabPanel header="By Dataset">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid" *ngIf="gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.WHOLE_DATASET">
                                                <div class="row">
                                                    <div class="col-md-12"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Datasets</h3>
                                                            </div>

                                                            <div class="panel-body">

                                                                <table class="table">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td>
                                                                            <label class="the-label">Principal
                                                                                Investigator:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.CONTACT_PI_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                        <td>
                                                                            <label class="the-label">Project:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.PROJECT_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                        <td>
                                                                            <label class="the-label">Experiment:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.EXPERIMENT_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>

                                                                <dataset-datatable
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </dataset-datatable>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- dataset -->
                                    <p-tabPanel header="By Samples">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid" *ngIf="gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.BY_SAMPLE">
                                                <div class="row">
                                                    <div class="col-md-4"> <!-- inner column 1 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Filters</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <label class="the-label">Principal Investigator:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CONTACT_PI_HIERARCHY_ROOT">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Projects:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.PROJECTS_BY_CONTACT">
                                                                </name-id-list-box>


                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Dataset Types:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CV_DATATYPE">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Platforms:</label><BR>
                                                                <checklist-box
                                                                        [filterParamName]="nameIdFilterParamTypes.PLATFORMS"
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </checklist-box>

                                                            </div> <!-- panel body by sample filters filters -->
                                                        </div> <!-- panel by sample filters -->
                                                    </div> <!-- by sample filter column-->
                                                    <div class="col-md-8"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Included Samples</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-list-type
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onHeaderStatusMessage)="handleHeaderStatusMessage($event)">
                                                                </sample-list-type>
                                                                <hr style="width: 100%; color: black; height: 1px; background-color:black;"/>
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- samples -->
                                    <p-tabPanel header="By Markers">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid" *ngIf="gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.BY_MARKER">
                                                <div class="row">
                                                    <div class="col-md-4"> <!-- inner column 1 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Filters</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <label class="the-label">Dataset Types:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CV_DATATYPE">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Platforms:</label><BR>
                                                                <checklist-box
                                                                        [filterParamName]="nameIdFilterParamTypes.PLATFORMS"
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </checklist-box>

                                                            </div> <!-- panel body by sample filters filters -->
                                                        </div> <!-- panel by sample filters -->
                                                    </div> <!-- by sample filter column-->
                                                    <div class="col-md-8"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Included Markers</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- markers -->
                                </p-tabView> <!-- tabview -->
                            </p-panel> <!-- panel -->

                        </div>  <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->


                        <p-dialog header="System Message"
                                  modal="modal"
                                  [(visible)]="display"
                                  (onHide)="onHideMessageDialog($event)"
                                  [contentStyle]="{'width': '400px'}">
                            <div class="panel panel-primary">
                                <div class="panel-body" [id]="viewIdGeneratorService.makeStandardId(typeControl.SYSTEM_STATUS_MESSAGE_BODY)">
                                    {{currentStatusMessage}}
                                    <!--<status-display [messages$]="messages$"></status-display>-->
                                </div> <!-- panel body -->

                            </div> <!-- panel primary -->
                        </p-dialog>

                        <div class="col-md-4"> <!-- outer grid column 2: Criteria summary -->

                            <div class="well">
                                <table>
                                    <tr>
                                        <td colspan="2">
                                            <export-format
                                                    [fileFormat$]="selectedExtractFormat$"
                                                    [gobiiExtractFilterType]="gobiiExtractFilterType">
                                            </export-format>
                                        </td>
                                        <td style="vertical-align: top;">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <name-id-list-box
                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                    [filterParamName]="nameIdFilterParamTypes.MAPSETS">
                                            </name-id-list-box>
                                        </td>
                                    </tr>
                                </table>

                            </div>

                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Extraction Criteria</h3>
                                </div>
                                <div class="panel-body">
                                    <status-display-tree
                                            [fileItemEventChange]="treeFileItemEvent"
                                            [gobiiExtractFilterTypeEvent]="gobiiExtractFilterType"
                                            (onAddMessage)="handleHeaderStatusMessage($event)">
                                    </status-display-tree>

                                    <BR>

                                    <button type="submit"
                                            [class]="submitButtonStyle"
                                            [disabled]="submitButtonStyle === buttonStyleSubmitNotReady"
                                            (mouseenter)="handleOnMouseOverSubmit($event,true)"
                                            (mouseleave)="handleOnMouseOverSubmit($event,false)"
                                            (click)="handleExtractSubmission()"
                                            [id]="viewIdGeneratorService.makeStandardId(typeControl.SUBMIT_BUTTON_EXTRACT)">Submit
                                    </button>

                                    <button type="clear"
                                            [class]="clearButtonStyle"
                                            (click)="handleClearTree()">Clear
                                    </button>

                                </div> <!-- panel body -->
                            </div> <!-- panel primary -->

                        </div>  <!-- outer grid column 2: Criteria summary -->


                    </div> <!-- .row of outer grid -->

                </div>
            </div> <!-- panel primary -->
        </div> <!-- panel-default  -->` // end template
                }) // @Component
                ,
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    authentication_service_1.AuthenticationService,
                    dto_request_service_1.DtoRequestService,
                    store_1.Store,
                    file_item_service_1.FileItemService,
                    instruction_submission_service_1.InstructionSubmissionService,
                    core_1.ChangeDetectorRef,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], ExtractorRoot);
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map