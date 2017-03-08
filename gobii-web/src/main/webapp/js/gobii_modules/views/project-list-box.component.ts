import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {Project} from "../model/project";
import {DtoRequestItemProject} from "../services/app/dto-request-item-project";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {EntityType, EntitySubType} from "../model/type-entity";
import {EntityFilter} from "../model/type-entity-filter";
import {CvFilterType} from "../model/cv-filter-type";
import {Header} from "../model/payload/header";

@Component({
    selector: 'project-list-box',
    inputs: ['primaryInvestigatorId', 'nameIdList','nameIdListPIs','gobiiExtractFilterType'],
    outputs: ['onProjectSelected', 'onAddHeaderStatus'],
    template: `<name-id-list-box
                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                    [entityType]="entityTypeForTemplates.Projects"
                    [entityFilter] = "entityFilterForTemplates.BYTYPEID"
                    [entityFilterValue] = "primaryInvestigatorId"
                    (onNameIdSelected) = "handleProjectSelected($event)"
                    (onError) = "handleHeaderStatus($event)">
                </name-id-list-box>
		        
                <div *ngIf="project">
                    <BR>
                     <fieldset class="form-group">
                        <b>Name:</b> {{project.projectName}}<BR>
                        <b>Description:</b> {{project.projectDescription}}<BR>
                        <b>Principle Investigator:</b> {{primaryInvestigatorName}}
                      </fieldset> 
                </div>		        
` // end template

})

export class ProjectListBoxComponent implements OnInit,OnChanges {

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    private entityTypeForTemplates = EntityType;
    private entityFilterForTemplates = EntityFilter;
    private entitySubTypeForTemplates = EntitySubType;
    private cvFilterTypeForTemplates = CvFilterType;


    // useg    privatre
    private project:Project;
    private nameIdList:NameId[];
    private nameIdListPIs:NameId[];
    private primaryInvestigatorId:string;
    private primaryInvestigatorName:string;
    private onProjectSelected:EventEmitter<string> = new EventEmitter();
    private onAddHeaderStatus:EventEmitter<Header> = new EventEmitter();

    private handleProjectSelected(arg) {
        let selectedProjectId = arg.id;
        this.setProjectDetails(selectedProjectId);
        this.onProjectSelected.emit(selectedProjectId);
    }

    private handleHeaderStatus(arg:Header) {
        this.onAddHeaderStatus.emit(arg);
    }


    constructor(private _dtoRequestServiceProject:DtoRequestService<Project>) {


    } // ctor

    private setProjectDetails(projectId:string):void {
        let scope$ = this;
        this._dtoRequestServiceProject.get(new DtoRequestItemProject(Number(projectId)))
            .subscribe(projects => {
                    if (projects[0]) {
                        scope$.project = projects[0];
                        scope$.primaryInvestigatorId = String(projects[0].piContact);
                        scope$.setPiName();
                    }
                },
                headerStatusMessage => {
                    scope$.handleHeaderStatus(headerStatusMessage);
                });
    }

    ngOnInit():any {

        let foo:string = "foo";
    }

    private setPiName() {

        this.primaryInvestigatorName = undefined;
        if( this.primaryInvestigatorId && this.nameIdListPIs) {
            this.nameIdListPIs.forEach(n => {
                if(n.id === this.primaryInvestigatorId) {
                    this.primaryInvestigatorName = n.name;

                }
            })
        }
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if (changes['primaryInvestigatorId'] && changes['primaryInvestigatorId'].currentValue) {
            this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;
            
        }

        if (changes['nameIdList']) {
            if (changes['nameIdList'].currentValue) {
                this.nameIdList = changes['nameIdList'].currentValue;
                this.setProjectDetails(this.nameIdList[0].id);
            }
        }

        if (changes['nameIdListPIs']) {
            if (changes['nameIdListPIs'].currentValue) {
                this.nameIdListPIs = changes['nameIdListPIs'].currentValue;
            }
        }

        //

    }
}
