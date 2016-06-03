import {Component, OnInit} from '@angular/core';

import {NameIdListBoxComponent} from '../views/name-id-list-box.component';


@Component({
    selector: 'page-by-project',
    directives: [NameIdListBoxComponent],
    template: `
            <form>
                <fieldset class="well the-fieldset">
                <legend class="the-legend">Search Criteria</legend>
                
                <name-id-list-box></name-id-list-box>
                
                </fieldset>
            </form>
    ` // end template
})

export class PageByProjectComponent implements OnInit {

    constructor(//	  private _heroService: HeroService,
                //	  private _routeParams: RouteParams
    ) {
    } // ctor

    ngOnInit() {
        /*
         let id = +this._routeParams.get('id');
         this._heroService.getHero(id)
         .then(hero => this.hero = hero);
         */
    }

}
