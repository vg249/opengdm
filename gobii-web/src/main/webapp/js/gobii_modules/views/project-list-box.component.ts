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
import {NameIdRequestParams} from "../model/name-id-request-params";
import {NameIdLabelType} from "../model/name-id-label-type";
import {NameIdFilterParamTypes} from "../model/type-nameid-filter-params";
import {GobiiFileItem} from "../model/gobii-file-item";
import {Observable} from "rxjs/Observable";
import {FileItemService} from "../services/core/file-item-service";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
// import * as fileItemAction from '../store/actions/fileitem-action';
// import * as treeNodeAction from '../store/actions/treenode-action';


@Component({
    selector: 'project-list-box',
    inputs: ['primaryInvestigatorId',
        'gobiiExtractFilterType',
        'reinitProjectList'],
    outputs: ['onProjectSelected',
        'onAddHeaderStatus'],
    template: `
        <name-id-list-box
                [fileItems$]="fileItemsProjects$"
                [gobiiExtractFilterType]="gobiiExtractFilterType"
                [notifyOnInit]="true"
                [doTreeNotifications]="reinitProjectList"
                [nameIdRequestParams]="nameIdRequestParamsProject"
                (onNameIdSelected)="handleProjectSelected($event)"
                (onError)="handleHeaderStatus($event)">
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

export class ProjectListBoxComponent implements OnInit, OnChanges {

    public gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    public nameIdRequestParamsProject: NameIdRequestParams;


    // useg    privatre
    public project: Project;
    public primaryInvestigatorId: string;
    public primaryInvestigatorName: string;
    public onProjectSelected: EventEmitter<string> = new EventEmitter();
    public onAddHeaderStatus: EventEmitter<Header> = new EventEmitter();
    public reinitProjectList: boolean = false;
    public fileItemsProjects$: Observable<GobiiFileItem[]>;


    private handleProjectSelected(arg) {
        let selectedProjectId = arg.id;
//        this.setProjectDetails(selectedProjectId);
        this.onProjectSelected.emit(selectedProjectId);
    }

    private handleHeaderStatus(arg: Header) {
        this.onAddHeaderStatus.emit(arg);
    }


    constructor(private _dtoRequestServiceProject: DtoRequestService<Project>,
                private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {


        this.fileItemsProjects$ = this.store.select(fromRoot.getProjects);

    } // ctor

    private setProjectDetails(projectId: string): void {
        let scope$ = this;
        this._dtoRequestServiceProject.get(new DtoRequestItemProject(Number(projectId)))
            .subscribe(projects => {
                    if (projects[0]) {
                        scope$.project = projects[0];
                        scope$.primaryInvestigatorId = String(projects[0].piContact);
                    }
                },
                headerStatusMessage => {
                    scope$.handleHeaderStatus(headerStatusMessage);
                });
    }

    ngOnInit(): any {

        let foo: string = "foo";

    }

    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        this.nameIdRequestParamsProject = NameIdRequestParams
            .build(NameIdFilterParamTypes.PROJECTS,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Projects)
            .setEntityFilter(EntityFilter.BYTYPEID)
            .setNameIdLabelType(this.reinitProjectList ? NameIdLabelType.ALL : NameIdLabelType.UNKNOWN);

        let foo: string = "foo";

        if (changes['gobiiExtractFilterType'] && changes['gobiiExtractFilterType'].currentValue) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                this.nameIdRequestParamsProject.setGobiiExtractFilterType(changes['gobiiExtractFilterType'].currentValue);
            }
        }

        if (changes['primaryInvestigatorId'] && changes['primaryInvestigatorId'].currentValue) {
            this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;

            this.nameIdRequestParamsProject.setFkEntityFilterValue(this.primaryInvestigatorId);
            this.nameIdRequestParamsProject.setNameIdLabelType(this.reinitProjectList ? NameIdLabelType.ALL : NameIdLabelType.UNKNOWN);
            // this.fileItemService.loadNameIdsToFileItems(this.gobiiExtractFilterType,
            //     this.nameIdRequestParamsProject);

        }


    }
}
