System.register(["./header", "./payload"], function (exports_1, context_1) {
    "use strict";
    var header_1, payload_1, PayloadEnvelope;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (header_1_1) {
                header_1 = header_1_1;
            },
            function (payload_1_1) {
                payload_1 = payload_1_1;
            }
        ],
        execute: function () {
            PayloadEnvelope = class PayloadEnvelope {
                constructor(header, payload) {
                    this.header = header;
                    this.payload = payload;
                }
                static fromJSON(json) {
                    let header = header_1.Header.fromJSON(json.header);
                    let payload = payload_1.Payload.fromJSON(json.payload);
                    return new PayloadEnvelope(header, payload);
                } // fromJson()
                static wrapSingleDTOInJSON(payLoad) {
                    let returnVal = {};
                    returnVal.payload = { "data": [] };
                    returnVal.payload.data.push(payLoad);
                    // returnVal.processType = this.processType;
                    // returnVal.instructionFileName = this.instructionFileName;
                    // returnVal.gobiiCropType = this.gobiiCropType;
                    // returnVal.gobiiExtractorInstructions = [];
                    //
                    // this.gobiiExtractorInstructions.forEach(i => {
                    //     returnVal.gobiiExtractorInstructions.push(i.getJson());
                    // });
                    return returnVal;
                }
            };
            exports_1("PayloadEnvelope", PayloadEnvelope);
        }
    };
});
//# sourceMappingURL=payload-envelope.js.map