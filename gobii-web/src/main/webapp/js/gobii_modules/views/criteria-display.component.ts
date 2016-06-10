//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit,SimpleChange} from '@angular/core';
import {Observable} from "rxjs/Observable";

import {NameId} from '../model/name-id';
import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service';
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityType} from "../model/type-entity";
import {ProcessType} from "../model/type-process";


@Component({
    selector: 'criteria-display',
    inputs: ['displayItems'],
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: `<div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                <p *ngFor="let displayItem of displayItems">{{displayItem}}</p>

                </div>
` // end template

})

export class CriteriaDisplayComponent implements OnInit {


    // useg
    private displayItems:string[];
    constructor() {
    } // ctor


    ngOnInit():any {
        return null;
    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {
        this.displayItems= changes['displayItems'].currentValue;
    }

}
