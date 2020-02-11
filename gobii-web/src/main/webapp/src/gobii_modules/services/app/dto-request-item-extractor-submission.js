System.register(["@angular/core", "../../model/type-process", "../../model/extractor-instructions/dto-extractor-instruction-files", "../../model/payload/payload-envelope"], function (exports_1, context_1) {
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
    var core_1, type_process_1, dto_extractor_instruction_files_1, payload_envelope_1, DtoRequestItemExtractorSubmission;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (dto_extractor_instruction_files_1_1) {
                dto_extractor_instruction_files_1 = dto_extractor_instruction_files_1_1;
            },
            function (payload_envelope_1_1) {
                payload_envelope_1 = payload_envelope_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemExtractorSubmission = /** @class */ (function () {
                function DtoRequestItemExtractorSubmission(extractorInstructionFilesDTO) {
                    this.extractorInstructionFilesDTO = extractorInstructionFilesDTO;
                    this.processType = type_process_1.ProcessType.CREATE;
                    this.extractorInstructionFilesDTO = extractorInstructionFilesDTO;
                }
                DtoRequestItemExtractorSubmission.prototype.getUrl = function () {
                    return "gobii/v1/instructions/extractor";
                }; // getUrl()
                DtoRequestItemExtractorSubmission.prototype.getRequestBody = function () {
                    var rawJsonExtractorInstructionFileDTO = this.extractorInstructionFilesDTO.getJson();
                    var payloadEnvelope = payload_envelope_1.PayloadEnvelope.wrapSingleDTOInJSON(rawJsonExtractorInstructionFileDTO);
                    var returnVal = JSON.stringify(payloadEnvelope);
                    return returnVal;
                };
                DtoRequestItemExtractorSubmission.prototype.resultFromJson = function (json) {
                    var returnVal = dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO.fromJson(json.payload.data[0]);
                    return returnVal;
                };
                DtoRequestItemExtractorSubmission = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO])
                ], DtoRequestItemExtractorSubmission);
                return DtoRequestItemExtractorSubmission;
            }());
            exports_1("DtoRequestItemExtractorSubmission", DtoRequestItemExtractorSubmission);
        }
    };
});
//# sourceMappingURL=dto-request-item-extractor-submission.js.map