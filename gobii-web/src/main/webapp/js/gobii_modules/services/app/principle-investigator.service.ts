import {Injectable, Component} from 'angular2/core';

import {NameId} from '../../model/name-id';
import {NameIdListService} from './name-id-list.service';

@Injectable()
export class PrincipleInvestigatorService {


	constructor( private _nameIdListService: NameIdListService ) {
	} // ctor

	
	getNameIds() {

		var nameIds: NameId[] = [
			{"id": 11, "name": "Mr. Nice"},
			{"id": 12, "name": "Narco"},
			{"id": 13, "name": "Bombasto"},
			{"id": 14, "name": "Celeritas"},
			{"id": 15, "name": "Magneta"},
			{"id": 16, "name": "RubberMan"},
			{"id": 17, "name": "Dynama"},
			{"id": 18, "name": "Dr IQ"},
			{"id": 19, "name": "Magma"},
			{"id": 20, "name": "Tornado"}
		];		
					
		return nameIds;
	}

} 
