///<reference path="../../../../node_modules/angular2/typings/browser.d.ts"/>

// Angular Imports
import { Component }       from 'angular2/core';
import { HTTP_PROVIDERS }    from 'angular2/http';
// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports
import { ExportFormatComponent } from '../views/export-format.component';
import { SearchCriteriaBySamplesComponent } from '../root/page-by-samples.component';
import { PageByProjectComponent } from '../root/page-by-project.component';

import {PrincipleInvestigatorService} from '../services/app/principle-investigator.service';
import {NameIdListService} from '../services/app/name-id-list.service';

	
@Component({
	selector: 'extractor-root',
	styleUrls: ['/extractor-ui.css'],
	templateUrl: 'root/app.extractorroot.html',
	directives: [ExportFormatComponent, 
	SearchCriteriaBySamplesComponent,
	PageByProjectComponent],
	providers: [
		HTTP_PROVIDERS,
		PrincipleInvestigatorService,
		NameIdListService,
	]  
}) // @Component

/*
@RouteConfig([

	  {
		path: '/dashboard',
		name: 'Dashboard',
		component: DashboardComponent,
		useAsDefault: true
	},
	{
		path: '/heroes',
		name: 'Heroes',
		component: HeroesComponent
	  },
	  {
		path: '/detail/:id',
		name: 'HeroDetail',
		component: HeroDetailComponent
	},

]) // @RouteConfig
*/
	
export class ExtractorRoot {
  title = 'Tour of Heroes';
}

