import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";

@Injectable()
export class TreeStructureService {

    private makeCommonNodes(gobiiExtractFilterType: GobiiExtractFilterType): GobiiTreeNode[] {

        let returnVal: GobiiTreeNode[] = [

            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.JOB_ID)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Contacts)
                .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                .setLabel(Labels.instance().entityNodeLabels[EntitySubType.CONTACT_SUBMITED_BY]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.EXPORT_FORMAT)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Mapsets)
                .setLabel(Labels.instance().entityNodeLabels[EntityType.Mapsets]),
        ]

        return returnVal;
    }


    // Note that we aren't storing these nodes as state -- we only make them
    // and return them. We rely on the store to make them part of state.
    // For now, this is the only context in which container type is ste on tree nodes;
    // tree nodes that are created dynamically will only ever replace or be stored within
    // one of these original structural nodes. See the tree nodes reducer for how the
    // container types are actually used
    public getInitialTree(): GobiiTreeNode[] {

        let returnVal: GobiiTreeNode[] = [

            // BY DATASET
            ...this.makeCommonNodes(GobiiExtractFilterType.WHOLE_DATASET),
            GobiiTreeNode.build(GobiiExtractFilterType.WHOLE_DATASET, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.DataSets)
                .setContainerType(ContainerType.DATA),

            // BY SAMPLE
            ...this.makeCommonNodes(GobiiExtractFilterType.BY_SAMPLE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CvTerms)
                .setCvFilterType(CvFilterType.DATASET_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Platforms),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.TREE_STRUCTURE)
                .setContainerType(ContainerType.STRUCTURE)
                .setLabel("Samples Criteria")
                .setExpanded(true)
                .setChildren([
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Contacts)
                        .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Projects)
                        .setContainerType(ContainerType.DATA),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_FILE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setContainerType(ContainerType.DATA),
                ]),


            // BY MARKER
            ...this.makeCommonNodes(GobiiExtractFilterType.BY_MARKER),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CvTerms)
                .setCvFilterType(CvFilterType.DATASET_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.TREE_STRUCTURE)
                .setContainerType(ContainerType.STRUCTURE)
                .setLabel("Markers Criteria")
                .setExpanded(true)
                .setChildren([
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Platforms)
                        .setContainerType(ContainerType.DATA),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_FILE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_LIST_ITEM)
                        .setContainerType(ContainerType.DATA),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.MarkerGroups)
                        .setContainerType(ContainerType.DATA)
                ])
        ];

        // we know we only have to go one level deep in this case -- no need to recurse
        returnVal.forEach(function (currentNode, idx, nodes) {

            currentNode.getChildren().forEach(currentChild => {
                currentChild.parent = currentNode
            })
        });

        this.setTreeNodeProperties(returnVal);
        return returnVal;

    }

    private applyLabel(gobiiTreeNode: GobiiTreeNode) {

        let labelValue: string = null;

        if (gobiiTreeNode.getItemType() === ExtractorItemType.ENTITY) {

            if (gobiiTreeNode.getEntitySubType() === EntitySubType.UNKNOWN) {

                if (gobiiTreeNode.getEntityType() !== EntityType.CvTerms) {
                    labelValue = Labels.instance().entityNodeLabels[gobiiTreeNode.getEntityType()];
                } else {
                    labelValue = Labels.instance().cvFilterNodeLabels[gobiiTreeNode.getCvFilterType()];
                }
            } else {
                labelValue = Labels.instance().entitySubtypeNodeLabels[gobiiTreeNode.getEntitySubType()];
            }

        } else {
            labelValue = Labels.instance().treeExtractorTypeLabels[gobiiTreeNode.getItemType()];
        }

        gobiiTreeNode.setLabel(labelValue);

    }

    private setTreeNodeProperties(treeNodes: GobiiTreeNode[]) {

        treeNodes.forEach(tn => {
            if (( tn.children === null ) || ( tn.children.length <= 0  )) {
                this.addIconsToNode(tn, false);
                this.applyLabel(tn);
            } else {
                this.setTreeNodeProperties(tn.children);
            }
        })

    }


    private addEntityIconToNode(entityType: EntityType, cvFilterType: CvFilterType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DataSets) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.Contacts) {

            treeNode.icon = "fa-user-o";
            treeNode.expandedIcon = "fa-user-o";
            treeNode.collapsedIcon = "fa-user-o";

        } else if (entityType === EntityType.Mapsets) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";

        } else if (entityType === EntityType.Platforms) {

            treeNode.icon = "fa-calculator";
            treeNode.expandedIcon = "fa-calculator";
            treeNode.collapsedIcon = "fa-calculator";

        } else if (entityType === EntityType.Projects) {

            treeNode.icon = "fa-clipboard";
            treeNode.expandedIcon = "fa-clipboard";
            treeNode.collapsedIcon = "fa-clipboard";

        } else if (entityType === EntityType.CvTerms) {

            if (cvFilterType === CvFilterType.DATASET_TYPE) {
                treeNode.icon = "fa-file-excel-o";
                treeNode.expandedIcon = "fa-file-excel-o";
                treeNode.collapsedIcon = "fa-file-excel-o";
            }

        } else if (entityType === EntityType.MarkerGroups) {

            // if (isParent) {
            treeNode.icon = "fa-pencil";
            treeNode.expandedIcon = "fa-pencil";
            treeNode.collapsedIcon = "fa-pencil";
            // } else {
            //     treeNode.icon = "fa-map-marker";
            //     treeNode.expandedIcon = "fa-map-marker";
            //     treeNode.collapsedIcon = "fa-map-marker";
            // }
        }
    }


    private addIconsToNode(treeNode: GobiiTreeNode, isParent: boolean) {

        // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {

        if (treeNode.getEntityType() != null
            && treeNode.getEntityType() != EntityType.UNKNOWN) {

            this.addEntityIconToNode(treeNode.getEntityType(), treeNode.getCvFilterType(), treeNode);

        } else if (treeNode.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
            treeNode.icon = "fa-columns";
            treeNode.expandedIcon = "fa-columns";
            treeNode.collapsedIcon = "fa-columns";
        } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_FILE) {
            treeNode.icon = "fa-file-text-o";
            treeNode.expandedIcon = "fa-file-text-o";
            treeNode.collapsedIcon = "fa-file-text-o";
        } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
            if (isParent) {
                treeNode.icon = "fa-list-ul";
                treeNode.expandedIcon = "fa-list-ul";
                treeNode.collapsedIcon = "fa-list-ul";
            } else {
                treeNode.icon = "fa-eyedropper";
                treeNode.expandedIcon = "fa-eyedropper";
                treeNode.collapsedIcon = "fa-eyedropper";
            }
        } else if (treeNode.getItemType() === ExtractorItemType.MARKER_FILE) {
            treeNode.icon = "fa-file-text-o";
            treeNode.expandedIcon = "fa-file-text-o";
            treeNode.collapsedIcon = "fa-file-text-o";
        } else if (treeNode.getItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

            if (isParent) {
                treeNode.icon = "fa-list-ul";
                treeNode.expandedIcon = "fa-list-ul";
                treeNode.collapsedIcon = "fa-list-ul";
            } else {
                treeNode.icon = "fa-map-marker";
                treeNode.expandedIcon = "fa-map-marker";
                treeNode.collapsedIcon = "fa-map-marker";
            }
        } else if (treeNode.getItemType() === ExtractorItemType.JOB_ID) {
            treeNode.icon = "fa-info-circle";
            treeNode.expandedIcon = "fa-info-circle";
            treeNode.collapsedIcon = "fa-info-circle";
        } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
            treeNode.icon = "fa-info-circle";
            treeNode.expandedIcon = "fa-info-circle";
            treeNode.collapsedIcon = "fa-info-circle";
        } else {
            //     }
            // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
            treeNode.icon = "fa-folder";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-folder";
        }
    }

    public makeTreeNodeFromFileItem(gobiiFileItem: GobiiFileItem): GobiiTreeNode {

        let returnVal: GobiiTreeNode = GobiiTreeNode
            .build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
            .setEntityType(gobiiFileItem.getEntityType())
            .setEntitySubType(gobiiFileItem.getEntitySubType())
            .setCvFilterType(gobiiFileItem.getCvFilterType());

        this.addIconsToNode(returnVal, false);
        this.applyLabel(returnVal);
        this.addFileItemNameToNode(returnVal,gobiiFileItem);

        return returnVal;
    }

    addFileItemNameToNode(gobiiTreeNode: GobiiTreeNode, gobiiFileItem: GobiiFileItem) {

        if (gobiiTreeNode.getContainerType() === ContainerType.DATA) {
            gobiiTreeNode.label = gobiiFileItem.getItemName();
        } else { // coves the LEAF node use case
            if (gobiiFileItem.getExtractorItemType() == ExtractorItemType.EXPORT_FORMAT) {
                let gobiiExtractFormat: GobiiExtractFormat = <GobiiExtractFormat> GobiiExtractFormat[gobiiFileItem.getItemId()];
                gobiiTreeNode.label +=  ": " + Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
            } else if (gobiiFileItem.getExtractorItemType() == ExtractorItemType.JOB_ID) {
                gobiiTreeNode.label = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID]
                    + ": " + gobiiFileItem.getItemId();
            } else {
                if (gobiiFileItem.getProcessType() !== ProcessType.DELETE) {
                    gobiiTreeNode.label +=  ": " + gobiiFileItem.getItemName();
                } else {
                    gobiiTreeNode.label = gobiiFileItem.getItemName();
                }
            }
        }
    }


}
