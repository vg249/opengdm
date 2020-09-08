import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import { map } from 'rxjs/operators';
import { from } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';

@Injectable()
export class AuthenticationService {


    constructor(private _http: HttpClient,
                private _keycloakService: KeycloakService) {
    }

    private defaultUser: string = 'USER_READER';
    private defaultPassword: string = 'reader';
    private token: string = null;
    private userName: string = null;
    private userEmail: string = null;
    private profileLoaded: boolean = false;
    private _gobiiCropType: string = "";
    //private authUrl: string = "gobii/v1/auth";


    public getToken() {
        return from(this._keycloakService.getToken());

        //return this.token;

    } // getToken()

    private setToken(token: string) {
        this.token = token;
    }

    public loadUserProfile() {
        let scope$ = this;
        from(this._keycloakService.loadUserProfile()).subscribe(
            profile => {
                scope$.profileLoaded = true;
                scope$.userEmail = profile.email;
            }
        );
    }


    public getGobiiCropType(): string {
        //return "dev";
        return this._gobiiCropType;
    }

    public setGobiiCropType(gobiiCropType: string) {
         this._gobiiCropType = gobiiCropType;
    }

    // public getUserName(): string {
    //     return this.userName;
    // }
    public getUserName() {
        return this._keycloakService.getUsername();
    }

    public getUserEmail() {
        return this.userEmail;
    }

    public isProfileLoaded() {
        return this.profileLoaded;
    }
    
}