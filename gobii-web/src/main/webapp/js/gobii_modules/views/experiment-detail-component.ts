import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemExperiment} from "../services/app/dto-request-item-experiment";
import {Experiment} from "../model/experiment"

@Component({
    selector: 'experiment-detail-box',
    inputs: ['experimentId', 'visible'],
    template: ` <div *ngIf="experiment">
                     <fieldset>
                        Name: {{experiment.experimentName}}<BR>
                        Code: {{experiment.experimentCode}}<BR>
                        Data File: {{experiment.experimentDataFile}}<BR>
                        Status: {{experiment.status}}<BR>
                      </fieldset> 
                </div>` // end template
})

export class ExperimentDetailBoxComponent implements OnInit,OnChanges {


    // useg
    private experiment:Experiment;
    private experimentId:number;
    private visible: boolean;

    constructor(private _dtoRequestService:DtoRequestService<Experiment>) {
    } // ctor

    private setExperiment():void {

        if (this.experimentId && this.experimentId > 0) {
            let scope$ = this;
            this._dtoRequestService.getResult(new DtoRequestItemExperiment(this.experimentId)).subscribe(experiment => {
                    if (experiment) {
                        scope$.experiment = experiment
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
                });

        } // if we have a legit experimentId

    } // setList()

    ngOnInit():any {

        this.setExperiment();
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.experimentId = changes['experimentId'].currentValue;
        this.setExperiment();
    }
}
