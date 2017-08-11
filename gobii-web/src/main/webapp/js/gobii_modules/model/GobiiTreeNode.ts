import {TreeNode} from "primeng/components/common/api";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {ExtractorItemType} from "./file-model-node";
import {Guid} from "./guid";


export class GobiiTreeNode implements TreeNode {

    constructor(parent: GobiiTreeNode,
                fileModelNodeId: string,
                fileItemId: string,
                required: boolean) {

        this.id = Guid.generateUUID();


        this.parent = parent;
        this.fileModelNodeId = fileModelNodeId;
        this.fileItemId = fileItemId;
        this.required = required;
    }


    public static build(extractoItemType: ExtractorItemType,
                        entityType: EntityType): GobiiTreeNode {

        let returnVal: GobiiTreeNode = new GobiiTreeNode(
            null,
            null,
            null,
            null
        );

        returnVal.setItemType(extractoItemType);
        returnVal.setEntityType(entityType);

        return returnVal;

    } //build


    getId(): string {
        return this.id;
    }

//unique identifiers
    getItemType(): ExtractorItemType {
        return this.itemType;
    }

    setItemType(value: ExtractorItemType): GobiiTreeNode {
        this.itemType = value;
        return this;
    }

    getEntityType(): EntityType {
        return this.entityType;
    }

    setEntityType(value: EntityType): GobiiTreeNode {
        this.entityType = value;
        return this;
    }

    getEntitySubType(): EntitySubType {
        return this.entitySubType;
    }

    setEntitySubType(value: EntitySubType): GobiiTreeNode {
        this.entitySubType = value;
        return this;
    }

    private id: string;
    public itemType: ExtractorItemType = ExtractorItemType.ENTITY;
    public entityType: EntityType = EntityType.UNKNOWN;
    public entitySubType: EntitySubType = EntitySubType.UNKNOWN;
    public cvFilterType: CvFilterType = CvFilterType.UNKNOWN;


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
    public active: boolean = false;

    getActive(): boolean {
        return this.active;
    }

    setActive(value: boolean): GobiiTreeNode {
        this.active = value;
        return this;
    }


    getCvFilterType(): CvFilterType {
        return this.cvFilterType;
    }

    setCvFilterType(value: CvFilterType) : GobiiTreeNode {
        this.cvFilterType = value;
        return this;
    }

    getLabel(): string {
        return this.label;
    }

    setLabel(value: string) : GobiiTreeNode {
        this.label = value;
        return this;
    }

    getData(): any {
        return this.data;
    }

    setData(value: any) : GobiiTreeNode {
        this.data = value;
        return this;
    }

    getIcon(): any {
        return this.icon;
    }

    setIcon(value: any) : GobiiTreeNode {
        this.icon = value;
        return this;
    }

    getExpandedIcon(): any {
        return this.expandedIcon;
    }

    setExpandedIcon(value: any) : GobiiTreeNode {
        this.expandedIcon = value;
        return this;
    }

    getCollapsedIcon(): any {
        return this.collapsedIcon;
    }

    setCollapsedIcon(value: any) : GobiiTreeNode {
        this.collapsedIcon = value;
        return this;
    }

    getChildren(): GobiiTreeNode[] {
        return this.children;
    }

    setChildren(value: GobiiTreeNode[]) : GobiiTreeNode {
        this.children = value;
        return this;
    }

    getLeaf(): boolean {
        return this.leaf;
    }

    setLeaf(value: boolean) : GobiiTreeNode {
        this.leaf = value;
        return this;
    }

    getExpanded(): boolean {
        return this.expanded;
    }

    setExpanded(value: boolean) : GobiiTreeNode {
        this.expanded = value;
        return this;
    }

    getType(): string {
        return this.type;
    }

    setType(value: string) : GobiiTreeNode {
        this.type = value;
        return this;
    }

    getPartialSelected(): boolean {
        return this.partialSelected;
    }

    setPartialSelected(value: boolean) : GobiiTreeNode {
        this.partialSelected = value;
        return this;
    }

    getFileItemId(): string {
        return this.fileItemId;
    }

    setFileItemId(value: string) : GobiiTreeNode {
        this.fileItemId = value;
        return this;
    }

    getRequired(): boolean {
        return this.required;
    }

    setRequired(value: boolean) : GobiiTreeNode {
        this.required = value;
        return this;
    }
}