System.register(["./gobii-extractor-instruction"], function (exports_1, context_1) {
    "use strict";
    var gobii_extractor_instruction_1, ExtractorInstructionFilesDTO;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            }
        ],
        execute: function () {
            ExtractorInstructionFilesDTO = class ExtractorInstructionFilesDTO {
                constructor(gobiiExtractorInstructions, instructionFileName) {
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.instructionFileName = instructionFileName;
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.instructionFileName = instructionFileName;
                } // ctor
                getGobiiExtractorInstructions() {
                    return this.gobiiExtractorInstructions;
                }
                setGobiiExtractorInstructions(value) {
                    this.gobiiExtractorInstructions = value;
                }
                getInstructionFileName() {
                    return this.instructionFileName;
                }
                setInstructionFileName(value) {
                    this.instructionFileName = value;
                }
                getJson() {
                    let returnVal = {};
                    returnVal.instructionFileName = this.instructionFileName;
                    returnVal.gobiiExtractorInstructions = [];
                    this.gobiiExtractorInstructions.forEach(i => {
                        returnVal.gobiiExtractorInstructions.push(i.getJson());
                    });
                    return returnVal;
                } // getJson()
                static fromJson(json) {
                    let gobiiExtractorInstructions = [];
                    json.gobiiExtractorInstructions.forEach(i => gobiiExtractorInstructions.push(gobii_extractor_instruction_1.GobiiExtractorInstruction.fromJson(i)));
                    let returnVal = new ExtractorInstructionFilesDTO(gobiiExtractorInstructions, json.instructionFileName);
                    return returnVal;
                }
            };
            exports_1("ExtractorInstructionFilesDTO", ExtractorInstructionFilesDTO);
        }
    };
});
//# sourceMappingURL=dto-extractor-instruction-files.js.map