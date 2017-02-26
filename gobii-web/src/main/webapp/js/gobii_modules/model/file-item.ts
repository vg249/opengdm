import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";
import {Guid} from "./guid";
import {EntityType} from "./type-entity";

export class FileItem {
    constructor(public processType: ProcessType,
                public entityType:EntityType,
                public itemId: string,
                public itemName: string,
                public checked: boolean,
                public required: boolean) {

        this.processType = processType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.required = required;
//        this.uniqueId = Guid.generateUUID();
    }

    //OnChange does not see the CheckboxEvent as being a new event unless it's
    //a branch new instance, even if any of the property values are different.
    //I'm sure there's a better way to do this. For example, the tree component should
    //subscribe to an observer that is fed by the root component?
    public static newCheckboxEvent(checkboxEvent:FileItem): FileItem {

//        let existingUniqueId = checkboxEvent.uniqueId;

        let returnVal:FileItem  = new FileItem(
            checkboxEvent.processType,
            checkboxEvent.entityType,
            checkboxEvent.itemId,
            checkboxEvent.itemName,
            checkboxEvent.checked,
            checkboxEvent.required
        );

//        returnVal.uniqueId = existingUniqueId;

        return returnVal;
    }

    public uniqueId:string;

} // FileItem()
