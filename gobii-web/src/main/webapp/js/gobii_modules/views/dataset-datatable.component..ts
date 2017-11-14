import {Component, Input, OnInit} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemService} from "../services/core/file-item-service";
import {FileItemParamNames} from "../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import {DataSet} from "../model/dataset";


@Component({
    selector: 'dataset-datatable',
    inputs: ['gobiiExtractFilterType'],
    outputs: [],
    template: `
        <p-dataTable [value]="datasets$ | async">
            <p-column field="name" header="Name"></p-column>
            <p-column field="modifiedDate" header="Modified"></p-column>
        </p-dataTable>
    ` // end template

})
export class DatasetDatatableComponent implements OnInit {

    //cars: Car[];

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {
    }

    public datasets$: Observable<DataSet[]> = this.store.select(fromRoot.getDatsetEntities);

    @Input()
    private gobiiExtractFilterType: GobiiExtractFilterType;

    ngOnInit() {

        this.fileItemService.loadEntityList(this.gobiiExtractFilterType, FileItemParamNames.DATASETS);

    } // ngOnInit()

}