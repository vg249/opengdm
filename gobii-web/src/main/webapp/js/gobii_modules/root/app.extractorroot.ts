///<reference path="../../../../../../typings/index.d.ts"/>

import {Component} from "@angular/core";
import {HTTP_PROVIDERS} from "@angular/http";
import {ExportFormatComponent} from "../views/export-format.component";
import {SearchCriteriaBySamplesComponent} from "./page-by-samples.component";
import {PageByProjectComponent} from "./page-by-project.component";
import {PrincipleInvestigatorService} from "../services/app/principle-investigator.service";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {ContactsListBoxComponent} from "../views/contacts-list-box.component";
import {ProjectListBoxComponent} from "../views/project-list-box.component";
// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    directives: [ExportFormatComponent,
        ContactsListBoxComponent,
        ProjectListBoxComponent],
    styleUrls: ['/extractor-ui.css'],
    providers: [
        HTTP_PROVIDERS,
        AuthenticationService,
        DtoRequestService,
        PrincipleInvestigatorService
    ],
    template: `
        <div class = "panel panel-default">
        
           <div class = "panel-heading">
              <h1 class = "panel-title">GOBii Extractor</h1>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                    <div class="col-md-3"> 
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Principle Investigator</legend>
                        <contacts-list-box></contacts-list-box>
                        </fieldset>
                        <fieldset class="well the-fieldset">
                        <legend class="the-legend">Projects</legend>
                        <project-list-box></project-list-box>
                        </fieldset>
                        
                    </div>  <!-- outer grid column 1-->
                    <div class="col-md-5"> 
                            <div class="row">
                                <div class="col-md-12">							
                                    <page-by-project></page-by-project>
                                </div>
                            </div> <!-- inner grid row 1 -->
                            
                            <div class="row">
                                <div class="col-md-12">
                                    <export-format></export-format>
                                </div>
                            </div> <!-- inner grid row 2 -->
        
                            <div class="row">
                                <div class="col-md-12">
                                    SEARCH BUTTON GOES HERE
                                </div>
                            </div> <!-- inner grid row 3 -->
        
        
                         
                    </div>  <!-- outer grid column 2 (inner grid)-->
                    
                    <div class="col-md-4">
                        FILTERS GO HERE
                    </div>  <!-- outer grid column 3-->
                    
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div> 
	` // end template
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
    title = 'Gobii Web';

    constructor() {
        let foo = "foo";
    }

}

