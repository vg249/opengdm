//import {RouteParams} from '@angular/router-deprecated';
import {
    Component,
    OnInit,
    OnChanges,
    SimpleChange,
    EventEmitter
} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";


@Component({
    selector: 'project-list-box',
    inputs: ['primaryInvestigatorId'],
    outputs: ['onProjectSelected'],
    template: `<select name="projects" 
                multiple="multiple"
                 size="nameIdList.length"
                (change)="handleProjectSelected($event)">
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class ProjectListBoxComponent implements OnInit,OnChanges {


    // useg
    private nameIdList:NameId[];
    private primaryInvestigatorId:string;
    private onProjectSelected:EventEmitter<string> = new EventEmitter();
    private handleProjectSelected(arg) {
        this.onProjectSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _nameIdListService:DtoRequestService<NameId[]>) {


    } // ctor

    private setList():void {

        let scope$ = this;
        this._nameIdListService.getNameIds(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.Project,
            this.primaryInvestigatorId)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.nameIdList = nameIds
                } else {
                    scope$.nameIdList = [new NameId(0, "<none>")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });
    } // setList()

    ngOnInit():any {

        this.setList();
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;
        this.setList();
//        console.log('ngOnChanges - myProp = ' + changes['primaryInvestigatorId'].currentValue);
    }
}
