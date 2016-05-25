import {Injectable} from 'angular2/core';

import {NameId} from '../../model/name-id';
import {Http, Response, Headers} from 'angular2/http';
import {Observable}     from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class NameIdListService {


    constructor(private _http:Http) {
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


        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');


        return this
            ._http
            .post("load/nameidlist", requestBody, {headers: headers})
            .map(this.handleResponse)
            .catch(this.handleError);
    }

    private handleResponse(response:Response) {

        if (response.status < 200 || response.status > 300) {
            throw new Error('Bad response status: ' + response.status);
        }

        let payload = response.json;
        console.log(payload);
        return [];
    }

    private handleError(error:any) {
        let errorMessage = error.message;
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }
} 
