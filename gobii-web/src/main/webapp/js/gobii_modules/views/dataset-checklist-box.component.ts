//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {CheckBoxEvent} from "../model/event-checkbox";

@Component({
    selector: 'dataset-checklist-box',
    inputs: ['experimentId'],
    outputs: ['onItemChecked'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
 
                        <div *ngFor="let nameId of nameIdList" >
                            <input  type="checkbox" 
                                (click)=handleItemSelected($event)
                                value={{nameId.id}} 
                                name="{{nameId.name}}">&nbsp;{{nameId.name}}
                        </div>            
     
                    </div>        
                </form>
` // end template

})


export class DataSetCheckListBoxComponent implements OnInit,OnChanges {


    // useg
    private nameIdList:NameId[];
    private experimentId:string;
    private onItemChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();

    private handleItemSelected(arg) {
        let foo = arg;
        let checkEvent:CheckBoxEvent = new CheckBoxEvent(arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE,
            arg.currentTarget.value,
            arg.currentTarget.name);
        this.onItemChecked.emit(checkEvent);
    }

    constructor(private _nameIdListService:DtoRequestService<NameId[]>) {


    } // ctor

    private checkedItems:string[];
    private setList():void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;
        this._nameIdListService.getNameIds(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.DataSetNamesByExperimentId,
            this.experimentId)).subscribe(nameIds => {
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
        this.experimentId = changes['experimentId'].currentValue;
        this.setList();
    }
}
