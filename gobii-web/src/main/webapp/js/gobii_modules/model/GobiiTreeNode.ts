import {TreeNode} from "primeng/components/common/api";
import {EntityType} from "./type-entity";

export class GobiiTreeNode implements  TreeNode{

    public constructor(public entityType:EntityType) {
        this.entityType = entityType;
    }


    public label: string;
    public data: any;
    public icon: any;
    public expandedIcon: any;
    public collapsedIcon: any;
    public children: GobiiTreeNode[] = [];
    public leaf: boolean;
    public expanded: boolean;
    public type: string;
    public parent: TreeNode;
    public partialSelected: boolean;


}