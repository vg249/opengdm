import {HttpClient, HttpResponse, HttpHeaders} from "@angular/common/http";
import {HeaderNames} from "./header-names";

export class HttpValues {

    public static S_FORBIDDEN = 403;

    public static makeTokenHeaders(token:string,gobiiCropType:string): Headers {

        let returnVal = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerToken, token);
        returnVal.append(HeaderNames.headerGobiiCrop, gobiiCropType);
        return returnVal;
    }

    public static makeContentHeaders(): Headers {
        let returnVal = new Headers();
        returnVal.append('Content-Type', 'application/json');
        returnVal.append('Accept', 'application/json');
        return returnVal;
    }

    public static makeLoginHeaders(userName:string,password): Headers {
        let returnVal: Headers = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerUserName, userName);
        returnVal.append(HeaderNames.headerPassword, password);
        return returnVal;
    }
}
