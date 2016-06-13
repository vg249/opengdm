System.register(["@angular/core", "../model/server-config", "../services/core/dto-request.service", "../services/app/dto-request-item-serverconfigs", "../model/type-crop"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, server_config_1, dto_request_service_1, dto_request_item_serverconfigs_1, type_crop_1;
    var ContactsListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_crop_1_1) {
                type_crop_1 = type_crop_1_1;
            }],
        execute: function() {
            ContactsListBoxComponent = (function () {
                function ContactsListBoxComponent(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                    this.onServerSelected = new core_1.EventEmitter();
                    var scope$ = this;
                    _dtoRequestService.getItemList(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig(type_crop_1.GobiiCropType.UNDEFINED, "<undefined>", 0)];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return console.log(m.message); });
                    });
                } // ctor
                ContactsListBoxComponent.prototype.handleServerSelected = function (arg) {
                    this.onServerSelected.emit(this.serverConfigList[arg.srcElement.selectedIndex]);
                };
                ContactsListBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                ContactsListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'contacts-list-box',
                        outputs: ['onContactSelected'],
                        template: "<select name=\"serverConfigs\" (change)=\"handleServerSelected($event)\" >\n\t\t\t<option *ngFor=\"let serverConfig of serverConfigList\" \n\t\t\t\tvalue={{nameId.id}}>{{serverConfig.crop}}</option>\n\t\t</select>\n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], ContactsListBoxComponent);
                return ContactsListBoxComponent;
            }());
            exports_1("ContactsListBoxComponent", ContactsListBoxComponent);
        }
    }
});
//# sourceMappingURL=crops-list-box.component.js.map