import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemDataSet} from "../services/app/dto-request-item-dataset";
import {DataSet} from "../model/dataset"

@Component({
    selector: 'dataset-detail-box',
    inputs: ['dataSetId', 'visible'],
    template: ` <div *ngIf="dataSet">
                     <fieldset>
                        Name: {{dataSet.name}}<BR>
                        Data Table: {{dataSet.dataTable}}<BR>
                        Data File: {{dataSet.dataFile}}<BR>
                        Quality Table: {{dataSet.qualityTable}}<BR>
                        Quality File: {{dataSet.qualityFile}}<BR>
                      </fieldset> 
                </div>` // end template

})

export class DatasetDetailBoxComponent implements OnInit,OnChanges {


    // useg
    private dataSet:DataSet;
    private dataSetId:number;
    private visible: boolean;

    constructor(private _dtoRequestService:DtoRequestService<DataSet>) {
    } // ctor

    private setDataset():void {

        if (this.dataSetId && this.dataSetId > 0) {
            let scope$ = this;
            this._dtoRequestService.getResult(new DtoRequestItemDataSet(this.dataSetId)).subscribe(dataSet => {
                    if (dataSet) {
                        scope$.dataSet = dataSet
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
                });
            
        } // if we have a legit dataSetId
        
    } // setList()

    ngOnInit():any {

        this.setDataset();
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.dataSetId = changes['dataSetId'].currentValue;
        this.setDataset();
    }
}
