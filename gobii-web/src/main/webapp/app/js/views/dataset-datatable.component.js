System.register(["@angular/core", "@ngrx/store", "../store/reducers", "../store/actions/history-action", "../model/type-extractor-filter", "../services/core/file-item-service", "../model/file-item-param-names", "../store/actions/fileitem-action", "../services/core/dto-request.service", "../services/app/jsontogfi/json-to-gfi-dataset", "../services/core/filter-params-coll", "../services/app/dto-request-item-gfi", "../services/app/jsontogfi/json-to-gfi-analysis", "../model/cv-filter-type", "../model/type-entity", "../model/gobii-file-item-compound-id", "../model/type-extractor-item", "rxjs", "rxjs/operators", "../store/actions/action-payload-filter", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, store_1, fromRoot, historyAction, type_extractor_filter_1, file_item_service_1, file_item_param_names_1, fileAction, dto_request_service_1, json_to_gfi_dataset_1, filter_params_coll_1, dto_request_item_gfi_1, json_to_gfi_analysis_1, cv_filter_type_1, type_entity_1, gobii_file_item_compound_id_1, type_extractor_item_1, rxjs_1, operators_1, action_payload_filter_1, view_id_generator_service_1, DatasetDatatableComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (json_to_gfi_dataset_1_1) {
                json_to_gfi_dataset_1 = json_to_gfi_dataset_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (dto_request_item_gfi_1_1) {
                dto_request_item_gfi_1 = dto_request_item_gfi_1_1;
            },
            function (json_to_gfi_analysis_1_1) {
                json_to_gfi_analysis_1 = json_to_gfi_analysis_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (rxjs_1_1) {
                rxjs_1 = rxjs_1_1;
            },
            function (operators_1_1) {
                operators_1 = operators_1_1;
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            DatasetDatatableComponent = class DatasetDatatableComponent {
                constructor(store, fileItemService, filterParamsColl, fileItemRequestService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterParamsColl = filterParamsColl;
                    this.fileItemRequestService = fileItemRequestService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.foo = "foo";
                    this.onClickForNextPage$ = new rxjs_1.Subject();
                    this.doPaging = false;
                    this.datasetAnalysesNames = [];
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                    this.analysisPanelCollapsed = true;
                    this.analysisPanelToggle = true;
                    this.filterToExtractReady = true;
                    this.disableFilterToExtractReadyCheckbox = false;
                    this.page = 0;
                    if (this.doPaging) {
                        this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntitiesPaged);
                    }
                    else {
                        this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntities);
                    }
                    this.onClickForNextPage$
                        .pipe(operators_1.withLatestFrom(this.store))
                        .subscribe(([data, state]) => {
                        if (state.fileItems.filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED]) {
                            let pagination = state.fileItems.filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED].pagination;
                            if (pagination) {
                                this.fileItemService.loadPagedEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, pagination.pagedQueryId, pagination.pageSize, ++this.page);
                            }
                        }
                    });
                }
                /**
                 * Event to allow only extract ready jobs to be selected.
                 * @param fi - GobiiFileItem
                 */
                hideNonExtractReadyJobs(fi, jobStatusFilterValues) {
                    return !action_payload_filter_1.ExtractReadyPayloadFilter.isExtractReady(fi, jobStatusFilterValues);
                }
                handleFilterToExtractReadyChecked(event) {
                    let jobStatusFilterValues;
                    if (event === true) {
                        /**
                         * bug/GSD-557
                         * Load only if datasets with associated jobs of type "load" has listed status.
                         * For load jobs, status should be "completed".
                         */
                        jobStatusFilterValues = {
                            "load": ["completed"]
                        };
                    }
                    else {
                        jobStatusFilterValues = null;
                    }
                    this.store.dispatch(new fileAction.LoadFilterAction({
                        filterId: file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS,
                        filter: new action_payload_filter_1.ExtractReadyPayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DATASET, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.UNKNOWN)), null, null, null, null, null, jobStatusFilterValues)
                    }));
                }
                handleHideOverlayPanel($event) {
                    this.datasetAnalysesNames = [];
                    this.analysisPanelCollapsed = true;
                }
                /***
                 * Lazy load dataset when the dataset pane is opened. Notice that we don't dispatch the dataset to the store.
                 * There are a couple of things to note here:
                 * 1) Keeping data local to the component breaks the store model, because we are effectively keeping some state locally.
                 *    I would argue that this issues is mitigated by the fact the data are only used in that pop up and then they go away;
                 * 2) Consequently, if the user returns over and over again to the same dataset, we are taking on the otherwise unnecessary
                 *    expense of repeating the same query. However, it is my judgement that that scenario will happen infrequently enough
                 *    that we don't need to worry about this for now.
                 * @param event
                 * @param {GobiiFileItem} dataSeItem
                 * @param {OverlayPanel} datasetOverlayPanel
                 */
                selectDataset(event, dataSeItem, datasetOverlayPanel) {
                    let datasetId = dataSeItem.getEntity().id;
                    let filterParams = this.filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.DATASET_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                    let dtoRequestItemGfi = new dto_request_item_gfi_1.DtoRequestItemGfi(filterParams, datasetId.toString(), new json_to_gfi_dataset_1.JsonToGfiDataset(filterParams, this.filterParamsColl));
                    this.fileItemRequestService
                        .get(dtoRequestItemGfi)
                        .subscribe(entityItems => {
                        if (entityItems.length === 1 && entityItems[0].getEntity()) {
                            this.selectedDatasetDetailEntity = entityItems[0].getEntity();
                            this.analysisPanelToggle = this.selectedDatasetDetailEntity.analysesIds.length > 0;
                            datasetOverlayPanel.toggle(event);
                        }
                        else {
                            this.store
                                .dispatch(new historyAction.AddStatusMessageAction("There is no dataset data for dataset id "
                                + datasetId.toString()));
                        }
                    });
                }
                /***
                 * Lazy-load analyses if there are any.
                 * The note about not putting these data in the store with regard to the dataset entity applies to
                 * the analyses.
                 * @param event
                 */
                handleOpenAnalysesTab(event) {
                    if (this.selectedDatasetDetailEntity) {
                        let datasetId = this.selectedDatasetDetailEntity.id;
                        let filterParams = this.filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.ANALYSES_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                        let dtoRequestItemGfi = new dto_request_item_gfi_1.DtoRequestItemGfi(filterParams, datasetId.toString(), new json_to_gfi_analysis_1.JsonToGfiAnalysis(filterParams, this.filterParamsColl));
                        this.fileItemRequestService
                            .get(dtoRequestItemGfi)
                            .subscribe(entityItems => {
                            this.datasetAnalysesNames = entityItems
                                .map(gfi => gfi.getItemName());
                        });
                    } // if we have a selected datset entity
                }
                handleRowChecked(checked, selectedDatasetFileItem) {
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), checked);
                }
                handleRowSelect(event) {
                    let selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                }
                handleRowUnSelect(event) {
                    let selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                }
                handleOnRowClick(event) {
                    let selectedDataset = event.data;
                }
                handleItemChecked(currentFileItemUniqueId, isChecked) {
                    if (isChecked) {
                        this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
                    }
                    else {
                        this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
                    }
                } // handleItemChecked()
                ngOnInit() {
                    //this.handleLoadPage(1);
                } // ngOnInit()
                // gobiiExtractType is not set until you get OnChanges
                ngOnChanges(changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                this.filterToExtractReady = true;
                                if (this.doPaging) {
                                    this.fileItemService.loadPagedEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, null, 5, 0);
                                }
                                else {
                                    this.fileItemService.loadEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST);
                                }
                                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, null);
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                } // ngonChanges
            };
            __decorate([
                core_1.Input(),
                __metadata("design:type", Number)
            ], DatasetDatatableComponent.prototype, "gobiiExtractFilterType", void 0);
            DatasetDatatableComponent = __decorate([
                core_1.Component({
                    selector: 'dataset-datatable',
                    inputs: [],
                    outputs: [],
                    template: `
        <div style="border: 1px solid #336699; padding-left: 5px">
            <BR>
            <p-checkbox binary="true"
                        [(ngModel)]="filterToExtractReady"
                        (onChange)="handleFilterToExtractReadyChecked($event)"
                        [disabled]="disableFilterToExtractReadyCheckbox">
            </p-checkbox>
            <label class="the-legend">Extract-Ready&nbsp;</label>
            <BR>
            <div class="container-fluid">


                <!--<name-id-list-box-->
                <!--[gobiiExtractFilterType]="gobiiExtractFilterType"-->
                <!--[filterParamName]="nameIdFilterParamTypes.CV_JOB_STATUS">-->
                <!--</name-id-list-box>-->

            </div> <!--status selector row -->
            <p-dataTable [value]="datasetsFileItems$ | async"
                         [(selection)]="selectedDatasets"
                         (onRowSelect)="handleRowSelect($event)"
                         (onRowUnselect)="handleRowUnSelect($event)"
                         (onRowClick)="handleOnRowClick($event)"
                         dataKey="_entity.id"
                         resizableColumns="true"
                         scrollable="true"
                         scrollHeight="700px"
                         scrollWidth="100%"
                         columnResizeMode="expand">
                <p-column field="_entity.id" header="Id" hidden="true"></p-column>
                <p-column [style]="{'width':'5%','text-align':'center'}">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        <p-checkbox binary="true"
                                    [ngModel]="fi.getSelected()"
                                    (onChange)="handleRowChecked($event, fi)"
                                    [hidden]="hideNonExtractReadyJobs(fi, {
                                        'load' : ['completed']
                                    })"
                                    [id]="viewIdGeneratorService.makeDatasetRowCheckboxId(fi._entity.datasetName)">
                        </p-checkbox>

                    </ng-template>
                </p-column>
                <p-column [style]="{'width':'5%','text-align':'center'}">
                    <ng-template let-col="rowData" pTemplate="body">
                        <button type="button"
                                pButton (click)="selectDataset($event,col,datasetOverlayPanel);"
                                icon="fa-bars"
                                style="font-size: 10px"></button>
                    </ng-template>
                </p-column>
                <p-column field="_entity.datasetName"
                          header="Name"
                          [style]="{'width': '18%'}"
                          [sortable]="true">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.datasetName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.datasetName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.projectName"
                          header="Project"
                          [style]="{'width': '18%'}"
                          [sortable]="true">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.projectName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.projectName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.experimentName"
                          header="Experiment"
                          [style]="{'width': '18%'}"
                          [sortable]="true">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.experimentName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.experimentName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.piLastName"
                          header="PI"
                          [style]="{'width': '18%'}"
                          [sortable]="true">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.piLastName}}, {{fi._entity.piFirstName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.piLastName}}, {{fi._entity.piFirstName}} </span>
                    </ng-template>
                </p-column>
                <!--<p-column field="_entity.jobStatusName" header="Status"></p-column>-->
                <!--<p-column field="_entity.jobTypeName" header="Type"></p-column>-->
                <p-column field="_entity.loadedDate"
                          header="Loaded"
                          [style]="{'width': '18%'}"
                          [sortable]="true">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        {{fi._entity.loadedDate | date:'yyyy-MM-dd' }}
                    </ng-template>
                </p-column>
            </p-dataTable>
            <p-overlayPanel #datasetOverlayPanel
                            appendTo="body"
                            showCloseIcon="true"
                            (onBeforeHide)="handleHideOverlayPanel($event)">


                <!-- you have to  -->
                <legend>Details:
                    {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datasetName : null}}
                </legend>


                <div class="panel panel-default">
                    <table class="table table-striped table-hover">
                        <!--<table class="table table-striped table-hover table-bordered">-->
                        <tbody>
                        <tr>
                            <td><b>Principal Investigator</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.piLastName + ", " + selectedDatasetDetailEntity.piFirstName : null}}
                            </td>
                        </tr>
                        <tr>
                            <td><b>Loaded By</b></td>
                            <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.loaderLastName : null}}
                                {{ (selectedDatasetDetailEntity && selectedDatasetDetailEntity.loaderFirstName) ? ", " : null}}
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.loaderFirstName : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Loaded Date</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? (selectedDatasetDetailEntity.loadedDate | date:'yyyy-MM-dd') : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Project</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.projectName : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Data Type</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datatypeName : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Calling Analysis</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.callingAnalysisName : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Total Samples</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalSamples : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Total Markers</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalMarkers : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Experiment</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.experimentName : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Platform</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.platformName : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Protocol</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.protocolName : null}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <BR>
                <div>

                    <p-panel
                            header="{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.analysesIds.length : null}} Analyses"
                            (onBeforeToggle)="handleOpenAnalysesTab($event)"
                            [(toggleable)]="analysisPanelToggle"
                            [(collapsed)]="analysisPanelCollapsed">
                        <p *ngFor="let name of datasetAnalysesNames">
                            {{ name }}
                        </p>
                    </p-panel>
                </div>

            </p-overlayPanel>

            <div *ngIf="doPaging">
                <button pButton type="button" (click)="onClickForNextPage$.next($event)" label="Test Paging"></button>
            </div>
        </div> <!-- enclosing box  -->
    ` // end template
                }),
                __metadata("design:paramtypes", [store_1.Store,
                    file_item_service_1.FileItemService,
                    filter_params_coll_1.FilterParamsColl,
                    dto_request_service_1.DtoRequestService,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], DatasetDatatableComponent);
            exports_1("DatasetDatatableComponent", DatasetDatatableComponent);
        }
    };
});
//# sourceMappingURL=dataset-datatable.component.js.map