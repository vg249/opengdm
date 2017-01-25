import {Component, OnInit, EventEmitter} from '@angular/core';


@Component({
    selector: 'export-type',
    outputs: ['onExportTypeSelected'],
    template: `<label class="the-label">Export By:&nbsp;</label>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="byDataSet" checked="checked">Data Set&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="bySample">Sample&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="byMarker" >Marker&nbsp;` // end template
})

export class ExportTypeComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
        //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onExportTypeSelected:EventEmitter<string> = new EventEmitter();

    private handleExportTypeSelected(arg) {
        if (arg.srcElement.checked) {

            this.onExportTypeSelected.emit(arg.srcElement.value)
        }
    }


    ngOnInit() {
    }

}
