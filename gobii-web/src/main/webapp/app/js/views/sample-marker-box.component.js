System.register(["@angular/core", "../model/dto-header-status-message", "../model/type-extractor-filter", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "./entity-labels", "../model/file-item-param-names", "../store/reducers", "../services/core/file-item-service", "@ngrx/store", "../model/type-status-level", "../store/actions/history-action", "../model/type-entity", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, dto_header_status_message_1, type_extractor_filter_1, gobii_file_item_1, type_process_1, type_extractor_item_1, entity_labels_1, file_item_param_names_1, fromRoot, file_item_service_1, store_1, type_status_level_1, historyAction, type_entity_1, view_id_generator_service_1, SampleMarkerBoxComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            SampleMarkerBoxComponent = class SampleMarkerBoxComponent {
                constructor(store, fileItemService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.nameIdFilterParamTypesMarkerGroup = file_item_param_names_1.FilterParamNames.MARKER_GROUPS;
                    this.extractorItemType = type_extractor_item_1.ExtractorItemType;
                    this.ITEM_FILE_TYPE = "ITEM_FILE_TYPE";
                    this.ITEM_LIST_TYPE = "ITEM_LIST_TYPE";
                    this.MARKER_GROUP_TYPE = "MARKER_GROUP_TYPE";
                    this.maxListItems = 200;
                    this.displayMaxItemsExceeded = false;
                    this.displayChoicePrompt = false;
                    this.selectedListType = this.ITEM_FILE_TYPE;
                    this.displayUploader = true;
                    this.displayListBox = false;
                    this.displayMarkerGroupBox = false;
                    this.displayMarkerGroupRadio = false;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onSampleMarkerError = new core_1.EventEmitter();
                    this.currentFileItems = [];
                }
                // private handleUserSelected(arg) {
                //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                // }
                //
                // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
                //
                // } // ctor
                handleTextBoxDataSubmitted(items) {
                    if (items.length <= this.maxListItems) {
                        let nonDuplicateItems = [];
                        items.forEach(item => {
                            if (!nonDuplicateItems.find(ii => ii === item)) {
                                nonDuplicateItems.push(item);
                            }
                        });
                        let listItemType = this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER ?
                            type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM : type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                        nonDuplicateItems.forEach(listItem => {
                            if (listItem && listItem !== "") {
                                this.fileItemService
                                    .loadFileItem(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(listItemType)
                                    .setItemId(listItem)
                                    .setItemName(listItem)
                                    .setIsEphemeral(true), true);
                            }
                        });
                    }
                    else {
                        if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            this.maxExceededTypeLabel = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM];
                        }
                        else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            this.maxExceededTypeLabel = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM];
                        }
                        else {
                            this.handleStatusHeaderMessage(new dto_header_status_message_1.HeaderStatusMessage("This control does not handle the currently selected item type: "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], null, null));
                        }
                        this.displayMaxItemsExceeded = true;
                    }
                }
                makeLabel(inputType) {
                    let returnVal;
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        if (inputType === this.ITEM_FILE_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_FILE];
                        }
                        else if (inputType === this.ITEM_LIST_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM];
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type making input type label "
                                + inputType
                                + " for extract type "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                        }
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        if (inputType === this.ITEM_FILE_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_FILE];
                        }
                        else if (inputType === this.ITEM_LIST_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM];
                        }
                        else if (inputType === this.MARKER_GROUP_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MARKER_GROUP];
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type making input type label "
                                + inputType
                                + " for extract type "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                        }
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("This component is not intended to be used in extract type: " + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                    }
                    return returnVal;
                }
                handleSampleMarkerChoicesExist(previousInputType, proposedInputType) {
                    let returnVal = false;
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe((fileItems) => {
                        let extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                        let extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                        if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                        }
                        else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM;
                            extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.MARKER_FILE;
                        }
                        this.currentFileItems = fileItems.filter(item => {
                            return ((item.getExtractorItemType() === extractorItemTypeListToFind) ||
                                (item.getExtractorItemType() === extractorItemTypeFileToFind) ||
                                (item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY &&
                                    item.getEntityType() === type_entity_1.EntityType.MARKER_GROUP));
                        });
                        if (this.currentFileItems.length > 0) {
                            this.extractTypeLabelProposed = this.makeLabel(proposedInputType);
                            this.extractTypeLabelExisting = this.makeLabel(previousInputType);
                            this.displayChoicePrompt = true;
                            returnVal = true;
                            // it does not seem that the PrimeNG dialog really blocks in the usual sense;
                            // so we have to chain what we do next off of the click events on the dialog.
                            // see handleUserChoice()
                        }
                    }, hsm => {
                        this.handleStatusHeaderMessage(hsm);
                    }).unsubscribe();
                    return returnVal;
                }
                handleUserChoice(userChoice) {
                    this.displayChoicePrompt = false;
                    if (this.currentFileItems.length > 0 && userChoice === true) {
                        this.setDisplayFlags(this.extractTypeLabelProposed);
                        this.currentFileItems.forEach(currentFileItem => {
                            currentFileItem.setProcessType(type_process_1.ProcessType.DELETE);
                            this.fileItemService
                                .unloadFileItemFromExtract(currentFileItem);
                        });
                    }
                    else {
                        this.setDisplayFlags(this.extractTypeLabelExisting);
                    } // if-else user answered "yes"
                }
                setDisplayFlags(labelValue) {
                    if (labelValue === this.makeLabel(this.ITEM_LIST_TYPE)) {
                        this.displayListBox = true;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = false;
                        this.selectedListType = this.ITEM_LIST_TYPE;
                    }
                    else if (labelValue === this.makeLabel(this.ITEM_FILE_TYPE)) {
                        this.displayListBox = false;
                        this.displayUploader = true;
                        this.displayMarkerGroupBox = false;
                        this.selectedListType = this.ITEM_FILE_TYPE;
                    }
                    else if (labelValue === this.makeLabel(this.MARKER_GROUP_TYPE)) {
                        this.displayListBox = false;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = true;
                        this.selectedListType = this.MARKER_GROUP_TYPE;
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type setting display flags for label type "
                            + labelValue
                            + " for extract type "
                            + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                    }
                } // setDisplayFlags()
                getPreviousInputType() {
                    let returnVal;
                    if (this.displayListBox) {
                        returnVal = this.ITEM_LIST_TYPE;
                    }
                    else if (this.displayUploader) {
                        returnVal = this.ITEM_FILE_TYPE;
                    }
                    else if (this.displayMarkerGroupBox) {
                        returnVal = this.MARKER_GROUP_TYPE;
                    }
                    return returnVal;
                }
                handleTextBoxChanged(event) {
                    // if there is no existing selected list or file, then this is just a simple setting
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_LIST_TYPE) === false) {
                        this.displayListBox = true;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = false;
                        // this.displayListBox = true;
                        // this.displayUploader = false;
                    }
                }
                handleOnClickBrowse($event) {
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_FILE_TYPE) === false) {
                        this.displayListBox = false;
                        this.displayUploader = true;
                        this.displayMarkerGroupBox = false;
                        // this.displayListBox = false;
                        // this.displayUploader = true;
                    }
                }
                handleMarkerGroupChanged($event) {
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.MARKER_GROUP_TYPE) === false) {
                        this.displayListBox = false;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = true;
                    }
                }
                handleStatusHeaderMessage(statusMessage) {
                    this.onSampleMarkerError.emit(statusMessage);
                }
                ngOnInit() {
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.MARKER_GROUPS, null);
                    }
                    return null;
                }
                ngOnChanges(changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            //this.notificationSent = false;
                            if (this.gobiiExtractFilterType == type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                this.displayMarkerGroupRadio = true;
                            }
                            else {
                                this.displayMarkerGroupRadio = false;
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                } // ngonChanges
            };
            SampleMarkerBoxComponent = __decorate([
                core_1.Component({
                    selector: 'sample-marker-box',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onSampleMarkerError'],
                    encapsulation: core_1.ViewEncapsulation.Emulated,
                    styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
                        "js/node_modules/primeng/resources/primeng.css",
                        "js/node_modules/bootswatch/cerulean/bootstrap.min.css"],
                    template: `
        <div class="container-fluid">

            <div class="row">

                <p-radioButton
                        (click)="handleOnClickBrowse($event)"
                        name="listType"
                        value="ITEM_FILE_TYPE"
                        [(ngModel)]="selectedListType"
                        [id]="viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(ITEM_FILE_TYPE))">
                </p-radioButton>
                <label class="the-legend">File&nbsp;</label>
                <p-radioButton
                        (click)="handleTextBoxChanged($event)"
                        name="listType"
                        value="ITEM_LIST_TYPE"
                        [(ngModel)]="selectedListType"
                        [id]="viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(ITEM_LIST_TYPE))">
                </p-radioButton>
                <label class="the-legend">List&nbsp;</label>
                <p-radioButton *ngIf="displayMarkerGroupRadio"
                               (click)="handleMarkerGroupChanged($event)"
                               name="listType"
                               value="MARKER_GROUP_TYPE"
                               [(ngModel)]="selectedListType"
                               [id]="viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(MARKER_GROUP_TYPE))">
                </p-radioButton>
                <label *ngIf="displayMarkerGroupRadio"
                       class="the-legend">Marker Groups&nbsp;</label>

            </div>

            <div class="row">

                <div *ngIf="displayUploader" class="col-md-8">
                    <uploader
                            [gobiiExtractFilterType]="gobiiExtractFilterType"
                            (onUploaderError)="handleStatusHeaderMessage($event)"></uploader>
                </div>

                <div *ngIf="displayListBox" class="col-md-8">
                    <text-area
                            (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"></text-area>
                </div>
                <div *ngIf="displayListBox" class="col-md-4">
                    <p class="text-warning">{{maxListItems}} maximum</p>
                </div>

                <div *ngIf="selectedListType == MARKER_GROUP_TYPE" class="col-md-8">
                    <checklist-box
                            [filterParamName]="nameIdFilterParamTypesMarkerGroup"
                            [gobiiExtractFilterType]="gobiiExtractFilterType">
                    </checklist-box>
                </div>

            </div>

            <div>
                <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt"
                          modal="modal" width="300" height="300" responsive="true">
                    <p>{{extractTypeLabelExisting}} already selected. Specify {{extractTypeLabelProposed}}
                        instead?</p>
                    <p-footer>
                        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                            <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)"
                                    label="No"></button>
                            <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)"
                                    label="Yes"></button>
                        </div>
                    </p-footer>
                </p-dialog>
            </div>

                <p-dialog header="Maximum {{maxExceededTypeLabel}} Items Exceeded" [(visible)]="displayMaxItemsExceeded"
                          modal="modal" [dismissableMask]="true" width="300" height="200" [responsive]="true">
                    <p>You attempted to paste more than {{maxListItems}} {{maxExceededTypeLabel}} items; Please reduce
                        the size of the list</p>

                    <p-footer>
                        <button type="button" pButton (click)="displayMaxItemsExceeded=false" label="OK"></button>
                    </p-footer>
                </p-dialog>

        </div>`
                }),
                __metadata("design:paramtypes", [store_1.Store,
                    file_item_service_1.FileItemService,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], SampleMarkerBoxComponent);
            exports_1("SampleMarkerBoxComponent", SampleMarkerBoxComponent);
        }
    };
});
//# sourceMappingURL=sample-marker-box.component.js.map