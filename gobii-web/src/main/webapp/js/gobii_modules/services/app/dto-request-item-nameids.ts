import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestItem} from "./../core/dto-request-item";


@Injectable()
export class DtoRequestItemNameIds implements DtoRequestItem<NameId[]>{


    public getUrl():string {
        return "load/nameidlist";
    } // getUrl()

    public getRequestBody(): string {
        return JSON.stringify({
            "processType": "READ",
            "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
            "dtoHeaderResponse": {"succeeded": true, "statusMessages": []},
            "entityType": "DBTABLE",
            "entityName": "datasetnames",
            "namesById": {},
            "filter": null
        })
    }

    // public getRequestBody(): string {
    //     return JSON.stringify({
    //         "processType": "READ",
    //         "dtoHeaderAuth": {"userName": null, "password": null, "token": null},
    //         "entityType": "DBTABLE",
    //         "entityName": "datasetnames",
    //         "filter": null
    //     })
    // }

    public resultFromJson(json):NameId[] {

        let returnVal:NameId[] = [];
        console.log(json);

        let arrayOfIds = Object.keys(json.namesById);
        arrayOfIds.forEach(id => {
            let currentVal:string = json.namesById[id];
            returnVal.push(new NameId(id, currentVal));
        });

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }
    
    
} // DtoRequestItemNameIds() 







