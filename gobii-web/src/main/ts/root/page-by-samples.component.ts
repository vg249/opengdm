import {RouteParams} from 'angular2/router';
import { Component, OnInit } from 'angular2/core';
//import {CORE_DIRECTIVES, FORM_DIRECTIVES, NgClass, NgStyle} from 'angular2/common';



@Component({
  selector: 'search-criteria-samples',
  templateUrl: 'js/gobii_modules/root/page-by-samples.component.html',
  //directives: [FileSelect]
//  directives: [FILE_UPLOAD_DIRECTIVES,FileUploader]
})

export class SearchCriteriaBySamplesComponent implements OnInit {
	
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
