import {Component, OnInit, ViewChild, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {Tree} from "primeng/components/tree/tree";
import {CheckBoxEvent} from "../model/event-checkbox";
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
    inputs: ['checkBoxEventChange', 'gobiiExtractFilterType'],
    outputs: ['onItemSelected', 'onItemChecked'],
    template: ` 
                    <p-tree [value]="gobiiTreeNodes" selectionMode="checkbox" [(selection)]="selectedGobiiNodes"></p-tree>
                    <!--<p-tree [value]="demoTreeNodes" selectionMode="checkbox" [(selection)]="selectedDemoNodes"></p-tree>-->
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {


    constructor() {
    }

    ngOnInit() {


        this.makeDemoTreeNodes();

        this.setUpRequredItems();

    }

    getTemplates(gobiiExtractFilterType: GobiiExtractFilterType): StatusTreeTemplate[] {

        if (this.templates.size === 0) {

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
            this.templates[GobiiExtractFilterType.WHOLE_DATASET] = [];
            this.templates[GobiiExtractFilterType.WHOLE_DATASET] =
                this.templates[GobiiExtractFilterType.WHOLE_DATASET].concat(submissionItemsForAll);
            this.templates[GobiiExtractFilterType.WHOLE_DATASET].push(
                StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setCategoryName(this.entityNodeLabels[EntityType.DataSets])
                    .addChild(
                        StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.DataSets)
                            .setEntityName(this.entityNodeLabels[EntityType.DataSets])
                            .setCardinality(CardinalityType.ONE_OR_MORE))
            );


            // ******** SET UP extract by samples
            // -- Data set type
            this.templates[GobiiExtractFilterType.BY_SAMPLE] = [];
            this.templates[GobiiExtractFilterType.BY_SAMPLE] =
                this.templates[GobiiExtractFilterType.BY_SAMPLE].concat(submissionItemsForAll);
            this.templates[GobiiExtractFilterType.BY_SAMPLE].push(
                StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
            );

            // -- Platforms
            this.templates[GobiiExtractFilterType.BY_SAMPLE].push(
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
            this.templates[GobiiExtractFilterType.BY_SAMPLE].push(
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
        }

        return this.templates[gobiiExtractFilterType]
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


    addIconsToNode(statusTreeTemplate: StatusTreeTemplate, treeNode: GobiiTreeNode) {

        // if( statusTreeTemplate.getItemType() == ExtractorItemType.ENTITY ) {

        if (statusTreeTemplate.getEntityType() != null
            && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {

            if (statusTreeTemplate.getEntityType() === EntityType.DataSets) {

                treeNode.icon = "fa-database";
                treeNode.expandedIcon = "fa-database";
                treeNode.collapsedIcon = "fa-database";

            } else if (statusTreeTemplate.getEntityType() === EntityType.Contacts) {

                treeNode.icon = "fa-address-book-o";
                treeNode.expandedIcon = "fa-address-book-o";
                treeNode.collapsedIcon = "fa-address-book-o";

            } else if (statusTreeTemplate.getEntityType() === EntityType.Mapsets) {

                treeNode.icon = "fa-map-o";
                treeNode.expandedIcon = "fa-map-o";
                treeNode.collapsedIcon = "fa-map-o";
            }

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


    makeCbEventFromNode(treeNode: TreeNode): CheckBoxEvent {

        let returnVal: CheckBoxEvent = null;

        return returnVal;

    }


    makeNodeFromCbEvent(cbEvent: CheckBoxEvent): GobiiTreeNode {

        let returnVal: GobiiTreeNode = new GobiiTreeNode();
        returnVal.entityType = cbEvent.entityType;


        returnVal.label = cbEvent.name;

        return returnVal;
    }


    placeNodeInTree(treeNode: GobiiTreeNode) {

        // if (treeNode.entityType === EntityType.DataSets) {
        //
        //     let parentNode: GobiiTreeNode = this.gobiiTreeNodes.filter(n => n.entityType === EntityType.DataSets)[0];
        //     if (parentNode == null) {
        //         parentNode = this.makeGobiiTreeNodeForEntity(EntityType.DataSets);
        //         parentNode.label = this.entityNodeLabels[EntityType.DataSets];
        //         this.gobiiTreeNodes.push(parentNode);
        //     }
        //
        //     parentNode.expanded = true;
        //     parentNode.children.push(treeNode);
        //     this.selectedGobiiNodes.push(treeNode);
        //
        // }

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

        return returnVal;
    }


    setUpRequredItems() {

        let statusTreeTemplates: StatusTreeTemplate[] = this.getTemplates(this.gobiiExtractFilterType);

        let mainNode: GobiiTreeNode = new GobiiTreeNode();
        mainNode.label = this.extractorFilterTypeLabels[this.gobiiExtractFilterType].trim();
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


    gobiiExtractFilterType: GobiiExtractFilterType;

    onItemChecked: EventEmitter < CheckBoxEvent > = new EventEmitter();
    onItemSelected: EventEmitter < CheckBoxEvent > = new EventEmitter();


    templates: Map < GobiiExtractFilterType, Array < StatusTreeTemplate >> =
        new Map<GobiiExtractFilterType,Array<StatusTreeTemplate>>();

    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {

            let itemChangedEvent: CheckBoxEvent = changes['checkBoxEventChange'].currentValue;

            let treeNode: GobiiTreeNode = this.makeNodeFromCbEvent(itemChangedEvent);


            this.placeNodeInTree(treeNode);
            //this.treeNodes.push(treeNode);

            //this.placeNodeInTree(treeNode);

            // this.treeNodes.push(treeNode);
            //


            // if (this.itemChangedEvent) {
            //     let itemToChange:CheckBoxEvent =
            //         this.checkBoxEvents.filter(e => {
            //             return e.id == changes['checkBoxEventChange'].currentValue.id;
            //         })[0];
            //
            //     //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
            //     if (itemToChange) {
            //         itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
            //         itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
            //     }
            // }
        } else if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            this.gobiiExtractFilterType = changes['gobiiExtractFilterType'].currentValue;

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
