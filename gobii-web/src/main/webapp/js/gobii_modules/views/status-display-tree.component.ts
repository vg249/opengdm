import {Component, OnInit, ViewChild, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {Tree} from "primeng/components/tree/tree";
import {FileItem} from "../model/file-item";
import {GobiiTreeNode} from "../model/GobiiTreeNode";
import {EntityType, EntitySubType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {
    StatusTreeTemplate, ExtractorItemType, ExtractorCategoryType,
    CardinalityType
} from "../model/extractor-submission-item";
import {CvFilterType} from "../model/cv-filter-type";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
@Component({
    selector: 'status-display-tree',
    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
    template: ` 
                    <p-tree [value]="gobiiTreeNodes" selectionMode="checkbox" [(selection)]="selectedGobiiNodes"></p-tree>
                    <!--<p-tree [value]="demoTreeNodes" selectionMode="checkbox" [(selection)]="selectedDemoNodes"></p-tree>-->
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {

    private onAddMessage: EventEmitter<string> = new EventEmitter();

    private reportMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    constructor() {
    }

    ngOnInit() {


        this.makeDemoTreeNodes();
        this.setUpRequredItems();

    }

    getTemplates(gobiiExtractFilterType: GobiiExtractFilterType, forceReset: boolean): StatusTreeTemplate[] {

        if (this.templates.size === 0 || forceReset === true) {

            this.entityNodeLabels[EntityType.DataSets] = "Data Sets";
            this.entityNodeLabels[EntityType.Platforms] = "Platforms";
            this.entityNodeLabels[EntityType.Mapsets] = "Mapsets";

            this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE] = "Dataset Type";

            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY] = "Submitted By";

            this.extractorFilterTypeLabels[GobiiExtractFilterType.WHOLE_DATASET] = "Extract by Dataset";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_SAMPLE] = "Extract by Sample";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_MARKER] = "Extract by Marker";

            // **** FOR ALL EXTRACTION TYPES
            let submissionItemsForAll: StatusTreeTemplate[] = [];
            submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Contacts)
                .setEntityName(this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.EXPORT_FORMAT)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityName("Export Formats")
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Mapsets)
                .setEntityName(this.entityNodeLabels[EntityType.Mapsets])
                .setCardinality(CardinalityType.ZERO_OR_ONE)
            );


            // ******** SET UP extract by dataset
            // -- Data set type
            let submissionItemsForDataSet: StatusTreeTemplate[] = [];
            submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
            submissionItemsForDataSet.push(
                StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setCategoryName(this.entityNodeLabels[EntityType.DataSets]));
            // .addChild(
            //     StatusTreeTemplate.build(ExtractorItemType.ENTITY)
            //         .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
            //         .setEntityType(EntityType.DataSets)
            //         .setEntityName(this.entityNodeLabels[EntityType.DataSets])
            //         .setCardinality(CardinalityType.ONE_OR_MORE))
//            );

            this.templates.set(GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);

            // ******** SET UP extract by samples
            // -- Data set type
            let submissionItemsForBySample: StatusTreeTemplate[] = [];
            submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
            submissionItemsForBySample.push(
                StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
            );

            // -- Platforms
            submissionItemsForBySample.push(
                StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                    .setCardinality(CardinalityType.ZERO_OR_MORE)
                    .addChild(
                        StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Platforms)
                            .setEntityName(this.entityNodeLabels[EntityType.Platforms])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
            );

            // -- Samples
            submissionItemsForBySample.push(
                StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.CONTAINER)
                    .setCategoryName("Sample Crieria")
                    .setCardinality(CardinalityType.ONE_OR_MORE)
                    .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                    .addChild(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                        .setCategoryType(ExtractorCategoryType.LEAF)
                        .setEntityType(EntityType.Contacts)
                        .setEntityName("Principle Investigator")
                        .setCardinality(CardinalityType.ZERO_OR_ONE)
                    )
                    .addChild(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Projects)
                        .setEntityName(this.entityNodeLabels[EntityType.Projects])
                        .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
                    .addChild(StatusTreeTemplate.build(ExtractorItemType.SAMPLE_LIST)
                        .setEntityName("Sample List")
                        .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                        .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
            );
            this.templates.set(GobiiExtractFilterType.BY_SAMPLE, submissionItemsForBySample);

        }

        return this.templates.get(gobiiExtractFilterType);
    }


// *****************************************************************
// *********************  TREE NODE DATA STRUCTURES AND EVENTS

    demoTreeNodes: TreeNode[] = [];
    selectedDemoNodes: TreeNode[] = [];


    gobiiTreeNodes: GobiiTreeNode[] = [];
    selectedGobiiNodes: GobiiTreeNode[] = [];
    entityNodeLabels: Map < EntityType, string > = new Map<EntityType,string>();
    entitySubtypeNodeLabels: Map < EntitySubType, string > = new Map<EntitySubType,string>();
    cvFilterNodeLabels: Map < CvFilterType, string > = new Map<CvFilterType,string>();
    extractorFilterTypeLabels: Map < GobiiExtractFilterType, string > = new Map<GobiiExtractFilterType, string>();
    treeCategoryLabels: Map<ExtractorCategoryType,string> = new Map<ExtractorCategoryType,string>();


    experimentId: string;


    nodeSelect(event) {
        //      this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.label});
    }

    nodeUnselect(event) {
//        this.msgs.push({severity: 'info', summary: 'Node Unselected', detail: event.node.label});
    }

    nodeExpandMessage(event) {
//        this.msgs.push({severity: 'info', summary: 'Node Expanded', detail: event.node.label});
    }

    nodeExpand(event) {
        if (event.node) {
            //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
            //this.nodeService.getLazyFiles().then(nodes => event.node.children = nodes);
        }
    }

    viewFile(file: TreeNode) {
        //      this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
    }

    unselectFile() {
//        this.selectedFile2 = null;
    }

    expandAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, true);
        });
    }

    collapseAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, false);
        });
    }


    expandRecursive(node: TreeNode, isExpand: boolean) {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach(childNode => {
                this.expandRecursive(childNode, isExpand);
            });
        }
    }


// ********************************************************************************
// ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS

    addEntityIconToNode(entityType: EntityType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DataSets) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-database";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.Contacts) {

            treeNode.icon = "fa-address-book-o";
            treeNode.expandedIcon = "fa-address-book-o";
            treeNode.collapsedIcon = "fa-address-book-o";

        } else if (entityType === EntityType.Mapsets) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";
        }
    }


    addIconsToNode(statusTreeTemplate: StatusTreeTemplate, treeNode: GobiiTreeNode) {

        // if( statusTreeTemplate.getItemType() == ExtractorItemType.ENTITY ) {

        if (statusTreeTemplate.getEntityType() != null
            && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {

            this.addEntityIconToNode(statusTreeTemplate.getEntityType(), treeNode);

        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
            treeNode.icon = "fa-columns";
            treeNode.expandedIcon = "fa-columns";
            treeNode.collapsedIcon = "fa-columns";
        } else {
            //     }
            // } else if (statusTreeTemplate.getItemType() == ExtractorItemType.CATEGORY ) {
            treeNode.icon = "fa-folder";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-folder";
        }
    }


    makeCbEventFromNode(treeNode: TreeNode): FileItem {

        let returnVal: FileItem = null;

        return returnVal;

    }


    findTemplate(extractorItemType: ExtractorItemType, entityType: EntityType) {

        let returnVal: StatusTreeTemplate = null;

        let statusTreeTemplates: StatusTreeTemplate[] = this.getTemplates(this.gobiiExtractFilterType, false);

        for (let idx: number = 0; ( idx < statusTreeTemplates.length) && (returnVal == null ); idx++) {
            let currentTemplate: StatusTreeTemplate = statusTreeTemplates[idx];
            returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
        }

        return returnVal;

    }


    findTemplateByCriteria(statusTreeTemplate: StatusTreeTemplate,
                           extractorItemType: ExtractorItemType, entityType: EntityType): StatusTreeTemplate {

        let returnVal: StatusTreeTemplate = null;

        if (statusTreeTemplate.getChildren() != null) {

            for (let idx: number = 0; ( idx < statusTreeTemplate.getChildren().length) && (returnVal == null ); idx++) {

                let currentTemplate: StatusTreeTemplate = statusTreeTemplate.getChildren()[idx];
                returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
            }
        }

        if (returnVal === null) {

            if (extractorItemType == statusTreeTemplate.getItemType()
                && entityType == statusTreeTemplate.getEntityType()) {

                returnVal = statusTreeTemplate;
            }
        }

        return returnVal;

    }

    addEntityNameToNode(statusTreeTemplate: StatusTreeTemplate, gobiiTreeNode: GobiiTreeNode, fileItemEvent: FileItem) {

        if (statusTreeTemplate.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {
            gobiiTreeNode.label = fileItemEvent.itemName;
        } else {
            gobiiTreeNode.label += statusTreeTemplate.getEntityName() + ": " + fileItemEvent.itemName;
        }
    }

    placeNodeInTree(fileItemEvent: FileItem) {

        let statusTreeTemplate: StatusTreeTemplate =
            this.findTemplate(ExtractorItemType.CATEGORY, fileItemEvent.entityType);


        if (statusTreeTemplate != null) {


            if (statusTreeTemplate.getCategoryType() === ExtractorCategoryType.LEAF) {

                let existingGobiiTreeNode: GobiiTreeNode = statusTreeTemplate.getGobiiTreeNode();
                this.addEntityNameToNode(statusTreeTemplate, existingGobiiTreeNode, fileItemEvent);

            } else if (statusTreeTemplate.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

                let newGobiiTreeNode = new GobiiTreeNode();
                newGobiiTreeNode.entityType = fileItemEvent.entityType;
                this.addEntityIconToNode(newGobiiTreeNode.entityType, newGobiiTreeNode);
                this.addEntityNameToNode(statusTreeTemplate, newGobiiTreeNode, fileItemEvent);
                statusTreeTemplate.getGobiiTreeNode().children.push(newGobiiTreeNode);
                statusTreeTemplate.getGobiiTreeNode().expanded = true;
                this.selectedGobiiNodes.push(newGobiiTreeNode);
                this.selectedGobiiNodes.push(statusTreeTemplate.getGobiiTreeNode());

            } else {
                this.reportMessage("The node of category  "
                    + statusTreeTemplate.getCategoryType()
                    + " for checkbox event " + fileItemEvent.itemName
                    + " could not be placed in the tree ");
            }


        } else {

            this.reportMessage("Could not place checkbox event "
                + fileItemEvent.itemName
                + " in tree");
        }

    } //


    makeTreeNodeFromTemplate(statusTreeTemplate: StatusTreeTemplate): GobiiTreeNode {

        let returnVal: GobiiTreeNode = null;


        if (statusTreeTemplate.getItemType() === ExtractorItemType.ENTITY) {

            returnVal = new GobiiTreeNode();
            returnVal.entityType = statusTreeTemplate.getEntityType();
            returnVal.label = statusTreeTemplate.getEntityName();


        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.CATEGORY) {

            returnVal = new GobiiTreeNode();

            if (statusTreeTemplate.getEntityType() != null
                && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {
                returnVal.entityType = statusTreeTemplate.getEntityType();
            }

            returnVal.label = statusTreeTemplate.getCategoryName();

        } else if (statusTreeTemplate.getItemType() == ExtractorItemType.EXPORT_FORMAT) {

            returnVal = new GobiiTreeNode();
            returnVal.label = "Export Format";
        }

        this.addIconsToNode(statusTreeTemplate, returnVal);

        if (null != returnVal) {

            if (( statusTreeTemplate.getCategoryType() != ExtractorCategoryType.ENTITY_CONTAINER
                && statusTreeTemplate.getChildren() !== null )
                && ( statusTreeTemplate.getChildren().length > 0)) {

                statusTreeTemplate.getChildren().forEach(
                    stt => {

                        let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(stt);
                        if (null != currentTreeNode) {
                            returnVal.children.push(currentTreeNode);
                        }
                    }
                );
            }
        }

        statusTreeTemplate.setGobiiTreeNode(returnVal);

        return returnVal;
    }


    setUpRequredItems() {

        this.gobiiTreeNodes  = [];

        let statusTreeTemplates: StatusTreeTemplate[] = this.getTemplates(this.gobiiExtractFilterType, false);

        let mainNode: GobiiTreeNode = new GobiiTreeNode();
        mainNode.label = this.extractorFilterTypeLabels[this.gobiiExtractFilterType];
        mainNode.icon = "fa-folder";
        mainNode.expandedIcon = "fa-folder";
        mainNode.expanded = true;


        statusTreeTemplates.forEach(
            currentFirstLevelTemplate => {

                let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(currentFirstLevelTemplate);
                if (currentTreeNode != null) {
                    mainNode.children.push(currentTreeNode);
                }
            }
        );

        this.gobiiTreeNodes.push(mainNode)

    }

// ********************************************************************************
// ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS


    gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    onItemChecked: EventEmitter < FileItem > = new EventEmitter();
    onItemSelected: EventEmitter < FileItem > = new EventEmitter();


    templates: Map < GobiiExtractFilterType, Array < StatusTreeTemplate >> =
        new Map<GobiiExtractFilterType,Array<StatusTreeTemplate>>();

    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {

            let itemChangedEvent: FileItem = changes['fileItemEventChange'].currentValue;


            this.placeNodeInTree(itemChangedEvent);
            //this.treeNodes.push(treeNode);

            //this.placeNodeInTree(treeNode);

            // this.treeNodes.push(treeNode);
            //


            // if (this.itemChangedEvent) {
            //     let itemToChange:FileItem =
            //         this.fileItemEvents.filter(e => {
            //             return e.id == changes['fileItemEventChange'].currentValue.id;
            //         })[0];
            //
            //     //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
            //     if (itemToChange) {
            //         itemToChange.processType = changes['fileItemEventChange'].currentValue.processType;
            //         itemToChange.checked = changes['fileItemEventChange'].currentValue.checked;
            //     }
            // }
        } else if (changes['gobiiExtractFilterTypeEvent']
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != null )
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != undefined )) {

            let newGobiiExtractFilterType: GobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;

            if (newGobiiExtractFilterType !== this.gobiiExtractFilterType) {
                this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                this.getTemplates(this.gobiiExtractFilterType, true);
                this.setUpRequredItems();
            }


            // this.setList(changes['nameIdList'].currentValue);

        }
    }


    makeDemoTreeNodes() {

        this.demoTreeNodes = [
            {
                "label": "Documents",
                "data": "Documents Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [{

                    "label": "Work",
                    "data": "Work Folder",
                    "expandedIcon": "fa-folder-open",
                    "collapsedIcon": "fa-folder",
                    "children": [{

                        "label": "Expenses.doc",
                        "icon": "fa-file-word-o",
                        "data": "Expenses Document"
                    }, {"label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document"}]
                },
                    {

                        "label": "Home",
                        "data": "Home Folder",
                        "expandedIcon": "fa-folder-open",
                        "collapsedIcon": "fa-folder",
                        "children": [{

                            "label": "Invoices.txt",
                            "icon": "fa-file-word-o",
                            "data": "Invoices for this month"
                        }]
                    }]
            },
            {

                "label": "Pictures",
                "data": "Pictures Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [
                    {"label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo"},
                    {"label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo"},
                    {"label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo"}]
            },
            {

                "label": "Movies",
                "data": "Movies Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [{

                    "label": "Al Pacino",
                    "data": "Pacino Movies",
                    "children": [{

                        "label": "Scarface",
                        "icon": "fa-file-video-o",
                        "data": "Scarface Movie"
                    }, {"label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie"}]
                },
                    {

                        "label": "Robert De Niro",
                        "data": "De Niro Movies",
                        "children": [{

                            "label": "Goodfellas",
                            "icon": "fa-file-video-o",
                            "data": "Goodfellas Movie"
                        }, {

                            "label": "Untouchables",
                            "icon": "fa-file-video-o",
                            "data": "Untouchables Movie"
                        }]
                    }]
            }
        ];


        this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0])
        this.demoTreeNodes[1].partialSelected = true;
        this.demoTreeNodes[1].expanded = true;
    }
}
