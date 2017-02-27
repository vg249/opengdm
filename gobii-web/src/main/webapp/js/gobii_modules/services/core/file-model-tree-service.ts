import {Injectable} from "@angular/core";
import {HttpValues} from "../../model/http-values";
import {Http} from "@angular/http";
import {AuthenticationService} from "./authentication.service";
import {DtoRequestItem} from "./dto-request-item";
import {DtoHeaderResponse} from "../../model/dto-header-response";
import {PayloadEnvelope} from "../../model/payload/payload-envelope";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import {FileItem} from "../../model/file-item";
import {FileModelState, FileModelTreeEvent} from "../../model/file-model-tree-event";

@Injectable()
export class FileModelTreeService {


    constructor() {
    }


    private mutate(fileItem: FileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = null;


        return returnVal;
    }

    public put(fileItem: FileItem): Observable < FileModelTreeEvent > {

        return Observable.create(observer => {

            let fileTreeEvent: FileModelTreeEvent = this.mutate(fileItem);

            observer.next(fileTreeEvent);
            observer.complete();
        });
    }

}
