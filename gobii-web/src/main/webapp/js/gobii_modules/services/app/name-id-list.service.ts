import {Injectable} from 'angular2/core';

import {NameId} from '../../model/name-id';
import {Http, Response} from 'angular2/http';
import {Observable}     from 'rxjs/Observable';

@Injectable()
export class NameIdListService {


    constructor(private _http:Http) {
    }

    public getNameIds():Observable<NameId[]> {

        let requestBody = `
            "processType": "READ",
            "entityType": "DBTABLE",
            "entityName": "project",
            "filter": "2"        
            `;

        return this
            ._http
            .post("extract/nameidlist", requestBody)
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
