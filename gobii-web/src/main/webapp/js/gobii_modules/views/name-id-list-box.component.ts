import {Component, OnInit, EventEmitter, OnChanges, SimpleChange} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {EntityType, EntitySubType} from "../model/type-entity";
import {CvFilterType} from "../model/cv-filter-type";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityFilter} from "../model/type-entity-filter";


@Component({
    selector: 'name-id-list-box',
    inputs: ['entityType', 'entityFilter', 'entityFilterValue', 'entitySubType',  'cvFilterType'],
    outputs: ['onNameIdSelected'],
    template: `<select name="users" (change)="handleNameIdSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit, OnChanges {

    constructor(private _dtoRequestService: DtoRequestService<NameId[]>) {

    } // ctor

    ngOnInit(): any {

        let scope$ = this;
        this._dtoRequestService.get(new DtoRequestItemNameIds(
            this.entityType,
            this.entityFilter,
            this.entityFilterValue)).subscribe(nameIds => {
                if (nameIds && ( nameIds.length > 0 )) {
                    scope$.nameIdList = nameIds;
                    scope$.selectedNameId = nameIds[0].id;
                    this.handleNameIdSelected(nameIds[0]);
                } else {
                    scope$.nameIdList = [new NameId("0", "ERROR NO " + EntityType[scope$.entityType], scope$.entityType)];
                }
            },
            dtoHeaderResponse => {
                // dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving nameIds: "
                //     + m.message))
            });
    }

    // useg
    private nameIdList: NameId[];

    // DtoRequestItemNameIds expects the value to be null if it's not set (not "UNKNOWN")
    private entityType: EntityType = null;
    private entityFilter: EntityFilter = null;
    private entityFilterValue: string = null;
    private entitySubType: EntitySubType = null;
    private cvFilterType: CvFilterType = null;

    private selectedNameId:string = null;

    private onNameIdSelected: EventEmitter<NameId> = new EventEmitter();

    private handleNameIdSelected(arg) {

        let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
            this.nameIdList[arg.srcElement.selectedIndex].name,
            this.entityType);

        this.onNameIdSelected.emit(nameId);
    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {
    }
}
