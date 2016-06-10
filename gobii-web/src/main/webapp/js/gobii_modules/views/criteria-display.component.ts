//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, SimpleChange} from "@angular/core";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";


@Component({
    selector: 'criteria-display',
    inputs: ['gobiiDatasetExtracts'],
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: `<div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                    <div *ngFor="let gobiiDataSetExtract of gobiiDatasetExtracts" >{{gobiiDataSetExtract.dataSetName}}</div>
                </div>
` // end template

})

export class CriteriaDisplayComponent implements OnInit {


    // useg
    private gobiiDatasetExtracts:GobiiDataSetExtract[] = [];
    constructor() {
    } // ctor


    ngOnInit():any {
        return null;
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.gobiiDatasetExtracts= changes['gobiiDatasetExtracts'].currentValue;
    }

}
