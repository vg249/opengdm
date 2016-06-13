//import {RouteParams} from '@angular/router-deprecated';
import {Component, OnInit, EventEmitter} from "@angular/core";
import {ServerConfig} from "../model/server-config";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import * as EntityFilters from "../model/type-entity-filter";
import {GobiiCropType} from "../model/type-crop";


@Component({
    selector: 'crops-list-box',
    outputs: ['onServerSelected'],
    template: `<select name="serverConfigs" (change)="handleServerSelected($event)" >
			<option *ngFor="let serverConfig of serverConfigList" 
				value={{serverConfig.domain}}>{{serverConfig.crop}}</option>
		</select>
` // end template

})

export class CropsListBoxComponent implements OnInit {


    // useg
    private serverConfigList:ServerConfig[];

    private onServerSelected:EventEmitter<ServerConfig> = new EventEmitter();

    private handleServerSelected(arg) {
        this.onServerSelected.emit(this.serverConfigList[arg.srcElement.selectedIndex]);
    }

    constructor(private _dtoRequestService:DtoRequestService<ServerConfig[]>) {

        let scope$ = this;
        _dtoRequestService.getItemList(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {
                if (serverConfigs && ( serverConfigs.length > 0 )) {
                    scope$.serverConfigList = serverConfigs;
                } else {
                    scope$.serverConfigList = [new ServerConfig("<undefined>","<undefined>",0)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
            });

    } // ctor


    ngOnInit():any {
        return null;
    }
}
