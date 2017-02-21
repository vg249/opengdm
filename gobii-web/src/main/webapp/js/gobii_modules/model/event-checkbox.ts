import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";
import {Guid} from "./guid";

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
//        this.uniqueId = Guid.generateUUID();
    }

    //OnChange does not see the CheckboxEvent as being a new event unless it's
    //a branch new instance, even if any of the property values are different.
    //I'm sure there's a better way to do this. For example, the tree component should
    //subscribe to an observer that is fed by the root component?
    public static newCheckboxEvent(checkboxEvent:CheckBoxEvent): CheckBoxEvent {

//        let existingUniqueId = checkboxEvent.uniqueId;

        let returnVal:CheckBoxEvent  = new CheckBoxEvent(
            checkboxEvent.processType,
            checkboxEvent.id,
            checkboxEvent.name,
            checkboxEvent.checked,
            checkboxEvent.required
        );

//        returnVal.uniqueId = existingUniqueId;

        return returnVal;
    }

    public uniqueId:string;

} // CheckBoxEvent()
