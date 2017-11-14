import {Component, EventEmitter, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model//type-extractor-item";
import {Labels} from "./entity-labels";
import {FileItemParamNames} from "../model/file-item-param-names";
import * as fromRoot from '../store/reducers';
import {FileItemService} from "../services/core/file-item-service";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";


@Component({
    selector: 'sample-marker-box',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleMarkerError'],
    template: `
        <div class="container-fluid">

            <div class="row">

                <input type="radio"
                       (click)="handleOnClickBrowse($event)"
                       name="listType"
                       value="itemFile"
                       [(ngModel)]="selectedListType">
                <label class="the-legend">File&nbsp;</label>
                <input type="radio"
                       (click)="handleTextBoxChanged($event)"
                       name="listType"
                       value="itemArray"
                       [(ngModel)]="selectedListType">
                <label class="the-legend">List&nbsp;</label>
                <input *ngIf="displayMarkerGroupRadio"
                       type="radio"
                       (click)="handleMarkerGroupChanged($event)"
                       name="listType"
                       value="markerGroupsType"
                       [(ngModel)]="selectedListType">
                <label *ngIf="displayMarkerGroupRadio"
                       class="the-legend">Marker Groups&nbsp;</label>

            </div>

            <div class="row">

                <div *ngIf="displayUploader" class="col-md-8">
                    <uploader
                            [gobiiExtractFilterType]="gobiiExtractFilterType"
                            (onUploaderError)="handleStatusHeaderMessage($event)"></uploader>
                </div>

                <div *ngIf="displayListBox" class="col-md-8">
                    <text-area
                            (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"></text-area>
                </div>
                <div *ngIf="displayListBox" class="col-md-4">
                    <p class="text-warning">{{maxListItems}} maximum</p>
                </div>

                <div *ngIf="selectedListType == 'markerGroupsType'" class="col-md-8">
                    <checklist-box
                            [nameIdFilterParamTypes]="nameIdFilterParamTypesMarkerGroup"
                            [gobiiExtractFilterType]="gobiiExtractFilterType">
                    </checklist-box>
                </div>

            </div>

            <div>
                <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt"
                          modal="modal" width="300" height="300" responsive="true">
                    <p>A {{extractTypeLabelExisting}} is already selected. Do you want to remove it and specify a {{extractTypeLabelProposed}}
                        instead?</p>
                    <p-footer>
                        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                            <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)"
                                    label="No"></button>
                            <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)"
                                    label="Yes"></button>
                        </div>
                    </p-footer>
                </p-dialog>
            </div>
            <div>
                <p-dialog header="Maximum {{maxExceededTypeLabel}} Items Exceeded" [(visible)]="displayMaxItemsExceeded"
                          modal="modal" width="300" height="300" responsive="true">
                    <p>You attempted to paste more than {{maxListItems}} {{maxExceededTypeLabel}} items; Please reduce
                        the size of the list</p>
                </p-dialog>
            </div>`

})

export class SampleMarkerBoxComponent implements OnInit, OnChanges {

    public constructor(private store: Store<fromRoot.State>,
                       private fileItemService: FileItemService) {

    }

    public nameIdFilterParamTypesMarkerGroup:FileItemParamNames = FileItemParamNames.MARKER_GROUPS;


    public maxListItems: number = 200;
    public displayMaxItemsExceeded: boolean = false;
    public maxExceededTypeLabel: string;

    public displayChoicePrompt: boolean = false;
    public selectedListType: string = "itemFile";

    public displayUploader: boolean = true;
    public displayListBox: boolean = false;
    public displayMarkerGroupRadio: boolean = false;

    public gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    public onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    public extractTypeLabelExisting: string;
    public extractTypeLabelProposed: string;


    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    public handleTextBoxDataSubmitted(items: string[]) {

        if (items.length <= this.maxListItems) {

            let nonDuplicateItems: string[] = [];
            items.forEach(item => {

                if( !nonDuplicateItems.find(ii => ii === item)) {
                    nonDuplicateItems.push(item);
                }


            });

            let listItemType: ExtractorItemType =
                this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                    ExtractorItemType.MARKER_LIST_ITEM : ExtractorItemType.SAMPLE_LIST_ITEM;

            nonDuplicateItems.forEach(listItem => {

                if (listItem && listItem !== "") {

                    this.fileItemService
                        .loadFileItem(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                            .setExtractorItemType(listItemType)
                            .setItemId(listItem)
                            .setItemName(listItem), true);
                }
            });

        } else {

            if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];
            } else {
                this.handleStatusHeaderMessage(new HeaderStatusMessage("This control does not handle the currently selected item type: "
                    + GobiiExtractFilterType[this.gobiiExtractFilterType]
                    , null, null))
            }

            this.displayMaxItemsExceeded = true;
        }
    }


    private currentFileItems: GobiiFileItem[] = [];

    handleSampleMarkerChoicesExist(): boolean {

        let returnVal: boolean = false;

        this.store.select(fromRoot.getAllFileItems)
            .subscribe(fileItems => {

                    let extractorItemTypeListToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;
                    let extractorItemTypeFileToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;

                    if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                        extractorItemTypeListToFind = ExtractorItemType.SAMPLE_LIST_ITEM;
                        extractorItemTypeFileToFind = ExtractorItemType.SAMPLE_FILE;
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                        extractorItemTypeListToFind = ExtractorItemType.MARKER_LIST_ITEM;
                        extractorItemTypeFileToFind = ExtractorItemType.MARKER_FILE;
                    }

                    this.currentFileItems = fileItems.filter(item => {
                        return ( ( item.getExtractorItemType() === extractorItemTypeListToFind ) ||
                            (item.getExtractorItemType() === extractorItemTypeFileToFind) )
                    });

                    if (this.currentFileItems.length > 0) {

                        this.extractTypeLabelExisting = Labels.instance().treeExtractorTypeLabels[this.currentFileItems[0].getExtractorItemType()];

                        if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                            this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE];

                        } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

                            this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE];

                        } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                            this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];

                        } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE) {

                            this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
                        }

                        this.displayChoicePrompt = true;
                        returnVal = true;
                        // it does not seem that the PrimeNG dialog really blocks in the usual sense;
                        // so we have to chain what we do next off of the click events on the dialog.
                        // see handleUserChoice()

                    } else {

                    }
                },
                hsm => {
                    this.handleStatusHeaderMessage(hsm)
                }
            ).unsubscribe();

        return returnVal;

    }

    handleUserChoice(userChoice: boolean) {

        this.displayChoicePrompt = false;

        if (this.currentFileItems.length > 0 && userChoice === true) {

            // based on what _was_ the current item, we now make the current selection the other one
            if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                this.displayListBox = false;
                this.displayUploader = true;

                this.selectedListType = "itemFile";

            } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                this.displayListBox = true;
                this.displayUploader = false;

                this.selectedListType = "itemArray";

            }

            this.currentFileItems.forEach(currentFileItem => {

                currentFileItem.setProcessType(ProcessType.DELETE);
                this.fileItemService
                    .unloadFileItemFromExtract(currentFileItem);
            });
        } else {
            // we leave things as they are; however, because the user clicked a radio button,
            // we have to reset it to match the currently displayed list selector
            if (this.selectedListType === "itemFile") {

                this.displayListBox = true;
                this.displayUploader = false;

                this.selectedListType = "itemArray"

            } else if (this.selectedListType === "itemArray") {

                this.displayListBox = false;
                this.displayUploader = true;

                this.selectedListType = "itemFile"

            }

        } // if-else user answered "yes"
    }

    private handleTextBoxChanged(event) {

        // if there is no existing selected list or file, then this is just a simple setting
        if (this.handleSampleMarkerChoicesExist() === false) {

            this.displayListBox = true;
            this.displayUploader = false;

            // this.displayListBox = true;
            // this.displayUploader = false;

        }
    }

    private handleOnClickBrowse($event) {

        if (this.handleSampleMarkerChoicesExist() === false) {

            this.displayListBox = false;
            this.displayUploader = true;

            // this.displayListBox = false;
            // this.displayUploader = true;

        }
    }

    handleMarkerGroupChanged($event) {
        if (this.handleSampleMarkerChoicesExist() === false) {

            this.displayListBox = false;
            this.displayUploader = false;
        }
    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }


    ngOnInit(): any {

        if( this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ) {
            this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FileItemParamNames.MARKER_GROUPS,
                null);
        }

        return null;
    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                //this.notificationSent = false;

                if (this.gobiiExtractFilterType == GobiiExtractFilterType.BY_MARKER) {
                    this.displayMarkerGroupRadio = true;
                } else {
                    this.displayMarkerGroupRadio = false
                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges

}
