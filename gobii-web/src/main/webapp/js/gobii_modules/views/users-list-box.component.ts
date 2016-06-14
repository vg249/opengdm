//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import * as EntityFilters from "../model/type-entity-filter"


@Component({
    selector: 'users-list-box',
    outputs: ['onUserSelected'],
    template: `<select name="users" (change)="handleUserSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class UsersListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onUserSelected:EventEmitter<string> = new EventEmitter();
    private handleUserSelected(arg) {
        this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {

        let scope$ = this;
        _dtoRequestService.getResult(new DtoRequestItemNameIds(ProcessType.READ,
            EntityType.AllContacts)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.nameIdList = nameIds
                } else {
                    scope$.nameIdList = [new NameId(0, "<none>")];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });

    } // ctor


    ngOnInit():any {
        return null;
    }
}
