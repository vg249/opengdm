import {Injectable} from "@angular/core";
import {FileItem} from "../../model/file-item";
import {FileModelState, FileModelTreeEvent} from "../../model/file-model-tree-event";
import {FileModelNode, ExtractorItemType, ExtractorCategoryType, CardinalityType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {EntityType, EntitySubType} from "../../model/type-entity";
import {CvFilterType} from "../../model/cv-filter-type";
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {ProcessType} from "../../model/type-process";


@Injectable()
export class FileModelTreeService {


    constructor() {
    }

    fileModelNodeTree: Map < GobiiExtractFilterType, Array < FileModelNode >> =
        new Map<GobiiExtractFilterType,Array<FileModelNode>>();


    entityNodeLabels: Map < EntityType, string > = new Map<EntityType,string>();
    entitySubtypeNodeLabels: Map < EntitySubType, string > = new Map<EntitySubType,string>();
    cvFilterNodeLabels: Map < CvFilterType, string > = new Map<CvFilterType,string>();
    extractorFilterTypeLabels: Map < GobiiExtractFilterType, string > = new Map<GobiiExtractFilterType, string>();
    treeExtractorTypeLabels: Map<ExtractorItemType,string> = new Map<ExtractorItemType,string>();


    private validateModel(): boolean {

        //When this method is implemented, it will confirm that the sturcture of the tree is correct;
        //this is not just an intellectual exercise; the findFileModelNode() method (and probably others)
        //make important assumptions about the structure of the model. These are the assumptions:
        // 0) There is one, and only one, FileModelNode per EntityType and CvFilter value
        // there are a bunch of other rules pertaining to how tree nodes are associated with FileModelNodes,
        // but since that's in the presentation department, you'll see that over there in in the status tree.

        return true;

    }

    private getFileModelNodes(gobiiExtractFilterType: GobiiExtractFilterType): FileModelNode[] {


        if (this.fileModelNodeTree.size === 0) {

            this.entityNodeLabels[EntityType.DataSets] = "Data Sets";
            this.entityNodeLabels[EntityType.Platforms] = "Platforms";
            this.entityNodeLabels[EntityType.Mapsets] = "Mapsets";
            this.entityNodeLabels[EntityType.Projects] = "Projects";

            this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE] = "Dataset Type";

            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY] = "User";

            this.extractorFilterTypeLabels[GobiiExtractFilterType.WHOLE_DATASET] = "Extract by Dataset";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_SAMPLE] = "Extract by Sample";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_MARKER] = "Extract by Marker";

            this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST] = "Sample List";
            this.treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST] = "Marker List";
            this.treeExtractorTypeLabels[ExtractorItemType.CROP_TYPE] = "Crop Type";
            this.treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT] = "Format";


            // **** FOR ALL EXTRACTION TYPES **********************************************************************
            // **** THESE ARE ALL ROOT LEVEL NODES
            let submissionItemsForAll: FileModelNode[] = [];
            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Contacts)
                .setEntityName(this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.EXPORT_FORMAT, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setCategoryName(this.treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setEntityName(this.treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.CATEGORY, null)
                .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                .setEntityType(EntityType.Mapsets)
                .setCategoryName(this.entityNodeLabels[EntityType.Mapsets])
                .setCardinality(CardinalityType.ZERO_OR_ONE)
            );


            // **** SET UP EXTRACT BY DATASET  **********************************************************************
            // -- Data set type
            let submissionItemsForDataSet: FileModelNode[] = [];
            submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
            submissionItemsForDataSet.push(
                FileModelNode.build(ExtractorItemType.CATEGORY, null)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setCategoryName(this.entityNodeLabels[EntityType.DataSets])
                    .setCardinality(CardinalityType.ONE_OR_MORE));

            this.fileModelNodeTree.set(GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);

            // **** SET UP EXTRACT BY SAMPLES  **********************************************************************
            // -- Data set type
            let submissionItemsForBySample: FileModelNode[] = [];
            submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
            submissionItemsForBySample.push(
                FileModelNode.build(ExtractorItemType.ENTITY, null)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
            );

            // -- Platforms
            let currentParent: FileModelNode = null;
            submissionItemsForBySample.push(FileModelNode.build(ExtractorItemType.CATEGORY, null)
                .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                .setEntityType(EntityType.Platforms)
                .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                .setCardinality(CardinalityType.ZERO_OR_MORE)
            );


            // -- Samples
            submissionItemsForBySample
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.CATEGORY, null)
                        .setCategoryType(ExtractorCategoryType.MODEL_CONTAINER)
                        .setCategoryName("Sample Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Contacts)
                            .setEntityName(this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR])
                            .setCardinality(CardinalityType.ZERO_OR_ONE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(EntityType.Projects)
                            .setEntityName(this.entityNodeLabels[EntityType.Projects])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.SAMPLE_LIST, currentParent)
                            .setCategoryType(ExtractorCategoryType.CATEGORY_CONTAINER)
                            .setEntityName(this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST])
                            .setCategoryName(this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        ));

            this.fileModelNodeTree
                .set(GobiiExtractFilterType.BY_SAMPLE,
                    submissionItemsForBySample
                );


            // **** SET UP EXTRACT BY MARKERS  **********************************************************************
            let submissionItemsForByMarkers: FileModelNode[] = [];
            submissionItemsForByMarkers = submissionItemsForByMarkers.concat(submissionItemsForAll);
            submissionItemsForByMarkers.push(
                FileModelNode.build(ExtractorItemType.ENTITY, null)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForByMarkers
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.CATEGORY, null)
                        .setCategoryType(ExtractorCategoryType.MODEL_CONTAINER)
                        .setCategoryName("Markers Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Platforms])
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(EntityType.Platforms)
                            .setEntityName(this.entityNodeLabels[EntityType.Platforms])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.MARKER_LIST, currentParent)
                            .setCategoryType(ExtractorCategoryType.CATEGORY_CONTAINER)
                            .setEntityName(this.treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST])
                            .setCategoryName(this.treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        ));


            this.fileModelNodeTree
                .set(GobiiExtractFilterType.BY_MARKER,
                    submissionItemsForByMarkers
                );


            if (this.validateModel() == false) {
                //raise major warning
            }
        }

        return this.fileModelNodeTree.get(gobiiExtractFilterType);
    }

    private mutate(fileItem: FileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = null;

        if (fileItem.getGobiiExtractFilterType() != GobiiExtractFilterType.UNKNOWN) {

            let fileModelNode: FileModelNode = this.findFileModelNode(fileItem.getGobiiExtractFilterType(), fileItem.getEntityType(), fileItem.getCvFilterType());

            if (fileItem.getProcessType() === ProcessType.CREATE) {

                this.placeNodeInModel(fileModelNode, fileItem);
                returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.NOT_COMPLETE, null);

            } else if (fileItem.getProcessType() === ProcessType.DELETE) {


                this.removeFromModel(fileModelNode, fileItem);
                returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.NOT_COMPLETE, null);

            } else {
                returnVal = new FileModelTreeEvent(fileItem,
                    null,
                    FileModelState.ERROR,
                    "Unhandled file item process type: " + ProcessType[fileItem.getProcessType()]);
            }


        } else {
            returnVal = new FileModelTreeEvent(fileItem,
                null,
                FileModelState.ERROR,
                "An invalid extract filter type was specified");
        }


        return returnVal;
    }


    findFileModelNode(gobiiExtractFilterType: GobiiExtractFilterType, entityType: EntityType, cvFilter: CvFilterType) {

        let fileModelNodes: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);

        let returnVal: FileModelNode = null;

        for (let idx: number = 0; ( idx < fileModelNodes.length) && (returnVal == null ); idx++) {
            let currentTemplate: FileModelNode = fileModelNodes[idx];
            returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilter);
        }

        return returnVal;

    }


    findTemplateByCriteria(fileModelNode: FileModelNode,
                           entityType: EntityType,
                           cvFilterType: CvFilterType): FileModelNode {

        let returnVal: FileModelNode = null;

        if (fileModelNode.getChildren() != null) {

            for (let idx: number = 0; ( idx < fileModelNode.getChildren().length) && (returnVal == null ); idx++) {

                let currentTemplate: FileModelNode = fileModelNode.getChildren()[idx];
                returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilterType);
            }
        }

        if (returnVal === null) {

            if (entityType == fileModelNode.getEntityType() && cvFilterType == fileModelNode.getCvFilterType()) {

                returnVal = fileModelNode;
            }
        }

        return returnVal;

    }


    private placeNodeInModel(fileModelNode: FileModelNode, fileItem: FileItem) {


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getChildFileItems().length === 0) {
                fileModelNode.getChildFileItems().push(fileItem);
            } else {
                fileModelNode.getChildFileItems()[0] = fileItem;
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

            let existingItems: FileItem[] = fileModelNode.getChildFileItems().filter(
                item => {
                    return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                }
            )

            if (existingItems.length === 0) {
                fileModelNode.getChildFileItems().push(fileItem);
            } else {
                let idx: number = fileModelNode.getChildFileItems().indexOf(existingItems[0]);
                fileModelNode.getChildFileItems()[idx] = fileItem;
            }

        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }


    } //

    private removeFromModel(fileModelNode: FileModelNode, fileItem: FileItem) {


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getChildFileItems()[0].getFileItemUniqueId() === fileItem.getFileItemUniqueId()) {
                fileModelNode.getChildFileItems().splice(0, 1);
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

            let existingItem: FileItem = fileModelNode.getChildFileItems().find(
                item => {
                    return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                }
            );

            let idxOfItemToRemove: number = fileModelNode.getChildFileItems().indexOf(existingItem);

            fileModelNode.getChildFileItems().splice(idxOfItemToRemove, 1);


        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }

    }

    private subjectTreeNotifications: Subject < FileModelTreeEvent > = new Subject<FileModelTreeEvent>();

    public treeNotifications(): Subject < FileModelTreeEvent > {
        return this.subjectTreeNotifications
    }

    private subjectFileItemNotifications: Subject < FileItem > = new Subject<FileItem>();

    public fileItemNotifications(): Subject < FileItem > {
        return this.subjectFileItemNotifications
    }


    public put(fileItem: FileItem): Observable < FileModelTreeEvent > {

        return Observable.create(observer => {

            let foo: string = "foo";

            let fileTreeEvent: FileModelTreeEvent = this.mutate(fileItem);


            observer.next(fileTreeEvent);
            observer.complete();

            this.subjectTreeNotifications.next(fileTreeEvent);
            this.subjectFileItemNotifications.next(fileTreeEvent.fileItem);
        });
    }

    public get(gobiiExtractFilterType: GobiiExtractFilterType): Observable < FileModelNode[] > {

        return Observable.create(observer => {
            let nodesForFilterType: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);
            observer.next(nodesForFilterType);
            observer.complete();
        });
    }

}
