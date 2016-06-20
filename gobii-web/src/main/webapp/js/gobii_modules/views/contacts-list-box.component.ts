//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";


@Component({
    selector: 'contacts-list-box',
    outputs: ['onContactSelected'],
    inputs: ['nameIdList'],
    template: `<select name="principleInvestigators" (change)="handleContactSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class ContactsListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onContactSelected:EventEmitter<string> = new EventEmitter();

    private handleContactSelected(arg) {
        this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {

    } // ctor

    ngOnInit():any {
        return null;
    }
}
