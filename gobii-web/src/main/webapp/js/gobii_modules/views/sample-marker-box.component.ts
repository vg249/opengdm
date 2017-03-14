import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {SampleMarkerList} from "../model/sample-marker-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";

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
                            value="uploadFile" 
                            checked="checked">&nbsp;File
                    </div> 
                    
                    <div class="col-md-8">
                        <uploader
                        [gobiiExtractFilterType] = "gobiiExtractFilterType"
                        (onUploaderError)="handleStatusHeaderMessage($event)"></uploader>
                    </div> 
                    
                 </div>
                 
                <div class="row">
                
                    <div class="col-md-2">
                        <input type="radio" 
                            (change)="handleListTypeSelected($event)" 
                            name="listType" 
                            value="pasteList" >&nbsp;List
                    </div> 
                    
                    <div class="col-md-8">
                        <text-area
                        (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"></text-area>
                    </div> 
                    
                 </div>
                 
`

})

export class SampleMarkerBoxComponent implements OnInit {

    public constructor(private _fileModelTreeService: FileModelTreeService) {

    }


    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onMarkerSamplesCompleted: EventEmitter<SampleMarkerList> = new EventEmitter();
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

            this._fileModelTreeService
                .put(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                    .setExtractorItemType(listItemType)
                    .setItemId(listItem)
                    .setItemName(listItem))
                .subscribe(null, headerStatusMessage => {
                    this.handleStatusHeaderMessage(headerStatusMessage)
                });
        });


    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }


    ngOnInit(): any {
        return null;
    }

}
