import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {ProcessType} from "../model/type-process";
import {CheckBoxEvent} from "../model/event-checkbox";


@Component({
    selector: 'checklist-box',
    inputs: ['checkBoxEventChange', 'nameIdList'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let checkBoxEvent of checkBoxEvents" 
                            (click)=handleItemSelected($event) 
                            (hover)=handleItemHover($event)>
                            <input  type="checkbox" 
                                (click)=handleItemChecked($event)
                                [checked]="checkBoxEvent.checked"
                                value={{checkBoxEvent.id}} 
                                name="{{checkBoxEvent.name}}">&nbsp;{{checkBoxEvent.name}}
                        </div>            
                    </div>
                </form>` // end template

})


export class CheckListBoxComponent implements OnInit,OnChanges {

    constructor(private _dtoRequestServiceNameId: DtoRequestService<NameId[]>) {

    } // ctor

    // useg
    private nameIdList: NameId[];
    private checkBoxEvents: CheckBoxEvent[] = [];
    private onItemChecked: EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected: EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onAddMessage: EventEmitter<string> = new EventEmitter();

    private handleItemChecked(arg) {

        let itemToChange: CheckBoxEvent =
            this.checkBoxEvents.filter(e => {
                return e.id == arg.currentTarget.value;
            })[0];

        //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
        itemToChange.processType = arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE;
        itemToChange.checked = arg.currentTarget.checked;
        this.onItemChecked.emit(itemToChange);

    } // handleItemChecked()

    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    private previousSelectedItem: any;

    private handleItemSelected(arg) {

        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;

        let checkBoxEvent: CheckBoxEvent = new CheckBoxEvent(ProcessType.READ,
            arg.currentTarget.children[0].value,
            arg.currentTarget.children[0].name,
            false,
            false);

        this.onItemSelected.emit(checkBoxEvent);

    }

    public setList(nameIdList: NameId[]): void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;

        scope$.nameIdList = nameIdList;

        if (scope$.nameIdList && ( scope$.nameIdList.length > 0 )) {

            scope$.checkBoxEvents = [];
            scope$.nameIdList.forEach(n => {
                scope$.checkBoxEvents.push(new CheckBoxEvent(
                    ProcessType.CREATE,
                    n.id,
                    n.name,
                    false,
                    false
                ));
            });

        } else {
            scope$.nameIdList = [new NameId(0, "<none>")];
        }


    } // setList()


    ngOnInit(): any {

    }

    private itemChangedEvent: CheckBoxEvent;

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {

            this.itemChangedEvent = changes['checkBoxEventChange'].currentValue;

            if (this.itemChangedEvent) {
                let itemToChange: CheckBoxEvent =
                    this.checkBoxEvents.filter(e => {
                        return e.id == changes['checkBoxEventChange'].currentValue.id;
                    })[0];

                //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
                if (itemToChange) {
                    itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
                    itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
                }
            }
        } else if (changes['nameIdList'] && changes['nameIdList'].currentValue) {

            this.setList(changes['nameIdList'].currentValue);

        }
    }
}
