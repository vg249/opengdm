import {Component, Input, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemService} from "../services/core/file-item-service";
import {FileItemParamNames} from "../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import {DataSet} from "../model/dataset";


@Component({
    selector: 'dataset-datatable',
    inputs: [],
    outputs: [],
    template: `
        <p-dataTable [value]="datasets$ | async">
            <p-column field="name" header="Name"></p-column>
            <p-column field="datasetId" header="Id"></p-column>
        </p-dataTable>
    ` // end template

})
export class DatasetDatatableComponent implements OnInit, OnChanges {

    //cars: Car[];

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {
    }

    public datasets$: Observable<DataSet[]> = this.store.select(fromRoot.getDatsetEntities);

    @Input()
    private gobiiExtractFilterType: GobiiExtractFilterType;

    ngOnInit() {

    } // ngOnInit()

    // gobiiExtractType is not set until you get OnChanges
    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                this.fileItemService.loadEntityList(this.gobiiExtractFilterType, FileItemParamNames.DATASETS);

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges
}