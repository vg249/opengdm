System.register(["@angular/core", "primeng/components/tree/tree"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, tree_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (tree_1_1) {
                tree_1 = tree_1_1;
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent() {
                }
                StatusDisplayTreeComponent.prototype.ngOnInit = function () {
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
                                    "children": [{ "label": "Expenses.doc", "icon": "fa-file-word-o", "data": "Expenses Document" }, { "label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document" }]
                                },
                                {
                                    "label": "Home",
                                    "data": "Home Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{ "label": "Invoices.txt", "icon": "fa-file-word-o", "data": "Invoices for this month" }]
                                }]
                        },
                        {
                            "label": "Pictures",
                            "data": "Pictures Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [
                                { "label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo" },
                                { "label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo" },
                                { "label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo" }
                            ]
                        },
                        {
                            "label": "Movies",
                            "data": "Movies Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Al Pacino",
                                    "data": "Pacino Movies",
                                    "children": [{ "label": "Scarface", "icon": "fa-file-video-o", "data": "Scarface Movie" }, { "label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie" }]
                                },
                                {
                                    "label": "Robert De Niro",
                                    "data": "De Niro Movies",
                                    "children": [{ "label": "Goodfellas", "icon": "fa-file-video-o", "data": "Goodfellas Movie" }, { "label": "Untouchables", "icon": "fa-file-video-o", "data": "Untouchables Movie" }]
                                }]
                        }
                    ];
                    var foo = "foo";
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
                };
                StatusDisplayTreeComponent.prototype.nodeSelect = function (event) {
                    this.msgs = [];
                    this.msgs.push({ severity: 'info', summary: 'Node Selected', detail: event.node.label });
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                    this.msgs = [];
                    this.msgs.push({ severity: 'info', summary: 'Node Unselected', detail: event.node.label });
                };
                StatusDisplayTreeComponent.prototype.nodeExpandMessage = function (event) {
                    this.msgs = [];
                    this.msgs.push({ severity: 'info', summary: 'Node Expanded', detail: event.node.label });
                };
                StatusDisplayTreeComponent.prototype.nodeExpand = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.viewFile = function (file) {
                    this.msgs = [];
                    this.msgs.push({ severity: 'info', summary: 'Node Selected with Right Click', detail: file.label });
                };
                StatusDisplayTreeComponent.prototype.unselectFile = function () {
                    this.selectedFile2 = null;
                };
                StatusDisplayTreeComponent.prototype.expandAll = function () {
                    var _this = this;
                    this.filesTree6.forEach(function (node) {
                        _this.expandRecursive(node, true);
                    });
                };
                StatusDisplayTreeComponent.prototype.collapseAll = function () {
                    var _this = this;
                    this.filesTree6.forEach(function (node) {
                        _this.expandRecursive(node, false);
                    });
                };
                StatusDisplayTreeComponent.prototype.expandRecursive = function (node, isExpand) {
                    var _this = this;
                    node.expanded = isExpand;
                    if (node.children) {
                        node.children.forEach(function (childNode) {
                            _this.expandRecursive(childNode, isExpand);
                        });
                    }
                };
                return StatusDisplayTreeComponent;
            }());
            __decorate([
                core_1.ViewChild('expandingTree'),
                __metadata("design:type", tree_1.Tree)
            ], StatusDisplayTreeComponent.prototype, "expandingTree", void 0);
            StatusDisplayTreeComponent = __decorate([
                core_1.Component({
                    selector: 'status-display-tree',
                    //    inputs: ['messages'],
                    //directives: [RADIO_GROUP_DIRECTIVES]
                    template: " <p-tree [value]=\"filesTree4\" selectionMode=\"checkbox\" [(selection)]=\"selectedFiles2\"></p-tree>\n                    <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n"
                }),
                __metadata("design:paramtypes", [])
            ], StatusDisplayTreeComponent);
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map