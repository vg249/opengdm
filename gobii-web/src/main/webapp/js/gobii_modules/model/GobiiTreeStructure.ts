import {TreeNode} from "primeng/components/common/api";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {ExtractorItemType} from "./file-model-node";
import {Guid} from "./guid";
import {ContainerType, GobiiTreeNode} from "./GobiiTreeNode";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {Labels} from "../views/entity-labels";


class TreeNodeUtil {

    public static makeCommonNodes(gobiiExtractFilterType: GobiiExtractFilterType): GobiiTreeNode[] {

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
}


let treeStructure: GobiiTreeNode[] = [

    // BY DATASET
    ...TreeNodeUtil.makeCommonNodes(GobiiExtractFilterType.WHOLE_DATASET),
    GobiiTreeNode.build(GobiiExtractFilterType.WHOLE_DATASET, ExtractorItemType.ENTITY)
        .setEntityType(EntityType.DataSets)
        .setLabel(Labels.instance().entityNodeLabels[EntityType.DataSets])
        .setContainerType(ContainerType.ITEM_NODE),

    // BY SAMPLE
    ...TreeNodeUtil.makeCommonNodes(GobiiExtractFilterType.BY_SAMPLE),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
        .setEntityType(EntityType.CvTerms)
        .setCvFilterType(CvFilterType.DATASET_TYPE)
        .setLabel(Labels.instance().cvFilterNodeLabels[CvFilterType.DATASET_TYPE]),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_TYPE)
        .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_TYPE]),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
        .setEntityType(EntityType.Platforms)
        .setLabel(Labels.instance().entityNodeLabels[EntityType.Platforms]),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.TREE_STRUCTURE)
        .setContainerType(ContainerType.TREE_NODE)
        .setLabel("Samples Criteria")
        .setExpanded(true)
        .setChildren([
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Contacts)
                .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                .setLabel(Labels.instance().entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR]),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Projects)
                .setContainerType(ContainerType.ITEM_NODE)
                .setLabel(Labels.instance().entityNodeLabels[EntityType.Projects]),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_FILE)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE]),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_TYPE)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_TYPE])
                .setContainerType(ContainerType.ITEM_NODE),
        ]),


    // BY MARKER
    ...TreeNodeUtil.makeCommonNodes(GobiiExtractFilterType.BY_MARKER),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
        .setEntityType(EntityType.CvTerms)
        .setCvFilterType(CvFilterType.DATASET_TYPE)
        .setLabel(Labels.instance().cvFilterNodeLabels[CvFilterType.DATASET_TYPE]),
    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.TREE_STRUCTURE)
        .setContainerType(ContainerType.TREE_NODE)
        .setLabel("Markers Criteria")
        .setExpanded(true)
        .setChildren([
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.Platforms)
                .setLabel(Labels.instance().entityNodeLabels[EntityType.Platforms])
                .setContainerType(ContainerType.ITEM_NODE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_FILE)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE]),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_LIST_ITEM)
                .setLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM])
                .setContainerType(ContainerType.ITEM_NODE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.MarkerGroups)
                .setLabel(Labels.instance().entityNodeLabels[EntityType.MarkerGroups])
                .setContainerType(ContainerType.ITEM_NODE)
        ])
];

export default treeStructure;