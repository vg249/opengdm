import {Component, OnInit, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";


@Component({
    selector: 'mapsets-list-box',
    outputs: ['onMapsetSelected'],
    inputs: ['nameIdList'],
    template: `<select name="mapsets" (change)="handleMapsetSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class MapsetsListBoxComponent implements OnInit {


    // useg
    private nameIdList:NameId[];

    private onMapsetSelected:EventEmitter<string> = new EventEmitter();

    private handleMapsetSelected(arg) {
        this.onMapsetSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    constructor() {

    } // ctor

    ngOnInit():any {
        return null;
    }
}
