///<reference path="../../../../../../typings/index.d.ts"/>
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {Component} from "@angular/core";
import {HttpModule} from "@angular/http";
import {ExportTypeComponent} from "../views/export-type.component";
import {ExportFormatComponent} from "../views/export-format.component";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {ContactsListBoxComponent} from "../views/contacts-list-box.component";
import {DatasetTypeListBoxComponent} from "../views/dataset-types-list-box.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
import {ExperimentListBoxComponent} from "../views/experiment-list-box.component";
import {DataSetCheckListBoxComponent} from "../views/dataset-checklist-box.component";
import {MapsetsListBoxComponent} from "../views/mapsets-list-box.component";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {CriteriaDisplayComponent} from "../views/criteria-display.component";
import {StatusDisplayComponent} from "../views/status-display-box.component";
import {ProcessType} from "../model/type-process";
import {CheckBoxEvent} from "../model/event-checkbox";
import {ServerConfig} from "../model/server-config";
import {EntityType} from "../model/type-entity";
import {CropsListBoxComponent} from "../views/crops-list-box.component";
import {UsersListBoxComponent} from "../views/users-list-box.component";
import {NameId} from "../model/name-id";
import {GobiiFileType} from "../model/type-gobii-file";
import {ExtractorInstructionFilesDTO} from "../model/extractor-instructions/dto-extractor-instruction-files";
import {GobiiExtractorInstruction} from "../model/extractor-instructions/gobii-extractor-instruction";
import {DtoRequestItemExtractorSubmission} from "../services/app/dto-request-item-extractor-submission";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import * as EntityFilters from "../model/type-entity-filter";
import {EntityFilter} from "../model/type-entity-filter";
import {CheckListBoxComponent} from "../views/checklist-box.component";
import {SampleMarkerBoxComponent} from "../views/sample-marker-box.component";
import {FileDropDirective, FileSelectDirective} from "ng2-file-upload";
import {SampleMarkerList} from "../model/sample-marker-list";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    styleUrls: ['/extractor-ui.css'],
    template: `<div class = "panel panel-default">
        
           <div class = "panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>

                <fieldset class="well the-fieldset">
                    <div class="col-md-2">
                        <crops-list-box
                            [serverConfigList]="serverConfigList"
                            [selectedServerConfig]="selectedServerConfig"
                            (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                    </div>
                    
                    <div class="col-md-3">
                       <export-type
                        (onExportTypeSelected)="handleExportTypeSelected($event)"></export-type>
                     </div>
                     
                </fieldset>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                    
                    <!--
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Submit As</legend>
                        <users-list-box
                            [nameIdList]="contactNameIdListForSubmitter"
                            (onUserSelected)="handleContactForSubmissionSelected($event)">
                        </users-list-box>
                        </fieldset>
                        -->
                        
                     <fieldset class="well the-fieldset">
                        <legend class="the-legend">Filters</legend><BR>
                        
                        
                        <div *ngIf="displaySelectorPi">
                            <label class="the-label">Principle Investigator:</label><BR>
                            <contacts-list-box [nameIdList]="contactNameIdListForPi" (onContactSelected)="handleContactForPiSelected($event)"></contacts-list-box>
                        </div>
                        
                        <div *ngIf="displaySelectorProject">
                            <BR>
                            <BR>
                            <label class="the-label">Project:</label><BR>
                            <project-list-box [primaryInvestigatorId] = "selectedContactIdForPi"
                                [nameIdList]="projectNameIdList"
                                [nameIdListPIs]="contactNameIdListForPi"
                                (onProjectSelected)="handleProjectSelected($event)"
                                (onAddMessage)="handleAddMessage($event)"></project-list-box>
                        </div>

                        <div *ngIf="displaySelectorDataType">
                            <BR>
                            <BR>
                            <label class="the-label">Dataset Types:</label><BR>
                            <dataset-types-list-box [nameIdList]="datasetTypeNameIdList" (onDatasetTypeSelected)="handleDatasetTypeSelected($event)"></dataset-types-list-box>
                        </div>

                        
                        <div *ngIf="displaySelectorExperiment">
                            <BR>
                            <BR>
                            <label class="the-label">Experiment:</label><BR>
                            <experiment-list-box [projectId] = "selectedProjectId"
                                [nameIdList] = "experimentNameIdList"
                                (onExperimentSelected)="handleExperimentSelected($event)"
                                (onAddMessage)="handleAddMessage($event)"></experiment-list-box>
                        </div>

                        <div *ngIf="displaySelectorPlatform">
                            <BR>
                            <BR>
                            <label class="the-label">Platforms:</label><BR>
                            <checklist-box
                                [checkBoxEventChange] = "platformCheckBoxEventChange"
                                [nameIdList] = "platformsNameIdList"
                                (onItemSelected)="handlePlatformSelected($event)"
                                (onItemChecked)="handlePlatformChecked($event)"
                                (onAddMessage) = "handleAddMessage($event)">
                            </checklist-box>
                         </div>


                        <div *ngIf="displayAvailableDatasets">
                            <BR>
                            <BR>
                            <label class="the-label">Data Sets</label><BR>
                            <dataset-checklist-box
                                [checkBoxEventChange] = "datasetCheckBoxEventChange"
                                [experimentId] = "selectedExperimentId" 
                                (onItemChecked)="handleCheckedDataSetItem($event)"
                                (onAddMessage) = "handleAddMessage($event)">
                            </dataset-checklist-box>
                        </div>
                    </fieldset>
                       
                       
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 
                        <div *ngIf="displayIncludedDatasetsGrid">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Datasets</legend>
                                <criteria-display 
                                    [dataSetCheckBoxEvents] = "datasetCheckboxEvents"
                                    (onItemUnChecked) = "handleExtractDataSetUnchecked($event)"></criteria-display>
                            </fieldset>
                        </div>
                        
                        <div *ngIf="displaySampleListTypeSelector">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Samples</legend>
                                <sample-list-type
                                    (onSampleListTypeSelected)="handleSampleListTypeSelected($event)">
                                 </sample-list-type>
                                <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                                <sample-marker-box 
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>
                        
                        <div *ngIf="displaySampleMarkerBox">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Markers</legend>
                                <sample-marker-box 
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>
                        
                    </div>  <!-- outer grid column 2-->
                    
                    
                    <div class="col-md-4">
                         
                            
                    <form>
			           <fieldset class="well the-fieldset">
                			<legend class="the-legend">Export</legend>
			           
                            <export-format (onFormatSelected)="handleFormatSelected($event)"></export-format>
                            <BR>
                       
                            <mapsets-list-box [nameIdList]="mapsetNameIdList" 
                                (onMapsetSelected)="handleMapsetSelected($event)"></mapsets-list-box>
                            <BR>
                            <BR>
                   
                            <input type="button" 
                            value="Submit"
                             [disabled]="(gobiiDatasetExtracts.length === 0)"
                            (click)="handleExtractSubmission()" >
            			</fieldset>
                    </form>
                            
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Status: {{currentStatus}}</legend>
                                <status-display-tree
                                    [checkBoxEventChange] = "treeCheckboxEvent">
                                </status-display-tree>
                            </fieldset>
                                   
                    </div>  <!-- outer grid column 3 (inner grid)-->
                                        
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div>` // end template
}) // @Component

export class ExtractorRoot {
    title = 'Gobii Web';


    private treeCheckboxEvent:CheckBoxEvent;
    private datasetCheckboxEvents: CheckBoxEvent[] = [];
    private gobiiDatasetExtracts: GobiiDataSetExtract[] = [];
    private messages: string[] = [];


    constructor(private _dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private _dtoRequestServiceNameIds: DtoRequestService<NameId[]>,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>) {

    }

    // ****************************************************************
    // ********************************************** SERVER SELECTION
    private selectedServerConfig: ServerConfig;
    private serverConfigList: ServerConfig[];
    private currentStatus: string;

    private initializeServerConfigs() {
        let scope$ = this;
        this._dtoRequestServiceServerConfigs.get(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {

                if (serverConfigs && ( serverConfigs.length > 0 )) {
                    scope$.serverConfigList = serverConfigs;

                    let serverCrop: String =
                        this._dtoRequestServiceServerConfigs.getGobiiCropType();

                    let gobiiVersion: string = this._dtoRequestServiceServerConfigs.getGobbiiVersion();

                    scope$.selectedServerConfig =
                        scope$.serverConfigList
                            .filter(c => {
                                    return c.crop === serverCrop;
                                }
                            )[0];

                    scope$.currentStatus = "GOBII Server " + gobiiVersion;
                    scope$.messages.push("Connected to database: " + scope$.selectedServerConfig.crop);
                    scope$.initializeContactsForSumission();
                    scope$.initializeContactsForPi();
                    scope$.initializeMapsetsForSumission();

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving server configs: "
                    + m.message))
            }
        )
        ;
    } // initializeServerConfigs()

    private handleServerSelected(arg) {
        this.selectedServerConfig = arg;
        // this._dtoRequestServiceNameIds
        //     .setCropType(GobiiCropType[this.selectedServerConfig.crop]);
        let currentPath = window.location.pathname;
        let currentPage: string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
        let newDestination = "http://"
            + this.selectedServerConfig.domain
            + ":"
            + this.selectedServerConfig.port
            + this.selectedServerConfig.contextRoot
            + currentPage;
//        console.log(newDestination);
        window.location.href = newDestination;
    } // handleServerSelected()


// ********************************************************************
// ********************************************** EXPORT TYPE SELECTION AND FLAGS


    private displayAvailableDatasets: boolean = true;
    private displaySelectorPi: boolean = true;
    private displaySelectorProject: boolean = true;
    private displaySelectorExperiment: boolean = true;
    private displaySelectorDataType: boolean = false;
    private displaySelectorPlatform: boolean = false;
    private displayIncludedDatasetsGrid: boolean = true;
    private displaySampleListTypeSelector: boolean = false;
    private displaySampleMarkerBox: boolean = false;


    private selectedExportType: GobiiExtractFilterType;

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {
        this.selectedExportType = arg;

        if (this.selectedExportType === GobiiExtractFilterType.WHOLE_DATASET) {

            this.displaySelectorPi = true;
            this.displaySelectorProject = true;
            this.displaySelectorExperiment = true;
            this.displayAvailableDatasets = true;
            this.displayIncludedDatasetsGrid = true;

            this.displaySelectorDataType = false;
            this.displaySelectorPlatform = false;
            this.displaySampleListTypeSelector = false;
            this.displaySampleMarkerBox = false;


        } else if (this.selectedExportType === GobiiExtractFilterType.BY_SAMPLE) {

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


        } else if (this.selectedExportType === GobiiExtractFilterType.BY_MARKER) {

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
    }

// ********************************************************************
// ********************************************** SUBMISSION-USER SELECTION
    private contactNameIdListForSubmitter: NameId[];
    private selectedContactIdForSubmitter: string;

    private handleContactForSubmissionSelected(arg) {
        this.selectedContactIdForSubmitter = arg;
    }

    private initializeContactsForSumission() {
        let scope$ = this;
        this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Contacts)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForSubmitter = nameIds
                    scope$.selectedContactIdForSubmitter = nameIds[0].id;
                } else {
                    scope$.contactNameIdListForSubmitter = [new NameId(0, "ERROR NO USERS")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving contacts: "
                    + m.message))
            });

    }


// ********************************************************************
// ********************************************** PI USER SELECTION
    private contactNameIdListForPi: NameId[];
    private selectedContactIdForPi: string;

    private handleContactForPiSelected(arg) {
        this.selectedContactIdForPi = arg;
        this.initializeProjectNameIds();
        //console.log("selected contact id:" + arg);
    }

    private initializeContactsForPi() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Contacts,
            EntityFilter.NONE)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.contactNameIdListForPi = nameIds;
                    scope$.selectedContactIdForPi = scope$.contactNameIdListForPi[0].id;
                } else {
                    scope$.contactNameIdListForPi = [new NameId(0, "ERROR NO USERS")];
                }

                scope$.initializeProjectNameIds();

            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving contacts for PIs: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** HAPMAP SELECTION
    private selectedFormatName: string = "Hapmap";

    private handleFormatSelected(arg) {
        this.selectedFormatName = arg;
        //console.log("selected contact id:" + arg);
    }

// ********************************************************************
// ********************************************** PROJECT ID
    private projectNameIdList: NameId[];
    private selectedProjectId: string;

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        this.displayExperimentDetail = false;
        this.displayDataSetDetail = false;
        this.initializeExperimentNameIds();
    }

    private initializeProjectNameIds() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Projects,
            EntityFilter.BYTYPEID,
            this.selectedContactIdForPi)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.projectNameIdList = nameIds;
                    scope$.selectedProjectId = nameIds[0].id;
                } else {
                    scope$.projectNameIdList = [new NameId(0, "<none>")];
                    scope$.selectedProjectId = undefined;
                }

                this.initializeExperimentNameIds();
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retriving project names: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** EXPERIMENT ID
    private displayExperimentDetail: boolean = false;

    private experimentNameIdList: NameId[];
    private selectedExperimentId: string;
    private selectedExperimentDetailId: string;

    private handleExperimentSelected(arg) {
        this.selectedExperimentId = arg;
        this.selectedExperimentDetailId = arg;
        this.displayExperimentDetail = true;

        //console.log("selected contact id:" + arg);
    }

    private initializeExperimentNameIds() {

        let scope$ = this;
        if (this.selectedProjectId) {
            this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
                EntityType.Experiments,
                EntityFilter.BYTYPEID,
                this.selectedProjectId)).subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {
                        scope$.experimentNameIdList = nameIds;
                        scope$.selectedExperimentId = scope$.experimentNameIdList[0].id;
                    } else {
                        scope$.experimentNameIdList = [new NameId(0, "<none>")];
                        scope$.selectedExperimentId = undefined;
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retreving experiment names: "
                        + m.message))
                });
        } else {
            scope$.experimentNameIdList = [new NameId(0, "<none>")];
            scope$.selectedExperimentId = undefined;
        }

    }

// ********************************************************************
// ********************************************** DATASET TYPE SELECTION
    private datasetTypeNameIdList: NameId[];
    private selectedDatasetTypeId: string;

    private handleDatasetTypeSelected(arg) {
        this.selectedDatasetTypeId = arg;
    }

    private initializeDatasetTypes() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.CvTerms,
            EntityFilter.BYTYPENAME,
            "dataset_type")).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.datasetTypeNameIdList = nameIds;
                    scope$.selectedDatasetTypeId = scope$.datasetTypeNameIdList[0].id;
                } else {
                    scope$.datasetTypeNameIdList = [new NameId(0, "ERROR NO DATASET TYPES")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving DatasetTypes: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** PLATFORM SELECTION
    private platformsNameIdList: NameId[];
    private selectedPlatformId: string;
    private checkedPlatformId: string;

    private handlePlatformSelected(arg) {
        this.selectedPlatformId = arg.id;
    }

    private handlePlatformChecked(arg) {
        this.checkedPlatformId = arg.id;
    }

    private platformCheckBoxEventChange: CheckBoxEvent;


    private initializePlatforms() {
        let scope$ = this;
        scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Platforms,
            EntityFilter.NONE)).subscribe(nameIds => {

                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.platformsNameIdList = nameIds;
                    scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
                } else {
                    scope$.platformsNameIdList = [new NameId(0, "ERROR NO PLATFORMS")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving PlatformTypes: "
                    + m.message))
            });
    }

// ********************************************************************
// ********************************************** DATASET ID
    private displayDataSetDetail: boolean = false;
    private dataSetIdToUncheck: number;

    private handleAddMessage(arg) {
        this.messages.push(arg);
    }


    private makeDatasetExtract() {

        this.gobiiDatasetExtracts.push(new GobiiDataSetExtract(GobiiFileType.GENERIC,
            false,
            Number(this.selectedDatasetId),
            this.selectedDatasetName,
            null,
            this.selectedExportType,
            this.markerList,
            this.sampleList,
            this.uploadFileName,
            this.selectedSampleListType,
            null,
            null));

    }


    private selectedDatasetId: string;
    private selectedDatasetName: string;

    private handleCheckedDataSetItem(arg: CheckBoxEvent) {


        this.selectedDatasetId = arg.id;

        if (ProcessType.CREATE == arg.processType) {

            this.makeDatasetExtract();

        } else {

            let indexOfEventToRemove: number = this.datasetCheckboxEvents.indexOf(arg);
            this.datasetCheckboxEvents.splice(indexOfEventToRemove, 1);

            this.gobiiDatasetExtracts =
                this.gobiiDatasetExtracts
                    .filter((item: GobiiDataSetExtract) => {
                        return item.getdataSetId() != Number(arg.id)
                    });
        } // if-else we're adding

        this.treeCheckboxEvent = CheckBoxEvent.newCheckboxEvent(arg);

    }

    private datasetCheckBoxEventChange: CheckBoxEvent;
    private changeTrigger: number = 0;

    private handleExtractDataSetUnchecked(arg: CheckBoxEvent) {
        // this.changeTrigger++;
        // this.dataSetIdToUncheck = Number(arg.id);



        this.datasetCheckboxEvents.push(arg);
        let dataSetExtractsToRemove: GobiiDataSetExtract[] = this.gobiiDatasetExtracts
            .filter(e => {
                return e.getdataSetId() === Number(arg.id)
            });

        if (dataSetExtractsToRemove.length > 0) {
            let idxToRemove = this.gobiiDatasetExtracts.indexOf(dataSetExtractsToRemove[0]);

            this.gobiiDatasetExtracts.splice(idxToRemove, 1);
        }

        this.datasetCheckBoxEventChange = arg;
        this.treeCheckboxEvent = CheckBoxEvent.newCheckboxEvent(arg);

    }


// ********************************************************************
// ********************************************** MAPSET SELECTION
    private mapsetNameIdList: NameId[];
    private selectedMapsetId: string;
    private nullMapsetName: string;

    private handleMapsetSelected(arg) {

        if (arg > 0) {
            this.selectedMapsetId = arg;
        } else {
            this.selectedMapsetId = undefined;
        }
    }

    private initializeMapsetsForSumission() {
        let scope$ = this;
        scope$.nullMapsetName = "<none>"
        this._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
            EntityType.Mapsets)).subscribe(nameIds => {

                scope$.mapsetNameIdList = [new NameId(0, scope$.nullMapsetName)]
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.mapsetNameIdList = scope$.mapsetNameIdList.concat(nameIds);
                    scope$.selectedMapsetId = nameIds[0].id;
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving mapsets: "
                    + m.message))
            });

    }


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    private markerList: string[] = null;
    private sampleList: string[] = null;
    private uploadFileName: string = null;

    private handleSampleMarkerListComplete(arg: SampleMarkerList) {

        let sampleMarkerList: SampleMarkerList = arg;


        if (sampleMarkerList.isArray) {
            if (this.selectedExportType === GobiiExtractFilterType.BY_SAMPLE) {
                this.sampleList = sampleMarkerList.items;

            } else if (this.selectedExportType === GobiiExtractFilterType.BY_MARKER) {
                this.markerList = sampleMarkerList.items;
            }
        } else {
            this.uploadFileName = sampleMarkerList.uploadFileName;
        }

        this.makeDatasetExtract();
    }


    // ********************************************************************
    // ********************************************** Sample List Type Selection
    private selectedSampleListType: GobiiSampleListType;

    private handleSampleListTypeSelected(arg: GobiiSampleListType) {
        this.selectedSampleListType = arg;
    }

    // ********************************************************************
    // ********************************************** Extract file submission
    private handleExtractSubmission() {

        let scope$ = this;
        let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];

        let gobiiFileType: GobiiFileType = GobiiFileType[this.selectedFormatName.toUpperCase()];
        this.gobiiDatasetExtracts.forEach(e => e.setgobiiFileType(gobiiFileType));

        let mapsetIds: number[] = [];

        if ((scope$.selectedMapsetId !== undefined)) {
            mapsetIds.push(Number(scope$.selectedMapsetId));
        }


        gobiiExtractorInstructions.push(
            new GobiiExtractorInstruction(
                this.gobiiDatasetExtracts,
                Number(this.selectedContactIdForSubmitter),
                null,
                mapsetIds)
        );


        let date: Date = new Date();
        let fileName: string = "extractor_"
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
        let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
            new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                fileName);
//this.selectedServerConfig.crop

        let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;

        this._dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
            .subscribe(extractorInstructionFilesDTO => {
                    extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                    scope$.messages.push("Extractor instruction file created on server: "
                        + extractorInstructionFilesDTOResponse.getInstructionFileName());
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Submitting extractor instructions: "
                        + m.message))
                });

    }

    ngOnInit(): any {

        this.initializeServerConfigs();

    }


}

