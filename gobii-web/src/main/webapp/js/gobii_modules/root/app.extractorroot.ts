///<reference path="../../../../../../typings/index.d.ts"/>

import {Component} from "@angular/core";
import {HTTP_PROVIDERS} from "@angular/http";
import {ExportFormatComponent} from "../views/export-format.component";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {ContactsListBoxComponent} from "../views/contacts-list-box.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
import {ExperimentListBoxComponent} from "../views/experiment-list-box.component";
import {DataSetCheckListBoxComponent} from "../views/dataset-checklist-box.component";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract"
import {CriteriaDisplayComponent} from "../views/criteria-display.component";
import {ProcessType} from "../model/type-process";
import {CheckBoxEvent} from "../model/event-checkbox";
import {ServerConfig} from "../model/server-config"
import {CropsListBoxComponent} from "../views/crops-list-box.component"
import {UsersListBoxComponent} from "../views/users-list-box.component"
import {NameId} from "../model/name-id";
import {DatasetDetailBoxComponent} from "../views/dataset-detail.component"
import {DataSet} from "../model/dataset"
// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    directives: [ExportFormatComponent,
        ContactsListBoxComponent,
        ProjectListBoxComponent,
        ExperimentListBoxComponent,
        DataSetCheckListBoxComponent,
        CriteriaDisplayComponent,
        CropsListBoxComponent,
        UsersListBoxComponent,
        DatasetDetailBoxComponent],
    styleUrls: ['/extractor-ui.css'],
    providers: [
        HTTP_PROVIDERS,
        AuthenticationService,
        DtoRequestService
    ],
    template: `
        <div class = "panel panel-default">
        
           <div class = "panel-heading">
              <h1 class = "panel-title">GOBii Extractor</h1>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Crop</legend>
                        <crops-list-box (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Submit As</legend>
                        <users-list-box (onUserSelected)="handleUserSelected($event)"></users-list-box>
                        </fieldset>
                        
                        <div class="col-md-12">
                            <export-format></export-format>
                        </div>

                        
                        
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Principle Investigator</legend>
                        <contacts-list-box (onContactSelected)="handleContactSelected($event)"></contacts-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Projects</legend>
                        <project-list-box [primaryInvestigatorId] = "selectedContactId" (onProjectSelected)="handleProjectSelected($event)" ></project-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Experiments</legend>
                        <experiment-list-box [projectId] = "selectedProjectId" (onExperimentSelected)="handleExperimentSelected($event)"></experiment-list-box>
                        </fieldset>
                        
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Data Sets</legend>
                        <dataset-checklist-box [experimentId] = "selectedExperimentId" 
                            (onItemChecked)="handleCheckedDataSetItem($event)"
                            (onItemSelected)="handleDataSetSelected($event)">
                        </dataset-checklist-box>
                        </fieldset>
                        
                    </div>  <!-- outer grid column 2-->
                    <div class="col-md-4">
                     
                        <fieldset class="well the-fieldset" style="vertical-align: top;">
                        <legend class="the-legend">Detail</legend>
                        <dataset-detail-box [dataSetId] = "selectedDataSetId"></dataset-detail-box>
                        </fieldset>
                     
                           
                        <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                        <legend class="the-legend">Extract Critiera</legend>
                        <criteria-display [gobiiDatasetExtracts] = "gobiiDatasetExtracts"></criteria-display>
                        </fieldset>
       
        
                         
                    </div>  <!-- outer grid column 3 (inner grid)-->
                                        
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div> 
	` // end template
}) // @Component

export class ExtractorRoot {
    title = 'Gobii Web';


    private gobiiDatasetExtracts:GobiiDataSetExtract[] = [];


    constructor() {
        let foo = "foo";
    }

    private selectedContactId:string = "1";

    private handleContactSelected(arg) {
        this.selectedContactId = arg;
        //console.log("selected contact id:" + arg);
    }

    private selectedProjectId:string = "0";

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        //console.log("selected contact id:" + arg);
    }

    private selectedExperimentId:string = "0";

    private handleExperimentSelected(arg) {
        this.selectedExperimentId = arg;
        //console.log("selected contact id:" + arg);
    }

    private selectedServerConfig:ServerConfig;

    private handleServerSelected(arg) {
        this.selectedServerConfig = arg;
        let currentPath = window.location.pathname;
        let currentPage:string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
        let newDestination = "http://"
            + this.selectedServerConfig.domain
            + ":"
            + this.selectedServerConfig.port
            + this.selectedServerConfig.contextRoot
            + currentPage;
//        console.log(newDestination);
        window.location.href = newDestination;
    } // handleServerSelected()

    private selectedUser:NameId
    private handleUserSelected(arg) {

    }

    private handleCheckedDataSetItem(arg:CheckBoxEvent) {
        if (ProcessType.CREATE == arg.processType) {
            this.gobiiDatasetExtracts.push(new GobiiDataSetExtract(Number(arg.id), arg.name));
        } else {

            this.gobiiDatasetExtracts =
                this.gobiiDatasetExtracts
                    .filter((item:GobiiDataSetExtract) => {
                        return item.getDataSetId() != Number(arg.id)
                    });
        } // if-else we're adding
    }

    private selectedDataSetId:number;
    private handleDataSetSelected(arg) {
        this.selectedDataSetId = arg;
    }

}

