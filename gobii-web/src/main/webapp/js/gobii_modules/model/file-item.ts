import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";
import {Guid} from "./guid";
import {EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";

export class FileItem {
    constructor(public gobiiExtractFilterType:GobiiExtractFilterType,
                public processType: ProcessType,
                public entityType:EntityType,
                public cvFilterType:CvFilterType,
                public itemId: string,
                public itemName: string,
                public checked: boolean,
                public required: boolean) {

        this.processType = processType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.required = required;
        this.fileItemUniqueId = Guid.generateUUID();
    }

    //OnChange does not see the FileItemEvent as being a new event unless it's
    //a branch new instance, even if any of the property values are different.
    //I'm sure there's a better way to do this. For example, the tree component should
    //subscribe to an observer that is fed by the root component?
    public static newFileItemEvent(fileItem:FileItem): FileItem {

        let existingUniqueId:string = fileItem.fileItemUniqueId;

        let returnVal:FileItem  = new FileItem(
            fileItem.gobiiExtractFilterType,
            fileItem.processType,
            fileItem.entityType,
            fileItem.cvFilterType,
            fileItem.itemId,
            fileItem.itemName,
            fileItem.checked,
            fileItem.required
        );

        returnVal.fileItemUniqueId = existingUniqueId;

        return returnVal;
    }

    public fileItemUniqueId:string;

} // FileItem()
