System.register(["../dto-header-auth", "./status", "./pagination"], function (exports_1, context_1) {
    "use strict";
    var dto_header_auth_1, status_1, pagination_1, Header;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (dto_header_auth_1_1) {
                dto_header_auth_1 = dto_header_auth_1_1;
            },
            function (status_1_1) {
                status_1 = status_1_1;
            },
            function (pagination_1_1) {
                pagination_1 = pagination_1_1;
            }
        ],
        execute: function () {
            Header = class Header {
                constructor(cropType, dtoHeaderAuth, status, gobiiVersion, pagination) {
                    this.cropType = cropType;
                    this.dtoHeaderAuth = dtoHeaderAuth;
                    this.status = status;
                    this.gobiiVersion = gobiiVersion;
                    this.pagination = pagination;
                }
                static fromJSON(json) {
                    let cropType = json.cropType;
                    let dtoHeaderAuth = dto_header_auth_1.DtoHeaderAuth.fromJSON(json.dtoHeaderAuth);
                    let status = status_1.Status.fromJSON(json.status);
                    let gobiiVersion = json.gobiiVersion;
                    let pagination = json.pagination ? pagination_1.Pagination.fromJSON(json.pagination) : null;
                    return new Header(cropType, dtoHeaderAuth, status, gobiiVersion, pagination); // new
                } // fromJson()
            };
            exports_1("Header", Header);
        }
    };
});
//# sourceMappingURL=header.js.map