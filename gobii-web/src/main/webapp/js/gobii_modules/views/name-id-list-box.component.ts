import {RouteParams} from 'angular2/router';
import { Component, OnInit } from 'angular2/core';

import {NameId} from '../model/name-id';
import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service.ts';


@Component({
  selector: 'name-id-list-box',
  //directives: [RADIO_GROUP_DIRECTIVES]
templateUrl: `
		<select name="principleInvestigators">
			<option *ngFor="#nameId of nameIds" 
				value={{nameId.id}}>{{nameId.name}}</option>
		</select>
` // end template

})

export class NameIdListBoxComponent implements OnInit {
	
	

	private nameIds: NameId[];
	

	// TODO: Use interface so this component can be reused?
	constructor(
	  private _principleInvestigatorService: PrincipleInvestigatorService
	  ) {
	} // ctor
	
	ngOnInit() {
		
		this.nameIds = this._principleInvestigatorService.getNameIds();
/*
		let id = +this._routeParams.get('id');
		this._heroService.getHero(id)
		  .then(hero => this.hero = hero);
*/		  
	} // ngOnInit

}
