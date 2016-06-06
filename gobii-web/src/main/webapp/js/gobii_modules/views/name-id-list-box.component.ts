//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";

import {NameId} from '../model/name-id';
import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service';
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";

@Component({
    selector: 'name-id-list-box',
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: `<select name="principleInvestigators">
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];
    constructor(private _principleInvestigatorService:PrincipleInvestigatorService,
                private _nameIdListService:DtoRequestService<NameId[]>) {


        let scope$ = this;
        _nameIdListService.getNameIds(new DtoRequestItemNameIds()).subscribe(nameIds => {
            scope$.nameIdList = nameIds
        } );

    } // ctor


    ngOnInit():any {
        return null;
    }
}
