import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestService} from "./../core/dto-request.service";
import {Observable} from "rxjs/Observable";
import {DtoRequestItemNameIds} from "./dto-request-item-nameids";
import {EntityType} from "../../model/type-entity";
import {ProcessType} from "../../model/type-process";

@Injectable()
export class PrincipleInvestigatorService {


    constructor(private _nameIdListService:DtoRequestService<NameId[]>) {
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
                this._nameIdListService.getNameIds(new DtoRequestItemNameIds(ProcessType.READ,
                    EntityType.DataSetNames))
                    .subscribe(nameIds => {
                            observer.next(nameIds);
                            observer.complete();
                        }
                    );
            }
        );
    } // getPiNameIds()

} 
