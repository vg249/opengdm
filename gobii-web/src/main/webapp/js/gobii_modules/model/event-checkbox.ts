import {ProcessType} from "./type-process";

export class CheckBoxEvent {
    constructor(public processType:ProcessType,
                public id:string,
                public name:string) {

        this.processType = processType;
        this.id = id;
        this.name = name;
    }
} // CheckBoxEvent()
