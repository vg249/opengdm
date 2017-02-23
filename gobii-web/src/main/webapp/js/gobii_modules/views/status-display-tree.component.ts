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

        this.entityNodeLabels[EntityType.DataSets] = "Data Sets";
        this.entityNodeLabels[EntityType.Platforms] = "Platforms";
        this.entityNodeLabels[EntityType.Mapsets] = "Mapsets";

        this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE] = "Dataset Type";

        this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
        this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY] = "Submitted By";

        this.makeDemoTreeNodes();



        // **** FOR ALL EXTRACTION TYPES
        let submissionItemsForAll: StatusTreeTemplate[] = [];
        submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
            .setEntityType(EntityType.Contacts)
            .setEntityName(this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
            .setCvFilterType(CvFilterType.DATASET_TYPE)
            .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
            .setCardinality(CardinalityType.ONE_ONLY)
        );

        submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.EXPORT_FORMAT)
            .setEntityName("Export Formats")
            .setCardinality(CardinalityType.ONE_ONLY)
        );

        submissionItemsForAll.push(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
            .setEntityType(EntityType.Mapsets)
            .setEntityName(this.entityNodeLabels[EntityType.Mapsets])
            .setCardinality(CardinalityType.ZERO_OR_ONE)
        );



        // ******** SET UP extract by dataset
        // -- Data set type
        this.submissionItems[GobiiExtractFilterType.WHOLE_DATASET] = [];
        this.submissionItems[GobiiExtractFilterType.WHOLE_DATASET].concat(submissionItemsForAll);
        this.submissionItems[GobiiExtractFilterType.WHOLE_DATASET].push(
            StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                .setEntityType(EntityType.DataSets)
                .setEntityName(this.entityNodeLabels[EntityType.DataSets])
                .setCardinality(CardinalityType.ONE_OR_MORE)
        );


        // ******** SET UP extract by samples
        // -- Data set type
        this.submissionItems[GobiiExtractFilterType.BY_SAMPLE] = [];
        this.submissionItems[GobiiExtractFilterType.BY_SAMPLE].concat(submissionItemsForAll);
        this.submissionItems[GobiiExtractFilterType.BY_SAMPLE].push(
            StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CvTerms)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                .setCardinality(CardinalityType.ONE_ONLY)
        );

        // -- Platforms
        this.submissionItems[GobiiExtractFilterType.BY_SAMPLE].push(
            StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                .setCategoryType(ExtractorCategoryType.CONTAINER)
                .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                .setCardinality(CardinalityType.ZERO_OR_MORE)
                .addChild(
                    StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Platforms)
                        .setEntityName(this.entityNodeLabels[EntityType.Platforms])
                        .setCardinality(CardinalityType.ZERO_OR_MORE)
                )
        );

        // -- Samples
        this.submissionItems[GobiiExtractFilterType.BY_SAMPLE].push(
            StatusTreeTemplate.build(ExtractorItemType.CATEGORY)
                .setCategoryType(ExtractorCategoryType.CONTAINER)
                .setCategoryName("Sample Crieria")
                .setCardinality(CardinalityType.ONE_OR_MORE)
                .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                .addChild(StatusTreeTemplate.build(ExtractorItemType.ENTITY)
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

    private makeDemoTreeNodes() {
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


    // *****************************************************************
    // *********************  TREE NODE DATA STRUCTURES AND EVENTS

    private demoTreeNodes: TreeNode[] = [];
    private selectedDemoNodes: TreeNode[] = [];


    private gobiiTreeNodes: GobiiTreeNode[] = [];
    private selectedGobiiNodes: GobiiTreeNode[] = [];
    private entityNodeLabels: Map<EntityType,string> = new Map<EntityType,string>();
    private entitySubtypeNodeLabels: Map<EntitySubType,string> = new Map<EntitySubType,string>();
    private cvFilterNodeLabels: Map<CvFilterType,string> = new Map<CvFilterType,string>();


    private experimentId: string;


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

    private expandRecursive(node: TreeNode, isExpand: boolean) {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach(childNode => {
                this.expandRecursive(childNode, isExpand);
            });
        }
    }


    // ********************************************************************************
    // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS


    private makeGobiiTreeNode(entityType: EntityType, isContainer: boolean): GobiiTreeNode {

        let returnVal: GobiiTreeNode = new GobiiTreeNode(entityType);

        if (isContainer) {
            returnVal.collapsedIcon = "fa-folder";
            returnVal.expandedIcon = "fa-folder-open";
        } else {
            if (entityType === EntityType.DataSets) {

                returnVal.icon = "fa-database";
            }
        }

        return returnVal;

    }

    private makeCbEventFromNode(treeNode: TreeNode): CheckBoxEvent {

        let returnVal: CheckBoxEvent = null;

        return returnVal;

    }


    private makeNodeFromCbEvent(cbEvent: CheckBoxEvent): GobiiTreeNode {

        let returnVal: GobiiTreeNode = this.makeGobiiTreeNode(cbEvent.entityType, false);

        returnVal.label = cbEvent.name;

        return returnVal;
    }


    private placeNodeInTree(treeNode: GobiiTreeNode) {

        if (treeNode.entityType === EntityType.DataSets) {

            let parentNode: GobiiTreeNode = this.gobiiTreeNodes.filter(n => n.entityType === EntityType.DataSets)[0];
            if (parentNode == null) {
                parentNode = this.makeGobiiTreeNode(EntityType.DataSets, true);
                parentNode.label = this.entityNodeLabels[EntityType.DataSets];
                this.gobiiTreeNodes.push(parentNode);
            }

            parentNode.expanded = true;
            parentNode.children.push(treeNode);
            this.selectedGobiiNodes.push(treeNode);

        }

    } //

    private setUpRequredItems() {

    }

    // ********************************************************************************
    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS


    private gobiiExtractFilterType: GobiiExtractFilterType;

    private onItemChecked: EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected: EventEmitter<CheckBoxEvent> = new EventEmitter();


    private submissionItems: Map<GobiiExtractFilterType,Array<StatusTreeTemplate>> =
        new Map<GobiiExtractFilterType,Array<StatusTreeTemplate>>();

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

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
            this.setUpRequredItems();

            // this.setList(changes['nameIdList'].currentValue);

        }
    }

}
