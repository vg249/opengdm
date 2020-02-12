System.register(["@angular/core", "ng2-file-upload", "../services/core/authentication.service", "../model/header-names", "../model/dto-header-status-message", "../model/file_name", "../model/gobii-file-item", "../model/type-extractor-filter", "../model/type-process", "../model//type-extractor-item", "@ngrx/store", "../store/reducers", "../services/core/file-item-service", "../store/actions/history-action", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, ng2_file_upload_1, authentication_service_1, header_names_1, dto_header_status_message_1, file_name_1, gobii_file_item_1, type_extractor_filter_1, type_process_1, type_extractor_item_1, store_1, fromRoot, file_item_service_1, historyAction, view_id_generator_service_1, type_control_1, URL, UploaderComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (ng2_file_upload_1_1) {
                ng2_file_upload_1 = ng2_file_upload_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (header_names_1_1) {
                header_names_1 = header_names_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (file_name_1_1) {
                file_name_1 = file_name_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            URL = 'gobii/v1/files/{gobiiJobId}/EXTRACTOR_INSTRUCTIONS?fileName=';
            UploaderComponent = class UploaderComponent {
                constructor(_authenticationService, store, fileItemService, viewIdGeneratorService) {
                    this._authenticationService = _authenticationService;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.typeControl = type_control_1.TypeControl;
                    this.onUploaderError = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.uploadComplete = false;
                    this.hasBaseDropZoneOver = false;
                    this.hasAnotherDropZoneOver = false;
                    this.onClickBrowse = new core_1.EventEmitter();
                } // ctor
                fileOverBase(e) {
                    this.hasBaseDropZoneOver = e;
                }
                fileOverAnother(e) {
                    this.hasAnotherDropZoneOver = e;
                }
                clearSelectedFile() {
                    this.selectedFile.nativeElement.value = '';
                }
                handleClickBrowse(event) {
                    this.onClickBrowse.emit(event);
                }
                onFileSelected(e) {
                    for (let fileItem of this.uploader.getNotUploadedItems()) {
                        if (fileItem.file.type !== "text/plain") {
                            this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage("Invalid Input.\nInput should be a text file with '.txt' extension.", null, null));
                            this.uploader.clearQueue();
                            this.clearSelectedFile();
                            break;
                        }
                    }
                }
                ngOnInit() {
                    let JobId$ = this.store.select(fromRoot.getJobId);
                    JobId$.subscribe(fileItemJobId => {
                        let jobId = fileItemJobId.getItemId();
                        let fileUploaderOptions = {};
                        let url = URL.replace("{gobiiJobId}", jobId);
                        let fileName = file_name_1.FileName.makeFileNameFromJobId(this.gobiiExtractFilterType, jobId);
                        url += fileName;
                        fileUploaderOptions.url = url;
                        fileUploaderOptions.headers = [];
                        fileUploaderOptions.removeAfterUpload = true;
                        let authHeader = { name: '', value: '' };
                        authHeader.name = header_names_1.HeaderNames.headerToken;
                        let token = this._authenticationService.getToken();
                        if (token) {
                            authHeader.value = token;
                            fileUploaderOptions.headers.push(authHeader);
                            this.uploader = new ng2_file_upload_1.FileUploader(fileUploaderOptions);
                            this.uploader.onBeforeUploadItem = (fileItem) => {
                                fileItem.file.name = fileName;
                            };
                            this.uploader.onCompleteItem = (item, response, status, headers) => {
                                if (status == 200) {
                                    let listItemType = this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER ?
                                        type_extractor_item_1.ExtractorItemType.MARKER_FILE : type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                                    this.fileItemService.loadFileItem(gobii_file_item_1.GobiiFileItem
                                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                        .setExtractorItemType(listItemType)
                                        .setItemId(item.file.name)
                                        .setItemName(item.file.name)
                                        .setIsEphemeral(true), true);
                                }
                                else {
                                    this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage(response, null, null));
                                    this.uploader.clearQueue();
                                    this.clearSelectedFile();
                                }
                            };
                        }
                        else {
                            this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage("Unauthenticated", null, null));
                        }
                    });
                    this.store.select(fromRoot.getUploadFiles)
                        .subscribe((fileFileItems) => {
                        if (fileFileItems && fileFileItems.length > 0) {
                            this.clearSelectedFile();
                            this.uploadComplete = false;
                        }
                    }, errorMessage => {
                        this.store.dispatch(new historyAction.AddStatusMessageAction(errorMessage));
                    });
                    // this._fileModelTreeService
                    //     .fileItemNotifications()
                    //     .subscribe(eventedFileItem => {
                    //             if (eventedFileItem.getProcessType() === ProcessType.DELETE) {
                    //                 let currentItemType: ExtractorItemType = ExtractorItemType.UNKNOWN;
                    //                 if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    //                     currentItemType = ExtractorItemType.SAMPLE_FILE;
                    //                 } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    //                     currentItemType = ExtractorItemType.MARKER_FILE;
                    //                 }
                    //
                    //                 if ((eventedFileItem.getGobiiEventOrigin() === GobiiUIEventOrigin.CRITERIA_TREE)
                    //                     && (eventedFileItem.getExtractorItemType() === currentItemType )) {
                    //                     this.clearSelectedFile();
                    //                     this.uploadComplete = false;
                    //                 }
                    //             }
                    //         },
                    //         responseHeader => {
                    //             this.onUploaderError.emit(responseHeader);
                    //         });
                }
            };
            __decorate([
                core_1.ViewChild('selectedFile', { static: false }),
                __metadata("design:type", Object)
            ], UploaderComponent.prototype, "selectedFile", void 0);
            UploaderComponent = __decorate([
                core_1.Component({
                    selector: 'uploader',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onUploaderError', 'onClickBrowse'],
                    template: `
        <style>
            .my-drop-zone {
                border: dotted 3px lightgray;
            }

            .nv-file-over {
                border: dotted 3px red;
            }

            /* Default class applied to drop zones on over */
            .another-file-over-class {
                border: dotted 3px green;
            }

            html, body {
                height: 100%;
            }
        </style>

        <div class="container">

            <div class="row">

                <div class="col-md-3">

                    <!-- DROP ZONES AND MULTI-FILE SELECT, UNUSED FOR NOW ======================
                    <h3>Select files</h3>

                    <div ng2FileDrop
                         [ngClass]="{'nv-file-over': hasBaseDropZoneOver}"
                         (fileOver)="fileOverBase($event)"
                         [uploader]="uploader"
                         class="well my-drop-zone">
                        Base drop zone
                    </div>

                    <div ng2FileDrop
                         [ngClass]="{'another-file-over-class': hasAnotherDropZoneOver}"
                         (fileOver)="fileOverAnother($event)"
                         [uploader]="uploader"
                         class="well my-drop-zone">
                        Another drop zone
                    </div>

                    Multiple
                    <input type="file" ng2FileSelect [uploader]="uploader" multiple /><br/>
                    ================================================================================ -->


                    <input #selectedFile
                           type="file"
                           ng2FileSelect
                           [uploader]="uploader"
                           [disabled]="uploadComplete"
                           (click)="handleClickBrowse($event)"
                           [id]="viewIdGeneratorService.makeStandardId(typeControl.FILE_SELECTOR_MARKER_SAMPLE_LIST_UPLOAD)"
                           accept="text/plain"
                           (onFileSelected)="onFileSelected($event)"
                    />
                    <!--  IF YOU REINSTATE THE QUEUES BELOW THIS BUTTON WILL BE SUPERFLUOUS -->
                    <BR>
                    <button type="button" class="btn btn-success"
                            (click)="uploader.uploadAll()"
                            [disabled]="!uploader.getNotUploadedItems().length"
                            [id]="viewIdGeneratorService.makeStandardId(typeControl.SUBMIT_BUTTON_UPLOAD_MARKER_SAMPLE_LIST)">
                        Upload
                    </button>
                </div>

                <div class="col-md-9" style="margin-bottom: 40px">


                    <!-- UPLOAD QUEUE UNUSED FOR NOW =========================================================
                    <h3>Upload queue</h3>
                    <p>Queue length: {{ uploader?.queue?.length }}</p>

                    <table class="table">
                        <thead>
                        <tr>
                            <th width="50%">Name</th>
                            <th>Size</th>
                            <th>Progress</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr *ngFor="let item of uploader.queue">
                            <td><strong>{{ item?.file?.name }}</strong></td>
                            <td *ngIf="uploader.isHTML5" nowrap>{{ item?.file?.size/1024/1024 | number:'.2' }} MB</td>
                            <td *ngIf="uploader.isHTML5">
                                <div class="progress" style="margin-bottom: 0;">
                                    <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': item.progress + '%' }"></div>
                                </div>
                            </td>
                            <td class="text-center">
                                <span *ngIf="item.isSuccess"><i class="glyphicon glyphicon-ok"></i></span>
                                <span *ngIf="item.isCancel"><i class="glyphicon glyphicon-ban-circle"></i></span>
                                <span *ngIf="item.isError"><i class="glyphicon glyphicon-remove"></i></span>
                            </td>
                            <td nowrap>
                                <button type="button" class="btn btn-success btn-xs"
                                        (click)="item.upload()" [disabled]="item.isReady || item.isUploading || item.isSuccess">
                                    <span class="glyphicon glyphicon-upload"></span> Upload
                                </button>
                                <button type="button" class="btn btn-warning btn-xs"
                                        (click)="item.cancel()" [disabled]="!item.isUploading">
                                    <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                                </button>
                                <button type="button" class="btn btn-danger btn-xs"
                                        (click)="item.remove()">
                                    <span class="glyphicon glyphicon-trash"></span> Remove
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <div>
                        <div>
                            Queue progress:
                            <div class="progress" style="">
                                <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': uploader.progress + '%' }"></div>
                            </div>
                        </div>
                        <button type="button" class="btn btn-success btn-s"
                                (click)="uploader.uploadAll()" [disabled]="!uploader.getNotUploadedItems().length">
                            <span class="glyphicon glyphicon-upload"></span> Upload all
                        </button>
                        <button type="button" class="btn btn-warning btn-s"
                                (click)="uploader.cancelAll()" [disabled]="!uploader.isUploading">
                            <span class="glyphicon glyphicon-ban-circle"></span> Cancel all
                        </button>
                        <button type="button" class="btn btn-danger btn-s"
                                (click)="uploader.clearQueue()" [disabled]="!uploader.queue.length">
                            <span class="glyphicon glyphicon-trash"></span> Remove all
                        </button>
                    </div>
                    == UPLOAD QUEUE UNUSED FOR NOW ========================================================= -->


                </div>

            </div>

        </div>`
                }),
                __metadata("design:paramtypes", [authentication_service_1.AuthenticationService,
                    store_1.Store,
                    file_item_service_1.FileItemService,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], UploaderComponent);
            exports_1("UploaderComponent", UploaderComponent);
        }
    };
});
//# sourceMappingURL=uploader.component.js.map