import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";
import {Guid} from "./guid";
import {EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";

export class FileItem {

    private _fileItemUniqueId: string;
    private constructor(private _gobiiExtractFilterType: GobiiExtractFilterType,
                private _processType: ProcessType,
                private _entityType: EntityType,
                private _cvFilterType: CvFilterType,
                private _itemId: string,
                private _itemName: string,
                private _checked: boolean,
                private _required: boolean) {

        this._processType = _processType;
        this._itemId = _itemId;
        this._itemName = _itemName;
        this._required = _required;
        this._fileItemUniqueId = Guid.generateUUID();

        if (this._cvFilterType === null) {
            this._cvFilterType = CvFilterType.UKNOWN;
        }

    }

    public static build(gobiiExtractFilterType: GobiiExtractFilterType,
                        processType: ProcessType): FileItem {

        let returnVal: FileItem = new FileItem(
            gobiiExtractFilterType,
            processType,
            EntityType.UNKNOWN,
            CvFilterType.UKNOWN,
            null,
            null,
            null,
            null
        );


        return returnVal;
    }

    //OnChange does not see the FileItemEvent as being a new event unless it's
    //a branch new instance, even if any of the property values are different.
    //I'm sure there's a better way to do this. For example, the tree component should
    //subscribe to an observer that is fed by the root component?
    public static fromFileItem(fileItem: FileItem, gobiiExtractFilterType: GobiiExtractFilterType): FileItem {

        let existingUniqueId: string = fileItem._fileItemUniqueId;

        let returnVal: FileItem = FileItem
            .build(gobiiExtractFilterType,fileItem._processType)
                .setEntityType(fileItem._entityType)
                .setCvFilterType(fileItem._cvFilterType)
                .setItemId(fileItem._itemId)
                .setItemName(fileItem._itemName)
                .setChecked(fileItem._checked)
                .setRequired(fileItem._required);

        returnVal._fileItemUniqueId = existingUniqueId;

        return returnVal;
    }

    setFileItemUniqueId(fileItemUniqueId:string): FileItem {
        this._fileItemUniqueId = fileItemUniqueId;
        return this;
    }

    getFileItemUniqueId(): string {
        return this._fileItemUniqueId;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): FileItem {
        this._gobiiExtractFilterType = value;
        return this;
    }

    getProcessType(): ProcessType {
        return this._processType;
    }

    setProcessType(value: ProcessType): FileItem {
        this._processType = value;
        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): FileItem {
        this._entityType = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): FileItem {
        this._cvFilterType = value;
        return this;
    }

    getItemId(): string {
        return this._itemId;
    }

    setItemId(value: string): FileItem {
        this._itemId = value;
        return this;
    }

    getItemName(): string {
        return this._itemName;
    }

    setItemName(value: string): FileItem {
        this._itemName = value;
        return this;
    }

    getChecked(): boolean {
        return this._checked;
    }

    setChecked(value: boolean) : FileItem{
        this._checked = value;
        return this;
    }

    getRequired(): boolean {
        return this._required;
    }

    setRequired(value: boolean): FileItem {
        this._required = value;
        return this;
    }

} // FileItem()
