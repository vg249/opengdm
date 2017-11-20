import {Component, Input, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemService} from "../services/core/file-item-service";
import {FilterParamNames} from "../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import {DataSet} from "../model/dataset";
import {GobiiFileItem} from "../model/gobii-file-item";
import * as fileAction from '../store/actions/fileitem-action';


@Component({
    selector: 'dataset-datatable',
    inputs: [],
    outputs: [],
    template: `
        <div style="border: 1px solid #336699; padding-left: 5px">
            <div class="container-fluid">
                <div class="row">
                    <BR>
                    <label class="the-legend">Filter by Status:&nbsp;</label>
                    <name-id-list-box
                            [gobiiExtractFilterType]="gobiiExtractFilterType"
                            [filterParamName]="nameIdFilterParamTypes.CV_JOB_STATUS">
                    </name-id-list-box>

                </div> <!--status selector row -->
                <div class="row">
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
                                            (onChange)="handleRowChecked(fi)">
                                </p-checkbox>

                            </ng-template>
                        </p-column>
                        <p-column field="_entity.id" header="Id" hidden="true"></p-column>
                        <p-column field="_entity.name" header="Name"></p-column>
                        <p-column field="_entity.jobStatusName" header="Status"></p-column>
                        <p-column field="jobSubmittedDate" header="Submitted">
                            <ng-template let-col let-fi="rowData" pTemplate="body">
                                {{fi._entity[col.field] | date:'yyyy-MM-dd HH:mm' }}
                            </ng-template>
                        </p-column>
                    </p-dataTable>
                </div> <!-- table row -->
            </div><!--container  -->
        </div> <!-- enclosing box  -->
    ` // end template

})
export class DatasetDatatableComponent implements OnInit, OnChanges {

    //cars: Car[];

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {
    }

    public datasetsFileItems$: Observable<GobiiFileItem[]> = this.store.select(fromRoot.getDatsetEntities);
    public selectedDatasets: GobiiFileItem[];
    public nameIdFilterParamTypes: any = Object.assign({}, FilterParamNames);


    public handleRowChecked(event) {
        let selectedDatasetFileItem: GobiiFileItem = event;
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event);
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

                this.fileItemService.loadEntityList(this.gobiiExtractFilterType, FilterParamNames.DATASETS);
                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                    FilterParamNames.CV_JOB_STATUS,
                    null);

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges
}