import {Component, OnInit, EventEmitter} from '@angular/core';


@Component({
    selector: 'export-type',
    outputs: ['onExportTypeSelected'],
    template: `<fieldset class="well .form-group">
			    <legend class="the-legend">Export By</legend>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="byDataSet" checked="checked">Data Set&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="bySample">Sample&nbsp;
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="byMarker" >Marker&nbsp;
			</fieldset>` // end template
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
        let foo = arg;
    }


    ngOnInit() {
        /*
         let id = +this._routeParams.get('id');
         this._heroService.getHero(id)
         .then(hero => this.hero = hero);
         */
    }

}
