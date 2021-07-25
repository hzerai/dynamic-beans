/**
 * 
 */
package com.rev.prototypes;

import java.util.List;

import com.windmill.metadata.Table;

/**
 * @author Habib Zerai
 *
 */
class DataBaseTraverser {
	
	public static void traverse(List<Table> tables) {
		for(Table table : tables) {
			traverseOneTable(table);
		}
	}

	/**
	 * @param table
	 */
	private static void traverseOneTable(Table table) {
		
	}

}
