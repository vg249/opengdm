import {Component, OnInit, EventEmitter, OnChanges, SimpleChange, DoCheck, KeyValueDiffers} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {EntityType, EntitySubType} from "../model/type-entity";
import {CvFilterType, CvFilters} from "../model/cv-filter-type";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Header} from "../model/payload/header";
import {ExtractorItemType} from "../model/file-model-node";
import {Guid} from "../model/guid";
import {NameIdService} from "../services/core/name-id-service";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {Labels} from "./entity-labels";


@Component({
    selector: 'name-id-list-box',
    inputs: ['gobiiExtractFilterType',
        'notifyOnInit',
        'nameIdRequestParams',
        'firstItemIsLabel'],
    outputs: ['onNameIdSelected', 'onError'],
    template: `<select name="users" (change)="handleNameIdSelected($event)" >
			<option *ngFor="let nameId of nameIdList " 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit, OnChanges, DoCheck {

    //private uniqueId:string;

    differ: any;

    constructor(private _nameIdService: NameIdService,
                private _fileModelTreeService: FileModelTreeService,
                private differs: KeyValueDiffers) {

        this.differ = differs.find({}).create(null);


    } // ctor

    private notificationSent = false;

    ngOnInit(): any {

        // entityFilterValue and entityFilter must either have values or be null.
        // if (this._nameIdService.validateRequest(this.nameIdRequestParams) ) {
        //     this.initializeNameIds();
        // }
    }


    private initializeNameIds() {

        let scope$ = this;
        this._nameIdService.get(this.nameIdRequestParams)
            .subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {
                        scope$.nameIdList = nameIds;

                        if (this.firstItemIsLabel) {
                            let label: string = "Select ";

                            if( scope$.nameIdRequestParams.getCvFilterType() !== CvFilterType.UNKNOWN  ) {
                                label += Labels.instance().cvFilterNodeLabels[scope$.nameIdRequestParams.getCvFilterType()];
                            } else if( scope$.nameIdRequestParams.getEntitySubType() !== EntitySubType.UNKNOWN ) {
                                label += Labels.instance().entitySubtypeNodeLabels[scope$.nameIdRequestParams.getEntitySubType()];
                            } else {
                                label += Labels.instance().entityNodeLabels[scope$.nameIdRequestParams.getEntityType()];
                            }

                            let labelNameId = new NameId("0", label, this.nameIdRequestParams.getEntityType());
                            scope$.nameIdList.unshift(labelNameId);

                        } else {
                            scope$.selectedNameId = nameIds[0].id;
                        }

                        if (this.notifyOnInit
                            && !this.firstItemIsLabel
                            && !this.notificationSent
                            && scope$.nameIdList [0].name != "<none>") {
                            this.updateTreeService(scope$.nameIdList [0]);
                            this.notificationSent = true;
                        }
                    }
                },
                responseHeader => {
                    this.handleHeaderStatus(responseHeader);
                });

    }

    // useg
    private nameIdList: NameId[];

    private notifyOnInit: boolean = false;
    private firstItemIsLabel: boolean = false;
    // DtoRequestItemNameIds expects the value to be null if it's not set (not "UNKNOWN")

    private nameIdRequestParams: NameIdRequestParams;

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    private selectedNameId: string = null;

    private onNameIdSelected: EventEmitter<NameId> = new EventEmitter();
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }

    private updateTreeService(nameId: NameId) {

        this.onNameIdSelected.emit(nameId);

        if (nameId.id !== "0") {

            let fileItem: GobiiFileItem = GobiiFileItem
                .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
                .setExtractorItemType(ExtractorItemType.ENTITY)
                .setEntityType(this.nameIdRequestParams.getEntityType())
                .setEntitySubType(this.nameIdRequestParams.getEntitySubType())
                .setCvFilterType(this.nameIdRequestParams.getCvFilterType())
                .setItemId(nameId.id)
                .setItemName(nameId.name);

            this._fileModelTreeService.put(fileItem)
                .subscribe(
                    null,
                    headerResponse => {
                        this.handleHeaderStatus(headerResponse)
                    });
        }
    }


    private handleNameIdSelected(arg) {

        let nameId: NameId = this.nameIdList[arg.srcElement.selectedIndex]

        // let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
        //     this.nameIdList[arg.srcElement.selectedIndex].name,
        //     this.entityType);

        this.updateTreeService(nameId);
    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                this.notificationSent = false;

                this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);

                let scope$ = this;
                this._fileModelTreeService
                    .fileItemNotifications()
                    .subscribe(fileItem => {
                        if (fileItem.getProcessType() === ProcessType.NOTIFY
                            && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                            scope$.initializeNameIds();


                        }
                    });

            } // if we have a new filter type

        } // if filter type changed


        if (changes['nameIdRequestParams']
            && ( changes['nameIdRequestParams'].currentValue != null )
            && ( changes['nameIdRequestParams'].currentValue != undefined )) {

        }


        // we may have gotten a filterValue now so we init if we do
        // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
        //     this.initializeNameIds();
        // }


    } // ngonChanges


    // angular change detection does not do deep comparison of objects that are
    // template properties. So we need to do some specialized change detection
    // references:
    //   https://juristr.com/blog/2016/04/angular2-change-detection/
    //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
    //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
    //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
    ngDoCheck(): void {

        var changes = this.differ.diff(this.nameIdRequestParams);

        if (changes) {
            if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                this.initializeNameIds();
            }
        }
    }
} // class
