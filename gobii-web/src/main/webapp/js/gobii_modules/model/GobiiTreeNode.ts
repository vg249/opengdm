import {TreeNode} from "primeng/components/common/api";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {ExtractorItemType} from "./file-model-node";
import {Guid} from "./guid";


export class GobiiTreeNode implements TreeNode {

    constructor(parent:GobiiTreeNode,
                fileModelNodeId: string,
                fileItemId: string,
                required: boolean) {

        this._id = Guid.generateUUID();


        this.parent = parent;
        this.fileModelNodeId = fileModelNodeId;
        this.fileItemId = fileItemId;
        this.required = required;
    }


    getId(): string {
        return this._id;
    }

//unique identifiers
    getItemType(): ExtractorItemType {
        return this._itemType;
    }

    setItemType(value: ExtractorItemType) {
        this._itemType = value;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType) {
        this._entityType = value;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType) {
        this._entitySubType = value;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType) {
        this._cvFilterType = value;
    }

    private _id:string;
    private _itemType: ExtractorItemType = ExtractorItemType.ENTITY;
    private _entityType: EntityType = EntityType.UNKNOWN;
    private _entitySubType: EntitySubType = EntitySubType.UNKNOWN;
    private _cvFilterType: CvFilterType = CvFilterType.UNKNOWN;
    
    
    //UI properties
    public label: string;
    public data: any;
    public icon: any;
    public expandedIcon: any;
    public collapsedIcon: any;
    public children: GobiiTreeNode[] = [];
    public leaf: boolean;
    public expanded: boolean;
    public type: string;
    public parent: GobiiTreeNode;
    public partialSelected: boolean;
    public fileModelNodeId: string;
    public fileItemId: string;
    public required: boolean = false;
    private _active: boolean = false;

    getActive(): boolean {
        return this._active;
    }

    setActive(value: boolean) {
        this._active = value;
    }
}