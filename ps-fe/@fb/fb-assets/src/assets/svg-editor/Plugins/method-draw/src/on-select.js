/*!
 * on-select
 * (c) 2023 lincong1987
 */




function onSelect(el){

	// el.getAttributeNode('fill').value = '#111'
	 // debugger
	var data = {
		 id: el.id,
		type: 'svg-el'
	}
	// console.log(el)
	window.parent.postMessage(data, '*');
}
