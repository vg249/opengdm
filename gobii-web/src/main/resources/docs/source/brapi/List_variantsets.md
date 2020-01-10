
Lists VariantSets in the GDM. 

**Request Parameters**

Field | Type | Description
------|------|------------
variantSetDbId | String | The ID of the VariantSet to be retrieved.
page | Integer | Page number to be fetched based on page size.
pageToken | String | Page Token to fetch a page. nextPageToken form previous page's meta data should be used. If pageNumber is specified pageToken will be ignored. pageToken can be used to sequentially get pages faster. If an invalid pageToken is given, then the page will start from beginning.
pageSize | String | Size of the page to be fetched. Default is 1000.

**Request Headers**
Field | Type | Description
------|------|------------
Authorization | String | HTTP HEADER - Token used for Authorization Bearer {token_string}

**Response Body**

Field | Type | Description
------|------|------------
metaData | Object | Brapi Metadata with nextPage token if user chose to use pageToken to access data sequentially faster.
result.data | Array | [List of [VariantSet Resource](#variantsetresourceexample)]


