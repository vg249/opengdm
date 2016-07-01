//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {CheckBoxEvent} from "../model/event-checkbox";
import {DtoRequestItemDataSet} from "../services/app/dto-request-item-dataset";
import {DataSet} from "../model/dataset";
import {DtoRequestItemAnalysis} from "../services/app/dto-request-item-analysis";
import {Analysis} from "../model/analysis";
import {DtoRequestItemCv} from "../services/app/dto-request-item-cv";
import {Cv} from "../model/cv";


@Component({
    selector: 'dataset-checklist-box',
    inputs: ['experimentId', 'dataSetIdToUncheck'],
    outputs: ['onItemChecked', 'onItemSelected', 'onAddMessage'],
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
                        <b>Name:</b> {{dataSet.name}}<BR>
                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>
                        <b>Data File:</b> {{dataSet.dataFile}}<BR>
                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>
                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>
                        <div *ngIf="analysisNames && (analysisNames.length > 0)">
                            <b>Analyses:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisName of analysisNames" >{{analysisName}}</li>
                                    </ul>
                        </div>
                        <div *ngIf="analysisTypes && (analysisTypes.length > 0)">
                            <b>Analysis Types:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisType of analysisTypes" >{{analysisType}}</li>
                                    </ul>
                        </div>
                      </fieldset> 
                </div>                
` // end template

})


export class DataSetCheckListBoxComponent implements OnInit,OnChanges {

    constructor(private _dtoRequestServiceNameId:DtoRequestService<NameId[]>,
                private _dtoRequestServiceDataSetDetail:DtoRequestService<DataSet>,
                private _dtoRequestServiceAnalysisDetail:DtoRequestService<Analysis>,
                private _dtoRequestServiceCvDetail:DtoRequestService<Cv>) {

    } // ctor

    // useg
    private nameIdList:NameId[];
    private experimentId:string;
    private onItemChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected:EventEmitter<number> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();
    private dataSet:DataSet;
    private analysisNames:string[] = [];
    private analysisTypes:string[] = [];
    private nameIdListAnalysisTypes:NameId[];

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
        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;
        this.setDatasetDetails(selectedDataSetId);
        this.onItemSelected.emit(selectedDataSetId);
    }


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
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                    + ": "
                    + m.message))
            });
    } // setList()

    private setDatasetDetails(dataSetId:number):void {

        let scope$ = this;
        scope$._dtoRequestServiceDataSetDetail.getResult(new DtoRequestItemDataSet(dataSetId))
            .subscribe(dataSet => {
                    if (dataSet) {
                        scope$.dataSet = dataSet;
                        scope$.analysisNames = [];
                        scope$.analysisTypes = [];

                        scope$.dataSet.analysesIds.forEach(
                            analysisId => {
                                let currentAnalysisId:number = analysisId;
                                if (currentAnalysisId) {
                                    scope$._dtoRequestServiceAnalysisDetail
                                        .getResult(new DtoRequestItemAnalysis(currentAnalysisId))
                                        .subscribe(analysis => {
                                                scope$.analysisNames.push(analysis.analysisName);
                                                if (analysis.anlaysisTypeId && scope$.nameIdListAnalysisTypes) {

                                                    scope$
                                                        .nameIdListAnalysisTypes
                                                        .forEach(t => {
                                                            if (Number(t.id) === analysis.anlaysisTypeId) {
                                                                scope$.analysisTypes.push(t.name);
                                                            }
                                                        });


                                                } // if we have an analysis type id
                                            },
                                            dtoHeaderResponse => {
                                                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                                            });
                                }
                            }
                        );
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                });

    } // setList()


    ngOnInit():any {

        let scope$ = this;
        scope$._dtoRequestServiceNameId
            .getResult(new DtoRequestItemNameIds(ProcessType.READ, EntityType.CvGroupTerms, "analysis_type"))
            .subscribe(nameIdList => {
                    scope$.nameIdListAnalysisTypes = nameIdList;
                    if (this.experimentId) {
                        this.setList();
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                });

    }


    private dataSetIdToUncheckFromEvent:number;
    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if (changes['experimentId']) {
            this.experimentId = changes['experimentId'].currentValue;
            if (this.experimentId) {
                this.setList();
            }
        }

        if (changes['dataSetIdToUncheck']) {
            this.dataSetIdToUncheckFromEvent = changes['dataSetIdToUncheck'].currentValue;
            if (this.dataSetIdToUncheckFromEvent) {

                let nameIdItemToRemove:NameId =
                    this.nameIdList
                        .filter(n => {
                            return Number(n.id) === this.dataSetIdToUncheckFromEvent
                        })[0];

                if( nameIdItemToRemove ) {
                    let indexOfItemToRemove = this.nameIdList.indexOf(nameIdItemToRemove);

                    this.nameIdList.splice(indexOfItemToRemove, 1);
                    this.nameIdList.splice(indexOfItemToRemove, 0, nameIdItemToRemove);
                }
            }
        }
    }
}
