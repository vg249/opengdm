import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {SampleMarkerList} from "../model/sample-marker-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";

@Component({
    selector: 'sample-marker-box',
    outputs: ['onMarkerSamplesCompleted', 'onSampleMarkerError'],
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


    private onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onMarkerSamplesCompleted: EventEmitter<SampleMarkerList> = new EventEmitter();
    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    private handleTextBoxDataSubmitted(arg) {

        let sampleMarkerList: SampleMarkerList = new SampleMarkerList(true, arg, null);
        this.onMarkerSamplesCompleted.emit(sampleMarkerList);

    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }


    ngOnInit(): any {
        return null;
    }

}
