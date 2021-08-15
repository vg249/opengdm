import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Store } from '@ngrx/store';

import { ExtractorItemType } from '../model//type-extractor-item';
import { HeaderStatusMessage } from '../model/dto-header-status-message';
import { FileName } from '../model/file_name';
import { GobiiFileItem } from '../model/gobii-file-item';
import { GobiiExtractFilterType } from '../model/type-extractor-filter';
import { ProcessType } from '../model/type-process';
import { AuthenticationService } from '../services/core/authentication.service';
import { FileItemService } from '../services/core/file-item-service';
import * as fromRoot from '../store/reducers';


@Component({
  selector: 'uploader',
  templateUrl: 'uploader.component.html'
})
export class UploaderComponent implements OnInit {
  @Input() gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
  @Input() requiredFileType: string;

  @Output() onUploaderError: EventEmitter<HeaderStatusMessage> = new EventEmitter();

  file: File = null;
  fileName = '';
  url = '';
  token = '';

  constructor(
    private authenticationService: AuthenticationService,
    private store: Store<fromRoot.State>,
    private fileItemService: FileItemService,
    private http: HttpClient
  ) { }

  ngOnInit(): any {
    this.store.select(fromRoot.getJobId).subscribe(fileItemJobId => {
      const crop = this.authenticationService.getGobiiCropType();
      const jobId: string = fileItemJobId.getItemId();

      this.fileName = FileName.makeFileNameFromJobId(this.gobiiExtractFilterType, jobId);
      this.url = `/gdm/crops/${crop}/gobii/v1/files/${jobId}/EXTRACTOR_INSTRUCTIONS?fileName=${this.fileName}`;
      this.authenticationService.getToken().subscribe(token => {
        this.token = `Bearer ${token}`;
      });
    });
  }

  onFileSelected(file: File[]): void {
    this.file = file[0];
  }

  onUpload(): void {
    if (!this.file) {
      return;
    }

    const httpOptions = {
      headers: new HttpHeaders({ Authorization: this.token })
    };

    const formData = new FormData();
    formData.append('file', this.file, this.fileName);

    const upload$ = this.http.post(this.url, formData, httpOptions);
    upload$.subscribe(
      success => {
        this.afterUpload();
      },
      error => {
        this.onUploaderError.emit(new HeaderStatusMessage(error.message, null, null));
      },
      () => this.file = null
    );
  }

  afterUpload(): void {
    const listItemType: ExtractorItemType = this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER
      ? ExtractorItemType.MARKER_FILE
      : ExtractorItemType.SAMPLE_FILE;

    this.fileItemService.loadFileItem(GobiiFileItem
      .build(this.gobiiExtractFilterType, ProcessType.CREATE)
      .setExtractorItemType(listItemType)
      .setItemId(this.fileName)
      .setItemName(this.fileName)
      .setIsEphemeral(true),
      true);
  }

}
