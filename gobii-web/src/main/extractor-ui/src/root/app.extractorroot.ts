////<reference path="../../../../../../typings/index.d.ts"/>
import {Component, OnInit, ChangeDetectorRef, ViewChild} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ServerConfig} from "../model/server-config";
import {EntitySubType, EntityType} from "../model/type-entity";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";
import {ExtractorItemType} from "../model/type-extractor-item";
import {GobiiExtractFormat} from "../model/type-extract-format";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {FileName} from "../model/file_name";
import {Contact} from "../model/contact";
import {ContactSearchType, DtoRequestItemContact} from "../services/app/dto-request-item-contact";
import {AuthenticationService} from "../services/core/authentication.service";
import {NameIdLabelType} from "../model/name-id-label-type";
import {StatusLevel} from "../model/type-status-level";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileItemAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {FilterParamNames} from "../model/file-item-param-names";
import {FileItemService} from "../services/core/file-item-service";
import {Observable} from "rxjs/Observable";
import {InstructionSubmissionService} from "../services/core/instruction-submission-service";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {ViewIdGeneratorService} from "../services/core/view-id-generator-service";
import {TypeControl} from "../services/core/type-control";
import { KeycloakService } from 'keycloak-angular';
import { DtoRequestItemCrops } from 'src/services/app/dto-request-item-crops';
import { Crop } from '../model/crop';
import { DtoRequestService2 } from '../services/core/dto-request.service2';
import { throwIfEmpty } from 'rxjs/operators';
import { ReplaceByItemIdAction } from '../store/actions/fileitem-action';

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports

@Component({
    selector: 'extractor-root',
    templateUrl: 'app.extractorroot.html'
}) 

export class ExtractorRoot implements OnInit {
    title = 'Gobii Web';


    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    // ************************************************************************

    //


    nameIdFilterParamTypes: any = Object.assign({}, FilterParamNames);
    public typeControl:any = TypeControl;

    //public selectedExtractFormat$: any = this.store.select(fromRoot.getSelectedFileFormat);
    public selectedExtractFormat$:Observable<GobiiFileItem> = this.store.select(fromRoot.getSelectedFileFormat);


    //public messages$: any = this.store.select(fromRoot.getStatusMessages);
    public messages$:Observable<string[]> = this.store.select(fromRoot.getStatusMessages);


    // ************************************************************************

    public treeFileItemEvent: GobiiFileItem;
    public gobiiExtractFilterTypeForExpressions: any = GobiiExtractFilterType;
    public loggedInUser: string = null;


    @ViewChild('piSelector') piSelector;
    @ViewChild('projSelector') projSelector;
    @ViewChild('expSelector') expSelector;
    @ViewChild('typeSelector') typeSelector;
    @ViewChild('platSelector') platSelector;

    constructor(private _dtoRequestServiceContact: DtoRequestService<Contact>,
                private _authenticationService: AuthenticationService,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>,
                private _dtoRequestServiceCrops: DtoRequestService2<Crop[]>,
                private store: Store<fromRoot.State>,
                private fileItemService: FileItemService,
                private instructionSubmissionService: InstructionSubmissionService,
                private changeDetectorRef: ChangeDetectorRef,
                public viewIdGeneratorService: ViewIdGeneratorService,
                //private _keycloakService: KeycloakService
    ) {

        this.messages$.subscribe(
            messages => {
                if (messages.length > 0) {

                    this.currentStatusMessage = messages[0];
                    this.showMessagesDialog();
                }
            }
        );

        if (this._authenticationService.isProfileLoaded() && !this._authenticationService.getUserEmail()) {
            this.displayEmailUpdateDialog = true;
        }

    }

    public display: boolean = false;
    public currentStatusMessage: string = null;

    public displayEmailUpdateDialog: boolean = false;

    showMessagesDialog() {
        this.display = true;

        //workaround for error when using observable in a
        //p-dialog; see https://github.com/angular/angular/issues/17572
        this.changeDetectorRef.detectChanges();
    }

    public onHideMessageDialog($event) {
        this.handleClearMessages();
    }

    // ****************************************************************
    // ********************************************** SERVER SELECTION
    public selectedServerConfig: ServerConfig;
    public serverConfigList: ServerConfig[];
    public currentStatus: string;

    private initializeCropType() {
        let scope$ = this;
        this._dtoRequestServiceCrops.get(new DtoRequestItemCrops()).subscribe(crops => {
            //
            if (crops) {
                this._authenticationService.setGobiiCropType(crops[0].cropType);
                this.fileItemService.loadCrops(this.gobiiExtractFilterType, crops, 0);
                
                scope$.initializeServerConfigs();   
            }
        })
    }

    private initializeServerConfigs() {
        let scope$ = this;

        this._dtoRequestServiceServerConfigs.get(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {
                if (serverConfigs && (serverConfigs.length > 0)) {
                
                    scope$.serverConfigList = serverConfigs;

                    let serverCrop: String =
                        this._dtoRequestServiceServerConfigs.getGobiiCropType();

                    let gobiiVersion: string = this._dtoRequestServiceServerConfigs.getGobbiiVersion();

                    scope$.currentStatus = "GOBII Server " + gobiiVersion;

                    scope$.selectedServerConfig =
                        scope$.serverConfigList
                            .filter(c => {
                                    return c.crop === serverCrop;
                                }
                            )[0];
                    this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);
//                    scope$.initializeSubmissionContact();
                    
                    //scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0, "")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving server configs: "
                    + m.message))
            }
        )
        ;
    } // initializeServerConfigs()

    private initializeSubmissionContact() {

        this.loggedInUser = this._authenticationService.getUserName();
        let scope$ = this;
        scope$._dtoRequestServiceContact.get(new DtoRequestItemContact(
            ContactSearchType.BY_USERNAME,
            this.loggedInUser)).subscribe(contact => {

                if (contact && contact.contactId && contact.contactId > 0) {

                    //loggedInUser
                    this.fileItemService.loadFileItem(GobiiFileItem.build(scope$.gobiiExtractFilterType, ProcessType.CREATE)
                            .setEntityType(EntityType.CONTACT)
                            .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                            .setCvFilterType(CvFilterType.UNKNOWN)
                            .setExtractorItemType(ExtractorItemType.ENTITY)
                            .setItemName(contact.email)
                            .setItemId(contact.contactId.toLocaleString()),
                        true);

                } else {
                    scope$.handleAddMessage("There is no contact associated with user " + this.loggedInUser);
                }

            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving contacts for submission: "
                    + m.message))
            });

        //   _dtoRequestServiceContact
    }

    public handleServerSelected(arg) {
        this.selectedServerConfig = arg;
        let currentPath = window.location.pathname;
        let currentPage: string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
        let newDestination = "http://"
            + this.selectedServerConfig.domain
            + ":"
            + this.selectedServerConfig.port
            + this.selectedServerConfig.contextRoot
            + currentPage;

        window.location.href = newDestination;
    } // handleServerSelected()


// ********************************************************************
// ********************************************** EXPORT TYPE SELECTION AND FLAGS


    public gobiiExtractFilterType: GobiiExtractFilterType;

    private refreshJobId() {

        let jobId: string = FileName.makeUniqueFileId();
        this.fileItemService.replaceFileItemByCompoundId(
            GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.JOB_ID)
                .setItemId(jobId)
                .setItemName(jobId)
                .setIsExtractCriterion(true))
    }

    private refreshSelectedCropDisplay() {
        this.fileItemService.replaceFileItemByCompoundId(
            GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.CROP_TYPE)
                .setItemId(this._authenticationService.getGobiiCropType())
                .setItemName(this._authenticationService.getGobiiCropType())
                .setIsExtractCriterion(true))
    }


    public handleTabPanelChange(event) {

        let tabIndex: number = event.index
        if (tabIndex === 0) { // By Dataset
            this.gobiiExtractFilterType = GobiiExtractFilterType.WHOLE_DATASET;
        } else if (tabIndex == 1) {

            this.gobiiExtractFilterType = GobiiExtractFilterType.BY_SAMPLE;

        } else if (tabIndex == 2) {

            this.gobiiExtractFilterType = GobiiExtractFilterType.BY_MARKER;

        }

        this.handleExportTypeSelected(this.gobiiExtractFilterType);

    }// handleTabPanelChange

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {


            this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
            this.store.dispatch(new fileItemAction.SetExtractType({gobiiExtractFilterType: arg}));

            this.gobiiExtractFilterType = arg;

            this.instructionSubmissionService.submitReady(this.gobiiExtractFilterType)
                .subscribe(submitReady => {
                    submitReady ? this.submitButtonStyle = this.buttonStyleSubmitReady : this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                })

            this.refreshSelectedCropDisplay();
            this.refreshJobId();

            if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CONTACT_PI_FILTER_OPTIONAL,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.PROJECT_FILTER_OPTIONAL,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.EXPERIMENT_FILTER_OPTIONAL,
                    null);

                this.fileItemService.loadFilter(this.gobiiExtractFilterType, FilterParamNames.DATASET_FILTER_OPTIONAL, null);

            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CONTACT_PI_HIERARCHY_ROOT,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.PROJECTS_BY_CONTACT,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CV_DATATYPE,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.PLATFORMS,
                    null);


            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CV_DATATYPE,
                    null);

                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.PLATFORMS,
                    null);

            }


            this.initializeSubmissionContact();

            this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.MAPSETS,
                null);


            //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
            let formatItem: GobiiFileItem = GobiiFileItem
                .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
                .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
                .setItemId(GobiiExtractFormat[GobiiExtractFormat.HAPMAP])
                .setItemName(GobiiExtractFormat[GobiiExtractFormat[GobiiExtractFormat.HAPMAP]]);
            this.fileItemService.replaceFileItemByCompoundId(formatItem);


            this.fileItemService
                .replaceFileItemByCompoundId(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                    .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                    .setItemName(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME])
                    .setItemId(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME]));

    }


    public handleAddMessage(arg) {
        this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(arg, StatusLevel.OK, undefined)))
    }

    public handleClearMessages() {
        this.store.dispatch(new historyAction.ClearStatusesAction())
    }

    public handleHeaderStatusMessage(statusMessage: HeaderStatusMessage) {

        if (!statusMessage.statusLevel || statusMessage.statusLevel != StatusLevel.WARNING) {
            this.handleAddMessage(statusMessage.message);
        } else {
            console.log(statusMessage.message);
        }
    }


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    // ********************************************************************
    // ********************************************** Extract file submission
    public submitButtonStyleDefault = "btn btn-primary";
    public buttonStyleSubmitReady = "btn btn-success";
    public buttonStyleSubmitNotReady = "btn btn-warning";
    public submitButtonStyle = this.buttonStyleSubmitNotReady;
    public clearButtonStyle = this.submitButtonStyleDefault;


    public handleOnMouseOverSubmit(arg, isEnter) {

        // this.criteriaInvalid = true;
        if (isEnter) {
            this.instructionSubmissionService.markMissingItems(this.gobiiExtractFilterType)
        } else {
            this.instructionSubmissionService.unmarkMissingItems(this.gobiiExtractFilterType)
        }

    }

    public handleResetCrop(crop) {
        this._authenticationService.setGobiiCropType(crop);
        this.handleClearTree();
    }

    public handleClearTree() {

        this.handleExportTypeSelected(this.gobiiExtractFilterType);
        this.clearSelections();

    }


    public clearSelections() {
        [this.piSelector,
         this.projSelector,
         this.expSelector,
         this.typeSelector,
         this.platSelector
        ].forEach(
            selector => {
                if (selector) selector.clearSelection();
            }
        )
    }

    public handleExtractSubmission() {

        this.instructionSubmissionService
            .submit(this.gobiiExtractFilterType)
            .subscribe(instructions => {
                this.refreshJobId();
                this.handleClearTree();
            });
    }

    ngOnInit(): any {
        this._authenticationService.loadUserProfile();
        this.initializeCropType();
        //this.initializeServerConfigs();
    } // ngOnInit()


}

