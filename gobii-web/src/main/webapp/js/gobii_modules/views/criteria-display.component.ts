//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {CheckBoxEvent} from "../model/event-checkbox";
import {ProcessType} from "../model/type-process";


@Component({
    selector: 'criteria-display',
    inputs: ['gobiiDatasetExtracts'],
    outputs: ['onItemUnChecked', 'onItemSelected'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let gobiiDataSetExtract of gobiiDatasetExtracts"
                                (click)=handleItemSelected($event)
                                (hover)=handleItemHover($event)>
                                <input  type="checkbox"
                                    (click)=handleItemUnChecked($event)
                                    value={{gobiiDataSetExtract.dataSetId}}
                                    name="{{gobiiDataSetExtract.dataSetName}}"
                                    checked>&nbsp;{{gobiiDataSetExtract.dataSetName}}
                        </div>
                    </div>
                </form>`

})

export class CriteriaDisplayComponent implements OnInit {


    // useg
    private gobiiDatasetExtracts:GobiiDataSetExtract[] = [];
    private onItemUnChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected:EventEmitter<number> = new EventEmitter();

    constructor() {
    } // ctor


    ngOnInit():any {
        return null;
    }

    // In this component, every item starts out checked; unchecking it removes it
    private handleItemUnChecked(arg) {
        let checkEvent:CheckBoxEvent = new CheckBoxEvent( ProcessType.DELETE,
            arg.currentTarget.value,
            arg.currentTarget.name);

        let itemToRemove:GobiiDataSetExtract =
            this.gobiiDatasetExtracts
                .filter(e => {
                    return e.getDataSetId() === Number(arg.currentTarget.value)
                })[0];

        let indexOfItemToRemove:number = this.gobiiDatasetExtracts.indexOf(itemToRemove);

        if( indexOfItemToRemove > -1 ) {
            this.gobiiDatasetExtracts.splice(indexOfItemToRemove,1);
        }

        this.onItemUnChecked.emit(checkEvent);
    }

    private previousSelectedItem:any;

    private handleItemSelected(arg) {
        let selectedDataSetId:number = Number(arg.currentTarget.children[0].value);
        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;
        this.onItemSelected.emit(selectedDataSetId);
    }


    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.gobiiDatasetExtracts = changes['gobiiDatasetExtracts'].currentValue;
    }

}
