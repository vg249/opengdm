import {Component, OnChanges, OnInit, SimpleChange} from "@angular/core";
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
import {FlexQueryService} from "../services/core/flex-query-service";


@Component({
    selector: 'flex-query-filter',
    inputs: ['gobiiExtractFilterType', 'filterParamNameVertices', 'filterParamNameVertexValues'],
    outputs: [],
    styleUrls: ["css/extractor-ui.css"],
    template: `
        <div class="panel panel-primary" [ngStyle]="currentStyle">
            <div class="panel-heading">
                <h3 class="panel-title">Filters</h3>
            </div>
            <div class="panel-body">
                <label class="the-label">Entity:</label><BR>
                <p-dropdown [options]="fileItemsEntityNames$ | async"
                            [(ngModel)]="selectedAllowableEntities"
                            [style]="{'width': '100%'}"
                            optionLabel="_itemName"
                            (onChange)="handleVertexSelected($event)"
                            [disabled]="currentStyle===disabledStyle">
                </p-dropdown>

                <BR>
                <BR>
                <label class="the-label">Select Entity Values</label><BR>
                <p-listbox [options]="fileItemsEntityValues$ | async"
                           [multiple]="true"
                           [(ngModel)]="selectedEntityValues" [style]="{'width':'100%'}"
                           optionLabel="_itemName"
                           [disabled]="currentStyle===disabledStyle"></p-listbox>
            </div>

            <div class="container">
                <p>Count: {{totalValues}} </p>
                <p>Selected: {{selectedEntityValues ? selectedEntityValues.length : 0}}</p>
            </div>
        </div>` // end template

})
export class FlexQueryFilterComponent implements OnInit, OnChanges {


    //these are dummy place holders for now
    public selectedAllowableEntities: GobiiFileItem;

    public totalValues: string = "0";
    public selectedEntityValues: GobiiFileItem[];

    public fileItemsEntityNames$: Observable<GobiiFileItem[]>;
    public fileItemsEntityValues$: Observable<GobiiFileItem[]>;
    public JobId$: Observable<GobiiFileItem>;

    public gobiiExtractFilterType: GobiiExtractFilterType;

    private filterParamNameVertices: FilterParamNames;
    private filterParamNameVertexValues: FilterParamNames;

    public enabledStyle = null;
    public disabledStyle = {'background': '#dddddd'};
    public currentStyle = this.disabledStyle;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: NameIdFileItemService,
                private filterService: FilterService,
                private flexQueryService: FlexQueryService) {


    } // ctor


    ngOnInit(): any {

        this.fileItemsEntityNames$ = this.filterService.getForFilter(this.filterParamNameVertices);
        this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues);
        this.JobId$ = this.store.select(fromRoot.getJobId);


        this
            .fileItemsEntityNames$
            .subscribe(items => {
                    if (this.previousSelectedItemId === null && items && items.length > 0) {
                        this.previousSelectedItemId = items[0].getFileItemUniqueId()
                    }

                    if (items.length > 1) {
                        this.currentStyle = this.enabledStyle;
                    } else {
                        this.currentStyle = this.disabledStyle;
                    }

                    if (items[0]) {
                        this.selectedAllowableEntities = items[0];
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

    public handleVertexSelected(arg) {

        let vertexId: string = null;
        if (arg.value._entity && arg.value._entity.vertexId) {
            vertexId = arg.value._entity.vertexId;
        } else {
            this.selectedAllowableEntities = null;
        }

        this.JobId$.subscribe(

            fileItemJobId => {
                this.flexQueryService.loadVertexValues(fileItemJobId.getItemId(),
                    arg.value._entity,
                    this.filterParamNameVertexValues);
            }
        );



        // this.filterService.loadFilter(this.gobiiExtractFilterType,
        //     this.filterParamNameVertices,
        //     vertexId);

        if (!this.gobiiExtractFilterType) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"))
        }

    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                    this.flexQueryService.loadVertices(this.filterParamNameVertices);

                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges

} // class
