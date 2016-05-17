import { Component, OnInit } from 'angular2/core';

import {NameIdListBoxComponent} from '../views/name-id-list-box.component';


@Component({
  selector: 'page-by-project',
  templateUrl: '../root/page-by-project.component.html',
  directives: [NameIdListBoxComponent]

})

export class PageByProjectComponent implements OnInit {
	
	constructor(
//	  private _heroService: HeroService,
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
