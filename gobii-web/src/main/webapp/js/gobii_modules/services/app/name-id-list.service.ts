import {Injectable} from "angular2/core";
import {NameId} from "../../model/name-id";
import {HttpValues} from "../../model/http-values";
import {Http, Response, Headers} from "angular2/http";
import {Observable} from "rxjs/Observable";
import {AuthenticationService} from '../core/authentication.service';
//import {DtoHeaderAuth} from "../../model/dto-header-auth";

import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';


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

        let requestBody = JSON.stringify({
            "processType": "READ",
            "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
            "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
            "entityType": "DBTABLE",
            "entityName": "datasetnames",
            "namesById": {},
            "filter": null
        });

        // **************************
        // this works:
        // return Observable.create(observer => {
        //     observer.next(this.mapToNameIds(JSON.parse('{"foo":"bar"}')));
        //     observer.complete();
        // })
        // ***************************

        let scope$ = this;
        let existingToken:string = this._authenticationService.getToken();
        if (existingToken) {
            let headers = HttpValues.makeTokenHeaders(existingToken);
            return Observable.create(observer => {
                    // observer.next(scope$.mapToNameIds(JSON.parse('{"foo" : "bar"}')));
                    // observer.complete();

                    this._http
                        .post("load/nameidlist", requestBody, {headers: headers})
                        .map(response => response.json())
                        .subscribe(json => {

                            scope$.nameIds = scope$.mapToNameIds(json);
                            observer.next(scope$.nameIds);
                            observer.complete();
                        })
                }
            ); // observer.create

        } else {
            return Observable.create(observer => {
                this._authenticationService
                    .authenticate(null, null)
                    .map(h => h.getToken())
                    .subscribe(token => {
                            let newTokenHeaders:Headers =
                                HttpValues.makeTokenHeaders(token);
                            scope$._http
                                .post("load/nameidlist", requestBody, {headers: newTokenHeaders})
                                .map(response => response.json())
                                .subscribe(json => {

                                    scope$.nameIds = scope$.mapToNameIds(json);
                                    observer.next(scope$.nameIds);
                                    observer.complete();

                                })

                        },
                        error => console.log(error.message));

            }); // observer create

        } // if-else we have a token

    } // getPiNameIds()


    private mapToNameIds(json:JSON):NameId[] {
        // for now, log the json and create a fake list
        console.log(json);
        return [new NameId(1, 'foo'), new NameId(2, 'bar')];
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
