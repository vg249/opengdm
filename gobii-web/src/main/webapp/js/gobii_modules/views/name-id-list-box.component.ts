import {Component, OnInit, EventEmitter, OnChanges, SimpleChange} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {EntityType, EntitySubType} from "../model/type-entity";
import {CvFilterType} from "../model/cv-filter-type";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityFilter} from "../model/type-entity-filter";
import {FileItem} from "../model/file-item";
import {ProcessType} from "../model/type-process";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Header} from "../model/payload/header";


@Component({
    selector: 'name-id-list-box',
    inputs: ['gobiiExtractFilterType','entityType', 'entityFilter', 'entitySubType', 'cvFilterType'],
    outputs: ['onNameIdSelected', 'onError'],
    template: `<select name="users" (change)="handleNameIdSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit, OnChanges {

    constructor(private _dtoRequestService: DtoRequestService<NameId[]>,
                private _fileModelTreeService: FileModelTreeService) {

    } // ctor

    ngOnInit(): any {

        // entityFilterValue and entityFilter must either have values or be null.
        if (this.entityFilter === EntityFilter.NONE) {
            this.entityFilter = null;
            this.entityFilterValue = null;
        } else if (this.entityFilter === null) {
            this.entityFilterValue = null;
        } else {
            this.entityFilterValue = this.getEntityFilterValue(this.entityType, this.entitySubType);
        }


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
            responseHeader => {
                this.handleResponseHeader(responseHeader);
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
    private gobiiExtractFilterType:GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    private selectedNameId: string = null;

    private onNameIdSelected: EventEmitter<NameId> = new EventEmitter();
    private onError: EventEmitter<Header> = new EventEmitter();


    private handleResponseHeader(header: Header) {

        this.onError.emit(header);
    }


    private handleNameIdSelected(arg) {

        let nameId:NameId = this.nameIdList[arg.srcElement.selectedIndex]

        // let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
        //     this.nameIdList[arg.srcElement.selectedIndex].name,
        //     this.entityType);
        // this.onNameIdSelected.emit(nameId);
        let fileItem: FileItem = FileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setEntityType(this.entityType)
            .setEntitySubType(this.entitySubType)
            .setItemId(nameId.id)
            .setItemName(nameId.name);

        this._fileModelTreeService.put(fileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });
    }


    private getEntityFilterValue(entityType: EntityType, entitySubType: EntitySubType): string {

        let returnVal: string = null;

        if (entityType === EntityType.Contacts) {
            if (entitySubType === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR) {
                returnVal = "PI";
            }
        }

        return returnVal;
    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {
    }
}
