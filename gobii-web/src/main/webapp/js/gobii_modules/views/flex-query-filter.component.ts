import {Component} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {Observable} from "rxjs/Observable";
import {FilterParamNames} from "../model/file-item-param-names";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import {FilterService} from "../services/core/filter-service";


@Component({
    selector: 'flex-query-filter',
    inputs: ['gobiiExtractFilterType', 'filterParamNameVertices', 'filterParamNameVertexValues'],
    outputs: [],
    styleUrls: ["css/extractor-ui.css"],
    template: `
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Filters</h3>
            </div>
            <div class="panel-body">
                <label class="the-label">Entity:</label><BR>
                <p-dropdown [options]="fileItemsEntityNames$ | async"
                            [(ngModel)]="selectedAllowableEntities"
                            [style]="{'width': '100%'}"
                            optionLabel="_itemName"
                            (onChange)="handleFileItemSelected($event)">
                </p-dropdown>

                <BR>
                <BR>
                <label class="the-label">Select Entity Values</label><BR>
                <p-listbox [options]="fileItemsEntityValues$ | async"
                           [multiple]="true"
                           [(ngModel)]="selectedEntityValues" [style]="{'width':'100%'}"
                           optionLabel="_itemName"></p-listbox>
            </div>

            <div class="container">
                <p>Count: {{totalValues}} </p>
                <p>Selected: {{selectedEntityValues ? selectedEntityValues.length : 0}}</p>
            </div>
        </div>` // end template

})
export class FlexQueryFilterComponent {


    //these are dummy place holders for now
    public selectedAllowableEntities: GobiiFileItem;

    public totalValues: string = "0";
    public selectedEntityValues: GobiiFileItem[];

    public fileItemsEntityNames$: Observable<GobiiFileItem[]>;
    public fileItemsEntityValues$: Observable<GobiiFileItem[]>;

    public gobiiExtractFilterType: GobiiExtractFilterType;

    private filterParamNameVertices: FilterParamNames;
    private filterParamNameVertexValues: FilterParamNames;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: NameIdFileItemService,
                private filterService: FilterService) {


    } // ctor


    ngOnInit(): any {

        this.fileItemsEntityNames$ = this.filterService.getForFilter(this.filterParamNameVertices)
        this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues)

        this
            .fileItemsEntityNames$
            .subscribe(items => {
                    if (this.previousSelectedItemId === null && items && items.length > 0) {
                        this.previousSelectedItemId = items[0].getFileItemUniqueId()
                    }

                },
                error => {
                    this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                });

        this
            .fileItemsEntityValues$
            .subscribe(items => {
                this.totalValues = items.length.toString()
            });
    }

    previousSelectedItemId: string = null;

    public handleFileItemSelected(arg) {

        this.filterService.loadFilter(this.gobiiExtractFilterType,
            this.filterParamNameVertices,
            arg.value._entity.vertexId);

        if (!this.gobiiExtractFilterType) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"))
        }

    }


} // class
