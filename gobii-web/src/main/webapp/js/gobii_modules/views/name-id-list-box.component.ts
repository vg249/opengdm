//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";

import {NameId} from '../model/name-id';
import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service';
import {NameIdListService} from "../services/app/name-id-list.service";

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


    // use
    private nameIdList:NameId[];
    constructor(private _principleInvestigatorService:PrincipleInvestigatorService,
                private _nameIdListService:NameIdListService) {


        let scope$ = this;
        _nameIdListService.getNameIds().subscribe(nameIds => {
            scope$.nameIdList = nameIds
        } );

    } // ctor

    get nameIds():Observable<NameId[]> {
        return this._nameIdListService.getNameIds();
    }


    ngOnInit():any {
        return null;
    }
}
