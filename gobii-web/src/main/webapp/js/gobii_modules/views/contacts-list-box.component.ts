//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import * as EntityFilters from "../model/type-entity-filter"


@Component({
    selector: 'contacts-list-box',
    template: `<select name="principleInvestigators">
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class ContactsListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    constructor(private _nameIdListService:DtoRequestService<NameId[]>) {

        let scope$ = this;
        _nameIdListService.getNameIds(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.Contact,
            EntityFilters.ENTITY_FILTER_CONTACT_PRINICPLE_INVESTIGATOR)).subscribe(nameIds => {
                scope$.nameIdList = nameIds
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });

    } // ctor


    ngOnInit():any {
        return null;
    }
}
