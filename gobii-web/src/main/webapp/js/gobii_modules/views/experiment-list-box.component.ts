//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {DtoRequestItemExperiment} from "../services/app/dto-request-item-experiment";
import {Experiment} from "../model/experiment"



@Component({
    selector: 'experiment-list-box',
    inputs: ['projectId'],
    outputs: ['onExperimentSelected'],
    template: `<select name="experiment" 
                    (change)="handleExperimentSelected($event)">
                    <option *ngFor="let nameId of nameIdList " 
                    value={{nameId.id}}>{{nameId.name}}</option>
		        </select>
                <div *ngIf="experiment">
    		        <BR>
                     <fieldset>
                        Name: {{experiment.experimentName}}<BR>
                        Code: {{experiment.experimentCode}}<BR>
                      </fieldset> 
                </div>		        
` // end template

})

export class ExperimentListBoxComponent implements OnInit,OnChanges {


    // useg
    private nameIdList:NameId[];
    private projectId:string;
    private experiment:Experiment;
    private onExperimentSelected:EventEmitter<string> = new EventEmitter();

    private handleExperimentSelected(arg) {
        this.onExperimentSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _dtoRequestServiceNameIds:DtoRequestService<NameId[]>,
                private _dtoRequestServiceExperiment:DtoRequestService<Experiment>) {
    } // ctor

    private setList():void {

        let scope$ = this;
        this._dtoRequestServiceNameIds.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.Experiment,
            this.projectId)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.nameIdList = nameIds
                    scope$.setExperimentDetail(scope$.nameIdList[0].id)
                } else {
                    scope$.nameIdList = [new NameId(0, "<none>")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });
    } // setList()

    private setExperimentDetail(experimentId:string):void {

        let scope$ = this;
        scope$._dtoRequestServiceExperiment.getResult(new DtoRequestItemExperiment(Number(experimentId))).subscribe(experiment => {
                if (experiment) {
                    scope$.experiment = experiment
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
        this.projectId = changes['projectId'].currentValue;
        this.setList();
    }
}
