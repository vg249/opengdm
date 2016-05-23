import {RouteParams} from 'angular2/router';
import { Component, OnInit } from 'angular2/core';


@Component({
  selector: 'export-format',
//  inputs: ['hero'],
  //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
	template: `
		<form>
			<fieldset class="well the-fieldset">
			<legend class="the-legend">Export Format</legend>
				<div class="control-group">
							  <input type="radio" name="Hapmap" value="Hapmap">Hapmap<br>
							  <input type="radio" name="FlapJack" value="FlapJack">FlapJack<br>
							  <input type="radio" name="VCF" value="VCF">VCF<br>
							  <input type="radio" name="HDF5" value="HDF5">HDF5<br>
							  <input type="radio" name="PLINK CSV" value="PLINK CSV">PLINK CSV<br>
				</div>
			</fieldset>
			
		</form>
	` // end template
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
