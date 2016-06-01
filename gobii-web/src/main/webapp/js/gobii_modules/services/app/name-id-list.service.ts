import {Injectable} from "angular2/core";
import {NameId} from "../../model/name-id";
import {Http, Response, Headers} from "angular2/http";
import {Observable} from "rxjs/Observable";
import {AuthenticationService} from '../core/authentication.service';
//import {DtoHeaderAuth} from "../../model/dto-header-auth";

import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import {HeaderNames} from "../../model/header-names";

@Injectable()
export class NameIdListService {


    constructor(private _http:Http,
                private _authenticationService:AuthenticationService) {
    }

    public getNameIds():Observable<NameId[]> {

        let requestBody = JSON.stringify({
            "processType": "READ",
            "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
            "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
            "entityType": "DBTABLE",
            "entityName": "datasetnames",
            "namesById": {},
            "filter": null
        });


        let token:string = this._authenticationService.getToken();
        if (!token) {
            this._authenticationService
                .authenticate(null, null)
                .subscribe(h => {
                        token = h.getToken();
                    },
                    error => console.log(error.message))
        }

        if (!token) {
            Observable.throw(Error("no authentication token"));
        }

        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        headers.append(HeaderNames.headerToken, token);

        return this
            ._http
            .post("load/nameidlist", requestBody, {headers: headers})
            .map(response => {
                let payload = response.json();
                console.log(payload);
                console.log(response.headers);
                return [];
            });

    }

}
