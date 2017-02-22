import {TreeNode} from "primeng/components/common/api";
import {EntityType} from "./type-entity";

export class GobiiTreeNode implements  TreeNode{

    public constructor(public entityType:EntityType) {
        this.entityType = entityType;
    }

}