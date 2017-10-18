import {Component, EventEmitter, KeyValueDiffers} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {ExtractorItemType} from "../model//type-extractor-item";
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
                           [checked]="gobiiFileItem.getSelected()"
                           value={{gobiiFileItem.getFileItemUniqueId()}}
                    name="{{gobiiFileItem.getItemName()}}">&nbsp;{{gobiiFileItem.getItemName()}}
                </div>
            </div>
        </form>` // end template

})


export class CheckListBoxComponent  {

    differ: any;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService:FileItemService,
                private differs: KeyValueDiffers) {

        this.differ = differs.find({}).create(null);


    } // ctor


    private retainHistory: boolean;
    private nameIdRequestParams: FileItemParams;
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    public gobiiFileItems$: Observable< GobiiFileItem[]>;

    public handleItemChecked(arg) {

        let currentFileItemUniqueId:string = arg.currentTarget.value;

        if (arg.currentTarget.checked) {
            this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
        } else {
            this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
        }

    } // handleItemChecked()

    public handleItemSelected(arg) {

//        arg.currentTarget.style = "background-color:#b3d9ff";

    }


    public handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


}
