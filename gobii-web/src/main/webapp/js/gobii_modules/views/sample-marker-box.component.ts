import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {SampleMarkerList} from "../model/sample-marker-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {Labels} from "./entity-labels";

@Component({
    selector: 'sample-marker-box',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleMarkerError'],
    template: `<div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-2"> 
                        <input type="radio" 
                            (change)="handleListTypeSelected($event)" 
                            name="listType" 
                            value="itemFile" 
                            [checked]="uploadSelected">&nbsp;File
                    </div> 
                    
                    <div class="col-md-8">
                        <uploader
                        [gobiiExtractFilterType] = "gobiiExtractFilterType"
                        (onUploaderError)="handleStatusHeaderMessage($event)"
                        (onClickBrowse)="handleOnClickBrowse($event)"></uploader>
                    </div> 
                    
                 </div>
                 
                <div class="row">
                
                    <div class="col-md-2">
                        <input type="radio" 
                            (change)="handleListTypeSelected($event)" 
                            name="listType" 
                            value="itemArray"
                            [checked]="listSelected">&nbsp;List
                    </div> 
                    
                    <div class="col-md-8">
                        <text-area
                        (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"
                        (onTextboxClicked)="handleTextBoxChanged($event)"></text-area>
                    </div> 
                    
                 </div>
                 <div>
                    <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt" modal="modal" width="300" height="300" responsive="true">
                        <p>A {{extractTypeLabelExisting}} is already selected. Do you want to remove it and specify a {{extractTypeLabelProposed}} instead?</p>
                            <p-footer>
                                <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                                    <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)" label="No"></button>
                                    <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)" label="Yes"></button>
                                </div>
                            </p-footer>
                    </p-dialog>
                  </div>
`

})

export class SampleMarkerBoxComponent implements OnInit {

    public constructor(private _fileModelTreeService: FileModelTreeService) {

    }

    private displayChoicePrompt: boolean = false;

    private uploadSelected: boolean = true;
    private listSelected: boolean = false;

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onMarkerSamplesCompleted: EventEmitter<SampleMarkerList> = new EventEmitter();

    private extractTypeLabelExisting: string;
    private extractTypeLabelProposed: string;

    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    private handleTextBoxDataSubmitted(items: string[]) {

        let listItemType: ExtractorItemType =
            this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                ExtractorItemType.MARKER_LIST_ITEM : ExtractorItemType.SAMPLE_LIST_ITEM;

        items.forEach(listItem => {

            if (listItem && listItem !== "") {

                this._fileModelTreeService
                    .put(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                        .setExtractorItemType(listItemType)
                        .setItemId(listItem)
                        .setItemName(listItem))
                    .subscribe(null, headerStatusMessage => {
                        this.handleStatusHeaderMessage(headerStatusMessage)
                    });
            }
        });
    }


    private currentFileItem:GobiiFileItem;
    handleListTypeSelected() {

        this._fileModelTreeService.getFileItems(this.gobiiExtractFilterType).subscribe(
            fileItems => {

                let extractorItemTypeListToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;
                let extractorItemTypeFileToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    extractorItemTypeListToFind = ExtractorItemType.SAMPLE_LIST_ITEM;
                    extractorItemTypeFileToFind = ExtractorItemType.SAMPLE_FILE;
                } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    extractorItemTypeListToFind = ExtractorItemType.MARKER_LIST_ITEM;
                    extractorItemTypeFileToFind = ExtractorItemType.MARKER_FILE;
                }

                this.currentFileItem = fileItems.find(item => {
                    return ( ( item.getExtractorItemType() === extractorItemTypeListToFind ) ||
                    (item.getExtractorItemType() === extractorItemTypeFileToFind) )
                });

                if (this.currentFileItem) {

                    this.extractTypeLabelExisting = Labels.instance().treeExtractorTypeLabels[this.currentFileItem.getExtractorItemType()];

                    if (this.currentFileItem.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE];

                    } else if (this.currentFileItem.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE];

                    } else if (this.currentFileItem.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];

                    } else if (this.currentFileItem.getExtractorItemType() === ExtractorItemType.MARKER_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
                    }

                    this.displayChoicePrompt = true;
                    // it does not seem that the PrimeNG dialog really blocks in the usual sense; 
                    // so we have to chain what we do next off of the click events on the dialog.
                    // see handleUserChoice() 

                }
            },
            hsm => {
                this.handleStatusHeaderMessage(hsm)
            }
        );

        // if (event.currentTarget.defaultValue === "itemArray") {
        //
        // } else if (event.currentTarget.defaultValue == "itemFile") {
        //
        // }

    }

    handleUserChoice(userChoice:boolean) {
        
        this.displayChoicePrompt = false;

        if (this.currentFileItem && userChoice === true) {

            this.currentFileItem.setProcessType(ProcessType.DELETE);
            this._fileModelTreeService
                .put(this.currentFileItem)
                .subscribe(fmte => {

                    // based on what _was_ the current item, we now make the current selection the other one
                    if(this.currentFileItem.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                    || this.currentFileItem.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM ) {
                        this.listSelected = false;
                        this.uploadSelected= true;
                    } else if(this.currentFileItem.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                        || this.currentFileItem.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE )  {
                        this.listSelected = true;
                        this.uploadSelected= false;
                    }


                }, headerStatusMessage => {
                    this.handleStatusHeaderMessage(headerStatusMessage)
                });
        }
    }

    private handleTextBoxChanged(event) {

        this.handleListTypeSelected();


    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }


    private handleOnClickBrowse($event) {

        this.handleListTypeSelected();

    }

    ngOnInit(): any {

//        this.extractTypeLabel = Labels.instance().extractorFilterTypeLabels[this.gobiiExtractFilterType];
        return null;
    }

}
