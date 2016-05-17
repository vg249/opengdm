import {RouteParams} from 'angular2/router';
import { Component, OnInit } from 'angular2/core';


@Component({
  selector: 'export-format',
//  inputs: ['hero'],
  templateUrl: '/views/export-format.component.html',
  //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
})

export class ExportFormatComponent implements OnInit {
	
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
