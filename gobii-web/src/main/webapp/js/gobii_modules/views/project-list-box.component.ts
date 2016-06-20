//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {Project} from "../model/project";
import {DtoRequestItemProject} from "../services/app/dto-request-item-project";

@Component({
    selector: 'project-list-box',
    inputs: ['primaryInvestigatorId','nameIdList'],
    outputs: ['onProjectSelected'],
    template: `<select name="projects" 
                    (change)="handleProjectSelected($event)">
                    <option *ngFor="let nameId of nameIdList " 
                    value={{nameId.id}}>{{nameId.name}}</option>
		        </select>
                <div *ngIf="project">
                    <BR>
                     <fieldset class="form-group">
                        Name: {{project.projectName}}<BR>
                        Description: {{project.projectDescription}}<BR>
                      </fieldset> 
                </div>		        
` // end template

})

export class ProjectListBoxComponent implements OnInit,OnChanges {


    // useg    privatre
    private project:Project;
    private nameIdList:NameId[];
    private primaryInvestigatorId:string;
    private onProjectSelected:EventEmitter<string> = new EventEmitter();

    private handleProjectSelected(arg) {
        let selectedProjectId = this.nameIdList[arg.srcElement.selectedIndex].id;
        this.setProjectDetails(selectedProjectId);
        this.onProjectSelected.emit(selectedProjectId);
    }

    constructor(private _dtoRequestServiceNameId:DtoRequestService<NameId[]>,
                private _dtoRequestServiceProject:DtoRequestService<Project>) {


    } // ctor

    private setList():void {

        // let scope$ = this;
        // this._dtoRequestServiceNameId.getResult(new DtoRequestItemNameIds(ProcessType.READ,
        //     EntityType.Project,
        //     this.primaryInvestigatorId)).subscribe(nameIds => {
        //         if (nameIds && ( nameIds.length > 0 )) {
        //             scope$.nameIdList = nameIds;
        //             scope$.setProjectDetails(scope$.nameIdList[0].id);
        //
        //         } else {
        //             scope$.nameIdList = [new NameId(0, "<none>")];
        //         }
        //     },
        //     dtoHeaderResponse => {
        //         dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
        //     });
    } // setList()

    private setProjectDetails(projectId:string):void {
        let scope$ = this;
        this._dtoRequestServiceProject.getResult(new DtoRequestItemProject(Number(projectId)))
            .subscribe(project => {
                if (project) {
                    scope$.project = project
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });
    }

    ngOnInit():any {

        //this.setList();
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if(changes['primaryInvestigatorId'] && changes['primaryInvestigatorId'].currentValue ) {
            this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;
        }

        if( changes['nameIdList']  ) {
            if(changes['nameIdList'].currentValue) {
                this.nameIdList = changes['nameIdList'].currentValue;
                this.setProjectDetails(this.nameIdList[0].id);
            }
        }
//        console.log('ngOnChanges - myProp = ' + changes['primaryInvestigatorId'].currentValue);
    }
}
