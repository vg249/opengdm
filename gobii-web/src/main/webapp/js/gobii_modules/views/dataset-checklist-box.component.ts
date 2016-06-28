//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {CheckBoxEvent} from "../model/event-checkbox";
import {DtoRequestItemDataSet} from "../services/app/dto-request-item-dataset";
import {DataSet} from "../model/dataset"


@Component({
    selector: 'dataset-checklist-box',
    inputs: ['experimentId'],
    outputs: ['onItemChecked','onItemSelected','onAddMessage'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let nameId of nameIdList" 
                            (click)=handleItemSelected($event) 
                            (hover)=handleItemHover($event)>
                            <input  type="checkbox" 
                                (click)=handleItemChecked($event)
                                value={{nameId.id}} 
                                name="{{nameId.name}}">&nbsp;{{nameId.name}}
                        </div>            
                    </div>
                </form>
                <div *ngIf="dataSet">
                    <BR>
                     <fieldset>
                        Name: {{dataSet.name}}<BR>
                        Data Table: {{dataSet.dataTable}}<BR>
                        Data File: {{dataSet.dataFile}}<BR>
                        Quality Table: {{dataSet.qualityTable}}<BR>
                        Quality File: {{dataSet.qualityFile}}<BR>
                      </fieldset> 
                </div>                
` // end template

})


export class DataSetCheckListBoxComponent implements OnInit,OnChanges {


    // useg
    private nameIdList:NameId[];
    private experimentId:string;
    private onItemChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected:EventEmitter<number> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();
    private dataSet:DataSet;

    private handleItemChecked(arg) {
        let checkEvent:CheckBoxEvent = new CheckBoxEvent(arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE,
            arg.currentTarget.value,
            arg.currentTarget.name);
        this.onItemChecked.emit(checkEvent);
    }
    
    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    private previousSelectedItem:any;
    private handleItemSelected(arg) {
        let selectedDataSetId:number = Number(arg.currentTarget.children[0].value);
        if( this.previousSelectedItem ) {this.previousSelectedItem.style = ""}
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;
        this.setDatasetDetails(selectedDataSetId);
        this.onItemSelected.emit(selectedDataSetId);
    }

    constructor(private _dtoRequestServiceNameId:DtoRequestService<NameId[]>,
                private _dtoRequestServiceDataSetDetail:DtoRequestService<DataSet>) {

    } // ctor

    private checkedItems:string[];
    private setList():void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;
        this._dtoRequestServiceNameId.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.DataSetNamesByExperimentId,
            this.experimentId)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.nameIdList = nameIds;
                    scope$.setDatasetDetails(scope$.nameIdList[0].id);
                } else {
                    scope$.nameIdList = [new NameId(0, "<none>")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message))
            });
    } // setList()

    private setDatasetDetails(dataSetId:number):void {

        let scope$ = this;
        scope$._dtoRequestServiceDataSetDetail.getResult(new DtoRequestItemDataSet(dataSetId)).subscribe(dataSet => {
                if (dataSet) {
                    scope$.dataSet = dataSet;
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
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
