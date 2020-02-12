System.register(["@angular/core"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var core_1, TextAreaComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            }
        ],
        execute: function () {
            TextAreaComponent = class TextAreaComponent {
                constructor() {
                    this.log = '';
                    this.onTextboxClicked = new core_1.EventEmitter();
                    this.onTextboxDataComplete = new core_1.EventEmitter();
                }
                logText(value) {
                    this.log += `Text changed to '${value}'\n`;
                }
                handleTextboxClicked(arg) {
                    this.onTextboxClicked.emit(arg);
                }
                handleTextboxDataComplete(arg) {
                    let items = arg.split("\n");
                    this.onTextboxDataComplete.emit(items);
                    this.textValue = '';
                }
            };
            TextAreaComponent = __decorate([
                core_1.Component({
                    selector: 'text-area',
                    outputs: ['onTextboxDataComplete', 'onTextboxClicked'],
                    template: `
        <textarea ref-textarea 
        [(ngModel)]="textValue" rows="4" style="width: 100%;"
        (click)="handleTextboxClicked($event)"></textarea><br/>
        <button (click)="handleTextboxDataComplete(textarea.value)">Add To Extract</button>
        <button (click)="textValue=''">Clear</button>
        
         <!--<h2>Log <button (click)="log=''">Clear</button></h2>-->
        <!--<pre>{{log}}</pre>-->
`
                })
            ], TextAreaComponent);
            exports_1("TextAreaComponent", TextAreaComponent);
        }
    };
});
//# sourceMappingURL=text-area.component.js.map