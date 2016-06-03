import {RouteParams} from 'angular2/router';
import {Component, OnInit} from 'angular2/core';
import {Observable} from "rxjs/Observable";

import {NameId} from '../model/name-id';
import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/inspectTime';
import 'rxjs/add/operator/startWith';
import {NameIdListService} from "../services/app/name-id-list.service";

@Component({
    selector: 'name-id-list-box',
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: `<select name="principleInvestigators">
			<option *ngFor="#nameId of nameIdList | async" 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit {


    private nameIdList:NameId[];


    // TODO: Use interface so this component can be reused?
    constructor(private _principleInvestigatorService:PrincipleInvestigatorService,
                private _nameIdListService:NameIdListService) {


        let scope$ = this;
        _nameIdListService.getNameIds().subscribe(nameIds => {
            scope$.nameIdList = nameIds
        } );

    } // ctor

    // get nameIds():Observable<NameId[]> {
    //     return this._nameIdListService.getFake().inspectTime(1000);
    // }
    

    ngOnInit() {

    } // ngOnInit

}
