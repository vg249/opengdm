import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {HttpValues} from "../../model/http-values";
import {Http, Response, Headers} from "@angular/http";
import {AuthenticationService} from '../core/authentication.service';
import {DtoRequestItem} from "./dto-request-item";

import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/complete';
import 'rxjs/add/observable/next';


@Injectable()
export class DtoRequestService<T> {


    constructor(private _http:Http,
                private _authenticationService:AuthenticationService) {
    }


    public getAString():string {
        return 'a string';
    }

    nameIds:NameId[];

    public getNameIds(dtoRequestItem:DtoRequestItem<T>):Observable < T > {

        return Observable.create(observer => {

            this._authenticationService
                .getToken()
                .subscribe(token => {

                    let headers = HttpValues.makeTokenHeaders(token);

                    this._http
                        .post(dtoRequestItem.getUrl(),
                            dtoRequestItem.getRequestBody(),
                            {headers: headers})
                        .map(response => response.json())
                        .subscribe(json => {

                            let result = dtoRequestItem.resultFromJson(json);
                            observer.next(result);
                            observer.complete();

                        }) // subscribe http

                }); // subscribe get authentication token

        }); // observable

    } // getPiNameIds()

}
