import {Injectable} from "angular2/core";
import {NameId} from "../../model/name-id";
import {NameIdListService} from "./name-id-list.service";
import {Observable} from "rxjs/Observable";

@Injectable()
export class PrincipleInvestigatorService {


    constructor(private _nameIdListService:NameIdListService) {
        this._nameIdListService = _nameIdListService;
    } // ctor

    getPiNameIds():Observable<NameId[]> {

        // return Observable.create(observer => {
        //         observer.next([new NameId(1, 'from pisvc 1'),
        //             new NameId(2, 'from pisvc 2')]);
        //         observer.complete();
        //     }
        // );
        return Observable.create(observer => {
                this._nameIdListService.getNameIds()
                    .subscribe(nameIds => {
                            observer.next(nameIds);
                            observer.complete();
                        }
                    );
            }
        );
    } // getPiNameIds()

} 
