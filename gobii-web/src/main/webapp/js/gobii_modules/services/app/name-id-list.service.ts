import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {HttpValues} from "../../model/http-values";
import {Http, Response, Headers} from "@angular/http";
import {AuthenticationService} from '../core/authentication.service';
//import {DtoHeaderAuth} from "../../model/dto-header-auth";

import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/complete';
import 'rxjs/add/observable/next';


@Injectable()
export class NameIdListService {


    constructor(private _http:Http,
                private _authenticationService:AuthenticationService) {
    }


    public getAString():string {
        return 'a string';
    }

    nameIds:NameId[];

    public getNameIds():Observable < NameId[] > {

        return Observable.create(observer => {

            let requestBody = JSON.stringify({
                "processType": "READ",
                "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
                "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
                "entityType": "DBTABLE",
                "entityName": "datasetnames",
                "namesById": {},
                "filter": null
            });

            let scope$ = this;
            this._authenticationService
                .getToken()
                .subscribe(token => {

                    let headers = HttpValues.makeTokenHeaders(token);

                    this._http
                        .post("load/nameidlist", requestBody, {headers: headers})
                        .map(response => response.json())
                        .subscribe(json => {

                            scope$.nameIds = scope$.mapToNameIds(json);
                            observer.next(scope$.nameIds);
                            observer.complete();

                        }) // subscribe http

                }); // subscribe get authentication token

        }); // observable

    } // getPiNameIds()


    private mapToNameIds(json):NameId[] {

        let returnVal:NameId[] = [];
        console.log(json);

        let arrayOfIds = Object.keys(json.namesById);
        arrayOfIds.forEach(id => {
            let currentVal:string = json.namesById[id];
            returnVal.push(new NameId(id, currentVal));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }

    public getFake():Observable<NameId[]> {
        let scope$ = this;
        return Observable.create(observer => {
            console.log("got fake");
            observer.next(scope$.mapToNameIds(JSON.parse('{"foo" : "bar"}')));
            observer.complete();
        });
    }


}
