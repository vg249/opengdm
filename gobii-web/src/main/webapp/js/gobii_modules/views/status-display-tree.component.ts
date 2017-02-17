import {Component, OnInit, ViewChild} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {Tree} from "primeng/components/tree/tree";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
@Component({
    selector: 'status-display-tree',
//    inputs: ['messages'],
    //directives: [RADIO_GROUP_DIRECTIVES]
    template: ` <p-tree [value]="filesTree4" selectionMode="checkbox" [(selection)]="selectedFiles2"></p-tree>
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit{


    msgs: Message[];

    @ViewChild('expandingTree')
    expandingTree: Tree;

    filesTree1: TreeNode[];
    filesTree2: TreeNode[];
    filesTree3: TreeNode[];
    filesTree4: TreeNode[];
    filesTree5: TreeNode[];
    filesTree6: TreeNode[];
    filesTree7: TreeNode[];
    filesTree8: TreeNode[];

    lazyFiles: TreeNode[];

    selectedFile: TreeNode;

    selectedFile2: TreeNode;

    selectedFile3: TreeNode;

    selectedFiles: TreeNode[];

    selectedFiles2: TreeNode[];

    items: MenuItem[];

    constructor() { }

    ngOnInit() {


        this.filesTree4 = [
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
                    "children": [{"label": "Expenses.doc", "icon": "fa-file-word-o", "data": "Expenses Document"}, {"label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document"}]
                },
                    {
                        "label": "Home",
                        "data": "Home Folder",
                        "expandedIcon": "fa-folder-open",
                        "collapsedIcon": "fa-folder",
                        "children": [{"label": "Invoices.txt", "icon": "fa-file-word-o", "data": "Invoices for this month"}]
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
                    "children": [{"label": "Scarface", "icon": "fa-file-video-o", "data": "Scarface Movie"}, {"label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie"}]
                },
                    {
                        "label": "Robert De Niro",
                        "data": "De Niro Movies",
                        "children": [{"label": "Goodfellas", "icon": "fa-file-video-o", "data": "Goodfellas Movie"}, {"label": "Untouchables", "icon": "fa-file-video-o", "data": "Untouchables Movie"}]
                    }]
            }
        ];


        let foo:string = "foo";
        // this.nodeService.getFiles().then(files => this.filesTree1 = files);
        // this.nodeService.getFiles().then(files => this.filesTree2 = files);
        // this.nodeService.getFiles().then(files => this.filesTree3 = files);
        // this.nodeService.getFiles().then(files => this.filesTree4 = files);
        // this.nodeService.getFiles().then(files => this.filesTree5 = files);
        // this.nodeService.getFiles().then(files => this.filesTree6 = files);
        // this.nodeService.getFiles().then(files => this.filesTree7 = files);

        //     this.filesTree8 = [{
        //         label: 'Root',
        //         children: files
        //     }];
        // });
        //
        // this.nodeService.getLazyFiles().then(files => this.lazyFiles = files);
        //
        // this.items = [
        //     {label: 'View', icon: 'fa-search', command: (event) => this.viewFile(this.selectedFile2)},
        //     {label: 'Unselect', icon: 'fa-close', command: (event) => this.unselectFile()}
        // ];
    }

    nodeSelect(event) {
        this.msgs = [];
        this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.label});
    }

    nodeUnselect(event) {
        this.msgs = [];
        this.msgs.push({severity: 'info', summary: 'Node Unselected', detail: event.node.label});
    }

    nodeExpandMessage(event) {
        this.msgs = [];
        this.msgs.push({severity: 'info', summary: 'Node Expanded', detail: event.node.label});
    }

    nodeExpand(event) {
        if(event.node) {
            //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
            //this.nodeService.getLazyFiles().then(nodes => event.node.children = nodes);
        }
    }

    viewFile(file: TreeNode) {
        this.msgs = [];
        this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
    }

    unselectFile() {
        this.selectedFile2 = null;
    }

    expandAll(){
        this.filesTree6.forEach( node => {
            this.expandRecursive(node, true);
        } );
    }

    collapseAll(){
        this.filesTree6.forEach( node => {
            this.expandRecursive(node, false);
        } );
    }

    private expandRecursive(node:TreeNode, isExpand:boolean){
        node.expanded = isExpand;
        if(node.children){
            node.children.forEach( childNode => {
                this.expandRecursive(childNode, isExpand);
            } );
        }
    }
}
