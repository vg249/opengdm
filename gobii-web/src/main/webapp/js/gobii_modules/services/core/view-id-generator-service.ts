import {FilterParamNames} from "../../model/file-item-param-names";
import {TypeControl} from "./type-control";

export class ViewIdGeneratorService {

    public static makeIdNameIdListBox(filterParamName:FilterParamNames): string {
        return TypeControl[TypeControl.NAME_ID_LIST] + "_" + filterParamName;
    }

}