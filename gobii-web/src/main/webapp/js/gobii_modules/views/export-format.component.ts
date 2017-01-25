import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit,EventEmitter} from '@angular/core';


@Component({
    selector: 'export-format',
    outputs: ['onFormatSelected'],
//  inputs: ['hero'],
    //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
    template: `
    		  <label class="the-label">Select Format:</label><BR>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="Hapmap" checked="checked">Hapmap<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="FlapJack">FlapJack<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleContactSelected($event)" name="format" value="MetaDataOnly">Dataset Metadata Only<br>
	` // end template
})

export class ExportFormatComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
                //	  private _routeParams: RouteParams
    ) {
    } // ctor

    private onFormatSelected:EventEmitter<string> = new EventEmitter();
    private handleContactSelected(arg) {
        if( arg.srcElement.checked ) {
        
            this.onFormatSelected.emit(arg.srcElement.value)
        }
        let foo = arg;
        //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }


    ngOnInit() {
        /*
         let id = +this._routeParams.get('id');
         this._heroService.getHero(id)
         .then(hero => this.hero = hero);
         */
    }

}
