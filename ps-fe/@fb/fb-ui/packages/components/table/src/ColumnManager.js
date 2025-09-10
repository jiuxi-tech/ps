/*!
 * ColumnManager
 * (c) 2021 lincong1987
 */

export function toRaw (obj) {

	//return Object.assign({}, {}, obj)

	return JSON.parse(JSON.stringify(obj))
}

export default class ColumnManager {
	constructor (columns) {
		this.columns = toRaw(columns)
		this._cached = {}
	}

	isAnyColumnsFreeze () {
		return this._cache('isAnyColumnsFreeze',
			() => this.columns.some(column => !!column.freeze))
	}

	isAnyColumnsLeftFreeze () {
		return this._cache('isAnyColumnsLeftFreeze', () =>
			this.columns.some(
				column => column.freeze === 'left' || column.freeze === true),
		)
	}

	isAnyColumnsRightFreeze () {
		return this._cache('isAnyColumnsRightFreeze', () =>
			this.columns.some(column => column.freeze === 'right'),
		)
	}

	leftColumns () {
		return this._cache('leftColumns', () =>
			this.groupedColumns().
				filter(
					column => column.freeze === 'left' || column.freeze === true),
		)
	}

	rightColumns () {
		return this._cache('rightColumns', () =>
			this.groupedColumns().filter(column => column.freeze === 'right'),
		)
	}

	leafColumns () {
		return this._cache('leafColumns', () => this._leafColumns(this.columns))
	}

	leftLeafColumns () {
		return this._cache('leftLeafColumns',
			() => this._leafColumns(this.leftColumns()))
	}

	rightLeafColumns () {
		return this._cache('rightLeafColumns',
			() => this._leafColumns(this.rightColumns()))
	}

	// add appropriate rowspan and colspan to column
	groupedColumns () {
		return this._cache('groupedColumns', () => {
			const _groupColumns = (
				columns, currentRow = 0, parentColumn = {}, rows = []) => {
				// track how many rows we got
				rows[currentRow] = rows[currentRow] || []
				const grouped = []
				const setRowSpan = column => {
					const rowSpan = rows.length - currentRow
					if (
						column &&
						!column.children && // parent columns are supposed to
						// be one row
						rowSpan > 1 &&
						(!column.rowSpan || column.rowSpan < rowSpan)
					) {
						column.rowSpan = rowSpan
					}
				}
				columns.forEach((column, index) => {
					const newColumn = {...column}
					rows[currentRow].push(newColumn)
					parentColumn.colSpan = parentColumn.colSpan || 0
					if (newColumn.children && newColumn.children.length > 0) {
						newColumn.children =
							_groupColumns(newColumn.children, currentRow + 1,
								newColumn,
								rows)
						parentColumn.colSpan += newColumn.colSpan
					} else {
						parentColumn.colSpan += 1
					}
					// update rowspan to all same row columns
					for (let i = 0; i < rows[currentRow].length - 1; i += 1) {
						setRowSpan(rows[currentRow][i])
					}
					// last column, update rowspan immediately
					if (index + 1 === columns.length) {
						setRowSpan(newColumn)
					}
					grouped.push(newColumn)
				})
				return grouped
			}
			return _groupColumns(this.columns)
		})
	}

	reset (columns) {
		this.columns = columns
		this._cached = {}
	}

	_cache (name, fn) {
		if (name in this._cached) {
			return this._cached[name]
		}
		this._cached[name] = fn()
		return this._cached[name]
	}

	_leafColumns (columns) {
		const leafColumns = []
		columns.forEach(column => {
			if (!column.children) {
				leafColumns.push(column)
			} else {
				leafColumns.push(...this._leafColumns(column.children))
			}
		})
		return leafColumns
	}

}
