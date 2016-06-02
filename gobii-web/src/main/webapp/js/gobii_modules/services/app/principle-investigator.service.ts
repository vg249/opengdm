import {Injectable} from "angular2/core";
import {NameId} from "../../model/name-id";
import {NameIdListService} from "./name-id-list.service";
import {Observable} from "rxjs/Observable";

@Injectable()
export class PrincipleInvestigatorService {


    constructor(private _nameIdListService:NameIdListService) {
        this._nameIdListService = _nameIdListService;
    } // ctor

    private nameIds:NameId[];
    private error:string;

    getNameIds():NameId[] {

        let astring:string = this._nameIdListService.getAString();


        this._nameIdListService.getNameIds()
            .subscribe(nameIds => this.nameIds = nameIds);
        

        //let astring:string =  _nameIdListService.get

        // let observable:Observable<NameId[]> = this._nameIdListService.getNameIds();
        // //observable.subscribe()
        //
        // let nameIdsReceived: NameId[] = [];
        // this._nameIdListService.getNameIds()
        //     .map(nameIds => nameIds)
        //     .subscribe(nameIds => nameIdsReceived = nameIds);

        // let nameIds: NameId[] = [
        // 	{"id": 11, "name": "Mr. Nice"},
        // 	{"id": 12, "name": "Narco"},
        // 	{"id": 13, "name": "Bombasto"},
        // 	{"id": 14, "name": "Celeritas"},
        // 	{"id": 15, "name": "Magneta"},
        // 	{"id": 16, "name": "RubberMan"},
        // 	{"id": 17, "name": "Dynama"},
        // 	{"id": 18, "name": "Dr IQ"},
        // 	{"id": 19, "name": "Magma"},
        // 	{"id": 20, "name": "Tornado"}
        // ];		

        return this.nameIds;
    }

} 
