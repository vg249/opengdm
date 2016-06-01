import {Injectable} from "angular2/core";
import {Http, Response, Headers} from "angular2/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import 'rxjs/add/observable/throw';
import {DtoHeaderAuth} from "../../model/dto-header-auth";
import {HeaderNames} from "../../model/header-names";


@Injectable()
export class AuthenticationService {


    constructor(private _http:Http) {
    }

    private defaultUser:string = 'USER_READER';
    private defaultPassword:string = 'reader';
    private token:string;

    public getToken():string {
        return this.token;
    }

    public authenticateDefault():Observable<DtoHeaderAuth> {
        return this.authenticate(null, null);
    }

    public authenticate(userName:string, password:string):Observable<DtoHeaderAuth> {

        // let requestBody = JSON.stringify({
        //     "processType": "READ",
        //     "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
        //     "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
        //     "entityType": "DBTABLE",
        //     "entityName": "datasetnames",
        //     "namesById": {},
        //     "filter": null
        // });
        //
        //

        let requestBody = JSON.stringify("nothing");
        let loginUser = userName ? userName : this.defaultUser;
        let loginPassword = password ? password : this.defaultPassword;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        headers.append(HeaderNames.headerUserName, loginUser);
        headers.append(HeaderNames.headerPassword, loginPassword);

        return this
            ._http
            .post("load/auth",requestBody, {headers: headers})
            .map(response => {

                let body = response.json();
                console.log(body);

//                response.headers.append(HeaderNames.headeraccessControl,HeaderNames.headerToken);
//                 let header:any = response.headers[HeaderNames.headerToken];
//                 if (header) {
//                     this.token = header.valueOf();
//                     if (!this.token) {
//                         //return Observable Observable.er("The token header does not have a value"));
//                         throw "The token header does not have a value";
//                     }
//                 } else {
//                     Observable.throw(Error("No token header was returned"));
//                 }

                return new DtoHeaderAuth(loginUser, null, this.token);

            }); // map()


    } // authenticate() 

}
