import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";

export class CheckBoxEvent {
    constructor(public processType: ProcessType,
                public id: string,
                public name: string,
                public checked: boolean,
                public required: boolean) {

        this.processType = processType;
        this.id = id;
        this.name = name;
        this.required = required;
    }

} // CheckBoxEvent()
