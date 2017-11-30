import {Component, Input, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as historyAction from '../store/actions/history-action';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemService} from "../services/core/file-item-service";
import {FilterParamNames} from "../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import {DataSet} from "../model/dataset";
import {GobiiFileItem} from "../model/gobii-file-item";
import * as fileAction from '../store/actions/fileitem-action';
import {ExtractorItemType} from "../model/type-extractor-item";
import {OverlayPanel} from "primeng/primeng";
import {DtoRequestService} from "../services/core/dto-request.service";
import {JsonToGfiDataset} from "../services/app/jsontogfi/json-to-gfi-dataset";
import {FilterParamsColl} from "../services/core/filter-params-coll";
import {DtoRequestItemGfi} from "../services/app/dto-request-item-gfi";
import {FilterParams} from "../model/file-item-params";


@Component({
    selector: 'dataset-datatable',
    inputs: [],
    outputs: [],
    template: `
        <div style="border: 1px solid #336699; padding-left: 5px">
            <div class="container-fluid">

                <BR>
                <label class="the-legend">Filter by Status:&nbsp;</label>
                <name-id-list-box
                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                        [filterParamName]="nameIdFilterParamTypes.CV_JOB_STATUS">
                </name-id-list-box>

            </div> <!--status selector row -->

            <p-dataTable [value]="datasetsFileItems$ | async"
                         [(selection)]="selectedDatasets"
                         (onRowSelect)="handleRowSelect($event)"
                         (onRowUnselect)="handleRowUnSelect($event)"
                         (onRowClick)="handleOnRowClick($event)"
                         dataKey="_entity.id">
                <p-column [style]="{'width':'30px'}">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        <p-checkbox binary="true"
                                    [ngModel]="fi.getSelected()"
                                    (onChange)="handleRowChecked($event, fi)"
                                    [disabled]="fi.getEntity().jobStatusName !== 'completed' || (fi.getEntity().jobTypeName !== 'load')">
                        </p-checkbox>

                    </ng-template>
                </p-column>
                <p-column [style]="{'width':'10%','text-align':'center'}">
                    <ng-template let-col="rowData" pTemplate="body">
                        <button type="button" pButton (click)="selectDataset($event,col,datasetOverlayPanel);"
                                icon="fa-bars"></button>
                    </ng-template>
                </p-column>
                <p-column field="_entity.id" header="Id" hidden="true"></p-column>
                <p-column field="_entity.datasetName" header="Name"></p-column>
                <p-column field="_entity.jobStatusName" header="Status"></p-column>
                <p-column field="_entity.jobTypeName" header="Type"></p-column>
                <p-column field="jobSubmittedDate" header="Submitted">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        {{fi._entity[col.field] | date:'yyyy-MM-dd HH:mm' }}
                    </ng-template>
                </p-column>
            </p-dataTable>
            <p-overlayPanel #datasetOverlayPanel appendTo="body" showCloseIcon="true">


                <!-- you have to  -->
                <legend>Details:
                    {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datasetName : null}}
                </legend>


                <div class="panel panel-default">
                    <table class="table table-striped table-hover">
                        <!--<table class="table table-striped table-hover table-bordered">-->
                        <tbody>
                        <tr>
                            <td><b>Principle Investigator</b></td>
                            <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.piEmail : null}}</td>
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

                        </tbody>
                    </table>
                </div>


                <div class="panel panel-default">
                    <div class="panel-heading" style="font-size: medium">
                        <b>Experiment:
                            {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.experimentName : null}}</b>

                    </div>
                    <div class="card text-white bg-info">

                        <div class="card-body">
                            <table class="table table-striped table-hover">
                                <!--<table class="table table-striped table-hover table-bordered">-->
                                <tbody>
                                <tr>
                                    <td>Platform:</td>
                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.platformName : null}}
                                    </td>
                                </tr>
                                <tr>
                                    <td>Protocol:</td>
                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.protocolName : null}}
                                    </td>
                                </tr>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </p-overlayPanel>

            <!--</div> &lt;!&ndash; table row &ndash;&gt;-->
            <!--</div>&lt;!&ndash;container  &ndash;&gt;-->
            <!--<button type="text" pButton label="Basic" (click)="datasetOverlayPanel.toggle($event, actualTarget)"></button>-->
            <!--<p (mouseenter)="datasetOverlayPanel.show($event,  actualTarget)">A random paragraph</p>-->
        </div> <!-- enclosing box  -->
    ` // end template

})

export class DatasetDatatableComponent implements OnInit, OnChanges {

    //cars: Car[];

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService,
                private filterParamsColl: FilterParamsColl,
                private fileItemRequestService: DtoRequestService<GobiiFileItem[]>) {
    }

    public datasetsFileItems$: Observable<GobiiFileItem[]> = this.store.select(fromRoot.getDatsetEntities);
    public selectedDatasets: GobiiFileItem[];
    public nameIdFilterParamTypes: any = Object.assign({}, FilterParamNames);


    public selectedDatasetDetailEntity: DataSet;

    selectDataset(event, dataSeItem: GobiiFileItem, datasetOverlayPanel: OverlayPanel) {
        //this.selectedCar = car;


        let datasetId: number = dataSeItem.getEntity().id;
        let filterParams: FilterParams = this.filterParamsColl.getFilter(FilterParamNames.DATASET_BY_DATASET_ID, GobiiExtractFilterType.WHOLE_DATASET);

        let dtoRequestItemGfi: DtoRequestItemGfi = new DtoRequestItemGfi(filterParams,
            datasetId.toString(),
            new JsonToGfiDataset(filterParams, this.filterParamsColl));

        this.fileItemRequestService
            .get(dtoRequestItemGfi)
            .subscribe(entityItems => {
                if (entityItems.length === 1 && entityItems[0].getEntity()) {

                    this.selectedDatasetDetailEntity = entityItems[0].getEntity();
                    datasetOverlayPanel.show(event);
                } else {
                    this.store
                        .dispatch(new historyAction.AddStatusMessageAction("There is no dataset data for dataset id "
                            + datasetId.toString()));

                }
            });


    }


    public handleRowChecked(checked: boolean, selectedDatasetFileItem: GobiiFileItem) {
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), checked);
    }


    public handleRowSelect(event) {
        let selectedDatasetFileItem: GobiiFileItem = event.data;
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
    }

    public handleRowUnSelect(event) {
        let selectedDatasetFileItem: GobiiFileItem = event.data;
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
    }

    public handleOnRowClick(event) {
        let selectedDataset: GobiiFileItem = event.data;

    }

    public handleItemChecked(currentFileItemUniqueId: string, isChecked: boolean) {

        if (isChecked) {
            this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
        } else {
            this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
        }

    } // handleItemChecked()


    @Input()
    public gobiiExtractFilterType: GobiiExtractFilterType;

    ngOnInit() {
    } // ngOnInit()

    // gobiiExtractType is not set until you get OnChanges
    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                this.fileItemService.loadEntityList(this.gobiiExtractFilterType, FilterParamNames.DATASET_LIST);
                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CV_JOB_STATUS,
                    null);


            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges
}