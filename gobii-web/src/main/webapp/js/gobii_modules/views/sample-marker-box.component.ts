//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {CheckBoxEvent} from "../model/event-checkbox";
import {ProcessType} from "../model/type-process";


@Component({
    selector: 'sample-marker-box',
    // inputs: ['dataSetCheckBoxEvents'],
    // outputs: ['onItemUnChecked', 'onItemSelected'],
    template: `	<form method="POST" action="/gobii-dev/gobii/v1/uploadfile/" enctype="multipart/form-data">
                    File to upload: <input type="file" name="file"><br /> 
                    Name: <input type="text" name="name"><br /> <br /> 
                    <input type="submit" value="Upload"> Press here to upload the file!
            </form>`

})

export class SampleMarkerBoxComponent implements OnInit {


    // useg
    // private dataSetCheckBoxEvents:CheckBoxEvent[] = [];
    // private onItemUnChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    // private onItemSelected:EventEmitter<number> = new EventEmitter();

    constructor() {
    } // ctor


    ngOnInit():any {
        return null;
    }


}
