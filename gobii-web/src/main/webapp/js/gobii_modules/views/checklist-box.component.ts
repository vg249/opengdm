import {Component, OnInit, OnChanges, SimpleChange, EventEmitter, Input, DoCheck, KeyValueDiffers} from "@angular/core";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {NameIdService} from "../services/core/name-id-service";
import {ExtractorItemType} from "../model/file-model-node";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {FileItemService} from "../services/core/file-item-service";


@Component({
    selector: 'checklist-box',
    inputs: ['gobiiExtractFilterType',
        'gobiiFileItems$',
        'nameIdRequestParams',
        'retainHistory'],
    outputs: ['onError'],
    template: `
        <form>
            <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                <div *ngFor="let gobiiFileItem of gobiiFileItems$ | async"
                     (click)=handleItemSelected($event)>
                    <input type="checkbox"
                           (click)=handleItemChecked($event)
                           [checked]="gobiiFileItem.getChecked()"
                           value={{gobiiFileItem.getFileItemUniqueId()}}
                    name="{{gobiiFileItem.getItemName()}}">&nbsp;{{gobiiFileItem.getItemName()}}
                </div>
            </div>
        </form>` // end template

})


export class CheckListBoxComponent implements OnInit, OnChanges, DoCheck {

    differ: any;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService:FileItemService,
                private differs: KeyValueDiffers) {

        this.differ = differs.find({}).create(null);


        //this.gobiiFileItems$ = this.store.select(fromRoot.getDatasetsByExperiment);

    } // ctor


    private retainHistory: boolean;
    private nameIdRequestParams: NameIdRequestParams;
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    public gobiiFileItems$: Observable< GobiiFileItem[]>;

    public handleItemChecked(arg) {

        let currentFileItemUniqueId:string = arg.currentTarget.value;

        if (arg.currentTarget.checked) {
            this.store.dispatch(new fileAction.SelectByFileItemUniqueId(currentFileItemUniqueId));
        } else {
            this.store.dispatch(new fileAction.DeSelectByFileItemUniqueId(currentFileItemUniqueId));
        }

    } // handleItemChecked()

    private handleItemSelected(arg) {

//        arg.currentTarget.style = "background-color:#b3d9ff";

    }


    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


    public setList(): void {

        // this._nameIdService.get(this.nameIdRequestParams)
        //     .subscribe(nameIds => {
        //             if (nameIds && ( nameIds.length > 0 )) {
        //
        //                 let fileItems: GobiiFileItem[] = [];
        //
        //                 nameIds.forEach(n => {
        //                     let currentFileItem: GobiiFileItem =
        //                         GobiiFileItem.build(
        //                             this.gobiiExtractFilterType,
        //                             ProcessType.CREATE)
        //                             .setExtractorItemType(ExtractorItemType.ENTITY)
        //                             .setEntityType(this.nameIdRequestParams.getEntityType())
        //                             .setCvFilterType(CvFilterType.UNKNOWN)
        //                             .setItemId(n.id)
        //                             .setItemName(n.name)
        //                             .setChecked(false)
        //                             .setRequired(false);
        //
        //
        //                     fileItems.push(currentFileItem);
        //                 });
        //
        //                 let loadAction: fileAction.LoadAction = new fileAction.LoadAction(fileItems);
        //                 this.store.dispatch(loadAction)
        //
        //             }
        //         },
        //         responseHeader => {
        //             this.handleHeaderStatus(responseHeader);
        //         });

    } // setList()


    ngOnInit(): any {

        // this._fileModelTreeService
        //     .fileItemNotifications()
        //     .subscribe(eventedFileItem => {
        //
        //             if (eventedFileItem) {
        //                 let itemToChange: GobiiFileItem =
        //                     this.gobiiFileItems.find(e => {
        //                         return e.getEntityType() == eventedFileItem.getEntityType()
        //                             && e.getItemName() == eventedFileItem.getItemName()
        //                     });
        //
        //                 //let indexOfItemToChange:number = this.gobiiFileItems.indexOf(arg.currentTarget.name);
        //                 if (itemToChange) {
        //                     itemToChange.setProcessType(eventedFileItem.getProcessType());
        //                     itemToChange.setChecked(eventedFileItem.getChecked());
        //                     this.updateCheckedItemHistory(itemToChange);
        //                 }
        //             }
        //         },
        //         responseHeader => {
        //             this.handleHeaderStatus(responseHeader);
        //         });

        // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
        //     this.setList()
        // }
    }

    private resetList() {
        // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
        //     this.setList();
        // }
    }

    ngOnChanges(changes: { [propName: string]: SimpleChange }) {


        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {


                //this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                this.resetList();
                // this.resetList();
                // this._fileModelTreeService
                //     .fileItemNotifications()
                //     .subscribe(fileItem => {
                //         if (fileItem.getProcessType() === ProcessType.NOTIFY
                //             && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                //
                //             this.resetList();
                //
                //         }
                //     });

            } // if we have a new filter type

        } // if filter type changed

    }

    ngDoCheck(): void {

        var changes = this.differ.diff(this.nameIdRequestParams);

        if (changes) {

            this.resetList();
        }
    }

}
