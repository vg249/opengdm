import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import { map } from 'rxjs/operators';
import { from } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';


import {DtoHeaderAuth} from "../../model/dto-header-auth";
import {HttpValues} from "../../model/http-values";

@Injectable()
export class AuthenticationService {


    constructor(private _http: HttpClient,
                private _keycloakService: KeycloakService) {
    }

    private defaultUser: string = 'USER_READER';
    private defaultPassword: string = 'reader';
    private token: string = null;
    private userName: string = null;
    private _gobiiCropType: string;
    //private authUrl: string = "gobii/v1/auth";


    public getToken() {
        return from(this._keycloakService.getToken());

        //return this.token;

    } // getToken()

    private setToken(token: string) {
        this.token = token;
    }

    public loadUserProfile() {
        from(this._keycloakService.loadUserProfile()).subscribe(
            profile => { console.log("User Profile loaded");}
        );
    }


    public getGobiiCropType(): string {
        return "dev";
        //return this._gobiiCropType;
    }

    // private setGobiiCropType(gobiiCropType: string) {
    //     this._gobiiCropType = gobiiCropType;
    // }

    // public getUserName(): string {
    //     return this.userName;
    // }
    public getUserName() {
        return this._keycloakService.getUsername();
    }

    // public authenticate(userName: string, password: string): Observable<DtoHeaderAuth> {

    //     let loginUser = userName ? userName : this.defaultUser;
    //     let loginPassword = password ? password : this.defaultPassword;

    //     let scope$ = this;
    //     let requestBody = JSON.stringify("nothing");
    //     let headers = HttpValues.makeLoginHeaders(loginUser, loginPassword);

    //     return Observable.create(observer => {
    //             this
    //                 ._http
    //                 .post(scope$.authUrl, requestBody, {headers: headers})
    //                 .pipe(
    //                     map((response: any) => response.json())
    //                 )
    //                 .subscribe(json => {
    //                         let dtoHeaderAuth: DtoHeaderAuth = DtoHeaderAuth
    //                             .fromJSON(json);
    //                         if (dtoHeaderAuth.getToken()) {
    //                             scope$.userName = userName.trim();
    //                             scope$.setToken(dtoHeaderAuth.getToken())
    //                             scope$.setGobiiCropType(dtoHeaderAuth.getGobiiCropType());
    //                             observer.next(dtoHeaderAuth);
    //                             observer.complete();
    //                         } else {
    //                             observer.error("No token was provided by server");
    //                         }
    //                     },
    //                     json => {

    //                         let message: string = json.status
    //                             + ": "
    //                             + json.statusText;

    //                         if (Number(json.status) == HttpValues.S_FORBIDDEN) {
    //                             message += ": " + json._body;
    //                         }

    //                         observer.error(message);

    //                     }); // subscribe
    //         } // observer callback
    //     ); // Observer.create() 


    // } // authenticate() 

}
/*
 // doing a plain xhr request also does not allow access to token response
 var xhr = new XMLHttpRequest();
 var url = "load/auth";
 xhr.open("POST", url, true);
 xhr.setRequestHeader('Content-Type', 'application/json');
 xhr.setRequestHeader('Accept', 'application/json');
 xhr.setRequestHeader(HeaderNames.headerUserName, loginUser);
 xhr.setRequestHeader(HeaderNames.headerPassword, loginPassword);

 xhr.onreadystatechange = function() {//Call a function when the state changes.
 if(xhr.readyState == 4 && xhr.status == 200) {
 console.log(xhr.responseText);
 }
 }
 xhr.send(null);
 */