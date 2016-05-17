import {Injectable, Component} from 'angular2/core';

import {NameId} from '../../model/name-id';
import { Http, Response } from 'angular2/http';
import { Observable }     from 'rxjs/Observable';

@Injectable()
export class NameIdListService {


	constructor (private _http : Http ) {}
	
	getNameIds() {
		
		let requestBody = {
			   "processType":"READ",
			   "entityType":"DBTABLE",
			   "entityName":"project",
			   "filter":"2"
			};

		var nameIds: NameId[];
		


		
					
		return nameIds;
	}

} 
